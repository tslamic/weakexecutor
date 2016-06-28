package com.github.tslamic.weakexecutor;

import java.util.concurrent.Executor;

import static com.github.tslamic.weakexecutor.Util.checkNotNull;

public class SimpleWeakExecutor implements WeakExecutor {

  private final Executor taskExecutor;
  private final Executor callbackExecutor;

  public SimpleWeakExecutor() {
    this(Platform.get().defaultTaskExecutor());
  }

  public SimpleWeakExecutor(Executor taskExecutor) {
    this(taskExecutor, Platform.get().defaultCallbackExecutor());
  }

  public SimpleWeakExecutor(Executor taskExecutor, Executor callbackExecutor) {
    this.taskExecutor = checkNotNull(taskExecutor, "task Executor is null");
    this.callbackExecutor = callbackExecutor;
  }

  @Override
  public <T> void execute(Task<T> task, Callback<T> callback) {
    execute(task, callback, TimeSpan.INFINITE);
  }

  @Override
  public <T> void execute(Task<T> task, Callback<T> callback, TimeSpan timeSpan) {
    final Runnable runnable = new SimpleWeakRunnable.Builder<>(task, callback)
        .callbackExecutor(callbackExecutor)
        .timeSpan(timeSpan)
        .build();
    taskExecutor.execute(runnable);
  }

}
