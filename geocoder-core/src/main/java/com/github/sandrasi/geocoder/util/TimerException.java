package com.github.sandrasi.geocoder.util;

/**
 * {@code TimerException} is thrown to indicate that an exception happened when the
 * {@link Timer} put the current thread to a sleeping state.
 */
public class TimerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new {@code TimerException} with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a call to
     * {@link Throwable#initCause(Throwable) initCause(Throwable)}.
     */
    public TimerException() {
        super();
    }

    /**
     * Constructs a new {@code TimerException} with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a call to
     * {@link Throwable#initCause(Throwable) initCause(Throwable)}.
     *
     * @param message the detail message
     */
    public TimerException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code TimerException} with the specified cause and a detail message of
     * {@code (cause == null) ? null : cause.toString()}.
     *
     * @param cause the original cause of this exception
     */
    public TimerException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new {@code GeocodeException} with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the original cause of this exception
     */
    public TimerException(String message, Throwable cause) {
        super(message, cause);
    }
}
