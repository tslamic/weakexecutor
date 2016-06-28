package com.github.tslamic.weakexecutor;

import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

class SimpleWeakRunnable<T> implements Runnable {

  static class Builder<T> {

    private final Task<T> task;
    private final WeakReference<Callback<T>> callbackRef;
    private Executor callbackExecutor = null;
    private TimeSpan timeSpan = TimeSpan.INFINITE;

    Builder(Task<T> task, Callback<T> callback) {
      this.task = Util.checkNotNull(task, "task is null");
      this.callbackRef = new WeakReference<>(Util.checkNotNull(callback, "callback is null"));
    }

    Builder<T> callbackExecutor(Executor executor) {
      this.callbackExecutor = executor;
      return this;
    }

    Builder<T> timeSpan(TimeSpan timeSpan) {
      this.timeSpan = Util.checkNotNull(timeSpan, "timeSpan is null");
      return this;
    }

    Runnable build() {
      return new SimpleWeakRunnable<>(this);
    }

  }

  private final Task<T> task;
  private final WeakReference<Callback<T>> callbackRef;
  private final Executor callbackExecutor;
  private final TimeSpan timeSpan;

  private SimpleWeakRunnable(Builder<T> builder) {
    this.task = builder.task;
    this.callbackRef = builder.callbackRef;
    this.callbackExecutor = builder.callbackExecutor;
    this.timeSpan = builder.timeSpan;
  }

  @Override
  public void run() {
    Platform.get().setBackgroundThreadPriority();

    try {
      T result;
      if (timeSpan.isInfinite()) {
        result = task.execute();
      } else {
        result = new TimeoutTask<>(task).get(timeSpan.duration, timeSpan.unit);
      }
      handleCallback(result, null);
    } catch (Exception e) {
      handleCallback(null, e);
    }
  }

  private void handleCallback(final T result, final Exception exception) {
    final Callback<T> callback = callbackRef.get();
    if (callback == null) {
      return;
    }
    if (callbackExecutor == null) {
      notifyCallback(callback, result, exception);
    } else {
      callbackExecutor.execute(new Runnable() {
        @Override
        public void run() {
          notifyCallback(callback, result, exception);
        }
      });
    }
  }

  private void notifyCallback(Callback<T> callback, T result, Exception exception) {
    if (result != null) {
      callback.onSuccess(result);
    } else {
      callback.onFailure(exception);
    }
  }

  private static class TimeoutTask<T> extends FutureTask<T> {
    public TimeoutTask(final Task<T> task) {
      super(new Callable<T>() {
        @Override
        public T call() throws Exception {
          return task.execute();
        }
      });
    }
  }
}
