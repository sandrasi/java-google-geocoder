package com.github.sandrasi.geocoder;

/**
 * An address geocoder that turns addresses into geographic coordinates or geographic coordinates into addresses.
 *
 * @param <T> the type of the geocode request that the implementing class can geocode
 * @param <S> the type of the geocode request builder to build arbitrary geocode requests that
 * the implementing class can geocode
 */
public interface Geocoder<T extends GeocodeRequest, S extends GeocodeRequest.Builder<T>> {

    /**
     * Creates a new geocode request-builder with the specified address.
     *
     * @param address the address to be geocoded
     * @return a new instance of {@link GeocodeRequest.Builder}
     */
    S newGeocodeRequestBuilder(String address);

    /**
     * Creates a new geocode request-builder with the specified geographic coordinates.
     *
     * @param latitude the latitude coordinate of the location to be reverse geocoded
     * @param longitude the longitude coordinate of the location to be reverse geocoded
     * @return a new instance of {@link GeocodeRequest.Builder}
     */
    S newGeocodeRequestBuilder(double latitude, double longitude);

    /**
     * Translates a human-readable address into geographic location on a map. In case the address is ambiguous
     * multiple results might be returned.
     *
     * @param address the address to be geocoded
     * @return the result of the address geocoding
     */
    GeocodeResponse geocodeAddress(String address);

    /**
     * Reverse geocodes the coordinates. In other words it translates a location on the map into a human-readable
     * address. Reverse geocoding is normally an estimate, that is the geocoder attempts to find the closest
     * addressable location within a certain tolerance. The geocoder might return not only the most accurate address
     * but any other which geograhically names the location.
     *
     * @param latitude the latitude coordinate of the location
     * @param longitude the longitude coordinate of the location
     * @return the result of the address lookup
     */
    GeocodeResponse lookupAddress(double latitude, double longitude);

    /**
     * Geocodes an arbitrary address or looks up an arbitrary geographic location described as a
     * {@code GeocodeRequest}. In case of geocoding addresses the result should be the same as that of
     * returned by {@link #geocodeAddress(String)}. In case of address lookup the result should be the same
     * as that of returned by {@linkplain #lookupAddress(double, double)}.
     *
     * @param geocodeRequest a request representing a geocodable address or geographic location
     * @return the result of the geocoding
     */
    GeocodeResponse geocode(T geocodeRequest);

    /**
     * Closes this geocoder and releases the allocated resources.
     */
    void close();
}
