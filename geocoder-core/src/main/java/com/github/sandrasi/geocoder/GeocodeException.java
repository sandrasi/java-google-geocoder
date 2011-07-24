package com.github.sandrasi.geocoder;

/**
 * {@code GeocodeException} is thrown to indicate that the geocoding operation can not be performed.
 */
public class GeocodeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new {@code GeocodeException} with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a call to
     * {@link Throwable#initCause(Throwable) initCause(Throwable)}.
     */
    public GeocodeException() {
        super();
    }

    /**
     * Constructs a new {@code GeocodeException} with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a call to
     * {@link Throwable#initCause(Throwable) initCause(Throwable)}.
     *
     * @param message the detail message
     */
    public GeocodeException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code GeocodeException} with the specified cause and a detail message of
     * {@code (cause == null) ? null : cause.toString()}.
     *
     * @param cause the original cause of this exception
     */
    public GeocodeException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new {@code GeocodeException} with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the original cause of this exception
     */
    public GeocodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
