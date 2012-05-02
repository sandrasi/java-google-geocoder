package com.github.sandrasi.geocoder.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimerTest {

    private static final int MAXIMUM_SLEEP_TIME = 100;

    private Timer subject;

    @Before
    public void setUp() {
        subject = new Timer(MAXIMUM_SLEEP_TIME);
    }

    @Test
    public void shouldSleepForMaximumSleepTime() {
        long timeBeforeTheFirstSleep = System.currentTimeMillis();

        subject.sleep();
        subject.sleep();

        long timeAfterTheSecondSleep = System.currentTimeMillis();

        assertTrue((timeAfterTheSecondSleep - timeBeforeTheFirstSleep) >= MAXIMUM_SLEEP_TIME);
    }

    @Test
    public void shouldNotSleepIfMoreThanMaximumSleepTimeHasPassedSinceTheLastSleep() throws Exception {
        subject.sleep();
        Thread.sleep(MAXIMUM_SLEEP_TIME * 2);

        long timeBeforeTheSecondSleep = System.currentTimeMillis();

        subject.sleep();

        long timeAfterTheSecondSleep = System.currentTimeMillis();
        long delta = 5;

        assertTrue((timeAfterTheSecondSleep - timeBeforeTheSecondSleep) < delta);
    }
}
