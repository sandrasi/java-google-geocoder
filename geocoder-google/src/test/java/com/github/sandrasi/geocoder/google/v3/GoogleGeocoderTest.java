package com.github.sandrasi.geocoder.google.v3;

import static com.github.sandrasi.geocoder.components.GeocodeStatus.ZERO_RESULTS;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Collections;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.github.sandrasi.geocoder.GeocodeResponse;
import com.github.sandrasi.geocoder.components.GeocodedAddress;
import com.github.sandrasi.geocoder.components.GeographicLocation;

public class GoogleGeocoderTest {

    private static final int HTTP_OK = 200;

    private HttpClient httpClient;
    private GoogleGeocoder subject;

    @Before
    public void setUp() {
        httpClient = EasyMock.createMock(HttpClient.class);
        subject = GoogleGeocoderFactory.createGoogleGeocoder(httpClient);
    }

    @Test
    public void shouldGeocodeAddress() throws Exception {
        Capture<HttpGet> capturedHttpGet = new Capture<HttpGet>();
        HttpResponse httpResponse = EasyMock.createMock(HttpResponse.class);
        StatusLine statusLine = EasyMock.createMock(StatusLine.class);
        HttpEntity httpEntity = EasyMock.createMock(HttpEntity.class);
        ClientConnectionManager clientConnectionManager = EasyMock.createMock(ClientConnectionManager.class);

        expect(httpClient.execute(capture(capturedHttpGet))).andReturn(httpResponse);
        expect(httpResponse.getStatusLine()).andReturn(statusLine);
        expect(statusLine.getStatusCode()).andReturn(HTTP_OK);
        expect(httpResponse.getEntity()).andReturn(httpEntity);
        expect(httpEntity.getContent()).andReturn(new ByteArrayInputStream("{\"status\":\"ZERO_RESULTS\",\"results\":[]}".getBytes()));
        expect(httpClient.getConnectionManager()).andReturn(clientConnectionManager);
        clientConnectionManager.closeExpiredConnections();
        replay(httpClient, httpResponse, statusLine, httpEntity, clientConnectionManager);

        GeocodeResponse geocodeResponse = subject.geocodeAddress("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA");

        verify(httpClient, httpResponse, statusLine, httpEntity, clientConnectionManager);

        URI expectedUri = URI.create("http://maps.googleapis.com/maps/api/geocode/json"
                + "?address=" + URLEncoder.encode("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", "UTF-8")
                + "&sensor=false");

        assertThat(capturedHttpGet.getValue().getURI(), is(expectedUri));
        assertThat(geocodeResponse.getGeocodeStatus(), is(ZERO_RESULTS));
        assertThat(geocodeResponse.getQueryString(), is("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA"));
        assertThat(geocodeResponse.getGeocodedAddresses(), is(Collections.<GeocodedAddress>emptyList()));
    }

    @Test
    public void shouldLookupAddress() throws Exception {
        Capture<HttpGet> capturedHttpGet = new Capture<HttpGet>();
        HttpResponse httpResponse = EasyMock.createMock(HttpResponse.class);
        StatusLine statusLine = EasyMock.createMock(StatusLine.class);
        HttpEntity httpEntity = EasyMock.createMock(HttpEntity.class);
        ClientConnectionManager clientConnectionManager = EasyMock.createMock(ClientConnectionManager.class);

        expect(httpClient.execute(capture(capturedHttpGet))).andReturn(httpResponse);
        expect(httpResponse.getStatusLine()).andReturn(statusLine);
        expect(statusLine.getStatusCode()).andReturn(HTTP_OK);
        expect(httpResponse.getEntity()).andReturn(httpEntity);
        expect(httpEntity.getContent()).andReturn(new ByteArrayInputStream("{\"status\":\"ZERO_RESULTS\",\"results\":[]}".getBytes()));
        expect(httpClient.getConnectionManager()).andReturn(clientConnectionManager);
        clientConnectionManager.closeExpiredConnections();
        replay(httpClient, httpResponse, statusLine, httpEntity, clientConnectionManager);

        GeocodeResponse geocodeResponse = subject.lookupAddress(37.422782, -122.085099);

        verify(httpClient, httpResponse, statusLine, httpEntity, clientConnectionManager);

        URI expectedUri = URI.create("http://maps.googleapis.com/maps/api/geocode/json"
                + "?latlng=" + URLEncoder.encode("37.422782,-122.085099", "UTF-8")
                + "&sensor=false");

        assertThat(capturedHttpGet.getValue().getURI(), is(expectedUri));
        assertThat(geocodeResponse.getGeocodeStatus(), is(ZERO_RESULTS));
        assertThat(geocodeResponse.getQueryString(), is("37.422782, -122.085099"));
        assertThat(geocodeResponse.getGeocodedAddresses(), is(Collections.<GeocodedAddress>emptyList()));
    }

