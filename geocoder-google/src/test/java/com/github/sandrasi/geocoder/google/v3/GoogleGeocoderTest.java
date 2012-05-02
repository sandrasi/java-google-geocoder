package com.github.sandrasi.geocoder.google.v3;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Collections;

import com.github.sandrasi.geocoder.GeocodeResponse;
import com.github.sandrasi.geocoder.components.GeocodedAddress;
import com.github.sandrasi.geocoder.components.GeographicLocation;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static com.github.sandrasi.geocoder.components.GeocodeStatus.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class GoogleGeocoderTest {

    private static final int HTTP_OK = 200;

    private HttpClient httpClient;
    private GoogleGeocoder subject;

    @Before
    public void setUp() {
        httpClient = mock(HttpClient.class);
        subject = GoogleGeocoderFactory.createGoogleGeocoder(httpClient);
    }

    @Test
    public void shouldGeocodeAddress() throws Exception {
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

        GeocodeResponse geocodeResponse = subject.geocodeAddress("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA");

        ArgumentCaptor<HttpGet> httpGetCaptor = ArgumentCaptor.forClass(HttpGet.class);
        verify(httpClient).execute(httpGetCaptor.capture());

        URI expectedUri = URI.create("http://maps.googleapis.com/maps/api/geocode/json"
                + "?address=" + URLEncoder.encode("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", "UTF-8")
                + "&sensor=false");

        assertThat(httpGetCaptor.getValue().getURI(), is(expectedUri));
        assertThat(geocodeResponse.getGeocodeStatus(), is(ZERO_RESULTS));
        assertThat(geocodeResponse.getQueryString(), is("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA"));
        assertThat(geocodeResponse.getGeocodedAddresses(), is(Collections.<GeocodedAddress>emptyList()));
    }

    @Test
    public void shouldLookupAddress() throws Exception {
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

        GeocodeResponse geocodeResponse = subject.lookupAddress(37.422782, -122.085099);

        ArgumentCaptor<HttpGet> httpGetCaptor = ArgumentCaptor.forClass(HttpGet.class);
        verify(httpClient).execute(httpGetCaptor.capture());

        URI expectedUri = URI.create("http://maps.googleapis.com/maps/api/geocode/json"
                + "?latlng=" + URLEncoder.encode("37.422782,-122.085099", "UTF-8")
                + "&sensor=false");

        assertThat(httpGetCaptor.getValue().getURI(), is(expectedUri));
        assertThat(geocodeResponse.getGeocodeStatus(), is(ZERO_RESULTS));
        assertThat(geocodeResponse.getQueryString(), is("37.422782, -122.085099"));
        assertThat(geocodeResponse.getGeocodedAddresses(), is(Collections.<GeocodedAddress>emptyList()));
    }

    @Test
    public void shouldGeocodeArbitraryRequest() throws Exception {
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

        GoogleGeocodeRequest googleGeocodeRequest = subject.newGeocodeRequestBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA")
                .withViewportBiasing(GeographicLocation.fromValues(36.421776, -123.084665), GeographicLocation.fromValues(38.421776, -121.084665))
                .withRegionBiasing("us")
                .inLanguage("en")
                .build();

        GeocodeResponse geocodeResponse = subject.geocode(googleGeocodeRequest);

        ArgumentCaptor<HttpGet> httpGetCaptor = ArgumentCaptor.forClass(HttpGet.class);
        verify(httpClient).execute(httpGetCaptor.capture());

        URI expectedUri = URI.create("http://maps.googleapis.com/maps/api/geocode/json"
                + "?address=" + URLEncoder.encode("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", "UTF-8")
                + "&bounds=" + URLEncoder.encode("36.421776,-123.084665|38.421776,-121.084665", "UTF-8")
                + "&region=us"
                + "&language=en"
                + "&sensor=false");

        assertThat(httpGetCaptor.getValue().getURI(), is(expectedUri));
        assertThat(geocodeResponse.getGeocodeStatus(), is(ZERO_RESULTS));
        assertThat(geocodeResponse.getQueryString(), is("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA"));
        assertThat(geocodeResponse.getGeocodedAddresses(), is(Collections.<GeocodedAddress>emptyList()));
    }

    @Test
    public void shouldUseSignatureInTheGeocodeRequest() throws Exception {
        subject = GoogleGeocoderFactory.createPremierGoogleGeocoder(httpClient, "johndoe", "foo");
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

        GeocodeResponse geocodeResponse = subject.geocodeAddress("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA");

        ArgumentCaptor<HttpGet> httpGetCaptor = ArgumentCaptor.forClass(HttpGet.class);
        verify(httpClient).execute(httpGetCaptor.capture());

        URI expectedUri = URI.create("http://maps.googleapis.com/maps/api/geocode/json"
                + "?address=" + URLEncoder.encode("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", "UTF-8")
                + "&client=johndoe"
                + "&sensor=false"
                + "&signature=EARBW423RYvqC4gtI9YHoefgB0s=");

        assertThat(httpGetCaptor.getValue().getURI(), is(expectedUri));
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
