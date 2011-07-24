package com.github.sandrasi.geocoder.coordinate;

/**
 * {@code CardinalDirection} represents the four main cardinal directions: north, south,
 * east and west. It also contains an enumerated value for the Equator which is neither north
 * nor south, and a value for the Prime Meridian which is neither east nor west.
 */
public enum CardinalDirection {

    /**
     * North
     */
    NORTH(GeographicCoordinateType.LATITUDE),

    /**
     * The Equator.
     */
    ZERO_LATITUDE_DEGREE(GeographicCoordinateType.LATITUDE),

    /**
     * South
     */
    SOUTH(GeographicCoordinateType.LATITUDE),

    /**
     * East
     */
    EAST(GeographicCoordinateType.LONGITUDE),

    /**
     * The Prime meridian
     */
    ZERO_LONGITUDE_DEGREE(GeographicCoordinateType.LONGITUDE),

    /**
     * West
     */
    WEST(GeographicCoordinateType.LONGITUDE);

    private final GeographicCoordinateType geographicCoordinateType;

    private CardinalDirection(GeographicCoordinateType geographicCoordinateType) {
        this.geographicCoordinateType = geographicCoordinateType;
    }

    /**
     * Returns this cardinal direction's coordinate type.
     *
     * @return {@link GeographicCoordinateType#LATITUDE} if this cardinal direction
     * is north, south or the Equator. {@link GeographicCoordinateType#LONGITUDE} if
     * this cardinal direction is east, west or the Prime meridian.
     */
    public GeographicCoordinateType getCoordinateType() {
        return geographicCoordinateType;
    }
}
