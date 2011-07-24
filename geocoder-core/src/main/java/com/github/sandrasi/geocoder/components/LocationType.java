package com.github.sandrasi.geocoder.components;

/**
 * {@code LocationType} is a marker interface to define the type of a location on a map.
 */
public enum LocationType {

    /**
     * Indicates that the location information is accurate down to street address precision.
     */
    ROOFTOP,

    /**
     * Indicates that the location information reflects an approximation (usually on a road)
     * interpolated between two precise points (such as intersections). Interpolated locations are generally used
     * when rooftop geocodes are unavailable for a street address.
     */
    RANGE_INTERPOLATED,

    /**
     * Indicates that the location is the geometric center of a polyline (for example, a street) or
     * a polygon (region).
     */
    GEOMETRIC_CENTER,

    /**
     * Indicates that the location information is approximate.
     */
    APPROXIMATE
}

