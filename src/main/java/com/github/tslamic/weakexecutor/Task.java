package com.github.tslamic.weakexecutor;

public interface Task<T> {

  T execute() throws Exception;

}
