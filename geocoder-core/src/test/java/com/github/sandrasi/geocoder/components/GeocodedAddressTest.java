package com.github.sandrasi.geocoder.components;

import static com.github.sandrasi.geocoder.components.AddressComponentType.COUNTRY;
import static com.github.sandrasi.geocoder.components.AddressComponentType.ESTABLISHMENT;
import static com.github.sandrasi.geocoder.components.AddressComponentType.LOCALITY;
import static com.github.sandrasi.geocoder.components.AddressComponentType.POLITICAL;
import static com.github.sandrasi.geocoder.components.AddressComponentType.STREET_ADDRESS;
import static com.github.sandrasi.geocoder.components.LocationType.ROOFTOP;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Test;

public class GeocodedAddressTest {

    @Test
    public void shouldBuildGeocodedAddress() {
        Geometry geometry = Geometry.newBuilder(GeographicLocation.fromValues(37.422782, -122.085099))
                .setLocationType(ROOFTOP)
                .setViewport(new GeographicArea(GeographicLocation.fromValues(37.4196344, -122.0882466), GeographicLocation.fromValues(37.4259296, -122.0819514)))
                .build();
        GeocodedAddress geocodedAddress = GeocodedAddress.newBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA")
                .addAddressType(STREET_ADDRESS)
                .addAddressComponent(AddressComponent.newBuilder(COUNTRY).setLongName("United States").setShortName("US").build())
                .addAddressComponents(Arrays.asList(AddressComponent.newBuilder(LOCALITY).setLongName("Mountain View").setShortName("Mountain View").build()))
                .setGeometry(geometry)
                .build();

        assertThat(geocodedAddress.getFormattedAddress(), is("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA"));
        assertThat(geocodedAddress.addressTypeIterator().next(), is(STREET_ADDRESS));
        assertTrue(geocodedAddress.hasAddressComponent(COUNTRY));
        assertThat(geocodedAddress.addressComponentIterator(COUNTRY).next(), is(AddressComponent.newBuilder(COUNTRY).setLongName("United States").setShortName("US").build()));
        assertTrue(geocodedAddress.hasAddressComponent(LOCALITY));
        assertThat(geocodedAddress.addressComponentIterator(LOCALITY).next(), is(AddressComponent.newBuilder(LOCALITY).setLongName("Mountain View").setShortName("Mountain View").build()));
        assertThat(geocodedAddress.getGeometry(), is(geometry));
        assertFalse(geocodedAddress.isPartialMatch());
    }

    @Test
    public void shouldReturnEmptyAddressTypeIteratorIfNoAddressTypeIsSet() {
        GeocodedAddress geocodedAddress = GeocodedAddress.newBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA")
                .build();

        Iterator<AddressComponentType> addressTypeIterator = geocodedAddress.addressTypeIterator();

        assertFalse(addressTypeIterator.hasNext());
    }

