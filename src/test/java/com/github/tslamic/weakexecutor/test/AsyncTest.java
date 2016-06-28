package com.github.tslamic.weakexecutor.test;

import org.junit.Assert;

import java.util.concurrent.CountDownLatch;

public abstract class AsyncTest {

  final CountDownLatch latch;

  AsyncTest(int count) {
    latch = new CountDownLatch(count);
    execute();
  }

  abstract void performTest(final CountDownLatch latch);

  void execute() {
    performTest(latch);
    try {
      latch.await();
    } catch (InterruptedException e) {
      Assert.fail(e.getMessage());
    }
  }

}
