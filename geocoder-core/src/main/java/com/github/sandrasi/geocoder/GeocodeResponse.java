package com.github.sandrasi.geocoder;

import java.util.List;

import com.github.sandrasi.geocoder.components.GeocodeStatus;
import com.github.sandrasi.geocoder.components.GeocodedAddress;

/**
 * {@code GeocodeResponse} represents the result of a geocoding query. It contains the textual format of the
 * original address that was geocoded or the geographic coordinates used to look up an address, the status of the
 * geocoding request and the one or more geocoded addresses.
 */
public interface GeocodeResponse {

    /**
     * Returns the original query string that was geocoded or looked up by the geocoding service.
     *
     * @return the textual format of the address which was geocoded or the geographic coordinates which
     * were used lookup an address
     */
    String getQueryString();

    /**
     * Returns the status of the geocoding geocoding query.
     */
    GeocodeStatus getGeocodeStatus();

    /**
     * Returns the geocoded address or the address which was looked up by the geographic coordinates. If the
     * geocoding query was ambiguous or the location represented by the geographic coordinates can be named
     * in many ways multiple addresses might be returned.
     *
     * @return the one or more result of the geocoding query
     */
    List<GeocodedAddress> getGeocodedAddresses();
}