    @Test
    public void shouldGeocodeArbitraryRequest() throws Exception {
        Capture<HttpGet> capturedHttpGet = new Capture<HttpGet>();
        HttpResponse httpResponse = EasyMock.createMock(HttpResponse.class);
        StatusLine statusLine = EasyMock.createMock(StatusLine.class);
        HttpEntity httpEntity = EasyMock.createMock(HttpEntity.class);
        ClientConnectionManager clientConnectionManager = EasyMock.createMock(ClientConnectionManager.class);

        expect(httpClient.execute(capture(capturedHttpGet))).andReturn(httpResponse);
        expect(httpResponse.getStatusLine()).andReturn(statusLine);
        expect(statusLine.getStatusCode()).andReturn(HTTP_OK);
        expect(httpResponse.getEntity()).andReturn(httpEntity);
        expect(httpEntity.getContent()).andReturn(new ByteArrayInputStream("{\"status\":\"ZERO_RESULTS\",\"results\":[]}".getBytes()));
        expect(httpClient.getConnectionManager()).andReturn(clientConnectionManager);
        clientConnectionManager.closeExpiredConnections();
        replay(httpClient, httpResponse, statusLine, httpEntity, clientConnectionManager);

        GoogleGeocodeRequest googleGeocodeRequest = subject.newGeocodeRequestBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA")
                .withViewportBiasing(GeographicLocation.fromValues(36.421776, -123.084665), GeographicLocation.fromValues(38.421776, -121.084665))
                .withRegionBiasing("us")
                .inLanguage("en")
                .build();

        GeocodeResponse geocodeResponse = subject.geocode(googleGeocodeRequest);

        verify(httpClient, httpResponse, statusLine, httpEntity, clientConnectionManager);

        URI expectedUri = URI.create("http://maps.googleapis.com/maps/api/geocode/json"
                + "?address=" + URLEncoder.encode("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", "UTF-8")
                + "&bounds=" + URLEncoder.encode("36.421776,-123.084665|38.421776,-121.084665", "UTF-8")
                + "&region=us"
                + "&language=en"
                + "&sensor=false");

        assertThat(capturedHttpGet.getValue().getURI(), is(expectedUri));
        assertThat(geocodeResponse.getGeocodeStatus(), is(ZERO_RESULTS));
        assertThat(geocodeResponse.getQueryString(), is("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA"));
        assertThat(geocodeResponse.getGeocodedAddresses(), is(Collections.<GeocodedAddress>emptyList()));
    }

    @Test
    public void shouldUseSignatureTheGeocodeRequest() throws Exception {
        subject = GoogleGeocoderFactory.createPremierGoogleGeocoder(httpClient, "johndoe", "foo");
        Capture<HttpGet> capturedHttpGet = new Capture<HttpGet>();
        HttpResponse httpResponse = EasyMock.createMock(HttpResponse.class);
        StatusLine statusLine = EasyMock.createMock(StatusLine.class);
        HttpEntity httpEntity = EasyMock.createMock(HttpEntity.class);
        ClientConnectionManager clientConnectionManager = EasyMock.createMock(ClientConnectionManager.class);

        expect(httpClient.execute(capture(capturedHttpGet))).andReturn(httpResponse);
        expect(httpResponse.getStatusLine()).andReturn(statusLine);
        expect(statusLine.getStatusCode()).andReturn(HTTP_OK);
        expect(httpResponse.getEntity()).andReturn(httpEntity);
        expect(httpEntity.getContent()).andReturn(new ByteArrayInputStream("{\"status\":\"ZERO_RESULTS\",\"results\":[]}".getBytes()));
        expect(httpClient.getConnectionManager()).andReturn(clientConnectionManager);
        clientConnectionManager.closeExpiredConnections();
        replay(httpClient, httpResponse, statusLine, httpEntity, clientConnectionManager);

        GeocodeResponse geocodeResponse = subject.geocodeAddress("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA");

        verify(httpClient, httpResponse, statusLine, httpEntity, clientConnectionManager);

        URI expectedUri = URI.create("http://maps.googleapis.com/maps/api/geocode/json"
                + "?address=" + URLEncoder.encode("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", "UTF-8")
                + "&client=johndoe"
                + "&sensor=false"
                + "&signature=EARBW423RYvqC4gtI9YHoefgB0s=");

        assertThat(capturedHttpGet.getValue().getURI(), is(expectedUri));
        assertThat(geocodeResponse.getGeocodeStatus(), is(ZERO_RESULTS));
        assertThat(geocodeResponse.getQueryString(), is("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA"));
        assertThat(geocodeResponse.getGeocodedAddresses(), is(Collections.<GeocodedAddress>emptyList()));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldCloseGoogleGeocoderSoThatFurtherGeocodingRequestsAreNotPossible() {
        GoogleGeocoder defaultGoogleGeocoder = GoogleGeocoderFactory.createDefaultGoogleGeocoder();

        defaultGoogleGeocoder.close();
        defaultGoogleGeocoder.geocodeAddress("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA");
    }
}