    @Test
    public void shouldReturnMultipleAddressComponentsIfMoreAddressComponentsHaveTheSameType() {
        GeocodedAddress geocodedAddress = GeocodedAddress.newBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA")
                .addAddressComponent(AddressComponent.newBuilder(COUNTRY).addAddressComponentType(POLITICAL).setLongName("United States").setShortName("US").build())
                .addAddressComponent(AddressComponent.newBuilder(LOCALITY).addAddressComponentType(POLITICAL).setLongName("Mountain View").setShortName("Mountain View").build())
                .build();

        Iterator<AddressComponent> addressComponentIterator = geocodedAddress.addressComponentIterator(POLITICAL);

        assertThat(addressComponentIterator.next(), is(AddressComponent.newBuilder(COUNTRY).addAddressComponentType(POLITICAL).setLongName("United States").setShortName("US").build()));
        assertThat(addressComponentIterator.next(), is(AddressComponent.newBuilder(LOCALITY).addAddressComponentType(POLITICAL).setLongName("Mountain View").setShortName("Mountain View").build()));
        assertFalse(addressComponentIterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldReturnEmptyAddressComponentIteratorIfAddressComponentTypeIsNotAvailableInTheGeocodedAddress() {
        GeocodedAddress geocodedAddress = GeocodedAddress.newBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA").build();
        Iterator<AddressComponent> addressComponentIterator = geocodedAddress.addressComponentIterator(LOCALITY);

        assertFalse(geocodedAddress.hasAddressComponent(LOCALITY));
        assertFalse(addressComponentIterator.hasNext());
        addressComponentIterator.next();
    }

    @Test
    public void shouldIterateOnAllTheAddressComponents() {
        GeocodedAddress geocodedAddress = GeocodedAddress.newBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA")
                .addAddressComponent(AddressComponent.newBuilder(COUNTRY).setLongName("United States").setShortName("US").build())
                .addAddressComponent(AddressComponent.newBuilder(LOCALITY).setLongName("Mountain View").setShortName("Mountain View").build())
                .build();

        Iterator<AddressComponent> addressComponentIterator = geocodedAddress.addressComponentIterator();

        assertThat(addressComponentIterator.next(), is(AddressComponent.newBuilder(COUNTRY).setLongName("United States").setShortName("US").build()));
        assertThat(addressComponentIterator.next(), is(AddressComponent.newBuilder(LOCALITY).setLongName("Mountain View").setShortName("Mountain View").build()));
        assertFalse(addressComponentIterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldReturnEmptyAddressComponentIteratorIfThereAreNoAddressComponentsInTheGeocodedAddress() {
        GeocodedAddress geocodedAddress = GeocodedAddress.newBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA").build();
        Iterator<AddressComponent> addressComponentIterator = geocodedAddress.addressComponentIterator();

        assertFalse(addressComponentIterator.hasNext());
        addressComponentIterator.next();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldReturnUnmodifiableIteratorForGivenAddressComponentType() {
        GeocodedAddress geocodedAddress = GeocodedAddress.newBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA")
                .addAddressComponent(AddressComponent.newBuilder(COUNTRY).setLongName("United States").setShortName("US").build())
                .build();

        Iterator<AddressComponent> addressComponentIterator = geocodedAddress.addressComponentIterator(COUNTRY);

        addressComponentIterator.next();
        addressComponentIterator.remove();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldReturnUnmodifiableIteratorForAllAddressComponentTypes() {
        GeocodedAddress geocodedAddress = GeocodedAddress.newBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA")
                .addAddressComponent(AddressComponent.newBuilder(COUNTRY).setLongName("United States").setShortName("US").build())
                .build();

        Iterator<AddressComponent> addressComponentIterator = geocodedAddress.addressComponentIterator();

        addressComponentIterator.next();
        addressComponentIterator.remove();
    }

    @Test
    public void shouldReturnZeroGeometryIfItIsNotSetInBuilder() {
        GeocodedAddress geocodedAddress = GeocodedAddress.newBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA").build();

        assertThat(geocodedAddress.getGeometry(), is(Geometry.newBuilder(GeographicLocation.fromValues(0, 0)).build()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfFormattedAddressIsNullInBuilder() {
        GeocodedAddress.newBuilder(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfAddedAddressTypeIsNullInBuilder() {
        GeocodedAddress.newBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA")
                .addAddressType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfAddedAddressTypesAreNullInBuilder() {
        GeocodedAddress.newBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA")
                .addAddressTypes(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfAddedAddressTypesContainNullElementInBuilder() {
        GeocodedAddress.newBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA")
                .addAddressTypes(Arrays.asList((AddressComponentType) null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfAddedAddressComponentIsNullInBuilder() {
        GeocodedAddress.newBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA")
                .addAddressComponent(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfAddedAddressComponentsAreNullInBuilder() {
        GeocodedAddress.newBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA")
                .addAddressComponents(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfAddedAddressComponentsContainNullElementInBuilder() {
        GeocodedAddress.newBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA")
                .addAddressComponents(Arrays.asList((AddressComponent) null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfGeometryIsSetToNullInBuilder() {
        GeocodedAddress.newBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA")
                .setGeometry(null);
    }

    @Test
    public void equalsAndHashCodeShouldWorkCorrectly() {
        AddressComponent addressComponent = AddressComponent.newBuilder(COUNTRY).setLongName("United States").setShortName("US").build();
        Geometry geometry = Geometry.newBuilder(GeographicLocation.fromValues(37.422782, -122.085099))
                .setLocationType(ROOFTOP)
                .setViewport(new GeographicArea(GeographicLocation.fromValues(37.4196344, -122.0882466), GeographicLocation.fromValues(37.4259296, -122.0819514)))
                .build();
        Geometry otherGeometry = Geometry.newBuilder(GeographicLocation.fromValues(37.09024, -95.712891))
                .setLocationType(ROOFTOP)
                .setViewport(new GeographicArea(GeographicLocation.fromValues(18.7763, 170.5957), GeographicLocation.fromValues(71.5388, -66.8850749)))
                .build();

        GeocodedAddress geocodedAddress = GeocodedAddress.newBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA")
                .addAddressType(STREET_ADDRESS)
                .addAddressComponent(addressComponent)
                .setGeometry(geometry)
                .build();
        GeocodedAddress otherGeocodedAddress = GeocodedAddress.newBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA")
                .addAddressType(STREET_ADDRESS)
                .addAddressComponent(addressComponent)
                .setGeometry(geometry)
                .build();
        GeocodedAddress geocodedAddressWithDifferentFormattedAddress = GeocodedAddress.newBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, United States")
                .addAddressType(STREET_ADDRESS)
                .addAddressComponent(addressComponent)
                .setGeometry(geometry)
                .build();
        GeocodedAddress geocodedAddressWithDifferentAddressTypes = GeocodedAddress.newBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, United States")
                .addAddressType(ESTABLISHMENT)
                .addAddressComponent(addressComponent)
                .setGeometry(geometry)
                .build();
        GeocodedAddress geocodedAddressWithDifferentAddressComponents = GeocodedAddress.newBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA")
                .addAddressType(STREET_ADDRESS)
                .addAddressComponent(addressComponent)
                .addAddressComponent(AddressComponent.newBuilder(LOCALITY).setLongName("Mountain View").setShortName("Mountain View").build())
                .setGeometry(geometry)
                .build();
        GeocodedAddress geocodedAddressWithDifferentGeometry = GeocodedAddress.newBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA")
                .addAddressType(STREET_ADDRESS)
                .addAddressComponent(addressComponent)
                .setGeometry(otherGeometry)
                .build();
        GeocodedAddress partiallyMatchingGeocodedAddress = GeocodedAddress.newBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA")
                .addAddressType(STREET_ADDRESS)
                .addAddressComponent(addressComponent)
                .setGeometry(geometry)
                .partialMatch()
                .build();

        assertFalse(geocodedAddress.equals(null));
        assertFalse(geocodedAddress.equals(new Object()));
        assertFalse(geocodedAddress.equals(geocodedAddressWithDifferentFormattedAddress));
        assertFalse(geocodedAddress.equals(geocodedAddressWithDifferentAddressTypes));
        assertFalse(geocodedAddress.equals(geocodedAddressWithDifferentAddressComponents));
        assertFalse(geocodedAddress.equals(geocodedAddressWithDifferentGeometry));
        assertFalse(geocodedAddress.equals(partiallyMatchingGeocodedAddress));
        assertTrue(geocodedAddress.equals(geocodedAddress));
        assertTrue(geocodedAddress.equals(otherGeocodedAddress));
        assertEquals(geocodedAddress.hashCode(), otherGeocodedAddress.hashCode());
    }

    @Test
    public void shouldConvertToString() {
        Geometry geometry = Geometry.newBuilder(GeographicLocation.fromValues(37.422782, -122.085099))
                .setLocationType(ROOFTOP)
                .setViewport(new GeographicArea(GeographicLocation.fromValues(37.4196344, -122.0882466), GeographicLocation.fromValues(37.4259296, -122.0819514)))
                .build();
        GeocodedAddress geocodedAddress = GeocodedAddress.newBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA")
                .addAddressType(STREET_ADDRESS)
                .addAddressComponent(AddressComponent.newBuilder(COUNTRY).addAddressComponentType(POLITICAL).setLongName("United States").setShortName("USA").build())
                .addAddressComponent(AddressComponent.newBuilder(LOCALITY).addAddressComponentType(POLITICAL).setLongName("Mountain View").setShortName("Mountain View").build())
                .setGeometry(geometry)
                .build();
        GeocodedAddress geocodedAddressWithoutAddressTypesAndAddressComponents = GeocodedAddress.newBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA")
                .setGeometry(geometry)
                .build();

        assertThat(geocodedAddress.toString(), is("formattedAddress: \"1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\", addressTypes: [\"STREET_ADDRESS\"], addressComponents: [{addressComponentTypes: [\"COUNTRY\", \"POLITICAL\"], longName: \"United States\", shortName: \"USA\"}, {addressComponentTypes: [\"LOCALITY\", \"POLITICAL\"], longName: \"Mountain View\", shortName: \"Mountain View\"}], geometry: {location: {latitude: {value: \"37.422782\", degrees: \"37\", minutes: \"25\", seconds: \"22\", millis: \"15\", cardinalDirection: \"NORTH\"}, longitude: {value: \"-122.085099\", degrees: \"122\", minutes: \"5\", seconds: \"6\", millis: \"356\", cardinalDirection: \"WEST\"}, elevation: \"0\"}, locationType: \"ROOFTOP\", viewport: {southWestCorner: {latitude: {value: \"37.4196344\", degrees: \"37\", minutes: \"25\", seconds: \"10\", millis: \"683\", cardinalDirection: \"NORTH\"}, longitude: {value: \"-122.0882466\", degrees: \"122\", minutes: \"5\", seconds: \"17\", millis: \"687\", cardinalDirection: \"WEST\"}, elevation: \"0\"}, northEastCorner: {latitude: {value: \"37.4259296\", degrees: \"37\", minutes: \"25\", seconds: \"33\", millis: \"346\", cardinalDirection: \"NORTH\"}, longitude: {value: \"-122.0819514\", degrees: \"122\", minutes: \"4\", seconds: \"55\", millis: \"25\", cardinalDirection: \"WEST\"}, elevation: \"0\"}}, bounds: {southWestCorner: {latitude: {value: \"37.4196344\", degrees: \"37\", minutes: \"25\", seconds: \"10\", millis: \"683\", cardinalDirection: \"NORTH\"}, longitude: {value: \"-122.0882466\", degrees: \"122\", minutes: \"5\", seconds: \"17\", millis: \"687\", cardinalDirection: \"WEST\"}, elevation: \"0\"}, northEastCorner: {latitude: {value: \"37.4259296\", degrees: \"37\", minutes: \"25\", seconds: \"33\", millis: \"346\", cardinalDirection: \"NORTH\"}, longitude: {value: \"-122.0819514\", degrees: \"122\", minutes: \"4\", seconds: \"55\", millis: \"25\", cardinalDirection: \"WEST\"}, elevation: \"0\"}}}, partialMatch: \"false\""));
        assertThat(geocodedAddressWithoutAddressTypesAndAddressComponents.toString(), is("formattedAddress: \"1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\", addressTypes: [], addressComponents: [], geometry: {location: {latitude: {value: \"37.422782\", degrees: \"37\", minutes: \"25\", seconds: \"22\", millis: \"15\", cardinalDirection: \"NORTH\"}, longitude: {value: \"-122.085099\", degrees: \"122\", minutes: \"5\", seconds: \"6\", millis: \"356\", cardinalDirection: \"WEST\"}, elevation: \"0\"}, locationType: \"ROOFTOP\", viewport: {southWestCorner: {latitude: {value: \"37.4196344\", degrees: \"37\", minutes: \"25\", seconds: \"10\", millis: \"683\", cardinalDirection: \"NORTH\"}, longitude: {value: \"-122.0882466\", degrees: \"122\", minutes: \"5\", seconds: \"17\", millis: \"687\", cardinalDirection: \"WEST\"}, elevation: \"0\"}, northEastCorner: {latitude: {value: \"37.4259296\", degrees: \"37\", minutes: \"25\", seconds: \"33\", millis: \"346\", cardinalDirection: \"NORTH\"}, longitude: {value: \"-122.0819514\", degrees: \"122\", minutes: \"4\", seconds: \"55\", millis: \"25\", cardinalDirection: \"WEST\"}, elevation: \"0\"}}, bounds: {southWestCorner: {latitude: {value: \"37.4196344\", degrees: \"37\", minutes: \"25\", seconds: \"10\", millis: \"683\", cardinalDirection: \"NORTH\"}, longitude: {value: \"-122.0882466\", degrees: \"122\", minutes: \"5\", seconds: \"17\", millis: \"687\", cardinalDirection: \"WEST\"}, elevation: \"0\"}, northEastCorner: {latitude: {value: \"37.4259296\", degrees: \"37\", minutes: \"25\", seconds: \"33\", millis: \"346\", cardinalDirection: \"NORTH\"}, longitude: {value: \"-122.0819514\", degrees: \"122\", minutes: \"4\", seconds: \"55\", millis: \"25\", cardinalDirection: \"WEST\"}, elevation: \"0\"}}}, partialMatch: \"false\""));
    }
}
