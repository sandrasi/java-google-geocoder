package com.github.sandrasi.geocoder;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class GeocodeExceptionTest {

    @Test
    public void shouldConstructGeocodeExceptionWithUninitializedDetailMessageAndUninitializedCause() {
        GeocodeException geocodeException = new GeocodeException();

        assertNull(geocodeException.getMessage());
        assertNull(geocodeException.getCause());
    }

    @Test
    public void shouldConstructGeocodeExceptionWithDetailMessageAndUninitializedCause() {
        GeocodeException geocodeException = new GeocodeException("test");

        assertThat(geocodeException.getMessage(), is("test"));
        assertNull(geocodeException.getCause());
    }

    @Test
    public void shouldConstructGeocodeExceptionWithMessageDescribingTheCauseAndCause() {
        Exception cause = new Exception();
        GeocodeException geocodeException = new GeocodeException(cause);

        assertThat(geocodeException.getMessage(), is(cause.toString()));
        assertEquals(cause, geocodeException.getCause());
    }

    @Test
    public void shouldConstructGeocodeExceptionWithMessageAndCause() {
        Exception cause = new Exception();
        GeocodeException geocodeException = new GeocodeException("test", cause);

        assertThat(geocodeException.getMessage(), is("test"));
        assertEquals(cause, geocodeException.getCause());
    }
}
