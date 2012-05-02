package com.github.sandrasi.geocoder.google.v3;

import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

import static org.junit.Assert.*;

public class GoogleGeocoderFactoryTest {

    @Test
    public void shouldCloseTimedGoogleGeocoder() {
        GoogleGeocoder geocoder = GoogleGeocoderFactory.createTimedGoogleGeocoder(0);

        try {
            geocoder.close();
            geocoder.geocodeAddress("foo");
            fail("geocoder.close() should have closed the geocoder so that no further geocoding is possible");
        } catch (IllegalStateException e) {
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldNotCloseTimedGoogleGeocoderWithExternalHttpClient() {
        GoogleGeocoderFactory.createTimedGoogleGeocoder(new DefaultHttpClient(), 0).close();
    }

    @Test
    public void shouldCloseTimedPremierGoogleGeocoder() {
        GoogleGeocoder geocoder = GoogleGeocoderFactory.createTimedPremierGoogleGeocoder("johndoe", "foo", 0);

        try {
            geocoder.close();
            geocoder.geocodeAddress("foo");
            fail("geocoder.close() should have closed the geocoder so that no further geocoding is possible");
        } catch (IllegalStateException e) {
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldNotCloseTimedPremierGoogleGeocoderWithExternalHttpClient() {
        GoogleGeocoderFactory.createTimedPremierGoogleGeocoder(new DefaultHttpClient(), "johndoe", "foo", 0).close();
    }
}
