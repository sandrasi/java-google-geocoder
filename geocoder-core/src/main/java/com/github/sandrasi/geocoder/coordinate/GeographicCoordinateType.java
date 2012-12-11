package com.github.sandrasi.geocoder.coordinate;

import static com.github.sandrasi.geocoder.coordinate.CardinalDirection.*;
import java.math.BigDecimal;
import org.apache.commons.lang3.Validate;

/**
 * {@code GeographicCoordinateType} represents the latitude and longitude coordinate types.
 */
public enum GeographicCoordinateType {

    /**
     * Latitude is the angular distance of a point's location north or south of the
     * Equator.
     */
    LATITUDE(CoordinateConstants.MINIMUM_LATITUDE, CoordinateConstants.MAXIMUM_LATITUDE),

    /**
     * Longitude is the angular distance of a point's location east of west of the
     * Prime (Greenwich) Meridian.
     */
    LONGITUDE(CoordinateConstants.MINIMUM_LONGITUDE, CoordinateConstants.MAXIMUM_LONGITUDE);

    private final int minValue;
    private final int maxValue;

    private GeographicCoordinateType(int minValue, int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    /**
     * Checks if the given {@code value} can be used with this coordinate type. If this
     * coordinate type is latitude then the values allowed are between -90.0 and 90.0. If
     * this coordinate type is longitude then the values allowed are between -180.0 and 180.0.
     *
     * @param value the floating point value of the angular distance
     * @throws NullPointerException if {@code value} is {@code null}
     * @return {@code true} if {@code value} is between the range specific to this
     * coordinate type; {@code false} otherwise
     */
    public boolean isValidValue(BigDecimal value) {
        Validate.notNull(value, "value is required");

        return (value.compareTo(BigDecimal.valueOf(minValue)) >= 0) && (value.compareTo(BigDecimal.valueOf(maxValue)) <= 0);
    }

    /**
     * Checks if the given {@code value} can be used with this coordinate type. If this
     * coordinate type is latitude then the values allowed are between -90.0 and 90.0. If
     * this coordinate type is longitude then the values allowed are between -180.0 and 180.0.
     *
     * @param value the floating point value of the angular distance
     * @return {@code true} if {@code value} is between the range specific to this
     * coordinate type; {@code false} otherwise
     */
    public boolean isValidValue(double value) {
        return (value >= minValue) && (value <= maxValue);
    }

    /**
     * Checks if the given angle defined by {@code degrees}, {@code minutes},
     * {@code seconds}, {@code millis} and {@code cardinalDirection} can be used
     * with this coordinate type. If this coordinate type is latitude then the angles allowed
     * are between 0&deg;0'0.0" and 90&deg;0'0.0" and the cardinal direction must be either north
     * or south. If this coordinate type is longitude then the angles allowed are between
     * 0&deg;0'0.0" and 180&deg;0'0.0" and the cardinal direction must be either east or west.
     *
     * @param degrees the degrees of the angle
     * @param minutes the minutes of the angle
     * @param seconds the seconds of the angle
     * @param millis the millis of the angle
     * @param cardinalDirection the cardinal direction of the given angle
     * @throws NullPointerException if {@code cardinalDirection} is {@code null}
     * @return {@code true} if angle is between the range specific to this coordinate type;
     * {@code false} otherwise
     */
    public boolean isValidAngle(int degrees, int minutes, int seconds, int millis, CardinalDirection cardinalDirection) {
        Validate.notNull(cardinalDirection, "cardinalDirection is required");

        if (isZeroCardinalDirection(cardinalDirection)) {
            return isZeroAngle(degrees, minutes, seconds, millis);
        }

        return isCardinalDirectionValid(cardinalDirection) && isAngleValueValid(degrees, minutes, seconds, millis);
    }

    private boolean isZeroCardinalDirection(CardinalDirection cardinalDirection) {
        return (cardinalDirection == ZERO_LATITUDE_DEGREE) || (cardinalDirection == ZERO_LONGITUDE_DEGREE);
    }

    private boolean isZeroAngle(int degrees, int minutes, int seconds, int millis) {
        return (degrees == 0) && (minutes == 0) && (seconds == 0) && (millis == 0);
    }

    private boolean isCardinalDirectionValid(CardinalDirection cardinalDirection) {
        switch (this) {
        case LATITUDE:
            return isLatitudialDirection(cardinalDirection);
        case LONGITUDE:
            return isLongitudialDirection(cardinalDirection);
        }

        return false;
    }

    private boolean isLongitudialDirection(CardinalDirection cardinalDirection) {
        return (cardinalDirection == EAST) || (cardinalDirection == WEST);
    }

    private boolean isLatitudialDirection(CardinalDirection cardinalDirection) {
        return (cardinalDirection == NORTH) || (cardinalDirection == SOUTH);
    }

    private boolean isAngleValueValid(int degrees, int minutes, int seconds, int millis) {
        if (degrees < maxValue) {
            return (degrees >= 0) && isMinutesValid(minutes) && isSecondsValid(seconds) && isMillisValid(millis);
        }

        return degrees == maxValue && minutes == 0 && seconds == 0 && millis == 0;
    }

    private boolean isMinutesValid(int minutes) {
        return (minutes >= 0) && (minutes <= CoordinateConstants.MAXIMUM_MINUTE);
    }

    private boolean isSecondsValid(int seconds) {
        return (seconds >= 0) && (seconds <= CoordinateConstants.MAXIMUM_SECONDS);
    }

    private boolean isMillisValid(int millis) {
        return (millis >= 0) && (millis <= CoordinateConstants.MAXIMUM_MILLIS);
    }

    /**
     * Common geographic coordinate-related constants.
     */
    public interface CoordinateConstants {

        /**
         * The minimum latitude
         */
        int MINIMUM_LATITUDE = -90;

        /**
         * The maximum latitude
         */
        int MAXIMUM_LATITUDE = 90;

        /**
         * The minimum longitude
         */
        int MINIMUM_LONGITUDE = -180;

        /**
         * The maximum longitude
         */
        int MAXIMUM_LONGITUDE = 180;

        /**
         * The maximum minute of a degree
         */
        int MAXIMUM_MINUTE = 59;

        /**
         * The maximum second of a minute
         */
        int MAXIMUM_SECONDS = 59;

        /**
         * The maximum millisecond of a second
         */
        int MAXIMUM_MILLIS = 999;
    }
}
