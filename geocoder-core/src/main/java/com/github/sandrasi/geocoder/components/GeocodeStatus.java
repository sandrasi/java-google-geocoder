package com.github.sandrasi.geocoder.components;

/**
 * {@code GeocodeStatus} contains enumerated values to describe the status of the geocoding operation.
 */
public enum GeocodeStatus {

    /**
     * Indicates that the query is invalid.
     */
    INVALID_REQUEST,

    /**
     * Indicates that no errors occurred; the address was successfully parsed and at least one geocode
     * result was returned.
     */
    OK,

    /**
     * Indicates that the geocoding request is over a specific quota.
     */
    OVER_QUERY_LIMIT,

    /**
     * Indicates that the geocode request was denie.
     */
    REQUEST_DENIED,

    /**
     * Indicates that the geocoding was successful but returned no results. This may occur if the
     * geocoding service was passed a non-existent address or a latitude-longitude pair in a remote
     * location.
     */
    ZERO_RESULTS
}
