package com.github.sandrasi.geocoder.google.v3;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import com.github.sandrasi.geocoder.GeocodeException;
import com.github.sandrasi.geocoder.GeocodeRequest;
import com.github.sandrasi.geocoder.GeocodeResponse;
import com.github.sandrasi.geocoder.components.GeographicArea;
import com.github.sandrasi.geocoder.components.GeographicLocation;
import org.apache.commons.lang3.Validate;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@code GoogleGeocodeRequest} represents the parameterized URL to the Google Geocoding service.
 * Two types of {@code GoogleGeocodeRequest} are available depending on how the class is instantiated:
 * it either represents an address lookup or a reverse geocoding of a geographic coordinate.
 */
public final class GoogleGeocodeRequest implements GeocodeRequest, Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleGeocodeRequest.class);

    private static final int HTTP_OK = 200;
    private static final String GOOGLE_MAPS_API_HOST = "http://maps.googleapis.com";
    private static final String GOOGLE_GEOCODING_SERVICE_URL = "/maps/api/geocode/json";
    private static final String CHARACTER_ENCODING = "UTF-8";

    private final String originalQueryString;
    private final URI requestUri;
    private final HttpClient httpClient;

    private GoogleGeocodeRequest(Builder builder) {
        originalQueryString = getOriginalQueryString(builder);
        requestUri = createUri(builder);
        httpClient = builder.httpClient;
    }

    private String getOriginalQueryString(Builder builder) {
        return (builder.address != null) ? builder.address : builder.geographicCoordinates.getLatitude() + ", " + builder.geographicCoordinates.getLongitude();
    }

    private URI createUri(Builder builder) {
        String urlEncodedQuery = createUrlEncodedQuery(builder);
        String signatureParameters = createSignatureParametersFor(urlEncodedQuery, builder.googleMapsApiPremierCredentials);

        return URI.create(GOOGLE_MAPS_API_HOST + urlEncodedQuery + signatureParameters);
    }

    private String createUrlEncodedQuery(Builder builder) {
        try {
            return GOOGLE_GEOCODING_SERVICE_URL + "?"
                    + ((builder.address != null) ? "address=" + URLEncoder.encode(builder.address, CHARACTER_ENCODING) : "")
                    + ((builder.geographicCoordinates != null) ? "latlng=" + URLEncoder.encode(builder.geographicCoordinates.getLatitude() + "," + builder.geographicCoordinates.getLongitude(), CHARACTER_ENCODING) : "")
                    + ((builder.viewportBias != null) ? "&bounds=" + URLEncoder.encode(viewportBiasToRequestParameters(builder.viewportBias), CHARACTER_ENCODING) : "")
                    + ((builder.regionBias != null) ? "&region=" + URLEncoder.encode(builder.regionBias, CHARACTER_ENCODING) : "")
                    + ((builder.language != null) ? "&language=" + URLEncoder.encode(builder.language, CHARACTER_ENCODING) : "")
                    + ((builder.googleMapsApiPremierCredentials != null) ? "&client=" + URLEncoder.encode(builder.googleMapsApiPremierCredentials.getClientId(), CHARACTER_ENCODING): "")
                    + "&sensor=false";
        } catch (UnsupportedEncodingException e) {
            throw new GeocodeException("The character encoding used to create a URI for this GoogleGeocodeRequest is not supported", e);
        }
    }

    private String viewportBiasToRequestParameters(GeographicArea viewportBias) {
        GeographicLocation swc = viewportBias.getSouthWestCorner();
        GeographicLocation nec = viewportBias.getNorthEastCorner();

        return swc.getLatitude() + "," + swc.getLongitude() + "|" + nec.getLatitude() + "," + nec.getLongitude();
    }

    private String createSignatureParametersFor(String urlEncodedQuery, GoogleMapsApiPremierCredentials googleMapsApiPremierCredentials) {
        return (googleMapsApiPremierCredentials != null) ? "&signature=" + googleMapsApiPremierCredentials.getSignatureFor(urlEncodedQuery) : "";
    }

    /**
     * Performs this geocode request by sending an HTTP request to the Google geocoding service.
     *
     * @throws GeocodeException if an HTTP protocol error or an I/O exception happens during the geocoding
     * @return the result of the geocoding
     */
    @Override
    public GeocodeResponse execute() {
        try {
            HttpGet httpGet = new HttpGet(requestUri);

            LOGGER.debug("Sending request to the Google Geocode service " + requestUri);

            HttpResponse httpResponse = httpClient.execute(httpGet);

            LOGGER.debug("Google Geocode response received");

            if (httpResponse.getStatusLine().getStatusCode() == HTTP_OK) {
                return JsonGeocodeResponseParser.parse(originalQueryString, httpResponse.getEntity().getContent());
            } else {
                throw new GeocodeException("An error occurred while geocoding: "
                        + httpResponse.getStatusLine().getStatusCode()
                        + " " + httpResponse.getStatusLine().getReasonPhrase()
                        + " " + EntityUtils.toString(httpResponse.getEntity()));
            }
        } catch (ClientProtocolException e) {
            LOGGER.error("An HTTP protocol error occurred", e);

            throw new GeocodeException("An HTTP protocol error occurred", e);
        } catch (IOException e) {
            LOGGER.error("An I/O exception occurred while reading the response", e);

            throw new GeocodeException("An I/O exception occurred while reading the response", e);
        } finally {
            httpClient.getConnectionManager().closeExpiredConnections();
        }
    }

    static Builder newBuilder(String address, HttpClient httpClient,
            GoogleMapsApiPremierCredentials googleMapsApiPremierCredentials) {
        return new Builder(address, httpClient, googleMapsApiPremierCredentials);
    }

    static Builder newBuilder(GeographicLocation geographicCoordinates, HttpClient httpClient,
            GoogleMapsApiPremierCredentials googleMapsApiPremierCredentials) {
        return new Builder(geographicCoordinates, httpClient, googleMapsApiPremierCredentials);
    }

    /**
     * A factory class to construct a new {@link GoogleGeocodeRequest} with only those components that are used for the geocoding.
     * All of the components are optional. The components of the request not used for the geocoding should not be defined.
     * The method calls to define the components can be chained.
     */
    public static final class Builder implements GeocodeRequest.Builder<GoogleGeocodeRequest> {

        private final HttpClient httpClient;
        private final GoogleMapsApiPremierCredentials googleMapsApiPremierCredentials;
        private String address;
        private GeographicLocation geographicCoordinates;
        private GeographicArea viewportBias;
        private String regionBias;
        private String language;

        private Builder(HttpClient httpClient, GoogleMapsApiPremierCredentials googleMapsApiPremierCredentials) {
            Validate.notNull(httpClient, "httpClient is required");

            this.httpClient = httpClient;
            this.googleMapsApiPremierCredentials = googleMapsApiPremierCredentials;
        }

        private Builder(String address, HttpClient httpClient,
                GoogleMapsApiPremierCredentials googleMapsApiPremierCredentials) {
            this(httpClient, googleMapsApiPremierCredentials);

            Validate.notNull(address, "address is required");

            this.address = address;
        }

        private Builder(GeographicLocation geographicCoordinates, HttpClient httpClient,
                GoogleMapsApiPremierCredentials googleMapsApiPremierCredentials) {
            this(httpClient, googleMapsApiPremierCredentials);

            Validate.notNull(geographicCoordinates, "geographicCoordinates is required");

            this.geographicCoordinates = geographicCoordinates;
        }

        /**
         * Instantiates a new {@link GoogleGeocodeRequest} with the components defined in this builder.
         *
         * @return a new instance of {@link GoogleGeocodeRequest}
         */
        @Override
        public GoogleGeocodeRequest build() {
            return new GoogleGeocodeRequest(this);
        }

        /**
         * Defines the bounding box of the viewport within which to bias geocode results more prominently.
         * <p />
         * <i>For more information see <a href="http://code.google.com/apis/maps/documentation/geocoding/#Viewports">
         * Viewport Biasing</a>.</i>
         *
         * @param southWestCorner the southwest coordinate of the bounding box
         * @param northEastCorner the northeast coordinate of the bounding box
         * @throws IllegalArgumentException if either {@code southWestCorner} or {@code northEastCorner} is {@code null}
         * @return a reference to this {@code Builder}
         */
        public Builder withViewportBiasing(GeographicLocation southWestCorner, GeographicLocation northEastCorner) {
            this.viewportBias = new GeographicArea(southWestCorner, northEastCorner);

            return this;
        }

        /**
         * Instructs the Google Geocoding API to return results biased to a particular region using the
         * {@code regionBias} parameter. Geocoding results can be biased for every domain in which the
         * main Google Maps application is officially launched.
         * <p />
         * <i>For more information see <a href="http://code.google.com/apis/maps/documentation/geocoding/#RegionCodes">
         * Region Biasing</a>.</i>
         *
         * @param regionBias the region code, specified as a ccTLD ("top-level domain") two-character value
         * @throws IllegalArgumentException if {@code regionBias} is {@code null}
         * @return a reference to this {@code Builder}
         */
        public Builder withRegionBiasing(String regionBias) {
            Validate.notNull(regionBias, "regionBias is required");

            this.regionBias = regionBias;

            return this;
        }

        /**
         * Sets the language in which to return results. If {@code language} is not supplied, then the Google geocoder
         * will attempt to use the native language of the domain from which the request is sent wherever possible.
         * <p />
         * <i>See the <a href="http://code.google.com/apis/maps/faq.html#languagesupport">supported list of domain languages</a>.
         * Note that Google often updates the supported languages so this list may not be exhaustive.</i>
         *
         * @param language the language code
         * @throws IllegalArgumentException if {@code language} is {@code null}
         * @return a reference to this {@code Builder}
         */
        public Builder inLanguage(String language) {
            Validate.notNull(language, "language is required");

            this.language = language;

            return this;
        }
    }
}
