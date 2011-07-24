package com.github.sandrasi.geocoder.components;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GeographicAreaTest {

    @Test
    public void shouldConstructGeographicAreaFromCorrectValues() {
        GeographicArea geographicArea = new GeographicArea(GeographicLocation.fromValues(0, 0), GeographicLocation.fromValues(1, 1));

        assertThat(geographicArea.getSouthWestCorner(), is(GeographicLocation.fromValues(0, 0)));
        assertThat(geographicArea.getNorthEastCorner(), is(GeographicLocation.fromValues(1, 1)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfSouthWestCornerIsNull() {
        new GeographicArea(null, GeographicLocation.fromValues(0, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfNorthEastCornerIsNull() {
        new GeographicArea(GeographicLocation.fromValues(0, 0), null);
    }

    @Test
    public void equalsAndHashCodeShouldWorkCorrectly() {
        GeographicArea geographicArea = new GeographicArea(GeographicLocation.fromValues(0, 0), GeographicLocation.fromValues(1, 1));
        GeographicArea otherGeographicArea = new GeographicArea(GeographicLocation.fromValues(0, 0), GeographicLocation.fromValues(1, 1));
        GeographicArea geographicAreaWithDifferentSouthWestCorner = new GeographicArea(GeographicLocation.fromValues(-1, -1), GeographicLocation.fromValues(1, 1));
        GeographicArea geographicAreaWithDifferentNorthEastCorner = new GeographicArea(GeographicLocation.fromValues(0, 0), GeographicLocation.fromValues(2, 2));

        assertFalse(geographicArea.equals(null));
        assertFalse(geographicArea.equals(new Object()));
        assertFalse(geographicArea.equals(geographicAreaWithDifferentSouthWestCorner));
        assertFalse(geographicArea.equals(geographicAreaWithDifferentNorthEastCorner));
        assertTrue(geographicArea.equals(geographicArea));
        assertTrue(geographicArea.equals(otherGeographicArea));
        assertEquals(geographicArea.hashCode(), otherGeographicArea.hashCode());
    }

    @Test
    public void shouldConvertToString() {
        GeographicArea geographicArea = new GeographicArea(GeographicLocation.fromValues(0, 0), GeographicLocation.fromValues(1, 1));

        assertThat(geographicArea.toString(), is("southWestCorner: {latitude: {value: \"0\", degrees: \"0\", minutes: \"0\", seconds: \"0\", millis: \"0\", cardinalDirection: \"ZERO_LATITUDE_DEGREE\"}, longitude: {value: \"0\", degrees: \"0\", minutes: \"0\", seconds: \"0\", millis: \"0\", cardinalDirection: \"ZERO_LONGITUDE_DEGREE\"}, elevation: \"0\"}, northEastCorner: {latitude: {value: \"1\", degrees: \"1\", minutes: \"0\", seconds: \"0\", millis: \"0\", cardinalDirection: \"NORTH\"}, longitude: {value: \"1\", degrees: \"1\", minutes: \"0\", seconds: \"0\", millis: \"0\", cardinalDirection: \"EAST\"}, elevation: \"0\"}"));
    }
}
