package com.github.tslamic.weakexecutor.test;

import com.github.tslamic.weakexecutor.Callback;
import com.github.tslamic.weakexecutor.SimpleWeakExecutor;
import com.github.tslamic.weakexecutor.Task;
import com.github.tslamic.weakexecutor.TimeSpan;
import com.github.tslamic.weakexecutor.WeakExecutor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SimpleWeakExecutorTest {

  WeakExecutor executor;

  @Before
  public void setUp() throws Exception {
    executor = new SimpleWeakExecutor();
  }

  @Test
  public void simpleExecution() {
    new AsyncTest(1) {
      @Override
      public void performTest(final CountDownLatch latch) {
        final Task<Integer> task = new IntTask(1000);
        final Callback<Integer> callback = new IntCallback() {
          @Override
          public void onSuccess(Integer result) {
            Assert.assertEquals(Integer.valueOf(1), result);
            latch.countDown();
          }
        };
        executor.execute(task, callback);
      }
    };
  }

  @Test
  public void simpleTimeout() {
    new AsyncTest(1) {
      @Override
      public void performTest(final CountDownLatch latch) {
        final Task<Integer> task = new IntTask(2000);
        final Callback<Integer> callback = new IntCallback() {
          @Override
          public void onFailure(Exception exception) {
            Assert.assertTrue(exception instanceof TimeoutException);
            latch.countDown();
          }
        };
        executor.execute(task, callback, TimeSpan.of(1000, TimeUnit.MILLISECONDS));
      }
    };
  }

  static class IntTask implements Task<Integer> {

    private final long sleep;

    public IntTask(long sleep) {
      this.sleep = sleep;
    }


    @Override
    public Integer execute() throws Exception {
      Thread.sleep(sleep);
      return 1;
    }

  }

  static class IntCallback implements Callback<Integer> {

    @Override
    public void onSuccess(Integer result) {
      Assert.fail();
    }

    @Override
    public void onFailure(Exception exception) {
      Assert.fail();
    }

  }

}
