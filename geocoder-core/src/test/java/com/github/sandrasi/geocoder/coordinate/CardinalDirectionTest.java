package com.github.sandrasi.geocoder.coordinate;

import org.junit.Test;

import static com.github.sandrasi.geocoder.coordinate.GeographicCoordinateType.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class CardinalDirectionTest {

    @Test
    public void shouldHaveCorrectCoordinateType() {
        assertThat(CardinalDirection.NORTH.getCoordinateType(), is(LATITUDE));
        assertThat(CardinalDirection.SOUTH.getCoordinateType(), is(LATITUDE));
        assertThat(CardinalDirection.EAST.getCoordinateType(), is(LONGITUDE));
        assertThat(CardinalDirection.WEST.getCoordinateType(), is(LONGITUDE));
    }
}
