package com.github.sandrasi.geocoder.components;

import java.io.Serializable;
import java.math.BigDecimal;

import com.github.sandrasi.geocoder.coordinate.GeographicCoordinate;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * {@code GeographicLocation} represents a point on the map with latitude and longitude coordinates and elevation.
 */
public final class GeographicLocation implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int PRECISION = 16;

    private final GeographicCoordinate latitude;
    private final GeographicCoordinate longitude;
    private final BigDecimal elevation;

    private GeographicLocation(GeographicCoordinate latitude, GeographicCoordinate longitude, BigDecimal elevation) {
        Validate.notNull(latitude, "latitude is required");
        Validate.notNull(longitude, "longitude is required");
        Validate.notNull(elevation, "elevation is required");

        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
    }

    /**
     * Constructs a new {@code GeographicLocation} with the given numeric coordinates and zero elevation.
     *
     * @param latitude the geographic latitude of the location
     * @param longitude the geographic longitude of the location
     * @throws IllegalArgumentException if {@code latitude} or {@code longitude} is an invalid geographic coordinate
     */
    public static GeographicLocation fromValues(double latitude, double longitude) {
        return fromValues(latitude, longitude, 0);
    }

    /**
     * Constructs a new {@code GeographicLocation} with the given numeric coordinates and elevation.
     *
     * @param latitude the geographic latitude of the location
     * @param longitude the geographic longitude of the location
     * @param elevation the elevation of the location
     * @throws IllegalArgumentException if {@code latitude} or {@code longitude} is an invalid geographic coordinate
     */
    public static GeographicLocation fromValues(double latitude, double longitude, double elevation) {
        GeographicCoordinate lat = GeographicCoordinate.latitudeFromDouble(latitude);
        GeographicCoordinate lng = GeographicCoordinate.longitudeFromDouble(longitude);

        return fromCoordinates(lat, lng, new BigDecimal(elevation));
    }

    /**
     * Constructs a new {@code GeographicLocation} with the given latitude and longitude coordinates and zero elevation.
     *
     * @param latitude the geographic latitude of the location
     * @param longitude the geographic longitude of the location
     * @throws IllegalArgumentException if {@code latitude} or {@code longitude} is {@code null}
     */
    public static GeographicLocation fromCoordinates(GeographicCoordinate latitude, GeographicCoordinate longitude) {
        return fromCoordinates(latitude, longitude, BigDecimal.ZERO);
    }

    /**
     * Constructs a new {@code GeographicLocation} with the given latitude and longitude coordinates and elevation.
     *
     * @param latitude the geographic latitude of the location
     * @param longitude the geographic longitude of the location
     * @param elevation the elevation of the location
     * @throws IllegalArgumentException if any of {@code latitude}, {@code longitude} or {@code elevation} is {@code null}
     */
    public static GeographicLocation fromCoordinates(GeographicCoordinate latitude, GeographicCoordinate longitude, BigDecimal elevation) {
        Validate.notNull(latitude, "latitude is required");
        Validate.notNull(longitude, "longitude is required");
        Validate.notNull(elevation, "elevation is required");

        return new GeographicLocation(latitude, longitude, elevation.setScale(PRECISION, BigDecimal.ROUND_HALF_EVEN));
    }

    /**
     * Returns the geographic latitude of this location.
     *
     * @return the value of the geographic latitude of this location
     */
    public double getLatitude() {
        return latitude.getDoubleValue();
    }

    /**
     * Returns the geographic longitude of this location.
     *
     * @return the value of the geographic longitude of this location
     */
    public double getLongitude() {
        return longitude.getDoubleValue();
    }

    /**
     * Returns the elevation of this location.
     *
     * @return the elevation of this location
     */
    public double getElevation() {
        return elevation.doubleValue();
    }

    /**
     * Compares the specified object with this {@code GeographicLocation} for equality.
     * Returns {@code true} if the given object is also a geographic location and the coordinates
     * and the elevation representing the two geographic locations are equal.
     *
     * @param o object to be compared for equality with this {@code GeographicLocation}
     * @return {@code true} if the specified object is equal to this geographic location; {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    /**
     * Returns the hash code value for this {@code GeographicLocation}. The hash code of a geographic location is
     * calculated from its coordinates and elevation.
     *
     * @return hash code value for this {@code GeographicLocation}
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * Returns the textual representation of this {@code GeographicLocation}.
     *
     * @return this {@code GeographicLocation} in string format
     */
    @Override
    public String toString() {
        String elevationString =  (elevation.compareTo(BigDecimal.ZERO) != 0) ? elevation.stripTrailingZeros().toPlainString() : "0";

        return String.format("latitude: {%s}, longitude: {%s}, elevation: \"%s\"",
                latitude, longitude, elevationString);
    }
}
