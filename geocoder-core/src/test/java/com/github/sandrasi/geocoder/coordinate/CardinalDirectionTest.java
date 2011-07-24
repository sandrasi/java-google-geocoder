package com.github.sandrasi.geocoder.coordinate;

import static com.github.sandrasi.geocoder.coordinate.GeographicCoordinateType.LATITUDE;
import static com.github.sandrasi.geocoder.coordinate.GeographicCoordinateType.LONGITUDE;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class CardinalDirectionTest {

    @Test
    public void shouldHaveCorrectCoordinateType() {
        assertThat(CardinalDirection.NORTH.getCoordinateType(), is(LATITUDE));
        assertThat(CardinalDirection.SOUTH.getCoordinateType(), is(LATITUDE));
        assertThat(CardinalDirection.EAST.getCoordinateType(), is(LONGITUDE));
        assertThat(CardinalDirection.WEST.getCoordinateType(), is(LONGITUDE));
    }
}
