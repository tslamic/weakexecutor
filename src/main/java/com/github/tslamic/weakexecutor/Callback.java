package com.github.tslamic.weakexecutor;

public interface Callback<T> {

  void onSuccess(T result);

  void onFailure(Exception exception);

}
