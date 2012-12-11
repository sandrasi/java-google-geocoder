package com.github.sandrasi.geocoder.google.v3;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.sandrasi.geocoder.Geocoder;

/**
 * {@code GoogleGeocoderFactory} is a factory class to instantiate different Google Geocoder
 * instances.
 */
public final class GoogleGeocoderFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleGeocoder.class);

    /**
     * The default request rate of consecutive geocoding requests in milliseconds. Allows only 2,500 requests within
     * 24 hours as it is currently stated in the <a href=http://code.google.com/apis/maps/documentation/geocoding/#Limits">
     * Google Geocoding API usage limitations</a>.
     */
    public static final long DEFAUL_REQUEST_RATE_IN_MILLISECONDS = 34560;

    private GoogleGeocoderFactory() {
    }

    /**
     * Constructs a new {@code GoogleGeocoder}. The geocoder instance maintains only one active HTTP
     * connection at any time and it is thread-safe. When the created instance of {@code GoogleGeocoder}
     * is no longer needed and is about to go out of scope it must be closed by calling the
     * {@link Geocoder#close() close()} method.
     *
     * @return a new instance of {@code GoogleGeocoder}
     */
    public static GoogleGeocoder createDefaultGoogleGeocoder() {
        return createTimedGoogleGeocoder(0);
    }

    /**
     * Constructs a new {@code GoogleGeocoder}. Depending on the used HTTP client the created instance
     * might or might not be thread-safe. This geocoder can not be closed by the {@link Geocoder#close() close()}
     * method for it uses an external {@code HttpClient}. Any attempt closing the geocoder will throw an
     * {@code UnsupportedOperationException}. Closing the used HTTP connections is only possible
     * through the {@code httpClient}.
     *
     * @param httpClient an HTTP client to execute the HTTP request to the Google Geocode service
     * @throws NullPointerException if {@code httpClient} is {@code null}
     * @return a new instance of {@code GoogleGeocoder}
     */
    public static GoogleGeocoder createGoogleGeocoder(HttpClient httpClient) {
        return createTimedGoogleGeocoder(httpClient, 0);
    }

    /**
     * Constructs a new premier {@code GoogleGeocoder}. The geocoder instance maintains only one active HTTP
     * connection at any time and it is thread-safe. When the created instance of {@code GoogleGeocoder}
     * is no longer needed and is about to go out of scope it must be closed by calling the
     * {@link Geocoder#close() close()} method.
     *
     * @param googleMapsApiPremierClientId The Google Maps API Premier id to access premier benefits of the
     * Google Maps API. For more information see <a href="http://www.google.com/enterprise/earthmaps/maps.html">
     * Google Maps API Premier</a>.
     * @param googleMapsApiPremierKey the cryptographic key used to sign the premier Google Maps API Web Services requests
     * @throws NullPointerException if either {@code googleMapsApiPremierClientId} or {@code googleMapsApiPremierKey}
     * is {@code null}
     * @throws IllegalArgumentException if either {@code googleMapsApiPremierClientId} or
     * {@code googleMapsApiPremierKey} is blank
     * @return a new instance of {@code GoogleGeocoder}
     */
    public static GoogleGeocoder createPremierGoogleGeocoder(String googleMapsApiPremierClientId,
            String googleMapsApiPremierKey) {
        return createTimedPremierGoogleGeocoder(googleMapsApiPremierClientId, googleMapsApiPremierKey, 0);
    }

    /**
     * Constructs a new premier {@code GoogleGeocoder}. Depending on the used HTTP client the created instance
     * might or might not be thread-safe. This geocoder can not be closed by the {@link Geocoder#close() close()}
     * method for it uses an external {@code HttpClient}. Any attempt closing the geocoder will throw an
     * {@code UnsupportedOperationException}. Closing the used HTTP connections is only possible
     * through the {@code httpClient}.
     *
     * @param httpClient an HTTP client to execute the HTTP request to the Google Geocode service
     * @param googleMapsApiPremierClientId The Google Maps API Premier id to access premier benefits of the
     * Google Maps API. For more information see <a href="http://www.google.com/enterprise/earthmaps/maps.html">
     * Google Maps API Premier</a>.
     * @param googleMapsApiPremierKey the cryptographic key used to sign the premier Google Maps API Web Services requests
     * @throws NullPointerException if either {@code httpClient} or {@code googleMapsApiPremierClientId} or
     * {@code googleMapsApiPremierKey} is {@code null}
     * @throws IllegalArgumentException if either {@code googleMapsApiPremierClientId} or {@code googleMapsApiPremierKey}
     * is blank
     * @return a new instance of {@code GoogleGeocoder}
     */
    public static GoogleGeocoder createPremierGoogleGeocoder(HttpClient httpClient,
            String googleMapsApiPremierClientId, String googleMapsApiPremierKey) {
        return createTimedPremierGoogleGeocoder(httpClient, googleMapsApiPremierClientId, googleMapsApiPremierKey, 0);
    }

    /**
     * Constructs a new timed {@code GoogleGeocoder} with the given request rate.
     * The geocoder instance maintains only one active HTTP connection at any time and it is thread-safe. When the created
     * instance of {@code GoogleGeocoder} is no longer needed and is about to go out of scope it must be closed by
     * calling the {@link Geocoder#close() close()} method.
     *
     * @param geocodingRequestRateInMilliseconds the minimum number of milliseconds between two consecutive geocoding requests
     * @throws IllegalArgumentException if {@code geocodingRequestRateInMilliseconds} is a negative number
     * @return a new instance of a timed {@code GoogleGeocoder}
     */
    public static GoogleGeocoder createTimedGoogleGeocoder(long geocodingRequestRateInMilliseconds) {
        return new GoogleGeocoder(new DefaultHttpClient(), geocodingRequestRateInMilliseconds) {

            @Override
            public void close() {
                getHttpClient().getConnectionManager().shutdown();

                LOGGER.debug("Google Geocoder closed");
            }
        };
    }

    /**
     * Constructs a new timed {@code GoogleGeocoder} with the given request rate. Depending on the used HTTP client
     * the created instance might or might not be thread-safe. This geocoder can not be closed by the
     * {@link Geocoder#close() close()} method for it uses an external {@code HttpClient}. Any attempt closing the
     * geocoder will throw an {@code UnsupportedOperationException}. Closing the used HTTP connections is only
     * possible through the {@code httpClient}.
     *
     * @param httpClient an HTTP client to execute the HTTP request to the Google Geocode service
     * @param geocodingRequestRateInMilliseconds the number of milliseconds between two consecutive geocoding requests
     * @throws NullPointerException if either {@code httpClient} or {@code googleMapsApiPremierClientId} or
     * {@code googleMapsApiPremierKey} is {@code null}
     * @throws IllegalArgumentException if either {@code googleMapsApiPremierClientId} or {@code googleMapsApiPremierKey}
     * is blank,
     * or if {@code geocodingRequestRateInMilliseconds} is a negative number
     * @return a new instance of a timed {@code GoogleGeocoder}
     */
    public static GoogleGeocoder createTimedGoogleGeocoder(HttpClient httpClient, long geocodingRequestRateInMilliseconds) {
        return new GoogleGeocoder(httpClient, geocodingRequestRateInMilliseconds) {

            @Override
            public void close() {
                throw new UnsupportedOperationException("The HTTP connections must be closed through the HttpClient used for this geocoder");
            }
        };
    }

    /**
     * Constructs a new timed premier {@code GoogleGeocoder} with the given request rate.
     * The geocoder instance maintains only one active HTTP connection at any time and it is thread-safe. When the created
     * instance of {@code GoogleGeocoder} is no longer needed and is about to go out of scope it must be closed by
     * calling the {@link Geocoder#close() close()} method.
     *
     * @param googleMapsApiPremierClientId The Google Maps API Premier id to access premier benefits of the
     * Google Maps API. For more information see <a href="http://www.google.com/enterprise/earthmaps/maps.html">
     * Google Maps API Premier</a>.
     * @param googleMapsApiPremierKey the cryptographic key used to sign the premier Google Maps API Web Services requests
     * @param geocodingRequestRateInMilliseconds the minimum number of milliseconds between two consecutive geocoding requests
     * @throws NullPointerException if either {@code googleMapsApiPremierClientId} or {@code googleMapsApiPremierKey}
     * is {@code null}
     * @throws IllegalArgumentException if either {@code googleMapsApiPremierClientId} or {@code googleMapsApiPremierKey}
     * is blank, or {@code geocodingRequestRateInMilliseconds} is a negative number
     * @return a new instance of a timed {@code GoogleGeocoder}
     */
    public static GoogleGeocoder createTimedPremierGoogleGeocoder(String googleMapsApiPremierClientId,
            String googleMapsApiPremierKey, long geocodingRequestRateInMilliseconds) {
        return new GoogleGeocoder(new DefaultHttpClient(),
                new GoogleMapsApiPremierCredentials(googleMapsApiPremierClientId, googleMapsApiPremierKey),
                geocodingRequestRateInMilliseconds) {

            @Override
            public void close() {
                getHttpClient().getConnectionManager().shutdown();

                LOGGER.debug("Google Geocoder closed");
            }
        };
    }

    /**
     * Constructs a new timed premier {@code GoogleGeocoder} with the given request rate. Depending on the used HTTP
     * client the created instance might or might not be thread-safe. This geocoder can not be closed by the
     * {@link Geocoder#close() close()} method for it uses an external {@code HttpClient}. Any attempt closing the
     * geocoder will throw an {@code UnsupportedOperationException}. Closing the used HTTP connections is only
     * possible through the {@code httpClient}.
     *
     * @param httpClient an HTTP client to execute the HTTP request to the Google Geocode service
     * @param googleMapsApiPremierClientId The Google Maps API Premier id to access premier benefits of the
     * Google Maps API. For more information see <a href="http://www.google.com/enterprise/earthmaps/maps.html">
     * Google Maps API Premier</a>.
     * @param googleMapsApiPremierKey the cryptographic key used to sign the premier Google Maps API Web Services requests
     * @param geocodingRequestRateInMilliseconds the number of milliseconds between two consecutive geocoding requests
     * @throws NullPointerException if either {@code httpClient} or {@code googleMapsApiPremierClientId} or
     * {@code googleMapsApiPremierKey} is {@code null}
     * @throws IllegalArgumentException if either {@code googleMapsApiPremierClientId} or {@code googleMapsApiPremierKey}
     * is blank,
     * or if {@code geocodingRequestRateInMilliseconds} is a negative number
     * @return a new instance of a timed {@code GoogleGeocoder}
     */
    public static GoogleGeocoder createTimedPremierGoogleGeocoder(HttpClient httpClient, String googleMapsApiPremierClientId,
            String googleMapsApiPremierKey, long geocodingRequestRateInMilliseconds) {
        return new GoogleGeocoder(httpClient,
                new GoogleMapsApiPremierCredentials(googleMapsApiPremierClientId, googleMapsApiPremierKey),
                geocodingRequestRateInMilliseconds) {

            @Override
            public void close() {
                throw new UnsupportedOperationException("The HTTP connections must be closed through the HttpClient used for this geocoder");
            }
        };
    }
}
