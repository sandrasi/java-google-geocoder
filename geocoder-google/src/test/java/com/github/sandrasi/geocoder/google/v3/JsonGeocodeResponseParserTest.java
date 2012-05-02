package com.github.sandrasi.geocoder.google.v3;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import com.github.sandrasi.geocoder.GeocodeException;
import com.github.sandrasi.geocoder.components.*;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.mockito.Mockito;

import static com.github.sandrasi.geocoder.components.AddressComponentType.*;
import static com.github.sandrasi.geocoder.components.GeocodeStatus.*;
import static com.github.sandrasi.geocoder.components.LocationType.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;

public class JsonGeocodeResponseParserTest {

    @Test
    public void shouldParseJsonGeocodeResponse() throws Exception {
        String jsonGeocodeResponse = IOUtils.toString(JsonGeocodeResponseParserTest.class.getResourceAsStream("/json/valid/validGeocodeResponse.json"));

        GoogleGeocodeResponse googleGeocodeResponse = JsonGeocodeResponseParser.parse("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", new ByteArrayInputStream(jsonGeocodeResponse.getBytes()));

        Geometry expectedGeometry = Geometry.newBuilder(GeographicLocation.fromValues(37.4227820, -122.0850990))
                .setLocationType(ROOFTOP)
                .setViewport(new GeographicArea(GeographicLocation.fromValues(37.4196344, -122.0882466), GeographicLocation.fromValues(37.4259296, -122.0819514)))
                .setBounds(new GeographicArea(GeographicLocation.fromValues(36.4196344, -123.0882466), GeographicLocation.fromValues(38.4259296, -121.0819514)))
                .build();
        GeocodedAddress expectedGeocodedAddress = GeocodedAddress.newBuilder("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA")
                .addAddressType(STREET_ADDRESS)
                .addAddressComponent(AddressComponent.newBuilder(AddressComponentType.STREET_NUMBER).setLongName("1600").setShortName("1600").build())
                .addAddressComponent(AddressComponent.newBuilder(AddressComponentType.ROUTE).setLongName("Amphitheatre Pkwy").setShortName("Amphitheatre Pkwy").build())
                .addAddressComponent(AddressComponent.newBuilder(AddressComponentType.LOCALITY).addAddressComponentType(POLITICAL).setLongName("Mountain View").setShortName("Mountain View").build())
                .addAddressComponent(AddressComponent.newBuilder(AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_3).addAddressComponentType(POLITICAL).setLongName("San Jose").setShortName("San Jose").build())
                .addAddressComponent(AddressComponent.newBuilder(AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_2).addAddressComponentType(POLITICAL).setLongName("Santa Clara").setShortName("Santa Clara").build())
                .addAddressComponent(AddressComponent.newBuilder(AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1).addAddressComponentType(POLITICAL).setLongName("California").setShortName("CA").build())
                .addAddressComponent(AddressComponent.newBuilder(AddressComponentType.COUNTRY).addAddressComponentType(POLITICAL).setLongName("United States").setShortName("US").build())
                .addAddressComponent(AddressComponent.newBuilder(AddressComponentType.POSTAL_CODE).setLongName("94043").setShortName("94043").build())
                .setGeometry(expectedGeometry)
                .build();

        assertThat(googleGeocodeResponse.getQueryString(), is("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA"));
        assertThat(googleGeocodeResponse.getGeocodeStatus(), is(OK));
        assertThat(googleGeocodeResponse.getGeocodedAddresses(), is(Arrays.asList(expectedGeocodedAddress)));
    }

    @Test
    public void shouldIgnoreUnknownAddressComponents() throws Exception {
        String jsonGeocodeResponse = IOUtils.toString(JsonGeocodeResponseParserTest.class.getResourceAsStream("/json/invalid/unknownAddressComponentGeocodeResponse.json"));

       GoogleGeocodeResponse googleGeocodeResponse = JsonGeocodeResponseParser.parse("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", new ByteArrayInputStream(jsonGeocodeResponse.getBytes()));

       assertFalse(googleGeocodeResponse.getGeocodedAddresses().get(0).addressComponentIterator().hasNext());
    }

