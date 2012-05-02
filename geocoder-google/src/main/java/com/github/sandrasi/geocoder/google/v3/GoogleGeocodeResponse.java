package com.github.sandrasi.geocoder.google.v3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.sandrasi.geocoder.GeocodeResponse;
import com.github.sandrasi.geocoder.components.GeocodeStatus;
import com.github.sandrasi.geocoder.components.GeocodedAddress;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static com.github.sandrasi.geocoder.components.GeocodeStatus.*;

/**
 * {@code GoogleGeocodeResponse} represents the result of an address lookup or a reverse geocoding.
 * The response contains the original address or the textual latitude / longitude values
 * sent to the Google Geocoding service, the status of the request and zero or more geocoded
 * addresses.
 */
public final class GoogleGeocodeResponse implements GeocodeResponse, Serializable {

    private static final long serialVersionUID = 1L;

    private final String queryString;
    private final GeocodeStatus geocodeStatus;
    private final List<GeocodedAddress> geocodedAddresses;

    private GoogleGeocodeResponse(Builder builder) {
        this.queryString = builder.queryString;
        this.geocodeStatus = builder.geocodeStatus;
        this.geocodedAddresses = Collections.unmodifiableList(builder.geocodedAddresses);
    }

    /**
     * Creates a new response-builder with the specified query string.
     *
     * @param queryString the original address or textual latitude / longitude values used in
     * the geocoding request
     * @throws IllegalArgumentException if {@code queryString} is {@code null}
     * @return a new instance of {@link GoogleGeocodeResponse.Builder}
     */
    public static Builder newBuilder(String queryString) {
        return new Builder(queryString);
    }

    /**
     * Returns the original address that was looked up or the textual latitude / longitude values
     * which were reverse geocoded.
     *
     * @return the original query sent in a request to the Google Geocoding service
     */
    @Override
    public String getQueryString() {
        return queryString;
    }

    /**
     * Returns the status of the geocoding request.
     *
     * @return the request status
     */
    @Override
    public GeocodeStatus getGeocodeStatus() {
        return geocodeStatus;
    }

    /**
     * Returns zero or more geocoded addresses. The returned list is unmodifiable.
     *
     * @return the list of geocoded addresses
     */
    @Override
    public List<GeocodedAddress> getGeocodedAddresses() {
        return geocodedAddresses;
    }

    /**
     * Compares the specified object with this {@code GoogleGeocodeResponse} for equality.
     * Returns {@code true} if the given object is also a geocode response and the statuses
     * and the geocoded addresses contained by the two geocode responses are equal.
     * <p>
     * <i>Note, that the original query used to get this response is not checked during the
     * comparison.</i>
     *
     * @param o object to be compared for equality with this {@code GoogleGeocodeResponse}
     * @return {@code true} if the specified object is equal to this geocode response; {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this != o) {
            if (o == null) {
                return false;
            }

            if (getClass() != o.getClass()) {
                return false;
            }

            GoogleGeocodeResponse other = (GoogleGeocodeResponse) o;

            if ((geocodeStatus != other.geocodeStatus) || !geocodedAddresses.equals(other.geocodedAddresses)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the hash code value for this {@code GoogleGeocodeResponse}. The hash code of a geocode response is
     * calculated from its status and the contained geocoded addresses.
     *
     * @return hash code value for this {@code GoogleGeocodeResponse}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(geocodeStatus).append(geocodedAddresses).hashCode();
    }

    /**
     * Returns the textual representation of this {@code GoogleGeocodeResponse}.
     *
     * @return this {@code GoogleGeocodeResponse} in string format
     */
    @Override
    public String toString() {
        String geocodedAddressString = !geocodedAddresses.isEmpty() ? ("{" + StringUtils.join(geocodedAddresses, "}, {") + "}") : "";

        return String.format("queryString: \"%s\", geocodeStatus: \"%s\", geocodedAddresses: [%s]",
                queryString, geocodeStatus, geocodedAddressString);
    }

    /**
     * A factory class to construct a new {@link GoogleGeocodeResponse} with or without specifying the geocoding status or adding
     * a geocoded address.
     * The method calls used to build the response can be chained.
     */
    public static final class Builder {

        private final String queryString;
        private GeocodeStatus geocodeStatus = INVALID_REQUEST;
        private final List<GeocodedAddress> geocodedAddresses = new ArrayList<>();

        private Builder(String queryString) {
            Validate.notNull(queryString, "queryString is required");

            this.queryString = queryString;
        }

        /**
         * Instantiates a new {@link GoogleGeocodeResponse} with the status and geocoded addresses
         * defined in this builder. In case the geocode status is not set in this builder the
         * instantiated {@code GoogleGeocodeResponse}'s status will be set to invalid.
         *
         * @return a new instance of {@link GoogleGeocodeRequest}
         */
        public GoogleGeocodeResponse build() {
            return new GoogleGeocodeResponse(this);
        }

        /**
         * Sets the status in the response built by this builder.
         *
         * @param geocodeStatus the status of the geocoding
         * @throws IllegalArgumentException if {@code geocodeStatus} is {@code null}
         * @return a reference to this {@code Builder}
         */
        public Builder setGeocodeStatus(GeocodeStatus geocodeStatus) {
            Validate.notNull(geocodeStatus, "geocodeStatus is required");

            this.geocodeStatus = geocodeStatus;

            return this;
        }

        /**
         * Adds the given geocoded address to the list of geocoded addresses in the response built by this builder.
         *
         * @param geocodedAddress a geocoded address
         * @throws IllegalArgumentException if {@code geocodedAddress} is {@code null}
         * @return a reference to this {@code Builder}
         */
        public Builder addGeocodedAddress(GeocodedAddress geocodedAddress) {
            Validate.notNull(geocodedAddress, "geocodedAddress is required");

            geocodedAddresses.add(geocodedAddress);

            return this;
        }

        /**
         * Adds all of the geocoded addresses to the list of geocoded addresses in the response built by this builder.
         *
         * @param geocodedAddresses a list of geocoded address
         * @throws IllegalArgumentException if {@code geocodedAddresses} is {@code null} or if it contains {@code null}
         * elements
         * @return a reference to this {@code Builder}
         */
        public Builder addGeocodedAddresses(List<GeocodedAddress> geocodedAddresses) {
            Validate.noNullElements(geocodedAddresses, "geocodedAddresses is required with non-null elements");

            this.geocodedAddresses.addAll(geocodedAddresses);

            return this;
        }
    }
}
