package com.github.sandrasi.geocoder.google.v3;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class GoogleMapsApiPremierCredentialsTest {

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionIfClientIdIsNull() {
        new GoogleMapsApiPremierCredentials(null, "foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfClientIdIsBlank() {
        new GoogleMapsApiPremierCredentials("   ", "foo");
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionIfKeyIsNull() {
        new GoogleMapsApiPremierCredentials("johndoe", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfKeyIsBlank() {
        new GoogleMapsApiPremierCredentials("johndoe", "   ");
    }

    @Test
    public void shouldSignStringWithTheKey() {
        GoogleMapsApiPremierCredentials subject = new GoogleMapsApiPremierCredentials("johndoe", "foo");
        String signature = subject.getSignatureFor("blah-blah");

        assertThat(signature, is("Y5z3sdaSOI0NhJ6zPuUMzox2ot4="));
    }
}
