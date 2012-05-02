package com.github.sandrasi.geocoder.util;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class TimerExceptionTest {

    @Test
    public void shouldConstructTimerExceptionWithUninitializedDetailMessageAndUninitializedCause() {
        TimerException timerException = new TimerException();

        assertNull(timerException.getMessage());
        assertNull(timerException.getCause());
    }

    @Test
    public void shouldConstructTimerExceptionWithDetailMessageAndUninitializedCause() {
        TimerException timerException = new TimerException("test");

        assertThat(timerException.getMessage(), is("test"));
        assertNull(timerException.getCause());
    }

    @Test
    public void shouldConstructTimerExceptionWithMessageDescribingTheCauseAndCause() {
        Exception cause = new Exception();
        TimerException timerException = new TimerException(cause);

        assertThat(timerException.getMessage(), is(cause.toString()));
        assertEquals(cause, timerException.getCause());
    }

    @Test
    public void shouldConstructTimerExceptionWithMessageAndCause() {
        Exception cause = new Exception();
        TimerException timerException = new TimerException("test", cause);

        assertThat(timerException.getMessage(), is("test"));
        assertEquals(cause, timerException.getCause());
    }
}
