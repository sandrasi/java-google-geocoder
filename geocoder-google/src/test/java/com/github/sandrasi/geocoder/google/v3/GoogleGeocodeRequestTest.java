package com.github.sandrasi.geocoder.google.v3;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Collections;

import com.github.sandrasi.geocoder.GeocodeException;
import com.github.sandrasi.geocoder.GeocodeResponse;
import com.github.sandrasi.geocoder.components.GeocodedAddress;
import com.github.sandrasi.geocoder.components.GeographicLocation;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import static com.github.sandrasi.geocoder.components.GeocodeStatus.*;
import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class GoogleGeocodeRequestTest {

    private static final int HTTP_OK = 200;
    private static final int HTTP_FORBIDDEN = 403;

    private HttpClient httpClient;
    private GoogleGeocoder googleGeocoder;

    @Before
    public void setUp() {
        httpClient = EasyMock.createMock(HttpClient.class);
        googleGeocoder = GoogleGeocoderFactory.createGoogleGeocoder(httpClient);
    }

    @Test
    public void shouldExecuteAddressGeocoding() throws Exception {
        GoogleGeocodeRequest subject = googleGeocoder.newGeocodeRequestBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA")
                .inLanguage("en")
                .withRegionBiasing("us")
                .withViewportBiasing(GeographicLocation.fromValues(0, 0), GeographicLocation.fromValues(1, 1))
                .build();
        Capture<HttpGet> capturedHttpGet = new Capture<>();
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

        GeocodeResponse geocodeResponse = subject.execute();

        verify(httpClient, httpResponse, statusLine, httpEntity, clientConnectionManager);

        URI expectedUri = URI.create("http://maps.googleapis.com/maps/api/geocode/json?"
                + "address=" + URLEncoder.encode("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", "UTF-8")
                + "&bounds=" + URLEncoder.encode("0.0,0.0|1.0,1.0", "UTF-8")
                + "&region=us"
                + "&language=en"
                + "&sensor=false");

        assertThat(capturedHttpGet.getValue().getURI(), is(expectedUri));
        assertThat(geocodeResponse.getGeocodeStatus(), is(ZERO_RESULTS));
        assertThat(geocodeResponse.getQueryString(), is("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA"));
        assertThat(geocodeResponse.getGeocodedAddresses(), is(Collections.<GeocodedAddress>emptyList()));
    }

    @Test
    public void shouldExecuteAddressLookup() throws Exception {
        GoogleGeocodeRequest subject = googleGeocoder.newGeocodeRequestBuilder(37.422782, -122.085099)
                .inLanguage("en")
                .withRegionBiasing("us")
                .withViewportBiasing(GeographicLocation.fromValues(0, 0), GeographicLocation.fromValues(1, 1))
                .build();
        Capture<HttpGet> capturedHttpGet = new Capture<>();
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

        GeocodeResponse geocodeResponse = subject.execute();

        verify(httpClient, httpResponse, statusLine, httpEntity, clientConnectionManager);

        URI expectedUri = URI.create("http://maps.googleapis.com/maps/api/geocode/json?"
                + "latlng=" + URLEncoder.encode("37.422782,-122.085099", "UTF-8")
                + "&bounds=" + URLEncoder.encode("0.0,0.0|1.0,1.0", "UTF-8")
                + "&region=us"
                + "&language=en"
                + "&sensor=false");

        assertThat(capturedHttpGet.getValue().getURI(), is(expectedUri));
        assertThat(geocodeResponse.getGeocodeStatus(), is(ZERO_RESULTS));
        assertThat(geocodeResponse.getQueryString(), is("37.422782, -122.085099"));
        assertThat(geocodeResponse.getGeocodedAddresses(), is(Collections.<GeocodedAddress>emptyList()));
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfHttpProtocolErrorOccurs() throws Exception {
        GoogleGeocodeRequest subject = googleGeocoder.newGeocodeRequestBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA").build();
        ClientConnectionManager clientConnectionManager = EasyMock.createMock(ClientConnectionManager.class);

        expect(httpClient.execute(anyObject(HttpGet.class))).andThrow(new ClientProtocolException("test exception"));
        expect(httpClient.getConnectionManager()).andReturn(clientConnectionManager);
        clientConnectionManager.closeExpiredConnections();
        replay(httpClient, clientConnectionManager);

        subject.execute();
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfTheGoogleGeocodeServiceReturnsAnErrorCode() throws Exception {
        GoogleGeocodeRequest subject = googleGeocoder.newGeocodeRequestBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA").build();
        HttpResponse httpResponse = EasyMock.createMock(HttpResponse.class);
        StatusLine statusLine = EasyMock.createMock(StatusLine.class);
        HttpEntity httpEntity = EasyMock.createMock(HttpEntity.class);
        ClientConnectionManager clientConnectionManager = EasyMock.createMock(ClientConnectionManager.class);

        expect(httpClient.execute(anyObject(HttpGet.class))).andReturn(httpResponse);
        expect(httpResponse.getStatusLine()).andReturn(statusLine);
        expect(statusLine.getStatusCode()).andReturn(HTTP_FORBIDDEN);
        expect(httpResponse.getStatusLine()).andReturn(statusLine);
        expect(statusLine.getStatusCode()).andReturn(HTTP_FORBIDDEN);
        expect(httpResponse.getStatusLine()).andReturn(statusLine);
        expect(statusLine.getReasonPhrase()).andReturn("Forbidden");
        expect(httpResponse.getEntity()).andReturn(httpEntity);
        expect(httpEntity.getContent()).andReturn(new ByteArrayInputStream("Unable to authenticate the supplied URL. Please check your client and signature parameters.".getBytes()));
        expect(httpClient.getConnectionManager()).andReturn(clientConnectionManager);
        clientConnectionManager.closeExpiredConnections();
        replay(httpClient, httpResponse, statusLine, httpEntity, clientConnectionManager);

        subject.execute();
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfResponseCanNotBeRead() throws Exception {
        GoogleGeocodeRequest subject = googleGeocoder.newGeocodeRequestBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA").build();
        HttpResponse httpResponse = EasyMock.createMock(HttpResponse.class);
        StatusLine statusLine = EasyMock.createMock(StatusLine.class);
        HttpEntity httpEntity = EasyMock.createMock(HttpEntity.class);
        ClientConnectionManager clientConnectionManager = EasyMock.createMock(ClientConnectionManager.class);

        expect(httpClient.execute(anyObject(HttpGet.class))).andReturn(httpResponse);
        expect(httpResponse.getStatusLine()).andReturn(statusLine);
        expect(statusLine.getStatusCode()).andReturn(HTTP_OK);
        expect(httpResponse.getEntity()).andReturn(httpEntity);
        expect(httpEntity.getContent()).andThrow(new IOException("test exception"));
        expect(httpClient.getConnectionManager()).andReturn(clientConnectionManager);
        clientConnectionManager.closeExpiredConnections();
        replay(httpClient, httpResponse, statusLine, httpEntity, clientConnectionManager);

        subject.execute();
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionIfAddressIsNullInBuilder() {
        googleGeocoder.newGeocodeRequestBuilder((String) null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionIfSouthWestCornerIsSetToNullInBuilder() {
        googleGeocoder.newGeocodeRequestBuilder("address").withViewportBiasing(null, GeographicLocation.fromValues(0, 0));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionIfNorthEastCornerIsSetToNullInBuilder() {
        googleGeocoder.newGeocodeRequestBuilder("address").withViewportBiasing(GeographicLocation.fromValues(0, 0), null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionIfRegionBiasIsSetToNullInBuilder() {
        googleGeocoder.newGeocodeRequestBuilder("address").withRegionBiasing(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionIfLanguageIsSetToNullInBuilder() {
        googleGeocoder.newGeocodeRequestBuilder("address").inLanguage(null);
    }
}
