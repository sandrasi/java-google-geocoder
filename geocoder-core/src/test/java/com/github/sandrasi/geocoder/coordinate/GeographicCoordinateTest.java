package com.github.sandrasi.geocoder.coordinate;

import java.math.BigDecimal;

import org.junit.Test;

import static com.github.sandrasi.geocoder.coordinate.CardinalDirection.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class GeographicCoordinateTest {

    @Test
    public void shouldCreateLatitudeFromBigDecimal() {
        GeographicCoordinate latitude = GeographicCoordinate.latitudeFromBigDecimal(new BigDecimal("1.0341677777777778"));

        assertThat(latitude.getValue(), is(new BigDecimal("1.0341677777777778")));
        assertThat(latitude.getDoubleValue(), is(1.0341677777777778));
        assertThat(latitude.getDegrees(), is(1));
        assertThat(latitude.getMinutes(), is(2));
        assertThat(latitude.getSeconds(), is(3));
        assertThat(latitude.getMillis(), is(4));
        assertThat(latitude.getCardinalDirection(), is(NORTH));
    }

    @Test
    public void shouldCreateLongitudeFromBigDecimal() {
        GeographicCoordinate longitude = GeographicCoordinate.longitudeFromBigDecimal(new BigDecimal("1.0341677777777778"));

        assertThat(longitude.getValue(), is(new BigDecimal("1.0341677777777778")));
        assertThat(longitude.getDoubleValue(), is(1.0341677777777778));
        assertThat(longitude.getDegrees(), is(1));
        assertThat(longitude.getMinutes(), is(2));
        assertThat(longitude.getSeconds(), is(3));
        assertThat(longitude.getMillis(), is(4));
        assertThat(longitude.getCardinalDirection(), is(EAST));
    }

    @Test
    public void shouldCreateLatitudeFromDouble() {
        GeographicCoordinate latitude = GeographicCoordinate.latitudeFromDouble(1.0341677777777778);

        assertThat(latitude.getValue(), is(new BigDecimal("1.0341677777777778")));
        assertThat(latitude.getDoubleValue(), is(1.0341677777777778));
        assertThat(latitude.getDegrees(), is(1));
        assertThat(latitude.getMinutes(), is(2));
        assertThat(latitude.getSeconds(), is(3));
        assertThat(latitude.getMillis(), is(4));
        assertThat(latitude.getCardinalDirection(), is(NORTH));
    }

    @Test
    public void shouldCreateLongitudeFromDouble() {
        GeographicCoordinate longitude = GeographicCoordinate.longitudeFromDouble(1.0341677777777778);

        assertThat(longitude.getValue(), is(new BigDecimal("1.0341677777777778")));
        assertThat(longitude.getDoubleValue(), is(1.0341677777777778));
        assertThat(longitude.getDegrees(), is(1));
        assertThat(longitude.getMinutes(), is(2));
        assertThat(longitude.getSeconds(), is(3));
        assertThat(longitude.getMillis(), is(4));
        assertThat(longitude.getCardinalDirection(), is(EAST));
    }

    @Test
    public void shouldCreateLatitudeFromString() {
        GeographicCoordinate latitude = GeographicCoordinate.latitudeFromString("1.0341677777777778");

        assertThat(latitude.getValue(), is(new BigDecimal("1.0341677777777778")));
        assertThat(latitude.getDoubleValue(), is(1.0341677777777778));
        assertThat(latitude.getDegrees(), is(1));
        assertThat(latitude.getMinutes(), is(2));
        assertThat(latitude.getSeconds(), is(3));
        assertThat(latitude.getMillis(), is(4));
        assertThat(latitude.getCardinalDirection(), is(NORTH));
    }

    @Test
    public void shouldCreateLongitudeFromString() {
        GeographicCoordinate longitude = GeographicCoordinate.longitudeFromString("1.0341677777777778");

        assertThat(longitude.getValue(), is(new BigDecimal("1.0341677777777778")));
        assertThat(longitude.getDoubleValue(), is(1.0341677777777778));
        assertThat(longitude.getDegrees(), is(1));
        assertThat(longitude.getMinutes(), is(2));
        assertThat(longitude.getSeconds(), is(3));
        assertThat(longitude.getMillis(), is(4));
        assertThat(longitude.getCardinalDirection(), is(EAST));
    }

    @Test
    public void shouldCreateGeographicCoordinateFromAngle() {
        GeographicCoordinate geographicCoordinate = GeographicCoordinate.fromAngle(1, 2, 3, 4, CardinalDirection.NORTH);

        assertThat(geographicCoordinate.getValue(), is(new BigDecimal("1.0341677777777778")));
        assertThat(geographicCoordinate.getDoubleValue(), is(1.0341677777777778));
        assertThat(geographicCoordinate.getDegrees(), is(1));
        assertThat(geographicCoordinate.getMinutes(), is(2));
        assertThat(geographicCoordinate.getSeconds(), is(3));
        assertThat(geographicCoordinate.getMillis(), is(4));
        assertThat(geographicCoordinate.getCardinalDirection(), is(NORTH));
    }

    @Test
    public void valueOfCoordinateShouldBeNegativeIfCoordinateIsASouthLatitude() {
        GeographicCoordinate geographicCoordinate = GeographicCoordinate.fromAngle(1, 2, 3, 4, CardinalDirection.SOUTH);

        assertThat(geographicCoordinate.getDoubleValue(), is(-1.0341677777777778));
    }

    @Test
    public void valueOfCoordinateShouldBeNegativeIfCoordinateIsAWestLongitude() {
        GeographicCoordinate geographicCoordinate = GeographicCoordinate.fromAngle(1, 2, 3, 4, CardinalDirection.WEST);

        assertThat(geographicCoordinate.getDoubleValue(), is(-1.0341677777777778));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionIfLatitudeValueIsNull() {
        GeographicCoordinate.latitudeFromBigDecimal(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionIfLongitudeValueIsNull() {
        GeographicCoordinate.longitudeFromBigDecimal(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionIfCardinalDirectionIsNull() {
        GeographicCoordinate.fromAngle(1, 2, 3, 4, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfLatitudeValueIsInvalid() {
        GeographicCoordinate.latitudeFromBigDecimal(new BigDecimal(91));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfLongitudeValueIsInvalid() {
        GeographicCoordinate.longitudeFromBigDecimal(new BigDecimal(181));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfLatitudeAngleIsInvalid() {
        GeographicCoordinate.fromAngle(91, 0, 0, 0, CardinalDirection.NORTH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfLongitudeAngleIsInvalid() {
        GeographicCoordinate.fromAngle(181, 0, 0, 0, CardinalDirection.EAST);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfAngleIsNotZeroWhenCardinalDirectionIsZeroLatitudeDegree() {
        GeographicCoordinate.fromAngle(1, 0, 0, 0, CardinalDirection.ZERO_LATITUDE_DEGREE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfAngleIsNotZeroWhenCardinalDirectionIsZeroLongitudeDegree() {
        GeographicCoordinate.fromAngle(1, 0, 0, 0, CardinalDirection.ZERO_LONGITUDE_DEGREE);
    }

    @Test
    public void shouldSetCardinalDirectionCorrectly() {
        GeographicCoordinate northernLatitude = GeographicCoordinate.latitudeFromDouble(1.0341677777777778);
        GeographicCoordinate equator = GeographicCoordinate.latitudeFromDouble(0);
        GeographicCoordinate southernLatitude = GeographicCoordinate.latitudeFromDouble(-1.0341677777777778);
        GeographicCoordinate easternLongitude = GeographicCoordinate.longitudeFromDouble(1.0341677777777778);
        GeographicCoordinate primeMeridian = GeographicCoordinate.longitudeFromDouble(0);
        GeographicCoordinate westernLongitude = GeographicCoordinate.longitudeFromDouble(-1.0341677777777778);

        assertThat(northernLatitude.getCardinalDirection(), is(NORTH));
        assertThat(equator.getCardinalDirection(), is(ZERO_LATITUDE_DEGREE));
        assertThat(southernLatitude.getCardinalDirection(), is(SOUTH));
        assertThat(easternLongitude.getCardinalDirection(), is(EAST));
        assertThat(primeMeridian.getCardinalDirection(), is(ZERO_LONGITUDE_DEGREE));
        assertThat(westernLongitude.getCardinalDirection(), is(WEST));
    }

    @Test
    public void equalsAndHashCodeShouldWorkCorrectly() {
        GeographicCoordinate latitudeFromBigDecimal = GeographicCoordinate.latitudeFromBigDecimal(new BigDecimal("1.0341677777777778"));
        GeographicCoordinate latitudeFromAngle = GeographicCoordinate.fromAngle(1, 2, 3, 4, CardinalDirection.NORTH);

        assertFalse(latitudeFromBigDecimal.equals(null));
        assertFalse(latitudeFromBigDecimal.equals(new Object()));
        assertFalse(latitudeFromBigDecimal.equals(GeographicCoordinate.EQUATOR));
        assertTrue(latitudeFromBigDecimal.equals(latitudeFromBigDecimal));
        assertTrue(latitudeFromBigDecimal.equals(latitudeFromAngle));
        assertEquals(latitudeFromBigDecimal.hashCode(), latitudeFromAngle.hashCode());
    }

    @Test
    public void shouldConvertToString() {
        GeographicCoordinate latitude = GeographicCoordinate.latitudeFromBigDecimal(new BigDecimal("1.0341677777777778"));

        assertThat(latitude.toString(), is("value: \"1.0341677777777778\", degrees: \"1\", minutes: \"2\", seconds: \"3\", millis: \"4\", cardinalDirection: \"NORTH\""));
        assertThat(GeographicCoordinate.EQUATOR.toString(), is("value: \"0\", degrees: \"0\", minutes: \"0\", seconds: \"0\", millis: \"0\", cardinalDirection: \"ZERO_LATITUDE_DEGREE\""));
        assertThat(GeographicCoordinate.PRIME_MERIDIAN.toString(), is("value: \"0\", degrees: \"0\", minutes: \"0\", seconds: \"0\", millis: \"0\", cardinalDirection: \"ZERO_LONGITUDE_DEGREE\""));
    }
}
