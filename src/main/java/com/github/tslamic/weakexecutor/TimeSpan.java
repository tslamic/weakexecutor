package com.github.tslamic.weakexecutor;

import java.util.concurrent.TimeUnit;

import static com.github.tslamic.weakexecutor.Util.checkNotNull;
import static com.github.tslamic.weakexecutor.Util.ensure;

public final class TimeSpan {

  public static final TimeSpan INFINITE = new TimeSpan(0, TimeUnit.MILLISECONDS);

  final long duration;
  final TimeUnit unit;

  private TimeSpan(long duration, TimeUnit unit) {
    this.duration = duration;
    this.unit = unit;
  }

  public static TimeSpan of(long duration, TimeUnit unit) {
    ensure(duration > 0, "duration must be positive");
    checkNotNull(unit, "unit is null");
    return new TimeSpan(duration, unit);
  }

  public long getDuration() {
    return duration;
  }

  public TimeUnit getUnit() {
    return unit;
  }

  public boolean isInfinite() {
    return duration <= 0;
  }

}
