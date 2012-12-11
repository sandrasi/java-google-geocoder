package com.github.sandrasi.geocoder.components;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * An {@code AddressComponent} represents a small part of a complete address.
 * Several address components can be used to compose an address.
 */
public final class AddressComponent implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String longName;
    private final String shortName;
    private final Set<AddressComponentType> addressComponentTypes;

    private AddressComponent(Builder builder) {
        this.addressComponentTypes = Collections.unmodifiableSet(builder.addressComponentTypes);
        this.longName = builder.longName;
        this.shortName = builder.shortName;
    }

    /**
     * Creates a new address component builder with the specified address compoenent type.
     *
     * @param addressComponentType the type of the address component built by the builder
     * @throws NullPointerException if {@code addressComponentType} is {@code null}
     * @return a new instance of {@link AddressComponent.Builder}
     */
    public static Builder newBuilder(AddressComponentType addressComponentType) {
        return new Builder(addressComponentType);
    }

    /**
     * Returns the types of this address component.
     *
     * @return one or more address component types
     */
    public Set<AddressComponentType> getAddressComponentTypes() {
        return addressComponentTypes;
    }

    /**
     * Returns the full text description or name of the address component.
     * <i>For example, an address component for the state of Alaska may have a long name of "Alaska"</i>.
     *
     * @return the long name of this address component
     */
    public String getLongName() {
        return longName;
    }

    /**
     * Returns an abbreviated textual name for the address component, if available.
     * <i>For example, an address component for the state of Alaska may have a short_name of "AK"
     * using the 2-letter postal abbreviation</i>.
     *
     * @return the short name if this address component
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * Compares the specified object with this {@code AddressComponent} for equality.
     * Returns {@code true} if the given object is also an address component and the type of
     * the address component, the long name and the short name represented by the two address
     * components are equal.
     *
     * @param o object to be compared for equality with this {@code AddressComponent}
     * @return {@code true} if the specified object is equal to this address component;
     * {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    /**
     * Returns the hash code value for this {@code AddressComponent}. The hash code of an address
     * component is calculated from the type of the address component, its long name and its short name.
     *
     * @return hash code value for this {@code AddressComponent}
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * Returns the textual representation of this {@code AddressComponent}.
     *
     * @return this {@code AddressComponent} in string format
     */
    @Override
    public String toString() {
        return String.format("addressComponentTypes: [\"%s\"], longName: \"%s\", shortName: \"%s\"",
                StringUtils.join(addressComponentTypes, "\", \""), longName, shortName);
    }

    /**
     * A factory class to construct a new {@link AddressComponent} with or without specifying the long
     * or the short name. The method calls used to build the address component can be chained.
     */
    public static final class Builder {

        private final Set<AddressComponentType> addressComponentTypes = new TreeSet<>();
        private String longName = "";
        private String shortName = "";

        private Builder(AddressComponentType addressComponentType) {
            addAddressComponentType(addressComponentType);
        }

        /**
         * Instantiates a new {@link AddressComponent} with the type of the address component, the
         * long and the short name set in this builder. In case the short or the long name is
         * not set in this builder the short or the long name will be an empty string
         * in the instantiated {@code AddressComponent}.
         *
         * @return a new instance of {@link AddressComponent}
         */
        public AddressComponent build() {
            return new AddressComponent(this);
        }

        /**
         * Adds the given {@code addressComponentType} to the types of the address component
         * built by this builder.
         *
         * @param addressComponentType the type of the address component
         * @throws NullPointerException if {@code addressComponentType} is {@code null}
         * @return a reference to this {@code Builder}
         */
        public Builder addAddressComponentType(AddressComponentType addressComponentType) {
            Validate.notNull(addressComponentType, "addressComponentType is required");

            addressComponentTypes.add(addressComponentType);

            return this;
        }

        /**
         * Adds all of the given address component types to the types of the address component
         * built by this builder.
         *
         * @param addressComponentTypes the types of the address component
         * @throws NullPointerException if {@code addressComponentTypes} is {@code null}
         * @throws IllegalArgumentException if any element of {@code addressComponentTypes} is {@code null}
         * @return a reference to this {@code Builder}
         */
        public Builder addAddressComponentTypes(Collection<AddressComponentType> addressComponentTypes) {
            Validate.noNullElements(addressComponentTypes, "addressComponentTypes is required with non-null elements");

            this.addressComponentTypes.addAll(addressComponentTypes);

            return this;
        }

        /**
         * Sets the full name of the address component built by this builder.
         *
         * @param longName the full name of the address component
         * @throws NullPointerException if {@code longName} is {@code null}
         * @return a reference to this {@code Builder}
         */
        public Builder setLongName(String longName) {
            Validate.notNull(longName, "longName is required");

            this.longName = longName;

            return this;
        }

        /**
         * Sets the short name of the address component built by this builder.
         *
         * @param shortName the short name of the address component
         * @throws NullPointerException if {@code shortName} is {@code null}
         * @return a reference to this {@code Builder}
         */
        public Builder setShortName(String shortName) {
            Validate.notNull(shortName, "shortName is required");

            this.shortName = shortName;

            return this;
        }
    }
}
