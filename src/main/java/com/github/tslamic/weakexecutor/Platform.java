package com.github.tslamic.weakexecutor;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class Platform {

  private static final Platform PLATFORM = findPlatform();

  static Platform get() {
    return PLATFORM;
  }

  private static Platform findPlatform() {
    try {
      Class.forName("android.os.Build");
      if (Build.VERSION.SDK_INT != 0) {
        return new Android();
      }
    } catch (ReflectiveOperationException ignore) {
    }
    return new Platform();
  }

  void setBackgroundThreadPriority() {
    Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
  }

  Executor defaultTaskExecutor() {
    return Executors.newSingleThreadExecutor();
  }

  Executor defaultCallbackExecutor() {
    return null;
  }

  static class Android extends Platform {
    @Override
    void setBackgroundThreadPriority() {
      android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
    }

    @Override
    Executor defaultCallbackExecutor() {
      return new MainThreadExecutor();
    }

    static class MainThreadExecutor implements Executor {

      private final Handler handler = new Handler(Looper.getMainLooper());

      @Override
      public void execute(Runnable r) {
        handler.post(r);
      }

    }

  }

}
