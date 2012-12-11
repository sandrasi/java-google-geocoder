package com.github.sandrasi.geocoder.coordinate;

import static com.github.sandrasi.geocoder.coordinate.CardinalDirection.*;
import static com.github.sandrasi.geocoder.coordinate.GeographicCoordinateType.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * {@code GeographicCoordinate} represents a horizontal or vertical coordinate in
 * the geographic coordinate system.
 */
public final class GeographicCoordinate implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int NUMBER_OF_MINUTES_IN_AN_HOUR = 60;
    private static final int NUMBER_OF_SECONDS_IN_A_MINUTE = 60;
    private static final int NUMBER_OF_MILLIS_IN_A_SECOND = 1000;
    private static final int ONE_HOUR_AS_MILLIS = 3600000;

    /**
     * The horizontal coordinate representing the Equator
     */
    public static final GeographicCoordinate EQUATOR = latitudeFromDouble(0.0);

    /**
     * The vertical coordinate representing the Prime meridian
     */
    public static final GeographicCoordinate PRIME_MERIDIAN = longitudeFromDouble(0.0);

    private static final int PRECISION = 16;

    private final BigDecimal value;

    private final int degrees;
    private final int minutes;
    private final int seconds;
    private final int millis;
    private final CardinalDirection cardinalDirection;

    private GeographicCoordinate(BigDecimal value, GeographicCoordinateType geographicCoordinateType) {
        Validate.notNull(value, "value is required");
        Validate.notNull(geographicCoordinateType, "geographicCoordinateType is required");
        Validate.isTrue(geographicCoordinateType.isValidValue(value), "Invalid coordinate");

        this.value = value.setScale(PRECISION, RoundingMode.HALF_EVEN);
        this.degrees = value.abs().intValue();
        BigDecimal min = value.abs().subtract(BigDecimal.valueOf(degrees)).multiply(BigDecimal.valueOf(NUMBER_OF_MINUTES_IN_AN_HOUR));
        this.minutes = min.intValue();
        BigDecimal sec = min.subtract(BigDecimal.valueOf(minutes)).multiply(BigDecimal.valueOf(NUMBER_OF_SECONDS_IN_A_MINUTE));
        this.seconds = sec.intValue();
        BigDecimal ms = sec.subtract(BigDecimal.valueOf(seconds)).multiply(BigDecimal.valueOf(NUMBER_OF_MILLIS_IN_A_SECOND));
        this.millis = ms.intValue();

        if (geographicCoordinateType == LATITUDE) {
            cardinalDirection = (value.compareTo(BigDecimal.ZERO) == 0) ? ZERO_LATITUDE_DEGREE : (value.compareTo(BigDecimal.ZERO) > 0) ? NORTH : SOUTH;
        } else {
            cardinalDirection = (value.compareTo(BigDecimal.ZERO) == 0) ? ZERO_LONGITUDE_DEGREE : (value.compareTo(BigDecimal.ZERO) > 0) ? EAST : WEST;
        }
    }

    private GeographicCoordinate(int degrees, int minutes, int seconds, int millis, CardinalDirection cardinalDirection) {
        Validate.notNull(cardinalDirection, "cardinalDirection is required");
        Validate.isTrue(cardinalDirection.getCoordinateType().isValidAngle(degrees, minutes, seconds, millis, cardinalDirection), "invalid geographic coordinate");

        this.degrees = degrees;
        this.minutes = minutes;
        this.seconds = seconds;
        this.millis = millis;
        this.cardinalDirection = cardinalDirection;
        this.value = BigDecimal.valueOf(degrees)
                .add(BigDecimal.valueOf((minutes * NUMBER_OF_SECONDS_IN_A_MINUTE + seconds) * NUMBER_OF_MILLIS_IN_A_SECOND + millis)
                .divide(BigDecimal.valueOf(ONE_HOUR_AS_MILLIS), PRECISION, RoundingMode.HALF_EVEN))
                .multiply(BigDecimal.valueOf(((cardinalDirection == ZERO_LATITUDE_DEGREE) || (cardinalDirection == ZERO_LONGITUDE_DEGREE) || (cardinalDirection == NORTH) || (cardinalDirection == EAST)) ? 1 : -1));
    }

    /**
     * Creates a new horizontal coordinate from the given floating point {@code value}
     *
     * @param value the floating point value of the horizontal coordinate
     * @throws NullPointerException if {@code value} is {@code null}
     * @throws IllegalArgumentException if {@code value} is not a valid latitude coordinate
     * @return a new instance of {@code GeographicCoordinate}
     */
    public static GeographicCoordinate latitudeFromBigDecimal(BigDecimal value) {
        return new GeographicCoordinate(value, GeographicCoordinateType.LATITUDE);
    }

    /**
     * Creates a new vertical coordinate from the given floating point {@code value}.
     *
     * @param value the floating point value of the vertical coordinate
     * @throws NullPointerException if {@code value} is {@code null}
     * @throws IllegalArgumentException if {@code value} is not a valid longitude coordinate
     * @return a new instance of {@code GeographicCoordinate}
     */
    public static GeographicCoordinate longitudeFromBigDecimal(BigDecimal value) {
        return new GeographicCoordinate(value, GeographicCoordinateType.LONGITUDE);
    }

    /**
     * Creates a new horizontal coordinate from the given floating point {@code value}.
     *
     * @param value the floating point value of the horizontal coordinate
     * @throws IllegalArgumentException if {@code value} is not a valid latitude coordinate
     * @return a new instance of {@code GeographicCoordinate}
     */
    public static GeographicCoordinate latitudeFromDouble(double value) {
        return new GeographicCoordinate(BigDecimal.valueOf(value), GeographicCoordinateType.LATITUDE);
    }

    /**
     * Creates a new vertical coordinate from the given string {@code value}.
     *
     * @param value the floating point value of the vertical coordinate
     * @throws IllegalArgumentException if {@code value} is not a valid longitude coordinate
     * @return a new instance of {@code GeographicCoordinate}
     */
    public static GeographicCoordinate longitudeFromDouble(double value) {
        return new GeographicCoordinate(BigDecimal.valueOf(value), GeographicCoordinateType.LONGITUDE);
    }

    /**
     * Creates a new horizontal coordinate from the given string {@code value}.
     *
     * @param value the floating point value of the horizontal coordinate
     * @throws IllegalArgumentException if {@code value} is not a valid latitude coordinate
     * @throws NumberFormatException if {@code value} is not a number
     * @return a new instance of {@code GeographicCoordinate}
     */
    public static GeographicCoordinate latitudeFromString(String value) {
        return new GeographicCoordinate(new BigDecimal(value), GeographicCoordinateType.LATITUDE);
    }

    /**
     * Creates a new vertical coordinate from the given string {@code value}.
     *
     * @param value the floating point value of the vertical coordinate
     * @throws IllegalArgumentException if {@code value} is not a valid longitude coordinate
     * @throws NumberFormatException if {@code value} is not a number
     * @return a new instance of {@code GeographicCoordinate}
     */
    public static GeographicCoordinate longitudeFromString(String value) {
        return new GeographicCoordinate(new BigDecimal(value), GeographicCoordinateType.LONGITUDE);
    }

    /**
     * Creates a new latitude or longitude coordinate from the given angle and cardinal
     * direction. If {@code cardinalDirection} is north or south a horizontal coordinate is
     * constructed. If {@code cardinalDirection} is east or west a vertical coordinate is
     * created.
     *
     * @param degrees the degrees of the angle
     * @param minutes the minutes of the angle
     * @param seconds the seconds of the angle
     * @param millis the millis of the angle
     * @param cardinalDirection the cardinal direction of the angle
     * @throws NullPointerException if {@code cardinalDirection} is {@code null}
     * @throws IllegalArgumentException if the given angle components and direction do not compose a valid coordinate together
     * @return a new instance of {@code GeographicCoordinate}
     */
    public static GeographicCoordinate fromAngle(int degrees, int minutes, int seconds, int millis, CardinalDirection cardinalDirection) {
        return new GeographicCoordinate(degrees, minutes, seconds, millis, cardinalDirection);
    }

    /**
     * Returns the value of this geographic coordinate.
     *
     * @return a floating point value as {@code BigDecimal}
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * Returns the value of this geographic coordinate.
     *
     * @return a floating point value
     */
    public double getDoubleValue() {
        return value.doubleValue();
    }

    /**
     * Returns the degree part of the angle this coordinate represents.
     *
     * @return an integer value between 0 and 90 if this geographic coordinate is
     * a horizontal coordinate; an integer value between 0 and 180 if this geographic
     * coordinate is a vertical coordinate
     */
    public int getDegrees() {
        return degrees;
    }

    /**
     * Returns the minute part of the angle this coordinate represents.
     *
     * @return an integer value between 0 and 59
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * Returns the second part of the angle this coordinate represents.
     *
     * @return an integer value between 0 and 59
     */
    public int getSeconds() {
        return seconds;
    }

    /**
     * Returns the millis part of the angle this coordinate represents.
     *
     * @return an integer value between 0 and 999
     */
    public int getMillis() {
        return millis;
    }

    /**
     * Returns the cardinal direction of this geograhic coordinate.
     *
     * @return <ul>
     * <li>{@link CardinalDirection#NORTH} if this geographic coordinate is a
     * horizontal coordinate and its value is positive.</li>
     * <li>{@link CardinalDirection#ZERO_LATITUDE_DEGREE} if this geographic coordinate
     * is a horizontal coordinate and its value is zero.</li>
     * <li>{@link CardinalDirection#SOUTH} if this geographic coordinate is a
     * horizontal coordinate and its value is negative.</li>
     * <li>{@link CardinalDirection#EAST} if this geographic coordinate is a
     * vertical coordinate and its value is positive.</li>
     * <li>{@link CardinalDirection#ZERO_LONGITUDE_DEGREE} if this geographic coordinate
     * is a vertical coordinate and its value is zero.</li>
     * <li>{@link CardinalDirection#WEST} if this geographic coordinate is a
     * vertical coordinate and its value is negative.</li>
     * </ul>
     */
    public CardinalDirection getCardinalDirection() {
        return cardinalDirection;
    }

    /**
     * Compares the specified object with this {@code GeographicCoordinate} for equality.
     * Returns {@code true} if the given object is also a geographic coordinate and the
     * values of the two geographic coordinates are equal.
     *
     * @param o object to be compared for equality with this {@code GeographicCoordinate}
     * @return {@code true} if the specified object is equal to this geographic coordinate;
     * {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this != o) {
            if (o == null) {
                return false;
            }

            if (getClass() != o.getClass()) {
                return false;
            }

            final GeographicCoordinate other = (GeographicCoordinate) o;

            if (!value.equals(other.value)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the hash code value for this {@code GeographicCoordinate}. The hash code of
     * a geographic coordinate is calculated from its value.
     *
     * @return hash code value for this {@code GeographicCoordinate}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(value).hashCode();
    }

    /**
     * Returns the textual representation of this {@code GeographicCoordinate}.
     *
     * @return this {@code GeographicCoordinate} in string format
     */
    @Override
    public String toString() {
        String valueString = (value.compareTo(BigDecimal.ZERO) != 0) ? value.stripTrailingZeros().toPlainString() : "0";

        return String.format("value: \"%s\", degrees: \"%d\", minutes: \"%d\", seconds: \"%d\", millis: \"%d\", cardinalDirection: \"%s\"",
                valueString, degrees, minutes, seconds, millis, cardinalDirection);
    }
}
