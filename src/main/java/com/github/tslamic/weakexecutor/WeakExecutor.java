package com.github.tslamic.weakexecutor;

public interface WeakExecutor {

  <T> void execute(Task<T> task, Callback<T> callback);

  <T> void execute(Task<T> task, Callback<T> callback, TimeSpan timeSpan);

}
