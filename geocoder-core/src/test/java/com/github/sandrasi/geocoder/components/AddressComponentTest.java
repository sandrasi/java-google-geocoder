package com.github.sandrasi.geocoder.components;

import static com.github.sandrasi.geocoder.components.AddressComponentType.NATURAL_FEATURE;
import static com.github.sandrasi.geocoder.components.AddressComponentType.POLITICAL;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.TreeSet;

import org.junit.Test;

public class AddressComponentTest {

    @Test
    public void shouldConstructAddressComponent() {
        AddressComponent addressComponent = AddressComponent.newBuilder(AddressComponentType.COUNTRY)
                .addAddressComponentType(POLITICAL)
                .addAddressComponentTypes(Arrays.asList(NATURAL_FEATURE))
                .setLongName("United States")
                .setShortName("US")
                .build();

        assertEquals(new TreeSet<AddressComponentType>(Arrays.asList(AddressComponentType.COUNTRY, AddressComponentType.POLITICAL, AddressComponentType.NATURAL_FEATURE)), addressComponent.getAddressComponentTypes());
        assertThat(addressComponent.getLongName(), is("United States"));
        assertThat(addressComponent.getShortName(), is("US"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfAddressComponentTypeIsNullInBuilder() {
        AddressComponent.newBuilder(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfAddedAddressComponentTypeIsNullInBuilder() {
        AddressComponent.newBuilder(AddressComponentType.COUNTRY).addAddressComponentType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfAddedAddressComponentTypesIsNullInBuilder() {
        AddressComponent.newBuilder(AddressComponentType.COUNTRY).addAddressComponentTypes(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfAddedAddressComponentTypesContainsNullElementInBuilder() {
        AddressComponent.newBuilder(AddressComponentType.COUNTRY).addAddressComponentTypes(Arrays.asList((AddressComponentType) null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfLongNameIsSetToNullBuilder() {
        AddressComponent.newBuilder(AddressComponentType.COUNTRY).setLongName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfShortNameIsSetToNullInBuilder() {
        AddressComponent.newBuilder(AddressComponentType.COUNTRY).setShortName(null);
    }

    @Test
    public void equalsAndHashCodeShouldWorkCorrectly() {
        AddressComponent addressComponent = AddressComponent.newBuilder(AddressComponentType.COUNTRY).setLongName("United States").setShortName("US").build();
        AddressComponent otherAddressComponent = AddressComponent.newBuilder(AddressComponentType.COUNTRY).setLongName("United States").setShortName("US").build();
        AddressComponent addressComponentWithDifferentAddressComponentType = AddressComponent.newBuilder(AddressComponentType.POLITICAL).setLongName("United States").setShortName("US").build();
        AddressComponent addressComponentWithDifferentLongName = AddressComponent.newBuilder(AddressComponentType.COUNTRY).setLongName("United States of America").setShortName("US").build();
        AddressComponent addressComponentWithDifferentShortName = AddressComponent.newBuilder(AddressComponentType.COUNTRY).setLongName("United States").setShortName("USA").build();

        assertFalse(addressComponent.equals(null));
        assertFalse(addressComponent.equals(new Object()));
        assertFalse(addressComponent.equals(addressComponentWithDifferentAddressComponentType));
        assertFalse(addressComponent.equals(addressComponentWithDifferentLongName));
        assertFalse(addressComponent.equals(addressComponentWithDifferentShortName));
        assertTrue(addressComponent.equals(addressComponent));
        assertTrue(addressComponent.equals(otherAddressComponent));
        assertEquals(addressComponent.hashCode(), otherAddressComponent.hashCode());
    }

    @Test
    public void shouldConvertToString() {
        AddressComponent addressComponent = AddressComponent.newBuilder(AddressComponentType.COUNTRY)
                .addAddressComponentType(AddressComponentType.POLITICAL)
                .setLongName("United States")
                .setShortName("US").build();

        assertThat(addressComponent.toString(), is("addressComponentTypes: [\"COUNTRY\", \"POLITICAL\"], longName: \"United States\", shortName: \"US\""));
    }
}
