package com.github.sandrasi.geocoder.google.v3;

import java.util.Arrays;

import com.github.sandrasi.geocoder.components.*;
import org.junit.Test;

import static com.github.sandrasi.geocoder.components.AddressComponentType.*;
import static com.github.sandrasi.geocoder.components.GeocodeStatus.*;
import static com.github.sandrasi.geocoder.components.LocationType.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class GoogleGeocodeResponseTest {

    @Test
    public void shouldBuildGeocodeResponse() {
        Geometry googleCampusGeometry = Geometry.newBuilder(GeographicLocation.fromValues(37.421844, -122.084026))
                .setLocationType(APPROXIMATE)
                .setViewport(new GeographicArea(GeographicLocation.fromValues(37.412982, -122.1000334), GeographicLocation.fromValues(37.430705, -122.0680186)))
                .build();
        Geometry googleplexGeometry = Geometry.newBuilder(GeographicLocation.fromValues(37.515428, -122.086923))
                .setLocationType(APPROXIMATE)
                .setViewport(new GeographicArea(GeographicLocation.fromValues(37.5122804, -122.0900706), GeographicLocation.fromValues(37.5185756, -122.0837754)))
                .build();
        GeocodedAddress googleCampusAddress = GeocodedAddress.newBuilder("Google Campus, Mountain View, CA 94043, USA")
                .addAddressType(POINT_OF_INTEREST)
                .setGeometry(googleCampusGeometry)
                .build();
        GeocodedAddress googleplexAddress = GeocodedAddress.newBuilder("Googleplex, Fremont, CA 94555, USA")
                .addAddressType(POINT_OF_INTEREST)
                .setGeometry(googleplexGeometry)
                .build();

        GoogleGeocodeResponse googleGeocodeResponse = GoogleGeocodeResponse.newBuilder("googleplex")
                .setGeocodeStatus(GeocodeStatus.OK)
                .addGeocodedAddress(googleCampusAddress)
                .addGeocodedAddresses(Arrays.asList(googleplexAddress))
                .build();

        assertThat(googleGeocodeResponse.getGeocodeStatus(), is(OK));
        assertThat(googleGeocodeResponse.getGeocodedAddresses(), is(Arrays.asList(googleCampusAddress, googleplexAddress)));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionIfQueryStringIsNullInBuilder() {
        GoogleGeocodeResponse.newBuilder(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionIfGeocodeStatusIsSetToNullInBuilder() {
        GoogleGeocodeResponse.newBuilder("Mountain View").setGeocodeStatus(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionIfAddedGeocodedAddressIsNullInBuilder() {
        GoogleGeocodeResponse.newBuilder("Mountain View").addGeocodedAddress(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionIfAddedGeocodedAddressesAreNullInBuilder() {
        GoogleGeocodeResponse.newBuilder("Mountain View").addGeocodedAddresses(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfAddedGeocodedAddressesContainsNullElementsInBuilder() {
        GoogleGeocodeResponse.newBuilder("Mountain View").addGeocodedAddresses(Arrays.asList((GeocodedAddress) null));
    }

    @Test
    public void equalsAndHashCodeShouldWorkCorrectly() {
        Geometry googleCampusGeometry = Geometry.newBuilder(GeographicLocation.fromValues(37.421844, -122.084026))
                .setLocationType(APPROXIMATE)
                .setViewport(new GeographicArea(GeographicLocation.fromValues(37.412982, -122.1000334), GeographicLocation.fromValues(37.430705, -122.0680186)))
                .build();
        Geometry googleplexGeometry = Geometry.newBuilder(GeographicLocation.fromValues(37.515428, -122.086923))
                .setLocationType(APPROXIMATE)
                .setViewport(new GeographicArea(GeographicLocation.fromValues(37.5122804, -122.0900706), GeographicLocation.fromValues(37.5185756, -122.0837754)))
                .build();
        GeocodedAddress googleCampusAddress = GeocodedAddress.newBuilder("Google Campus, Mountain View, CA 94043, USA")
                .addAddressType(POINT_OF_INTEREST)
                .setGeometry(googleCampusGeometry)
                .build();
        GeocodedAddress googleplexAddress = GeocodedAddress.newBuilder("Googleplex, Fremont, CA 94555, USA")
                .addAddressType(POINT_OF_INTEREST)
                .setGeometry(googleplexGeometry)
                .build();

        GoogleGeocodeResponse googleGeocodeResponse = GoogleGeocodeResponse.newBuilder("googleplex")
                .setGeocodeStatus(GeocodeStatus.OK)
                .addGeocodedAddress(googleCampusAddress)
                .build();
        GoogleGeocodeResponse otherGeocodeResponse = GoogleGeocodeResponse.newBuilder("googleplex")
                .setGeocodeStatus(GeocodeStatus.OK)
                .addGeocodedAddress(googleCampusAddress)
                .build();
        GoogleGeocodeResponse geocodeResponseWithDifferentQueryString = GoogleGeocodeResponse.newBuilder("query string")
                .setGeocodeStatus(GeocodeStatus.OK)
                .addGeocodedAddress(googleCampusAddress)
                .build();
        GoogleGeocodeResponse geocodeResponseWithDifferentGeocodeStatus = GoogleGeocodeResponse.newBuilder("googleplex")
                .setGeocodeStatus(GeocodeStatus.OVER_QUERY_LIMIT)
                .addGeocodedAddress(googleCampusAddress)
                .build();
        GoogleGeocodeResponse geocodeResponseWithDifferentGeocodedAddresses = GoogleGeocodeResponse.newBuilder("googleplex")
                .setGeocodeStatus(GeocodeStatus.OK)
                .addGeocodedAddress(googleplexAddress)
                .build();

        assertFalse(googleGeocodeResponse.equals(null));
        assertFalse(googleGeocodeResponse.equals(new Object()));
        assertFalse(googleGeocodeResponse.equals(geocodeResponseWithDifferentGeocodeStatus));
        assertFalse(googleGeocodeResponse.equals(geocodeResponseWithDifferentGeocodedAddresses));
        assertTrue(googleGeocodeResponse.equals(googleGeocodeResponse));
        assertTrue(googleGeocodeResponse.equals(otherGeocodeResponse));
        assertTrue(googleGeocodeResponse.equals(geocodeResponseWithDifferentQueryString));
        assertEquals(googleGeocodeResponse.hashCode(), otherGeocodeResponse.hashCode());
        assertEquals(googleGeocodeResponse.hashCode(), geocodeResponseWithDifferentQueryString.hashCode());
    }

    @Test
    public void shouldConvertToString() {
        Geometry googleCampusGeometry = Geometry.newBuilder(GeographicLocation.fromValues(37.421844, -122.084026))
                .setLocationType(APPROXIMATE)
                .setViewport(new GeographicArea(GeographicLocation.fromValues(37.412982, -122.1000334), GeographicLocation.fromValues(37.430705, -122.0680186)))
                .build();
        Geometry googleplexGeometry = Geometry.newBuilder(GeographicLocation.fromValues(37.515428, -122.086923))
                .setLocationType(APPROXIMATE)
                .setViewport(new GeographicArea(GeographicLocation.fromValues(37.5122804, -122.0900706), GeographicLocation.fromValues(37.5185756, -122.0837754)))
                .build();
        GeocodedAddress googleCampusAddress = GeocodedAddress.newBuilder("Google Campus, Mountain View, CA 94043, USA")
                .addAddressType(POINT_OF_INTEREST)
                .setGeometry(googleCampusGeometry)
                .build();
        GeocodedAddress googleplexAddress = GeocodedAddress.newBuilder("Googleplex, Fremont, CA 94555, USA")
                .addAddressType(POINT_OF_INTEREST)
                .setGeometry(googleplexGeometry)
                .build();

        GoogleGeocodeResponse googleGeocodeResponse = GoogleGeocodeResponse.newBuilder("googleplex")
                .setGeocodeStatus(GeocodeStatus.OK)
                .addGeocodedAddress(googleCampusAddress)
                .addGeocodedAddress(googleplexAddress)
                .build();
        GoogleGeocodeResponse geocodeResponseWithoutResults = GoogleGeocodeResponse.newBuilder("googleplex")
                .setGeocodeStatus(GeocodeStatus.ZERO_RESULTS)
                .build();

        assertThat(googleGeocodeResponse.toString(), is("queryString: \"googleplex\", geocodeStatus: \"OK\", geocodedAddresses: [{formattedAddress: \"Google Campus, Mountain View, CA 94043, USA\", addressTypes: [\"POINT_OF_INTEREST\"], addressComponents: [], geometry: {location: {latitude: {value: \"37.421844\", degrees: \"37\", minutes: \"25\", seconds: \"18\", millis: \"638\", cardinalDirection: \"NORTH\"}, longitude: {value: \"-122.084026\", degrees: \"122\", minutes: \"5\", seconds: \"2\", millis: \"493\", cardinalDirection: \"WEST\"}, elevation: \"0\"}, locationType: \"APPROXIMATE\", viewport: {southWestCorner: {latitude: {value: \"37.412982\", degrees: \"37\", minutes: \"24\", seconds: \"46\", millis: \"735\", cardinalDirection: \"NORTH\"}, longitude: {value: \"-122.1000334\", degrees: \"122\", minutes: \"6\", seconds: \"0\", millis: \"120\", cardinalDirection: \"WEST\"}, elevation: \"0\"}, northEastCorner: {latitude: {value: \"37.430705\", degrees: \"37\", minutes: \"25\", seconds: \"50\", millis: \"538\", cardinalDirection: \"NORTH\"}, longitude: {value: \"-122.0680186\", degrees: \"122\", minutes: \"4\", seconds: \"4\", millis: \"866\", cardinalDirection: \"WEST\"}, elevation: \"0\"}}, bounds: {southWestCorner: {latitude: {value: \"37.412982\", degrees: \"37\", minutes: \"24\", seconds: \"46\", millis: \"735\", cardinalDirection: \"NORTH\"}, longitude: {value: \"-122.1000334\", degrees: \"122\", minutes: \"6\", seconds: \"0\", millis: \"120\", cardinalDirection: \"WEST\"}, elevation: \"0\"}, northEastCorner: {latitude: {value: \"37.430705\", degrees: \"37\", minutes: \"25\", seconds: \"50\", millis: \"538\", cardinalDirection: \"NORTH\"}, longitude: {value: \"-122.0680186\", degrees: \"122\", minutes: \"4\", seconds: \"4\", millis: \"866\", cardinalDirection: \"WEST\"}, elevation: \"0\"}}}, partialMatch: \"false\"}, {formattedAddress: \"Googleplex, Fremont, CA 94555, USA\", addressTypes: [\"POINT_OF_INTEREST\"], addressComponents: [], geometry: {location: {latitude: {value: \"37.515428\", degrees: \"37\", minutes: \"30\", seconds: \"55\", millis: \"540\", cardinalDirection: \"NORTH\"}, longitude: {value: \"-122.086923\", degrees: \"122\", minutes: \"5\", seconds: \"12\", millis: \"922\", cardinalDirection: \"WEST\"}, elevation: \"0\"}, locationType: \"APPROXIMATE\", viewport: {southWestCorner: {latitude: {value: \"37.5122804\", degrees: \"37\", minutes: \"30\", seconds: \"44\", millis: \"209\", cardinalDirection: \"NORTH\"}, longitude: {value: \"-122.0900706\", degrees: \"122\", minutes: \"5\", seconds: \"24\", millis: \"254\", cardinalDirection: \"WEST\"}, elevation: \"0\"}, northEastCorner: {latitude: {value: \"37.5185756\", degrees: \"37\", minutes: \"31\", seconds: \"6\", millis: \"872\", cardinalDirection: \"NORTH\"}, longitude: {value: \"-122.0837754\", degrees: \"122\", minutes: \"5\", seconds: \"1\", millis: \"591\", cardinalDirection: \"WEST\"}, elevation: \"0\"}}, bounds: {southWestCorner: {latitude: {value: \"37.5122804\", degrees: \"37\", minutes: \"30\", seconds: \"44\", millis: \"209\", cardinalDirection: \"NORTH\"}, longitude: {value: \"-122.0900706\", degrees: \"122\", minutes: \"5\", seconds: \"24\", millis: \"254\", cardinalDirection: \"WEST\"}, elevation: \"0\"}, northEastCorner: {latitude: {value: \"37.5185756\", degrees: \"37\", minutes: \"31\", seconds: \"6\", millis: \"872\", cardinalDirection: \"NORTH\"}, longitude: {value: \"-122.0837754\", degrees: \"122\", minutes: \"5\", seconds: \"1\", millis: \"591\", cardinalDirection: \"WEST\"}, elevation: \"0\"}}}, partialMatch: \"false\"}]"));
        assertThat(geocodeResponseWithoutResults.toString(), is("queryString: \"googleplex\", geocodeStatus: \"ZERO_RESULTS\", geocodedAddresses: []"));
    }
}
