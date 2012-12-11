package com.github.sandrasi.geocoder.google.v3;

import com.github.sandrasi.geocoder.GeocodeResponse;
import com.github.sandrasi.geocoder.Geocoder;
import com.github.sandrasi.geocoder.components.GeographicLocation;
import com.github.sandrasi.geocoder.util.Timer;
import org.apache.commons.lang3.Validate;
import org.apache.http.client.HttpClient;

/**
 * {@code GoogleGeocoder} is a Java wrapper around the <i>Google Geocoding API</i>. It converts
 * addresses (like "1600 Amphitheatre Parkway, Mountain View, CA") into geographic coordinates
 * (like latitude 37.423021 and longitude -122.083739) by sending an HTTP request to the Google Geocoding
 * service. Additionally, the class allows you to perform the converse operation (turning
 * coordinates into addresses); this process is known as <i>reverse geocoding</i>.
 * <p>
 * The Google geocoding service is a subject to query limitations regarding the number of queries per 24 hours
 * and the request rate of the geocoding queries. This class supports the request rate limitation (and as such
 * the number of queries, too). <i>Even though using multiple instances of the {@code GoogleGeocoder} is
 * possible, only one instance is ought to be used at any time from the same IP address. Limiting the overall
 * rate of consecutive geocoding requests executed by the different geocoder instances is not possible
 * otherwise.</i>
 * <p>
 * <i>For more information see <a href="http://code.google.com/apis/maps/documentation/geocoding/">The
 * Google Geocoding API</a>.</i>
 */
public abstract class GoogleGeocoder implements Geocoder<GoogleGeocodeRequest, GoogleGeocodeRequest.Builder> {

    private final HttpClient httpClient;
    private final GoogleMapsApiPremierCredentials googleMapsApiPremierCredentials;
    private final Timer timer;

    /**
     * Constructs a new {@code GoogleGeocoder}. Depending on the used HTTP client the created instance
     * might or might not be thread-safe. The specified {@code geocodingRequestRateInMilliseconds}
     * controls the frequency of the consecutive geocoding requests. Two geocoding requests can not happen
     * faster than the given time.
     *
     * @param httpClient an HTTP client to execute the HTTP request to the Google Geocode service
     * @param geocodingRequestRateInMilliseconds the minimum time between two consecutive geocoding requests
     * @throws NullPointerException if {@code httpClient} is {@code null}
     * @throws IllegalArgumentException if {@code geocodingRequestRateInMilliseconds} is a negative number
     */
    protected GoogleGeocoder(HttpClient httpClient, long geocodingRequestRateInMilliseconds) {
        Validate.notNull(httpClient, "httpClient is required");

        this.httpClient = httpClient;
        this.googleMapsApiPremierCredentials = null;
        this.timer = new Timer(geocodingRequestRateInMilliseconds);
    }

    /**
     * Constructs a new {@code GoogleGeocoder}. Depending on the used HTTP client the created instance
     * might or might not be thread-safe. The specified {@code geocodingRequestRateInMilliseconds}
     * controls the frequency of the consecutive geocoding requests. Two geocoding requests can not happen
     * faster than the given time.
     *
     * @param httpClient an HTTP client to execute the HTTP request to the Google Geocode service
     * @param googleMapsApiPremierCredentials credentials for accessing the premier Google Maps API Web Services
     * @param geocodingRequestRateInMilliseconds the minimum time between two consecutive geocoding requests
     * @throws NullPointerException if either {@code httpClient} or {@code googleMapsApiPremierCrendentials}
     * is {@code null}
     * @throws IllegalArgumentException if {@code geocodingRequestRateInMilliseconds} is a negative number
     */
    protected GoogleGeocoder(HttpClient httpClient, GoogleMapsApiPremierCredentials googleMapsApiPremierCredentials,
            long geocodingRequestRateInMilliseconds) {
        Validate.notNull(httpClient, "httpClient is required");
        Validate.notNull(googleMapsApiPremierCredentials, "googleMapsApiPremierCredentials is required");

        this.httpClient = httpClient;
        this.googleMapsApiPremierCredentials = googleMapsApiPremierCredentials;
        this.timer = new Timer(geocodingRequestRateInMilliseconds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GoogleGeocodeRequest.Builder newGeocodeRequestBuilder(String address) {
        return GoogleGeocodeRequest.newBuilder(address, httpClient, googleMapsApiPremierCredentials);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GoogleGeocodeRequest.Builder newGeocodeRequestBuilder(double latitude, double longitude) {
        return GoogleGeocodeRequest.newBuilder(GeographicLocation.fromValues(latitude, longitude), httpClient, googleMapsApiPremierCredentials);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GeocodeResponse geocodeAddress(String address) {
        return geocode(newGeocodeRequestBuilder(address).build());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GeocodeResponse lookupAddress(double latitude, double longitude) {
        return geocode(newGeocodeRequestBuilder(latitude, longitude).build());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GeocodeResponse geocode(GoogleGeocodeRequest geocodeRequest) {
        timer.sleep();

        return geocodeRequest.execute();
    }

    /**
     * Provides access for the implementing classes to the used {@code HttpClient}.
     *
     * @return the HTTP client to execute the HTTP request to the Google Geocode service
     */
    protected HttpClient getHttpClient() {
        return httpClient;
    }
}
