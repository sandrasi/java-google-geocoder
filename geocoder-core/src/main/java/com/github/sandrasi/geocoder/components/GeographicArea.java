package com.github.sandrasi.geocoder.components;

import java.io.Serializable;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * {@code GeographicArea} represents a rectangular area on a map described with its south west and
 * north east coordinates. The two corners need not be located to each other as their name implies.
 */
public class GeographicArea implements Serializable {

    private static final long serialVersionUID = 1L;

    private final GeographicLocation southWestCorner;
    private final GeographicLocation northEastCorner;

    /**
     * Constructs a new {@code GeographicAre} with the given coordinates.
     *
     * @param southWestCorner the lower left corner of the area
     * @param northEastCorner the upper right corner of the area
     * @throws IllegalArgumentException if {@code southWestCorner} or {@code northEastCorner} is {@code null}
     */
    public GeographicArea(GeographicLocation southWestCorner, GeographicLocation northEastCorner) {
        Validate.notNull(southWestCorner, "southWestCorner is required");
        Validate.notNull(northEastCorner, "northEastCorner is required");

        this.southWestCorner = southWestCorner;
        this.northEastCorner = northEastCorner;
    }

    /**
     * Returns the coordinate used to set the south west corner of this geographic area.
     *
     * @return a geographic location
     */
    public GeographicLocation getSouthWestCorner() {
        return southWestCorner;
    }

    /**
     * Returns the coordinate used to set the north east corner of this geographic area.
     *
     * @return a geographic location
     */
    public GeographicLocation getNorthEastCorner() {
        return northEastCorner;
    }

    /**
     * Compares the specified object with this {@code GeographicArea} for equality.
     * Returns {@code true} if the given object is also a geographic area and the south west
     * and north east corners representing the two geographic areas are equal.
     *
     * @param o object to be compared for equality with this {@code GeographicArea}
     * @return {@code true} if the specified object is equal to this geographic area; {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    /**
     * Returns the hash code value for this {@code GeographicArea}. The hash code of a geographic area is
     * calculated from its south west and north east corners.
     *
     * @return hash code value for this {@code GeographicArea}
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * Returns the textual representation of this {@code GeographicArea}.
     *
     * @return this {@code GeographicArea} in string format
     */
    @Override
    public String toString() {
        return String.format("southWestCorner: {%s}, northEastCorner: {%s}",
                southWestCorner, northEastCorner);
    }
}
