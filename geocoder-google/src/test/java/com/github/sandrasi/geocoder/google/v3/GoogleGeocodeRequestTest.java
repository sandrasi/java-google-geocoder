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
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static com.github.sandrasi.geocoder.components.GeocodeStatus.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Matchers.any;

public class GoogleGeocodeRequestTest {

    private static final int HTTP_OK = 200;
    private static final int HTTP_FORBIDDEN = 403;

    private HttpClient httpClient;
    private GoogleGeocoder googleGeocoder;

    @Before
    public void setUp() {
        httpClient = mock(HttpClient.class);
        googleGeocoder = GoogleGeocoderFactory.createGoogleGeocoder(httpClient);
    }

    @Test
    public void shouldExecuteAddressGeocoding() throws Exception {
        GoogleGeocodeRequest subject = googleGeocoder.newGeocodeRequestBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA")
                .inLanguage("en")
                .withRegionBiasing("us")
                .withViewportBiasing(GeographicLocation.fromValues(0, 0), GeographicLocation.fromValues(1, 1))
                .build();
        HttpResponse httpResponse = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        HttpEntity httpEntity = mock(HttpEntity.class);
        ClientConnectionManager clientConnectionManager = mock(ClientConnectionManager.class);

        given(httpClient.execute(any(HttpGet.class))).willReturn(httpResponse);
        given(httpResponse.getStatusLine()).willReturn(statusLine);
        given(statusLine.getStatusCode()).willReturn(HTTP_OK);
        given(httpResponse.getEntity()).willReturn(httpEntity);
        given(httpEntity.getContent()).willReturn(new ByteArrayInputStream("{\"status\":\"ZERO_RESULTS\",\"results\":[]}".getBytes()));
        given(httpClient.getConnectionManager()).willReturn(clientConnectionManager);
        clientConnectionManager.closeExpiredConnections();

        GeocodeResponse geocodeResponse = subject.execute();

        ArgumentCaptor<HttpGet> httpGetCaptor = ArgumentCaptor.forClass(HttpGet.class);
        verify(httpClient).execute(httpGetCaptor.capture());

        URI expectedUri = URI.create("http://maps.googleapis.com/maps/api/geocode/json?"
                + "address=" + URLEncoder.encode("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", "UTF-8")
                + "&bounds=" + URLEncoder.encode("0.0,0.0|1.0,1.0", "UTF-8")
                + "&region=us"
                + "&language=en"
                + "&sensor=false");

        assertThat(httpGetCaptor.getValue().getURI(), is(expectedUri));
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
        HttpResponse httpResponse = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        HttpEntity httpEntity = mock(HttpEntity.class);
        ClientConnectionManager clientConnectionManager = mock(ClientConnectionManager.class);

        given(httpClient.execute(any(HttpGet.class))).willReturn(httpResponse);
        given(httpResponse.getStatusLine()).willReturn(statusLine);
        given(statusLine.getStatusCode()).willReturn(HTTP_OK);
        given(httpResponse.getEntity()).willReturn(httpEntity);
        given(httpEntity.getContent()).willReturn(new ByteArrayInputStream("{\"status\":\"ZERO_RESULTS\",\"results\":[]}".getBytes()));
        given(httpClient.getConnectionManager()).willReturn(clientConnectionManager);
        clientConnectionManager.closeExpiredConnections();

        GeocodeResponse geocodeResponse = subject.execute();

        ArgumentCaptor<HttpGet> httpGetCaptor = ArgumentCaptor.forClass(HttpGet.class);
        verify(httpClient).execute(httpGetCaptor.capture());

        URI expectedUri = URI.create("http://maps.googleapis.com/maps/api/geocode/json?"
                + "latlng=" + URLEncoder.encode("37.422782,-122.085099", "UTF-8")
                + "&bounds=" + URLEncoder.encode("0.0,0.0|1.0,1.0", "UTF-8")
                + "&region=us"
                + "&language=en"
                + "&sensor=false");

        assertThat(httpGetCaptor.getValue().getURI(), is(expectedUri));
        assertThat(geocodeResponse.getGeocodeStatus(), is(ZERO_RESULTS));
        assertThat(geocodeResponse.getQueryString(), is("37.422782, -122.085099"));
        assertThat(geocodeResponse.getGeocodedAddresses(), is(Collections.<GeocodedAddress>emptyList()));
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfHttpProtocolErrorOccurs() throws Exception {
        GoogleGeocodeRequest subject = googleGeocoder.newGeocodeRequestBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA").build();
        ClientConnectionManager clientConnectionManager = mock(ClientConnectionManager.class);

        given(httpClient.execute(any(HttpGet.class))).willThrow(ClientProtocolException.class);
        given(httpClient.getConnectionManager()).willReturn(clientConnectionManager);
        clientConnectionManager.closeExpiredConnections();

        subject.execute();
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfTheGoogleGeocodeServiceReturnsAnErrorCode() throws Exception {
        GoogleGeocodeRequest subject = googleGeocoder.newGeocodeRequestBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA").build();
        HttpResponse httpResponse = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        HttpEntity httpEntity = mock(HttpEntity.class);
        ClientConnectionManager clientConnectionManager = mock(ClientConnectionManager.class);

        given(httpClient.execute(any(HttpGet.class))).willReturn(httpResponse);
        given(httpResponse.getStatusLine()).willReturn(statusLine);
        given(statusLine.getStatusCode()).willReturn(HTTP_FORBIDDEN);
        given(httpResponse.getStatusLine()).willReturn(statusLine);
        given(statusLine.getStatusCode()).willReturn(HTTP_FORBIDDEN);
        given(httpResponse.getStatusLine()).willReturn(statusLine);
        given(statusLine.getReasonPhrase()).willReturn("Forbidden");
        given(httpResponse.getEntity()).willReturn(httpEntity);
        given(httpEntity.getContent()).willReturn(new ByteArrayInputStream("Unable to authenticate the supplied URL. Please check your client and signature parameters.".getBytes()));
        given(httpClient.getConnectionManager()).willReturn(clientConnectionManager);
        clientConnectionManager.closeExpiredConnections();

        subject.execute();
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfResponseCanNotBeRead() throws Exception {
        GoogleGeocodeRequest subject = googleGeocoder.newGeocodeRequestBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA").build();
        HttpResponse httpResponse = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        HttpEntity httpEntity = mock(HttpEntity.class);
        ClientConnectionManager clientConnectionManager = mock(ClientConnectionManager.class);

        given(httpClient.execute(any(HttpGet.class))).willReturn(httpResponse);
        given(httpResponse.getStatusLine()).willReturn(statusLine);
        given(statusLine.getStatusCode()).willReturn(HTTP_OK);
        given(httpResponse.getEntity()).willReturn(httpEntity);
        given(httpEntity.getContent()).willThrow(IOException.class);
        given(httpClient.getConnectionManager()).willReturn(clientConnectionManager);
        clientConnectionManager.closeExpiredConnections();

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
