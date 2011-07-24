package com.github.sandrasi.geocoder;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import org.junit.Test;

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
