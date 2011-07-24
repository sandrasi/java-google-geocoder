package com.github.sandrasi.geocoder;

/**
 * {@code GeocodeRequest} represents a geocodable address or geographic coordinates to lookup an address.
 */
public interface GeocodeRequest {

    /**
     * Performs this geocoding request.
     *
     * @return the result of the geocoding
     */
    GeocodeResponse execute();

    /**
     * {@code Builder} is common interface to build different geocoding request.
     *
     * @param <T> the type of the geocode request the implementing builder builds
     */
    interface Builder<T extends GeocodeRequest> {

        /**
         * Instantiates a new {@link GeocodeRequest}.
         *
         * @return a new instance of {@link GeocodeRequest}
         */
        T build();
    }
}
