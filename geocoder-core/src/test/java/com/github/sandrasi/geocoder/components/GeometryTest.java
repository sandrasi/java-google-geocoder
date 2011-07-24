package com.github.sandrasi.geocoder.components;

import static com.github.sandrasi.geocoder.components.LocationType.APPROXIMATE;
import static com.github.sandrasi.geocoder.components.LocationType.ROOFTOP;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GeometryTest {

    @Test
    public void shouldBuildGeometry() {
        Geometry geometry = Geometry.newBuilder(GeographicLocation.fromValues(37.422782, -122.085099))
                .setLocationType(ROOFTOP)
                .setViewport(new GeographicArea(GeographicLocation.fromValues(37.4196344, -122.0882466), GeographicLocation.fromValues(37.4259296, -122.0819514)))
                .setBounds(new GeographicArea(GeographicLocation.fromValues(36.4196344, -123.0882466), GeographicLocation.fromValues(38.4259296, -121.0819514)))
                .build();

        assertThat(geometry.getLocation(), is(GeographicLocation.fromValues(37.422782, -122.085099)));
        assertThat(geometry.getLocationType(), is(ROOFTOP));
        assertThat(geometry.getViewport(), is(new GeographicArea(GeographicLocation.fromValues(37.4196344, -122.0882466), GeographicLocation.fromValues(37.4259296, -122.0819514))));
        assertThat(geometry.getBounds(), is(new GeographicArea(GeographicLocation.fromValues(36.4196344, -123.0882466), GeographicLocation.fromValues(38.4259296, -121.0819514))));
    }

    @Test
    public void shouldInitializeLocationTypeToApproximateViewportToLargestAndBoundsToSmallestIfTheyAreNotSet() {
        Geometry geometry = Geometry.newBuilder(GeographicLocation.fromValues(37.422782, -122.085099))
                .build();

        assertThat(geometry.getLocationType(), is(APPROXIMATE));
        assertThat(geometry.getViewport(), is(new GeographicArea(GeographicLocation.fromValues(-90, -180), GeographicLocation.fromValues(90, 180))));
        assertThat(geometry.getBounds(), is(new GeographicArea(GeographicLocation.fromValues(0, 0), GeographicLocation.fromValues(0, 0))));
    }

    @Test
    public void shouldSetBoundsToViewportIfItIsNotSetInTheBuilder() {
        Geometry geometry = Geometry.newBuilder(GeographicLocation.fromValues(37.422782, -122.085099))
                .setViewport(new GeographicArea(GeographicLocation.fromValues(37.4196344, -122.0882466), GeographicLocation.fromValues(37.4259296, -122.0819514)))
                .build();

        assertThat(geometry.getViewport(), is(new GeographicArea(GeographicLocation.fromValues(37.4196344, -122.0882466), GeographicLocation.fromValues(37.4259296, -122.0819514))));
        assertThat(geometry.getBounds(), is(new GeographicArea(GeographicLocation.fromValues(37.4196344, -122.0882466), GeographicLocation.fromValues(37.4259296, -122.0819514))));
    }

    @Test
    public void shouldNotOverwriteBoundsWithViewportIfItIsAlreadySetInTheBuilder() {
        Geometry geometry = Geometry.newBuilder(GeographicLocation.fromValues(37.422782, -122.085099))
                .setBounds(new GeographicArea(GeographicLocation.fromValues(36.4196344, -123.0882466), GeographicLocation.fromValues(38.4259296, -121.0819514)))
                .setViewport(new GeographicArea(GeographicLocation.fromValues(37.4196344, -122.0882466), GeographicLocation.fromValues(37.4259296, -122.0819514)))
                .build();

        assertThat(geometry.getBounds(), is(new GeographicArea(GeographicLocation.fromValues(36.4196344, -123.0882466), GeographicLocation.fromValues(38.4259296, -121.0819514))));
        assertThat(geometry.getViewport(), is(new GeographicArea(GeographicLocation.fromValues(37.4196344, -122.0882466), GeographicLocation.fromValues(37.4259296, -122.0819514))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfLocationIsNullInBuilder() {
        Geometry.newBuilder(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfLocationTypeIsSetToNullInBuilder() {
        Geometry.newBuilder(GeographicLocation.fromValues(37.422782, -122.085099))
                .setLocationType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfViewportIsSetToNullInBuilder() {
        Geometry.newBuilder(GeographicLocation.fromValues(37.422782, -122.085099))
                .setViewport(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfBoundsIsSetToNullInBuilder() {
        Geometry.newBuilder(GeographicLocation.fromValues(37.422782, -122.085099))
                .setBounds(null);
    }

    @Test
    public void equalsAndHashCodeShouldWorkCorrectly() {
        Geometry geometry = Geometry.newBuilder(GeographicLocation.fromValues(37.422782, -122.085099))
                .setLocationType(ROOFTOP)
                .setViewport(new GeographicArea(GeographicLocation.fromValues(37.4196344, -122.0882466), GeographicLocation.fromValues(37.4259296, -122.0819514)))
                .setBounds(new GeographicArea(GeographicLocation.fromValues(36.4196344, -123.0882466), GeographicLocation.fromValues(38.4259296, -121.0819514)))
                .build();
        Geometry otherGeometry = Geometry.newBuilder(GeographicLocation.fromValues(37.422782, -122.085099))
                .setLocationType(ROOFTOP)
                .setViewport(new GeographicArea(GeographicLocation.fromValues(37.4196344, -122.0882466), GeographicLocation.fromValues(37.4259296, -122.0819514)))
                .setBounds(new GeographicArea(GeographicLocation.fromValues(36.4196344, -123.0882466), GeographicLocation.fromValues(38.4259296, -121.0819514)))
                .build();
        Geometry geometryWithDifferentLocation = Geometry.newBuilder(GeographicLocation.fromValues(0, 0))
                .setLocationType(ROOFTOP)
                .setViewport(new GeographicArea(GeographicLocation.fromValues(37.4196344, -122.0882466), GeographicLocation.fromValues(37.4259296, -122.0819514)))
                .setBounds(new GeographicArea(GeographicLocation.fromValues(36.4196344, -123.0882466), GeographicLocation.fromValues(38.4259296, -121.0819514)))
                .build();
        Geometry geometryWithDifferentLocationType = Geometry.newBuilder(GeographicLocation.fromValues(37.422782, -122.085099))
                .setLocationType(APPROXIMATE)
                .setViewport(new GeographicArea(GeographicLocation.fromValues(37.4196344, -122.0882466), GeographicLocation.fromValues(37.4259296, -122.0819514)))
                .setBounds(new GeographicArea(GeographicLocation.fromValues(36.4196344, -123.0882466), GeographicLocation.fromValues(38.4259296, -121.0819514)))
                .build();
        Geometry geometryWithDifferentViewport = Geometry.newBuilder(GeographicLocation.fromValues(37.422782, -122.085099))
                .setLocationType(ROOFTOP)
                .setViewport(new GeographicArea(GeographicLocation.fromValues(0, 0), GeographicLocation.fromValues(0, 0)))
                .setBounds(new GeographicArea(GeographicLocation.fromValues(36.4196344, -123.0882466), GeographicLocation.fromValues(38.4259296, -121.0819514)))
                .build();
        Geometry geometryWithDifferentBounds = Geometry.newBuilder(GeographicLocation.fromValues(37.422782, -122.085099))
                .setLocationType(ROOFTOP)
                .setViewport(new GeographicArea(GeographicLocation.fromValues(37.4196344, -122.0882466), GeographicLocation.fromValues(37.4259296, -122.0819514)))
                .setBounds(new GeographicArea(GeographicLocation.fromValues(0, 0), GeographicLocation.fromValues(0, 0)))
                .build();

        assertFalse(geometry.equals(null));
        assertFalse(geometry.equals(new Object()));
        assertFalse(geometry.equals(geometryWithDifferentLocation));
        assertFalse(geometry.equals(geometryWithDifferentLocationType));
        assertFalse(geometry.equals(geometryWithDifferentViewport));
        assertFalse(geometry.equals(geometryWithDifferentBounds));
        assertTrue(geometry.equals(geometry));
        assertTrue(geometry.equals(otherGeometry));
        assertEquals(geometry.hashCode(), otherGeometry.hashCode());
    }

    @Test
    public void shouldConvertToString() {
        Geometry geometry = Geometry.newBuilder(GeographicLocation.fromValues(37.422782, -122.085099))
                .setLocationType(ROOFTOP)
                .setViewport(new GeographicArea(GeographicLocation.fromValues(37.4196344, -122.0882466), GeographicLocation.fromValues(37.4259296, -122.0819514)))
                .setBounds(new GeographicArea(GeographicLocation.fromValues(36.4196344, -123.0882466), GeographicLocation.fromValues(38.4259296, -121.0819514)))
                .build();

        assertThat(geometry.toString(), is("location: {latitude: {value: \"37.422782\", degrees: \"37\", minutes: \"25\", seconds: \"22\", millis: \"15\", cardinalDirection: \"NORTH\"}, longitude: {value: \"-122.085099\", degrees: \"122\", minutes: \"5\", seconds: \"6\", millis: \"356\", cardinalDirection: \"WEST\"}, elevation: \"0\"}, locationType: \"ROOFTOP\", viewport: {southWestCorner: {latitude: {value: \"37.4196344\", degrees: \"37\", minutes: \"25\", seconds: \"10\", millis: \"683\", cardinalDirection: \"NORTH\"}, longitude: {value: \"-122.0882466\", degrees: \"122\", minutes: \"5\", seconds: \"17\", millis: \"687\", cardinalDirection: \"WEST\"}, elevation: \"0\"}, northEastCorner: {latitude: {value: \"37.4259296\", degrees: \"37\", minutes: \"25\", seconds: \"33\", millis: \"346\", cardinalDirection: \"NORTH\"}, longitude: {value: \"-122.0819514\", degrees: \"122\", minutes: \"4\", seconds: \"55\", millis: \"25\", cardinalDirection: \"WEST\"}, elevation: \"0\"}}, bounds: {southWestCorner: {latitude: {value: \"36.4196344\", degrees: \"36\", minutes: \"25\", seconds: \"10\", millis: \"683\", cardinalDirection: \"NORTH\"}, longitude: {value: \"-123.0882466\", degrees: \"123\", minutes: \"5\", seconds: \"17\", millis: \"687\", cardinalDirection: \"WEST\"}, elevation: \"0\"}, northEastCorner: {latitude: {value: \"38.4259296\", degrees: \"38\", minutes: \"25\", seconds: \"33\", millis: \"346\", cardinalDirection: \"NORTH\"}, longitude: {value: \"-121.0819514\", degrees: \"121\", minutes: \"4\", seconds: \"55\", millis: \"25\", cardinalDirection: \"WEST\"}, elevation: \"0\"}}"));
    }
}
