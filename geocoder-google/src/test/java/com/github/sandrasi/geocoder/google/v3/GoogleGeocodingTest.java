package com.github.sandrasi.geocoder.google.v3;

import com.github.sandrasi.geocoder.GeocodeResponse;
import com.github.sandrasi.geocoder.components.GeographicLocation;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static com.github.sandrasi.geocoder.components.GeocodeStatus.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class GoogleGeocodingTest {

    private static final String GOOGLE_MAPS_API_PREMIER_CLIENT_ID = "my-client-id";
    private static final String GOOGLE_MAPS_API_PREMIER_KEY = "my-premier-key";

    @Before
    public void setUp() throws Exception {
        Thread.sleep(500); // Sleep a bit so geocoding will not get rejected
    }

    @Test
    public void shouldGeocodeAddressUsingTheStandardGoogleGeocodingService() throws Exception {
        GoogleGeocoder subject = GoogleGeocoderFactory.createDefaultGoogleGeocoder();
        GeocodeResponse geocodeResponse = subject.geocodeAddress("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA");

        assertThat(geocodeResponse.getGeocodeStatus(), is(OK));
    }

    @Ignore("The client ID and the premier key are invalid")
    @Test
    public void shouldGeocodeAddressUsingThePremierGoogleGeocodingService() {
        GoogleGeocoder subject = GoogleGeocoderFactory.createPremierGoogleGeocoder(GOOGLE_MAPS_API_PREMIER_CLIENT_ID, GOOGLE_MAPS_API_PREMIER_KEY);
        GeocodeResponse geocodeResponse = subject.geocodeAddress("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA");

        assertThat(geocodeResponse.getGeocodeStatus(), is(OK));
    }

    @Test
    public void shouldLookupAddressUsingTheStandardGoogleGeocodingService() {
        GoogleGeocoder subject = GoogleGeocoderFactory.createDefaultGoogleGeocoder();
        GeocodeResponse geocodeResponse = subject.lookupAddress(37.4213068, -122.08529);

        assertThat(geocodeResponse.getGeocodeStatus(), is(OK));
    }

    @Ignore("The client ID and the premier key are invalid")
    @Test
    public void shouldLookupAddressUsingThePremierGoogleGeocodingService() {
        GoogleGeocoder subject = GoogleGeocoderFactory.createPremierGoogleGeocoder(GOOGLE_MAPS_API_PREMIER_CLIENT_ID, GOOGLE_MAPS_API_PREMIER_KEY);
        GeocodeResponse geocodeResponse = subject.lookupAddress(37.4213068, -122.08529);

        assertThat(geocodeResponse.getGeocodeStatus(), is(OK));
    }

    @Test
    public void shouldGeocodeFullySpecifiedQueryUsingTheStandardGoogleGeocodingService() {
        GoogleGeocoder subject = GoogleGeocoderFactory.createDefaultGoogleGeocoder();
        GoogleGeocodeRequest geocodeRequest = subject.newGeocodeRequestBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA")
                .inLanguage("en")
                .withRegionBiasing("us")
                .withViewportBiasing(GeographicLocation.fromValues(36.4213068, -123.08529), GeographicLocation.fromValues(38.4213068, -121.08529))
                .build();
        GeocodeResponse geocodeResponse = subject.geocode(geocodeRequest);

        assertThat(geocodeResponse.getGeocodeStatus(), is(OK));
    }

    @Ignore("The client ID and the premier key are invalid")
    @Test
    public void shouldGeocodeFullySpecifiedQueryUsingThePremierGoogleGeocodingService() {
        GoogleGeocoder subject = GoogleGeocoderFactory.createPremierGoogleGeocoder(GOOGLE_MAPS_API_PREMIER_CLIENT_ID, GOOGLE_MAPS_API_PREMIER_KEY);
        GoogleGeocodeRequest geocodeRequest = subject.newGeocodeRequestBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA")
                .inLanguage("en")
                .withRegionBiasing("us")
                .withViewportBiasing(GeographicLocation.fromValues(36.4213068, -123.08529), GeographicLocation.fromValues(38.4213068, -121.08529))
                .build();
        GeocodeResponse geocodeResponse = subject.geocode(geocodeRequest);

        assertThat(geocodeResponse.getGeocodeStatus(), is(OK));
    }
}