    @Test
    public void shouldParseEmptyResults() throws Exception {
        String jsonGeocodeResponse = IOUtils.toString(JsonGeocodeResponseParserTest.class.getResourceAsStream("/json/valid/emptyGeocodeResponse.json"));

        GoogleGeocodeResponse googleGeocodeResponse = JsonGeocodeResponseParser.parse("there is no such address", new ByteArrayInputStream(jsonGeocodeResponse.getBytes()));

        assertTrue(googleGeocodeResponse.getGeocodedAddresses().isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionIfOriginalQueryStringIsNull() throws Exception {
        String jsonGeocodeResponse = IOUtils.toString(JsonGeocodeResponseParserTest.class.getResourceAsStream("/json/valid/emptyGeocodeResponse.json"));

        JsonGeocodeResponseParser.parse(null, new ByteArrayInputStream(jsonGeocodeResponse.getBytes()));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionIfJsonInutStreamIsNull() {
        JsonGeocodeResponseParser.parse("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", null);
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfAnIoExceptionOccursWhileReadingTheResponse() throws Exception {
        InputStream inputStream = mock(InputStream.class);

        given(inputStream.read(Mockito.any(byte[].class), anyInt(), anyInt())).willThrow(IOException.class);

        JsonGeocodeResponseParser.parse("Test I/O exception", inputStream);
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfTheStatusFieldIsMissingFromTheResponse() throws Exception {
        String jsonGeocodeResponse = IOUtils.toString(JsonGeocodeResponseParserTest.class.getResourceAsStream("/json/invalid/missingStatusFieldGeocodeResponse.json"));

        JsonGeocodeResponseParser.parse("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", new ByteArrayInputStream(jsonGeocodeResponse.getBytes()));
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfTheResultsArrayIsMissingFromTheResponse() throws Exception {
        String jsonGeocodeResponse = IOUtils.toString(JsonGeocodeResponseParserTest.class.getResourceAsStream("/json/invalid/missingResultsArrayGeocodeResponse.json"));

        JsonGeocodeResponseParser.parse("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", new ByteArrayInputStream(jsonGeocodeResponse.getBytes()));
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfTheTypesArrayIsMissingFromTheResultObject() {
        String jsonGeocodeResponse =
            "{"
          + "    \"status\":\"ZERO_RESULTS\","
          + "    \"results\":["
          + "        {"
          + "            \"formatted_address\":\"1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\","
          + "            \"address_components\":[],"
          + "            \"geometry\":{"
          + "                \"location\":{"
          + "                    \"lat\":37.4227820,"
          + "                    \"lng\":-122.0850990"
          + "                },"
          + "                \"location_type\":\"ROOFTOP\","
          + "                \"viewport\":{"
          + "                    \"southwest\":{"
          + "                        \"lat\":37.4196344,"
          + "                        \"lng\":-122.0882466"
          + "                    },"
          + "                    \"northeast\":{"
          + "                        \"lat\":37.4259296,"
          + "                        \"lng\":-122.0819514"
          + "                    }"
          + "                }"
          + "            }"
          + "        }"
          + "    ]"
          + "}";

        JsonGeocodeResponseParser.parse("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", new ByteArrayInputStream(jsonGeocodeResponse.getBytes()));
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfTheFormattedAddressFieldIsMissingFromTheResultObject() {
        String jsonGeocodeResponse =
            "{"
          + "    \"status\":\"ZERO_RESULTS\","
          + "    \"results\":["
          + "        {"
          + "            \"types\":[\"street_address\"],"
          + "            \"address_components\":[],"
          + "            \"geometry\":{"
          + "                \"location\":{"
          + "                    \"lat\":37.4227820,"
          + "                    \"lng\":-122.0850990"
          + "                },"
          + "                \"location_type\":\"ROOFTOP\","
          + "                \"viewport\":{"
          + "                    \"southwest\":{"
          + "                        \"lat\":37.4196344,"
          + "                        \"lng\":-122.0882466"
          + "                    },"
          + "                    \"northeast\":{"
          + "                        \"lat\":37.4259296,"
          + "                        \"lng\":-122.0819514"
          + "                    }"
          + "                }"
          + "            }"
          + "        }"
          + "    ]"
          + "}";

        JsonGeocodeResponseParser.parse("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", new ByteArrayInputStream(jsonGeocodeResponse.getBytes()));
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfTheAddressComponentsArrayIsMissingFromTheResultObject() {
        String jsonGeocodeResponse =
            "{"
          + "    \"status\":\"ZERO_RESULTS\","
          + "    \"results\":["
          + "        {"
          + "            \"types\":[\"street_address\"],"
          + "            \"formatted_address\":\"1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\","
          + "            \"geometry\":{"
          + "                \"location\":{"
          + "                    \"lat\":37.4227820,"
          + "                    \"lng\":-122.0850990"
          + "                },"
          + "                \"location_type\":\"ROOFTOP\","
          + "                \"viewport\":{"
          + "                    \"southwest\":{"
          + "                        \"lat\":37.4196344,"
          + "                        \"lng\":-122.0882466"
          + "                    },"
          + "                    \"northeast\":{"
          + "                        \"lat\":37.4259296,"
          + "                        \"lng\":-122.0819514"
          + "                    }"
          + "                }"
          + "            }"
          + "        }"
          + "    ]"
          + "}";

        JsonGeocodeResponseParser.parse("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", new ByteArrayInputStream(jsonGeocodeResponse.getBytes()));
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfTheGeometryObjectIsMissingFromTheResultObject() {
        String jsonGeocodeResponse =
            "{"
          + "    \"status\":\"ZERO_RESULTS\","
          + "    \"results\":["
          + "        {"
          + "            \"types\":[\"street_address\"],"
          + "            \"formatted_address\":\"1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\","
          + "            \"address_components\":[]"
          + "        }"
          + "    ]"
          + "}";

        JsonGeocodeResponseParser.parse("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", new ByteArrayInputStream(jsonGeocodeResponse.getBytes()));
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfTheLongNameFieldIsMissingFromTheAddressComponentObject() {
        String jsonGeocodeResponse =
            "{"
          + "    \"status\":\"ZERO_RESULTS\","
          + "    \"results\":["
          + "        {"
          + "            \"types\":[\"street_address\"],"
          + "            \"formatted_address\":\"1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\","
          + "            \"address_components\":["
          + "                {"
          + "                    \"short_name\":\"US\","
          + "                    \"types\":[\"country\",\"political\"]"
          + "                }"
          +              "],"
          + "            \"geometry\":{"
          + "                \"location\":{"
          + "                    \"lat\":37.4227820,"
          + "                    \"lng\":-122.0850990"
          + "                },"
          + "                \"location_type\":\"ROOFTOP\","
          + "                \"viewport\":{"
          + "                    \"southwest\":{"
          + "                        \"lat\":37.4196344,"
          + "                        \"lng\":-122.0882466"
          + "                    },"
          + "                    \"northeast\":{"
          + "                        \"lat\":37.4259296,"
          + "                        \"lng\":-122.0819514"
          + "                    }"
          + "                }"
          + "            }"
          + "        }"
          + "    ]"
          + "}";

        JsonGeocodeResponseParser.parse("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", new ByteArrayInputStream(jsonGeocodeResponse.getBytes()));
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfTheShortNameFieldIsMissingFromTheAddressComponentObject() {
        String jsonGeocodeResponse =
            "{"
          + "    \"status\":\"ZERO_RESULTS\","
          + "    \"results\":["
          + "        {"
          + "            \"types\":[\"street_address\"],"
          + "            \"formatted_address\":\"1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\","
          + "            \"address_components\":["
          + "                {"
          + "                    \"long_name\":\"United States\","
          + "                    \"types\":[\"country\",\"political\"]"
          + "                }"
          +              "],"
          + "            \"geometry\":{"
          + "                \"location\":{"
          + "                    \"lat\":37.4227820,"
          + "                    \"lng\":-122.0850990"
          + "                },"
          + "                \"location_type\":\"ROOFTOP\","
          + "                \"viewport\":{"
          + "                    \"southwest\":{"
          + "                        \"lat\":37.4196344,"
          + "                        \"lng\":-122.0882466"
          + "                    },"
          + "                    \"northeast\":{"
          + "                        \"lat\":37.4259296,"
          + "                        \"lng\":-122.0819514"
          + "                    }"
          + "                }"
          + "            }"
          + "        }"
          + "    ]"
          + "}";

        JsonGeocodeResponseParser.parse("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", new ByteArrayInputStream(jsonGeocodeResponse.getBytes()));
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfTheTypesArrayIsMissingFromTheAddressComponentObject() {
        String jsonGeocodeResponse =
            "{"
          + "    \"status\":\"ZERO_RESULTS\","
          + "    \"results\":["
          + "        {"
          + "            \"types\":[\"street_address\"],"
          + "            \"formatted_address\":\"1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\","
          + "            \"address_components\":["
          + "                {"
          + "                    \"long_name\":\"United States\","
          + "                    \"short_name\":\"US\""
          + "                }"
          +              "],"
          + "            \"geometry\":{"
          + "                \"location\":{"
          + "                    \"lat\":37.4227820,"
          + "                    \"lng\":-122.0850990"
          + "                },"
          + "                \"location_type\":\"ROOFTOP\","
          + "                \"viewport\":{"
          + "                    \"southwest\":{"
          + "                        \"lat\":37.4196344,"
          + "                        \"lng\":-122.0882466"
          + "                    },"
          + "                    \"northeast\":{"
          + "                        \"lat\":37.4259296,"
          + "                        \"lng\":-122.0819514"
          + "                    }"
          + "                }"
          + "            }"
          + "        }"
          + "    ]"
          + "}";

        JsonGeocodeResponseParser.parse("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", new ByteArrayInputStream(jsonGeocodeResponse.getBytes()));
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfTheLocationObjectIsMissingFromTheGeometryObject() {
        String jsonGeocodeResponse =
            "{"
          + "    \"status\":\"ZERO_RESULTS\","
          + "    \"results\":["
          + "        {"
          + "            \"types\":[\"street_address\"],"
          + "            \"formatted_address\":\"1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\","
          + "            \"address_components\":["
          + "                {"
          + "                    \"long_name\":\"United States\","
          + "                    \"short_name\":\"US\","
          + "                    \"types\":[\"country\",\"political\"]"
          + "                }"
          +              "],"
          + "            \"geometry\":{"
          + "                \"location_type\":\"ROOFTOP\","
          + "                \"viewport\":{"
          + "                    \"southwest\":{"
          + "                        \"lat\":37.4196344,"
          + "                        \"lng\":-122.0882466"
          + "                    },"
          + "                    \"northeast\":{"
          + "                        \"lat\":37.4259296,"
          + "                        \"lng\":-122.0819514"
          + "                    }"
          + "                }"
          + "            }"
          + "        }"
          + "    ]"
          + "}";

        JsonGeocodeResponseParser.parse("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", new ByteArrayInputStream(jsonGeocodeResponse.getBytes()));
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfTheLocationTypeFieldIsMissingFromTheGeometryObject() {
        String jsonGeocodeResponse =
            "{"
          + "    \"status\":\"ZERO_RESULTS\","
          + "    \"results\":["
          + "        {"
          + "            \"types\":[\"street_address\"],"
          + "            \"formatted_address\":\"1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\","
          + "            \"address_components\":["
          + "                {"
          + "                    \"long_name\":\"United States\","
          + "                    \"short_name\":\"US\","
          + "                    \"types\":[\"country\",\"political\"]"
          + "                }"
          +              "],"
          + "            \"geometry\":{"
          + "                \"location\":{"
          + "                    \"lat\":37.4227820,"
          + "                    \"lng\":-122.0850990"
          + "                },"
          + "                \"viewport\":{"
          + "                    \"southwest\":{"
          + "                        \"lat\":37.4196344,"
          + "                        \"lng\":-122.0882466"
          + "                    },"
          + "                    \"northeast\":{"
          + "                        \"lat\":37.4259296,"
          + "                        \"lng\":-122.0819514"
          + "                    }"
          + "                }"
          + "            }"
          + "        }"
          + "    ]"
          + "}";

        JsonGeocodeResponseParser.parse("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", new ByteArrayInputStream(jsonGeocodeResponse.getBytes()));
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfTheViewportObjectIsMissingFromTheGeometryObject() {
        String jsonGeocodeResponse =
            "{"
          + "    \"status\":\"ZERO_RESULTS\","
          + "    \"results\":["
          + "        {"
          + "            \"types\":[\"street_address\"],"
          + "            \"formatted_address\":\"1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\","
          + "            \"address_components\":["
          + "                {"
          + "                    \"long_name\":\"United States\","
          + "                    \"short_name\":\"US\","
          + "                    \"types\":[\"country\",\"political\"]"
          + "                }"
          +              "],"
          + "            \"geometry\":{"
          + "                \"location\":{"
          + "                    \"lat\":37.4227820,"
          + "                    \"lng\":-122.0850990"
          + "                },"
          + "                \"location_type\":\"ROOFTOP\""
          + "            }"
          + "        }"
          + "    ]"
          + "}";

        JsonGeocodeResponseParser.parse("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", new ByteArrayInputStream(jsonGeocodeResponse.getBytes()));
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfTheLatitudeFieldIsMissingFromTheLocationObject() {
        String jsonGeocodeResponse =
            "{"
          + "    \"status\":\"ZERO_RESULTS\","
          + "    \"results\":["
          + "        {"
          + "            \"types\":[\"street_address\"],"
          + "            \"formatted_address\":\"1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\","
          + "            \"address_components\":["
          + "                {"
          + "                    \"long_name\":\"United States\","
          + "                    \"short_name\":\"US\","
          + "                    \"types\":[\"country\",\"political\"]"
          + "                }"
          +              "],"
          + "            \"geometry\":{"
          + "                \"location\":{"
          + "                    \"lng\":-122.0850990"
          + "                },"
          + "                \"location_type\":\"ROOFTOP\","
          + "                \"viewport\":{"
          + "                    \"southwest\":{"
          + "                        \"lat\":37.4196344,"
          + "                        \"lng\":-122.0882466"
          + "                    },"
          + "                    \"northeast\":{"
          + "                        \"lat\":37.4259296,"
          + "                        \"lng\":-122.0819514"
          + "                    }"
          + "                }"
          + "            }"
          + "        }"
          + "    ]"
          + "}";

        JsonGeocodeResponseParser.parse("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", new ByteArrayInputStream(jsonGeocodeResponse.getBytes()));
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfTheLongitudeFieldIsMissingFromTheLocationObject() {
        String jsonGeocodeResponse =
            "{"
          + "    \"status\":\"ZERO_RESULTS\","
          + "    \"results\":["
          + "        {"
          + "            \"types\":[\"street_address\"],"
          + "            \"formatted_address\":\"1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\","
          + "            \"address_components\":["
          + "                {"
          + "                    \"long_name\":\"United States\","
          + "                    \"short_name\":\"US\","
          + "                    \"types\":[\"country\",\"political\"]"
          + "                }"
          +              "],"
          + "            \"geometry\":{"
          + "                \"location\":{"
          + "                    \"lat\":37.4227820"
          + "                },"
          + "                \"location_type\":\"ROOFTOP\","
          + "                \"viewport\":{"
          + "                    \"southwest\":{"
          + "                        \"lat\":37.4196344,"
          + "                        \"lng\":-122.0882466"
          + "                    },"
          + "                    \"northeast\":{"
          + "                        \"lat\":37.4259296,"
          + "                        \"lng\":-122.0819514"
          + "                    }"
          + "                }"
          + "            }"
          + "        }"
          + "    ]"
          + "}";

        JsonGeocodeResponseParser.parse("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", new ByteArrayInputStream(jsonGeocodeResponse.getBytes()));
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfTheSouthWestObjectIsMissingFromTheViewportObject() {
        String jsonGeocodeResponse =
            "{"
          + "    \"status\":\"ZERO_RESULTS\","
          + "    \"results\":["
          + "        {"
          + "            \"types\":[\"street_address\"],"
          + "            \"formatted_address\":\"1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\","
          + "            \"address_components\":["
          + "                {"
          + "                    \"long_name\":\"United States\","
          + "                    \"short_name\":\"US\","
          + "                    \"types\":[\"country\",\"political\"]"
          + "                }"
          +              "],"
          + "            \"geometry\":{"
          + "                \"location\":{"
          + "                    \"lat\":37.4227820,"
          + "                    \"lng\":-122.0850990"
          + "                },"
          + "                \"location_type\":\"ROOFTOP\","
          + "                \"viewport\":{"
          + "                    \"northeast\":{"
          + "                        \"lat\":37.4259296,"
          + "                        \"lng\":-122.0819514"
          + "                    }"
          + "                }"
          + "            }"
          + "        }"
          + "    ]"
          + "}";

        JsonGeocodeResponseParser.parse("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", new ByteArrayInputStream(jsonGeocodeResponse.getBytes()));
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfTheNorthEastObjectIsMissingFromTheViewportObject() {
        String jsonGeocodeResponse =
            "{"
          + "    \"status\":\"ZERO_RESULTS\","
          + "    \"results\":["
          + "        {"
          + "            \"types\":[\"street_address\"],"
          + "            \"formatted_address\":\"1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\","
          + "            \"address_components\":["
          + "                {"
          + "                    \"long_name\":\"United States\","
          + "                    \"short_name\":\"US\","
          + "                    \"types\":[\"country\",\"political\"]"
          + "                }"
          +              "],"
          + "            \"geometry\":{"
          + "                \"location\":{"
          + "                    \"lat\":37.4227820,"
          + "                    \"lng\":-122.0850990"
          + "                },"
          + "                \"location_type\":\"ROOFTOP\","
          + "                \"viewport\":{"
          + "                    \"southwest\":{"
          + "                        \"lat\":37.4196344,"
          + "                        \"lng\":-122.0882466"
          + "                    }"
          + "                }"
          + "            }"
          + "        }"
          + "    ]"
          + "}";

        JsonGeocodeResponseParser.parse("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", new ByteArrayInputStream(jsonGeocodeResponse.getBytes()));
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfTheLatitudeFieldIsMissingFromTheViewportSouthWestObject() {
        String jsonGeocodeResponse =
            "{"
          + "    \"status\":\"ZERO_RESULTS\","
          + "    \"results\":["
          + "        {"
          + "            \"types\":[\"street_address\"],"
          + "            \"formatted_address\":\"1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\","
          + "            \"address_components\":["
          + "                {"
          + "                    \"long_name\":\"United States\","
          + "                    \"short_name\":\"US\","
          + "                    \"types\":[\"country\",\"political\"]"
          + "                }"
          +              "],"
          + "            \"geometry\":{"
          + "                \"location\":{"
          + "                    \"lat\":37.4227820,"
          + "                    \"lng\":-122.0850990"
          + "                },"
          + "                \"location_type\":\"ROOFTOP\","
          + "                \"viewport\":{"
          + "                    \"southwest\":{"
          + "                        \"lng\":-122.0882466"
          + "                    },"
          + "                    \"northeast\":{"
          + "                        \"lat\":37.4259296,"
          + "                        \"lng\":-122.0819514"
          + "                    }"
          + "                }"
          + "            }"
          + "        }"
          + "    ]"
          + "}";

        JsonGeocodeResponseParser.parse("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", new ByteArrayInputStream(jsonGeocodeResponse.getBytes()));
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfTheLongitudeFieldIsMissingFromTheViewportSouthWestObject() {
        String jsonGeocodeResponse =
            "{"
          + "    \"status\":\"ZERO_RESULTS\","
          + "    \"results\":["
          + "        {"
          + "            \"types\":[\"street_address\"],"
          + "            \"formatted_address\":\"1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\","
          + "            \"address_components\":["
          + "                {"
          + "                    \"long_name\":\"United States\","
          + "                    \"short_name\":\"US\","
          + "                    \"types\":[\"country\",\"political\"]"
          + "                }"
          +              "],"
          + "            \"geometry\":{"
          + "                \"location\":{"
          + "                    \"lat\":37.4227820,"
          + "                    \"lng\":-122.0850990"
          + "                },"
          + "                \"location_type\":\"ROOFTOP\","
          + "                \"viewport\":{"
          + "                    \"southwest\":{"
          + "                        \"lat\":37.4196344"
          + "                    },"
          + "                    \"northeast\":{"
          + "                        \"lat\":37.4259296,"
          + "                        \"lng\":-122.0819514"
          + "                    }"
          + "                }"
          + "            }"
          + "        }"
          + "    ]"
          + "}";

        JsonGeocodeResponseParser.parse("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", new ByteArrayInputStream(jsonGeocodeResponse.getBytes()));
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfTheLatitudeFieldIsMissingFromTheViewportNorthEastObject() {
        String jsonGeocodeResponse =
            "{"
          + "    \"status\":\"ZERO_RESULTS\","
          + "    \"results\":["
          + "        {"
          + "            \"types\":[\"street_address\"],"
          + "            \"formatted_address\":\"1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\","
          + "            \"address_components\":["
          + "                {"
          + "                    \"long_name\":\"United States\","
          + "                    \"short_name\":\"US\","
          + "                    \"types\":[\"country\",\"political\"]"
          + "                }"
          +              "],"
          + "            \"geometry\":{"
          + "                \"location\":{"
          + "                    \"lat\":37.4227820,"
          + "                    \"lng\":-122.0850990"
          + "                },"
          + "                \"location_type\":\"ROOFTOP\","
          + "                \"viewport\":{"
          + "                    \"southwest\":{"
          + "                        \"lat\":37.4196344,"
          + "                        \"lng\":-122.0882466"
          + "                    },"
          + "                    \"northeast\":{"
          + "                        \"lng\":-122.0819514"
          + "                    }"
          + "                }"
          + "            }"
          + "        }"
          + "    ]"
          + "}";

        JsonGeocodeResponseParser.parse("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", new ByteArrayInputStream(jsonGeocodeResponse.getBytes()));
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfTheLongitudeFieldIsMissingFromTheViewportNorthEastObject() {
        String jsonGeocodeResponse =
            "{"
          + "    \"status\":\"ZERO_RESULTS\","
          + "    \"results\":["
          + "        {"
          + "            \"types\":[\"street_address\"],"
          + "            \"formatted_address\":\"1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\","
          + "            \"address_components\":["
          + "                {"
          + "                    \"long_name\":\"United States\","
          + "                    \"short_name\":\"US\","
          + "                    \"types\":[\"country\",\"political\"]"
          + "                }"
          +              "],"
          + "            \"geometry\":{"
          + "                \"location\":{"
          + "                    \"lat\":37.4227820,"
          + "                    \"lng\":-122.0850990"
          + "                },"
          + "                \"location_type\":\"ROOFTOP\","
          + "                \"viewport\":{"
          + "                    \"southwest\":{"
          + "                        \"lat\":37.4196344,"
          + "                        \"lng\":-122.0882466"
          + "                    },"
          + "                    \"northeast\":{"
          + "                        \"lat\":37.4259296"
          + "                    }"
          + "                }"
          + "            }"
          + "        }"
          + "    ]"
          + "}";

        JsonGeocodeResponseParser.parse("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", new ByteArrayInputStream(jsonGeocodeResponse.getBytes()));
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfTheSouthWestObjectIsMissingFromTheBoundsObject() {
        String jsonGeocodeResponse =
            "{"
          + "    \"status\":\"ZERO_RESULTS\","
          + "    \"results\":["
          + "        {"
          + "            \"types\":[\"street_address\"],"
          + "            \"formatted_address\":\"1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\","
          + "            \"address_components\":["
          + "                {"
          + "                    \"long_name\":\"United States\","
          + "                    \"short_name\":\"US\","
          + "                    \"types\":[\"country\",\"political\"]"
          + "                }"
          +              "],"
          + "            \"geometry\":{"
          + "                \"location\":{"
          + "                    \"lat\":37.4227820,"
          + "                    \"lng\":-122.0850990"
          + "                },"
          + "                \"location_type\":\"ROOFTOP\","
          + "                \"viewport\":{"
          + "                    \"southwest\":{"
          + "                        \"lat\":37.4196344,"
          + "                        \"lng\":-122.0882466"
          + "                    },"
          + "                    \"northeast\":{"
          + "                        \"lat\":37.4259296,"
          + "                        \"lng\":-122.0819514"
          + "                    }"
          + "                },"
          + "                \"bounds\":{"
          + "                    \"northeast\":{"
          + "                        \"lat\":38.4259296,"
          + "                        \"lng\":-121.0819514"
          + "                    }"
          + "                }"
          + "            }"
          + "        }"
          + "    ]"
          + "}";

        JsonGeocodeResponseParser.parse("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", new ByteArrayInputStream(jsonGeocodeResponse.getBytes()));
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfTheNorthEastObjectIsMissingFromTheBoundsObject() {
        String jsonGeocodeResponse =
            "{"
          + "    \"status\":\"ZERO_RESULTS\","
          + "    \"results\":["
          + "        {"
          + "            \"types\":[\"street_address\"],"
          + "            \"formatted_address\":\"1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\","
          + "            \"address_components\":["
          + "                {"
          + "                    \"long_name\":\"United States\","
          + "                    \"short_name\":\"US\","
          + "                    \"types\":[\"country\",\"political\"]"
          + "                }"
          +              "],"
          + "            \"geometry\":{"
          + "                \"location\":{"
          + "                    \"lat\":37.4227820,"
          + "                    \"lng\":-122.0850990"
          + "                },"
          + "                \"location_type\":\"ROOFTOP\","
          + "                \"viewport\":{"
          + "                    \"southwest\":{"
          + "                        \"lat\":37.4196344,"
          + "                        \"lng\":-122.0882466"
          + "                    },"
          + "                    \"northeast\":{"
          + "                        \"lat\":37.4259296,"
          + "                        \"lng\":-122.0819514"
          + "                    }"
          + "                },"
          + "                \"bounds\":{"
          + "                    \"southwest\":{"
          + "                        \"lat\":36.4196344,"
          + "                        \"lng\":-123.0882466"
          + "                    }"
          + "                }"
          + "            }"
          + "        }"
          + "    ]"
          + "}";

        JsonGeocodeResponseParser.parse("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", new ByteArrayInputStream(jsonGeocodeResponse.getBytes()));
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfTheLatitudeFieldIsMissingFromTheBoundsSouthWestObject() {
        String jsonGeocodeResponse =
            "{"
          + "    \"status\":\"ZERO_RESULTS\","
          + "    \"results\":["
          + "        {"
          + "            \"types\":[\"street_address\"],"
          + "            \"formatted_address\":\"1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\","
          + "            \"address_components\":["
          + "                {"
          + "                    \"long_name\":\"United States\","
          + "                    \"short_name\":\"US\","
          + "                    \"types\":[\"country\",\"political\"]"
          + "                }"
          +              "],"
          + "            \"geometry\":{"
          + "                \"location\":{"
          + "                    \"lat\":37.4227820,"
          + "                    \"lng\":-122.0850990"
          + "                },"
          + "                \"location_type\":\"ROOFTOP\","
          + "                \"viewport\":{"
          + "                    \"southwest\":{"
          + "                        \"lat\":37.4196344,"
          + "                        \"lng\":-122.0882466"
          + "                    },"
          + "                    \"northeast\":{"
          + "                        \"lat\":37.4259296,"
          + "                        \"lng\":-122.0819514"
          + "                    }"
          + "                },"
          + "                \"bounds\":{"
          + "                    \"southwest\":{"
          + "                        \"lng\":-123.0882466"
          + "                    },"
          + "                    \"northeast\":{"
          + "                        \"lat\":38.4259296,"
          + "                        \"lng\":-121.0819514"
          + "                    }"
          + "                }"
          + "            }"
          + "        }"
          + "    ]"
          + "}";

        JsonGeocodeResponseParser.parse("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", new ByteArrayInputStream(jsonGeocodeResponse.getBytes()));
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfTheLongitudeFieldIsMissingFromTheBoundsSouthWestObject() {
        String jsonGeocodeResponse =
            "{"
          + "    \"status\":\"ZERO_RESULTS\","
          + "    \"results\":["
          + "        {"
          + "            \"types\":[\"street_address\"],"
          + "            \"formatted_address\":\"1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\","
          + "            \"address_components\":["
          + "                {"
          + "                    \"long_name\":\"United States\","
          + "                    \"short_name\":\"US\","
          + "                    \"types\":[\"country\",\"political\"]"
          + "                }"
          +              "],"
          + "            \"geometry\":{"
          + "                \"location\":{"
          + "                    \"lat\":37.4227820,"
          + "                    \"lng\":-122.0850990"
          + "                },"
          + "                \"location_type\":\"ROOFTOP\","
          + "                \"viewport\":{"
          + "                    \"southwest\":{"
          + "                        \"lat\":37.4196344,"
          + "                        \"lng\":-122.0882466"
          + "                    },"
          + "                    \"northeast\":{"
          + "                        \"lat\":37.4259296,"
          + "                        \"lng\":-122.0819514"
          + "                    }"
          + "                },"
          + "                \"bounds\":{"
          + "                    \"southwest\":{"
          + "                        \"lat\":36.4196344"
          + "                    },"
          + "                    \"northeast\":{"
          + "                        \"lat\":38.4259296,"
          + "                        \"lng\":-121.0819514"
          + "                    }"
          + "                }"
          + "            }"
          + "        }"
          + "    ]"
          + "}";

        JsonGeocodeResponseParser.parse("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", new ByteArrayInputStream(jsonGeocodeResponse.getBytes()));
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfTheLatitudeFieldIsMissingFromTheBoundsNorthEastObject() {
        String jsonGeocodeResponse =
            "{"
          + "    \"status\":\"ZERO_RESULTS\","
          + "    \"results\":["
          + "        {"
          + "            \"types\":[\"street_address\"],"
          + "            \"formatted_address\":\"1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\","
          + "            \"address_components\":["
          + "                {"
          + "                    \"long_name\":\"United States\","
          + "                    \"short_name\":\"US\","
          + "                    \"types\":[\"country\",\"political\"]"
          + "                }"
          +              "],"
          + "            \"geometry\":{"
          + "                \"location\":{"
          + "                    \"lat\":37.4227820,"
          + "                    \"lng\":-122.0850990"
          + "                },"
          + "                \"location_type\":\"ROOFTOP\","
          + "                \"viewport\":{"
          + "                    \"southwest\":{"
          + "                        \"lat\":37.4196344,"
          + "                        \"lng\":-122.0882466"
          + "                    },"
          + "                    \"northeast\":{"
          + "                        \"lat\":37.4259296,"
          + "                        \"lng\":-122.0819514"
          + "                    }"
          + "                },"
          + "                \"bounds\":{"
          + "                    \"southwest\":{"
          + "                        \"lat\":36.4196344,"
          + "                        \"lng\":-123.0882466"
          + "                    },"
          + "                    \"northeast\":{"
          + "                        \"lng\":-121.0819514"
          + "                    }"
          + "                }"
          + "            }"
          + "        }"
          + "    ]"
          + "}";

        JsonGeocodeResponseParser.parse("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", new ByteArrayInputStream(jsonGeocodeResponse.getBytes()));
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfTheLongitudeFieldIsMissingFromTheBoundsNorthEastObject() {
        String jsonGeocodeResponse =
            "{"
          + "    \"status\":\"ZERO_RESULTS\","
          + "    \"results\":["
          + "        {"
          + "            \"types\":[\"street_address\"],"
          + "            \"formatted_address\":\"1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\","
          + "            \"address_components\":["
          + "                {"
          + "                    \"long_name\":\"United States\","
          + "                    \"short_name\":\"US\","
          + "                    \"types\":[\"country\",\"political\"]"
          + "                }"
          +              "],"
          + "            \"geometry\":{"
          + "                \"location\":{"
          + "                    \"lat\":37.4227820,"
          + "                    \"lng\":-122.0850990"
          + "                },"
          + "                \"location_type\":\"ROOFTOP\","
          + "                \"viewport\":{"
          + "                    \"southwest\":{"
          + "                        \"lat\":37.4196344,"
          + "                        \"lng\":-122.0882466"
          + "                    },"
          + "                    \"northeast\":{"
          + "                        \"lat\":37.4259296,"
          + "                        \"lng\":-122.0819514"
          + "                    }"
          + "                },"
          + "                \"bounds\":{"
          + "                    \"southwest\":{"
          + "                        \"lat\":36.4196344,"
          + "                        \"lng\":-123.0882466"
          + "                    },"
          + "                    \"northeast\":{"
          + "                        \"lat\":38.4259296"
          + "                    }"
          + "                }"
          + "            }"
          + "        }"
          + "    ]"
          + "}";

        JsonGeocodeResponseParser.parse("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", new ByteArrayInputStream(jsonGeocodeResponse.getBytes()));
    }
}
