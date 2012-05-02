package com.github.sandrasi.geocoder.util;

import org.apache.commons.lang3.Validate;

/**
 * {@code Timer} can be used to put the current thread to a sleeping state. The length of the sleeping
 * period depends on the last time when the thread was put to sleeping state via the {@code Timer}
 * class and the maximum sleep time specified as a constructor argument.
 */
public class Timer {

    private final long maximumSleepTime;

    private long lastSleepTimestamp;

    /**
     * Constructs a new {@code Timer}. The timer will not sleep any longer than it is defined by the
     * {@code maximumSleepTime} parameter.
     *
     * @param maximumSleepTime the maximum number of milliseconds this timer can sleep for
     */
    public Timer(long maximumSleepTime) {
        Validate.isTrue(maximumSleepTime >= 0, "maximumSleepTime must be a non-negative number");

        this.maximumSleepTime = maximumSleepTime;
    }

    /**
     * Puts the current thread to a sleeping state. The length of the time period the thread sleeps for is
     * the difference of the maximum sleep time and the time elapsed since the last time the thread was
     * put to sleeping state with this timer. If more time has elapsed than the maximum sleep time the thread
     * is not put to a sleeping state. If the maximum sleep time is {@code 0} this method returns
     * immediately.
     */
    public synchronized void sleep() {
        if (maximumSleepTime == 0) {
            return;
        }

        long elapsedTime = System.currentTimeMillis() - lastSleepTimestamp;
        long sleepTime = maximumSleepTime - elapsedTime;

        if (sleepTime > 0) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                throw new TimerException("The thread " + Thread.currentThread() + " waiting for " + sleepTime + " milliseconds to elapse has been interrupted", e);
            }
        }

        lastSleepTimestamp = System.currentTimeMillis();
    }
}
