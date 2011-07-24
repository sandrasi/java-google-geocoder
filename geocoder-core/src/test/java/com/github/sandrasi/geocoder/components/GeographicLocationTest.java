package com.github.sandrasi.geocoder.components;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

import com.github.sandrasi.geocoder.coordinate.GeographicCoordinate;

public class GeographicLocationTest {

    @Test
    public void shouldCreateGeographicLocation() {
        GeographicLocation geographicLocationFromValues = GeographicLocation.fromValues(37.422782, -122.085099);
        GeographicLocation geographicLocationFromCoordinates = GeographicLocation.fromCoordinates(GeographicCoordinate.latitudeFromDouble(37.422782), GeographicCoordinate.longitudeFromDouble(-122.085099));

        assertThat(geographicLocationFromValues.getLatitude(), is(37.422782));
        assertThat(geographicLocationFromValues.getLongitude(), is(-122.085099));
        assertThat(geographicLocationFromValues.getElevation(), is(0.0));
        assertThat(geographicLocationFromCoordinates.getLatitude(), is(37.422782));
        assertThat(geographicLocationFromCoordinates.getLongitude(), is(-122.085099));
        assertThat(geographicLocationFromCoordinates.getElevation(), is(0.0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfLatitudeIsNull() {
        GeographicLocation.fromCoordinates(null, GeographicCoordinate.longitudeFromDouble(-122.085099), new BigDecimal(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfLongitudeIsNull() {
        GeographicLocation.fromCoordinates(GeographicCoordinate.latitudeFromDouble(37.422782), null, new BigDecimal(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfElevationIsNull() {
        GeographicLocation.fromCoordinates(GeographicCoordinate.latitudeFromDouble(37.422782), GeographicCoordinate.longitudeFromDouble(-122.085099), null);
    }

    @Test
    public void equalsAndHashCodeShouldWorkCorrectly() {
        GeographicLocation geographicLocation = GeographicLocation.fromValues(37.422782, -122.085099, 0);
        GeographicLocation otherGeographicLocation = GeographicLocation.fromValues(37.422782, -122.085099, 0);
        GeographicLocation geographicLocationWithDifferentLatitude = GeographicLocation.fromValues(0, -122.085099, 0);
        GeographicLocation geographicLocationWithDifferentLongitude = GeographicLocation.fromValues(37.422782, 0, 0);
        GeographicLocation geographicLocationWithDifferentElevation = GeographicLocation.fromValues(37.422782, -122.085099, 1);

        assertFalse(geographicLocation.equals(null));
        assertFalse(geographicLocation.equals(new Object()));
        assertFalse(geographicLocation.equals(geographicLocationWithDifferentLatitude));
        assertFalse(geographicLocation.equals(geographicLocationWithDifferentLongitude));
        assertFalse(geographicLocation.equals(geographicLocationWithDifferentElevation));
        assertTrue(geographicLocation.equals(geographicLocation));
        assertTrue(geographicLocation.equals(otherGeographicLocation));
        assertEquals(geographicLocation.hashCode(), otherGeographicLocation.hashCode());
    }

    @Test
    public void shouldConvertToString() {
        GeographicLocation geographicLocation = GeographicLocation.fromValues(37.422782, -122.085099, 0.0);
        GeographicLocation geographicLocationWithAltitude = GeographicLocation.fromValues(37.422782, -122.085099, 1.234);

        assertThat(geographicLocation.toString(), is("latitude: {value: \"37.422782\", degrees: \"37\", minutes: \"25\", seconds: \"22\", millis: \"15\", cardinalDirection: \"NORTH\"}, longitude: {value: \"-122.085099\", degrees: \"122\", minutes: \"5\", seconds: \"6\", millis: \"356\", cardinalDirection: \"WEST\"}, elevation: \"0\""));
        assertThat(geographicLocationWithAltitude.toString(), is("latitude: {value: \"37.422782\", degrees: \"37\", minutes: \"25\", seconds: \"22\", millis: \"15\", cardinalDirection: \"NORTH\"}, longitude: {value: \"-122.085099\", degrees: \"122\", minutes: \"5\", seconds: \"6\", millis: \"356\", cardinalDirection: \"WEST\"}, elevation: \"1.234\""));
    }
}
