package com.github.sandrasi.geocoder.components;

import static com.github.sandrasi.geocoder.components.LocationType.*;
import java.io.Serializable;
import com.github.sandrasi.geocoder.coordinate.GeographicCoordinateType.CoordinateConstants;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * {@code Geometry} represents geographic information about an address such as the location or
 * the size of the viewport to best display the location.
 */
public final class Geometry implements Serializable {

    private static final long serialVersionUID = 1L;

    private final GeographicLocation location;
    private final LocationType locationType;
    private final GeographicArea viewport;
    private final GeographicArea bounds;

    private Geometry(Builder builder) {
        this.location = builder.location;
        this.locationType = (builder.locationType != null) ? builder.locationType : APPROXIMATE;
        this.viewport = (builder.viewport != null) ? builder.viewport : new GeographicArea(GeographicLocation.fromValues(CoordinateConstants.MINIMUM_LATITUDE, CoordinateConstants.MINIMUM_LONGITUDE), GeographicLocation.fromValues(CoordinateConstants.MAXIMUM_LATITUDE, CoordinateConstants.MAXIMUM_LONGITUDE));
        this.bounds = (builder.bounds != null) ? builder.bounds : new GeographicArea(GeographicLocation.fromValues(0, 0), GeographicLocation.fromValues(0, 0));
    }

    /**
     * Creates a new geometry-builder with the specified geographic location.
     *
     * @param location the geographic location represented by the geometry built by the builder
     * @throws NullPointerException if {@code location} is {@code null}
     * @return a new instance of {@link Geometry.Builder}
     */
    public static Builder newBuilder(GeographicLocation location) {
        return new Builder(location);
    }

    /**
     * Returns the location represented by this geometry.
     *
     * @return a geographic location
     */
    public GeographicLocation getLocation() {
        return location;
    }

    /**
     * Returns the type of the location represented by this geometry.
     *
     * @return a location type
     */
    public LocationType getLocationType() {
        return locationType;
    }

    /**
     * Returns the recommended viewport size to display the location represented by this geometry.
     *
     * @return a geographic area
     */
    public GeographicArea getViewport() {
        return viewport;
    }

    /**
     * Returns the bounding box that fully contains this location.
     * <p>
     * <i>Note that these bounds may not match the recommended viewport. (For example,
     * San Francisco includes the Farallon islands, which are technically part of the city,
     * but probably should not be returned in the viewport.)</i>
     *
     * @return a geographic area
     */
    public GeographicArea getBounds() {
        return bounds;
    }

    /**
     * Compares the specified object with this {@code Geometry} for equality.
     * Returns {@code true} if the given object is also a geomtery and the location, the type of the
     * location, the viewport and the bounds contained by the two geometries are equal.
     *
     * @param o object to be compared for equality with this {@code Geometry}
     * @return {@code true} if the specified object is equal to this geometry; {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    /**
     * Returns the hash code value for this {@code Geometry}. The hash code of a geometry is
     * calculated from its location, the type of the location, the recommended viewport and the
     * bounds of the location.
     *
     * @return hash code value for this {@code Geometry}
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * Returns the textual representation of this {@code Geometry}
     *
     * @return this {@code Geometry} in string format
     */
    @Override
    public String toString() {
        return String.format("location: {%s}, locationType: \"%s\", viewport: {%s}, bounds: {%s}",
                location, locationType, viewport, bounds);
    }

    /**
     * A factory class to construct a new {@link Geometry} with or without specifying the type of the
     * location, the recommended viewport and the bounding box. The method calls used to build the
     * geometry can be chained.
     */
    public static final class Builder {

        private final GeographicLocation location;
        private LocationType locationType;
        private GeographicArea viewport;
        private GeographicArea bounds;

        private Builder(GeographicLocation location) {
            Validate.notNull(location, "location is required");

            this.location = location;
        }

        /**
         * Instantiates a new {@link Geometry} with the location, the type of the location, the viewport and
         * bounds set in this builder. In case the location type is not set in this builder the type of the
         * location will be set to {@link LocationType#APPROXIMATE} in the instantiated {@code Geometry}.
         * In case the viewport is not set in this builder the viewport of the instantiated {@code Geometry}
         * will be set to the biggest area possible on a map. In case neither the bounding box nor the viewport
         * is set in this builder the bounds will be set to a zero-sized rectangle in the created
         * {@code Geometry}. If the bounds are not but the viewport is set in this builder then the
         * size of the bounding box will be the same as that of the viewport in the instantiated
         * {@code Geometry}.
         *
         * @return a new instance of {@link Geometry}
         */
        public Geometry build() {
            return new Geometry(this);
        }

        /**
         * Sets the type of the location in the geometry built by this builder.
         *
         * @param locationType the type of the location
         * @throws NullPointerException if {@code locationType} is {@code null}
         * @return a reference to this {@code Builder}
         */
        public Builder setLocationType(LocationType locationType) {
            Validate.notNull(locationType, "locationType is required");

            this.locationType = locationType;

            return this;
        }


        /**
         * Sets the recommended viewport in the geometry built by this builder. If the bounds
         * are not yet set then the size of the bounding box is set to the same as the size of
         * the viewport.
         *
         * @param viewport the recommended viewport to best display the location
         * @throws NullPointerException if {@code viewport} is {@code null}
         * @return a reference to this {@code Builder}
         */
        public Builder setViewport(GeographicArea viewport) {
            Validate.notNull(viewport, "viewport is required");

            this.viewport = viewport;

            if (this.bounds == null) {
                this.bounds = viewport;
            }

            return this;
        }

        /**
         * Sets the bounding box in the geometry built by this builder.
         *
         * @param bounds the bounding box that fully contains the location
         * @throws NullPointerException if {@code bounds} are {@code null}
         * @return a reference to this {@code Builder}
         */
        public Builder setBounds(GeographicArea bounds) {
            Validate.notNull(bounds, "bounds is required");

            this.bounds = bounds;

            return this;
        }
    }
}
