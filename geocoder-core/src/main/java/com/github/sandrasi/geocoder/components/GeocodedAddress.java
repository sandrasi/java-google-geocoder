package com.github.sandrasi.geocoder.components;

import java.io.Serializable;
import java.util.*;

import com.google.common.collect.Iterators;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static com.github.sandrasi.geocoder.coordinate.GeographicCoordinate.*;

/**
 * {@code GeocodedAddress} is the result of an address geocoding or location lookup.
 */
public final class GeocodedAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String formattedAddress;
    private final Set<AddressComponentType> addressTypes;
    private final Map<AddressComponentType, List<AddressComponent>> addressComponents;
    private final Geometry geometry;
    private final boolean partialMatch;

    private GeocodedAddress(Builder builder) {
        this.formattedAddress = builder.formattedAddress;
        this.addressTypes = Collections.unmodifiableSet(builder.addressTypes);
        this.addressComponents = Collections.unmodifiableMap(builder.addressComponents);
        this.geometry = (builder.geometry != null) ? builder.geometry : Geometry.newBuilder(GeographicLocation.fromCoordinates(EQUATOR, PRIME_MERIDIAN)).build();
        this.partialMatch = builder.partialMatch;
    }

    /**
     * Creates a new geocoded address builder with the specified formatted address.
     *
     * @param formattedAddress the address formatted by the geocoding service
     * @throws IllegalArgumentException if {@code formattedAddress} is {@code null}
     * @return a new instance of {@link GeocodedAddress.Builder}
     */
    public static Builder newBuilder(String formattedAddress) {
        return new Builder(formattedAddress);
    }

    /**
     * Returns the formatted address contained in this geocoded address.
     *
     * @return the formatted address
     */
    public String getFormattedAddress() {
        return formattedAddress;
    }

    /**
     * Returns an iterator that iterates on all the address types that describe this
     * geocoded address.
     * <p>
     * <i>Note that the iterator is unmodifiable so any attempt trying to remove the last
     * retrieved address type will throw an exception.</i>
     *
     * @return an address component type iterator
     */
    public Iterator<AddressComponentType> addressTypeIterator() {
        return Iterators.unmodifiableIterator(addressTypes.iterator());
    }

    /**
     * Checks if this geocoded address contains any address component of the given type.
     *
     * @param addressComponentType an address component type
     * @return {@code true} if this geocoded address contains at least one address
     * component of the given type; {@code false} otherwise
     */
    public boolean hasAddressComponent(AddressComponentType addressComponentType) {
        return addressComponents.containsKey(addressComponentType);
    }

    /**
     * Returns an iterator that iterates on the address components of the given type in this
     * geocoded address.
     * <p>
     * <i>Note that the iterator is unmodifiable so any attempt trying to remove the
     * last retrieved address component will throw an exception.</i>
     *
     * @param addressComponentType the type of the address components to iterate on
     * @return an address component iterator
     */
    public Iterator<AddressComponent> addressComponentIterator(AddressComponentType addressComponentType) {
        if (addressComponents.containsKey(addressComponentType)) {
            return Iterators.unmodifiableIterator(addressComponents.get(addressComponentType).iterator());
        } else {
            return Iterators.emptyIterator();
        }
    }

    /**
     * Returns an iterator that iterates on all the address components contained in this
     * geocoded address.
     * <p>
     * <i>Note that the iterator is unmodifiable so any attempt trying to remove the
     * last retrieved address component will throw an exception.</i>
     *
     * @return an address component iterator
     */
    public Iterator<AddressComponent> addressComponentIterator() {
        return Iterators.unmodifiableIterator(getUniqueAddressComponents().iterator());
    }

    /**
     * Returns the geometry that describes this geocoded address.
     *
     * @return the geometry of this geocoded address
     */
    public Geometry getGeometry() {
        return geometry;
    }

    /**
     * Returns if this geocoded address is a partial match only.
     *
     * @return {@code true} if this geocoded address is a partion match;{@code false} otherwise
     */
    public boolean isPartialMatch() {
        return partialMatch;
    }

    /**
     * Compares the specified object with this {@code GeocodedAddress} for equality.
     * Returns {@code true} if the given object is also a geocoded address and the formatted
     * address, the address types, the address components, the geometry and the partial match
     * flag containted in the two geocoded addresses are equal.
     *
     * @param o object to be compared for equality with this {@code GeocodedAddress}
     * @return {@code true} if the specified object is equal to this geocoded address;
     * {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    /**
     * Returns the hash code value for this {@code GeocodedAddress}. The hash code of a geocoded
     * address is calculated from the contained formatted address, address types, address components,
     * geometry and partial match flag.
     *
     * @return hash code value for this {@code GeocodedAddress}
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * Returns the textual representation of this {@code GeocodedAddress}.
     *
     * @return this {@code GeocodedAddress} in string format
     */
    @Override
    public String toString() {
        String addressTypesString = (!addressTypes.isEmpty() ? ("\"" + StringUtils.join(addressTypes, "\", \"") + "\"") : "");
        String addressComponentsString = (!addressComponents.isEmpty() ? ("{" + StringUtils.join(getUniqueAddressComponents(), "}, {") + "}") : "");

        return String.format("formattedAddress: \"%s\", addressTypes: [%s], addressComponents: [%s], geometry: {%s}, partialMatch: \"%b\"",
                formattedAddress, addressTypesString, addressComponentsString, geometry, partialMatch);
    }

    private List<AddressComponent> getUniqueAddressComponents() {
        List<AddressComponent> uniqueAddressComponents = new ArrayList<>();

        for (List<AddressComponent> addressComponentList : addressComponents.values()) {
            for (AddressComponent addressComponent : addressComponentList) {
                if (!uniqueAddressComponents.contains(addressComponent)) {
                    uniqueAddressComponents.addAll(addressComponentList);
                }
            }
        }

        return uniqueAddressComponents;
    }

    /**
     * A factory class to construct a new {@link GeocodedAddress} with or without specifying address
     * types, the address components or the geometry information. The method calls used to build the
     * geocoded address can be chained.
     */
    public static final class Builder {

        private final String formattedAddress;
        private final Set<AddressComponentType> addressTypes = new TreeSet<>();
        private final Map<AddressComponentType, List<AddressComponent>> addressComponents = new TreeMap<>();
        private Geometry geometry;
        private boolean partialMatch = false;

        private Builder(String formattedAddress) {
            Validate.notNull(formattedAddress, "formattedAddress is required");

            this.formattedAddress = formattedAddress;
        }

        /**
         * Instantiates a new {@link GeocodedAddress} with the formatted address, the address
         * types, the address components and geometry information set in this builder. In case the
         * geometry is not set in this builder the geometry will be a default one with location set to
         * the intersection of the Equator and the Prime meridian in the instantiated
         * {@code GeocodedAddress}.
         *
         * @return a new instance of {@link GeocodedAddress}
         */
        public GeocodedAddress build() {
            for (AddressComponentType  addressComponentType : addressComponents.keySet()) {
                addressComponents.put(addressComponentType, Collections.unmodifiableList(addressComponents.get(addressComponentType)));
            }

            return new GeocodedAddress(this);
        }

        /**
         * Adds the given {@code addressType} to the types of the geocoded address built by
         * this builder.
         *
         * @param addressType the type of the geocoded address
         * @throws IllegalArgumentException if {@code addressType} is {@code null}
         * @return a reference to this {@code Builder}
         */
        public Builder addAddressType(AddressComponentType addressType) {
            Validate.notNull(addressType, "addressType is required");

            addressTypes.add(addressType);

            return this;
        }

        /**
         * Adds all of the given address types to the types of the geocoded address built by this
         * builder.
         *
         * @param addressTypes the types of the geocoded address
         * @throws IllegalArgumentException if {@code addressTypes} or any of its elements is {@code null}
         * @return a reference to this {@code Builder}
         */
        public Builder addAddressTypes(List<AddressComponentType> addressTypes) {
            Validate.noNullElements(addressTypes, "addressTypes is required with non-null elements");

            this.addressTypes.addAll(addressTypes);

            return this;
        }

        /**
         * Adds the given {@code addressComponent} to the geocoded address built by this
         * builder.
         *
         * @param addressComponent an address component
         * @throws IllegalArgumentException if {@code addressComponent} is {@code null}
         * @return a reference to this {@code Builder}
         */
        public Builder addAddressComponent(AddressComponent addressComponent) {
            Validate.notNull(addressComponent, "addressComponent is required");

            for (AddressComponentType addressComponentType : addressComponent.getAddressComponentTypes()) {
                List<AddressComponent> addressComponentsForASpecificType;

                if (!addressComponents.containsKey(addressComponentType)) {
                    addressComponentsForASpecificType = new ArrayList<>();

                    addressComponents.put(addressComponentType, addressComponentsForASpecificType);
                } else {
                    addressComponentsForASpecificType = addressComponents.get(addressComponentType);
                }

                addressComponentsForASpecificType.add(addressComponent);
            }

            return this;
        }

        /**
         * Adds all of the given address components to the components of the geocoded address built
         * by this builder.
         *
         * @param addressComponents the components of the geocoded address
         * @throws IllegalArgumentException if {@code addressComponents} or any of its elements
         * is {@code null}
         * @return a reference to this {@code Builder}
         */
        public Builder addAddressComponents(List<AddressComponent> addressComponents) {
            Validate.noNullElements(addressComponents, "addressComponents is required with non-null elements");

            for (AddressComponent addressComponent : addressComponents) {
                addAddressComponent(addressComponent);
            }

            return this;
        }

        /**
         * Sets the geometry information of the geocoded address built by this builder.
         *
         * @param geometry the geometry of the geocoded address
         * @throws IllegalArgumentException if {@code geometry} is {@code null}
         * @return a reference to this {@code Builder}
         */
        public Builder setGeometry(Geometry geometry) {
            Validate.notNull(geometry, "geometry is required");

            this.geometry = geometry;

            return this;
        }

        /**
         * Indicates that the {@code GeocodedAddress} built by this builder only
         * partially matches the geocoded address or the looked up location.
         *
         * @return a reference to this {@code Builder}
         */
        public Builder partialMatch() {
            partialMatch = true;

            return this;
        }
    }
}
