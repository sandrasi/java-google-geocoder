package com.github.sandrasi.geocoder.coordinate;

import static com.github.sandrasi.geocoder.coordinate.GeographicCoordinateType.LATITUDE;
import static com.github.sandrasi.geocoder.coordinate.GeographicCoordinateType.LONGITUDE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

public class GeographicCoordinateTypeTest {

    @Test
    public void shouldValidateBigDecimalLatitude() {
        assertTrue(LATITUDE.isValidValue(new BigDecimal(-90)));
        assertTrue(LATITUDE.isValidValue(new BigDecimal(90)));
        assertFalse(LATITUDE.isValidValue(new BigDecimal(-90.1)));
        assertFalse(LATITUDE.isValidValue(new BigDecimal(90.1)));
    }

    @Test
    public void shouldValidateBigDecimalLongitude() {
        assertTrue(LONGITUDE.isValidValue(new BigDecimal(-180)));
        assertTrue(LONGITUDE.isValidValue(new BigDecimal(180)));
        assertFalse(LONGITUDE.isValidValue(new BigDecimal(-180.1)));
        assertFalse(LONGITUDE.isValidValue(new BigDecimal(180.1)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfBigDecimalCoordinateIsNull() {
        LATITUDE.isValidValue(null);
    }

    @Test
    public void shouldValidateDoubleLatitude() {
        assertTrue(LATITUDE.isValidValue(-90.0));
        assertTrue(LATITUDE.isValidValue(90.0));
        assertFalse(LATITUDE.isValidValue(-90.1));
        assertFalse(LATITUDE.isValidValue(90.1));
    }

    @Test
    public void shouldValidateDoubleLongitude() {
        assertTrue(LONGITUDE.isValidValue(-180.0));
        assertTrue(LONGITUDE.isValidValue(180.0));
        assertFalse(LONGITUDE.isValidValue(-180.1));
        assertFalse(LONGITUDE.isValidValue(180.1));
    }

    @Test
    public void shouldValidateAngleLatitude() {
        assertTrue(LATITUDE.isValidAngle(90, 0, 0, 0, CardinalDirection.NORTH));
        assertTrue(LATITUDE.isValidAngle(90, 0, 0, 0, CardinalDirection.SOUTH));
        assertTrue(LATITUDE.isValidAngle(0, 0, 0, 0, CardinalDirection.ZERO_LATITUDE_DEGREE));
        assertFalse(LATITUDE.isValidAngle(90, 0, 0, 1, CardinalDirection.NORTH));
        assertFalse(LATITUDE.isValidAngle(90, 0, 0, 1, CardinalDirection.SOUTH));
        assertFalse(LATITUDE.isValidAngle(0, 0, 0, 1, CardinalDirection.ZERO_LATITUDE_DEGREE));
        assertFalse(LATITUDE.isValidAngle(-90, 0, 0, 0, CardinalDirection.NORTH));
        assertFalse(LATITUDE.isValidAngle(90, 0, 0, 0, CardinalDirection.EAST));
    }

    @Test
    public void shouldValidateAngleLongitude() {
        assertTrue(LONGITUDE.isValidAngle(180, 0, 0, 0, CardinalDirection.EAST));
        assertTrue(LONGITUDE.isValidAngle(180, 0, 0, 0, CardinalDirection.WEST));
        assertTrue(LONGITUDE.isValidAngle(0, 0, 0, 0, CardinalDirection.ZERO_LONGITUDE_DEGREE));
        assertFalse(LONGITUDE.isValidAngle(180, 0, 0, 1, CardinalDirection.EAST));
        assertFalse(LONGITUDE.isValidAngle(180, 0, 0, 1, CardinalDirection.WEST));
        assertFalse(LONGITUDE.isValidAngle(0, 0, 0, 1, CardinalDirection.ZERO_LONGITUDE_DEGREE));
        assertFalse(LONGITUDE.isValidAngle(-180, 0, 0, 0, CardinalDirection.EAST));
        assertFalse(LONGITUDE.isValidAngle(180, 0, 0, 0, CardinalDirection.NORTH));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfCardinalDirectionIsNull() {
        LATITUDE.isValidAngle(0, 0, 0, 0, null);
    }
}
