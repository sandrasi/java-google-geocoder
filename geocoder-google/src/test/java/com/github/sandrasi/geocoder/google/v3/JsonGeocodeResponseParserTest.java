package com.github.sandrasi.geocoder.google.v3;

import static com.github.sandrasi.geocoder.components.AddressComponentType.POLITICAL;
import static com.github.sandrasi.geocoder.components.AddressComponentType.STREET_ADDRESS;
import static com.github.sandrasi.geocoder.components.GeocodeStatus.OK;
import static com.github.sandrasi.geocoder.components.LocationType.ROOFTOP;
import static org.easymock.EasyMock.anyInt;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.junit.Test;

import com.github.sandrasi.geocoder.GeocodeException;
import com.github.sandrasi.geocoder.components.AddressComponent;
import com.github.sandrasi.geocoder.components.AddressComponentType;
import com.github.sandrasi.geocoder.components.GeocodedAddress;
import com.github.sandrasi.geocoder.components.GeographicArea;
import com.github.sandrasi.geocoder.components.GeographicLocation;
import com.github.sandrasi.geocoder.components.Geometry;

public class JsonGeocodeResponseParserTest {

    @Test
    public void shouldParseJsonGeocodeResponse() {
        String jsonGeocodeResponse =
                "{"
                + "    \"status\":\"OK\","
                + "    \"results\":["
                + "        {"
                + "            \"types\":[\"street_address\"],"
                + "            \"formatted_address\":\"1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\","
                + "            \"address_components\":["
                + "                {"
                + "                    \"long_name\":\"1600\","
                + "                    \"short_name\":\"1600\","
                + "                    \"types\":[\"street_number\"]"
                + "                },"
                + "                {"
                + "                    \"long_name\":\"Amphitheatre Pkwy\","
                + "                    \"short_name\":\"Amphitheatre Pkwy\","
                + "                    \"types\":[\"route\"]"
                + "                },"
                + "                {"
                + "                    \"long_name\":\"Mountain View\","
                + "                    \"short_name\":\"Mountain View\","
                + "                    \"types\":[\"locality\",\"political\"]"
                + "                },"
                + "                {"
                + "                    \"long_name\":\"San Jose\","
                + "                    \"short_name\":\"San Jose\","
                + "                    \"types\":[\"administrative_area_level_3\",\"political\"]"
                + "                },"
                + "                {"
                + "                    \"long_name\":\"Santa Clara\","
                + "                    \"short_name\":\"Santa Clara\","
                + "                    \"types\":[\"administrative_area_level_2\",\"political\"]"
                + "                },"
                + "                {"
                + "                    \"long_name\":\"California\","
                + "                    \"short_name\":\"CA\","
                + "                    \"types\":[\"administrative_area_level_1\",\"political\"]"
                + "                },"
                + "                {"
                + "                    \"long_name\":\"United States\","
                + "                    \"short_name\":\"US\","
                + "                    \"types\":[\"country\",\"political\"]"
                + "                },"
                + "                {"
                + "                    \"long_name\":\"94043\","
                + "                    \"short_name\":\"94043\","
                + "                    \"types\":[\"postal_code\"]"
                + "                }"
                + "            ],"
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
                + "                        \"lat\":38.4259296,"
                + "                        \"lng\":-121.0819514"
                + "                    }"
                + "                }"
                + "            }"
                + "        }"
                + "    ]"
                + "}";

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
    public void shouldIgnoreUnknownAddressComponents() {
        String jsonGeocodeResponse =
                "{"
              + "    \"status\":\"OK\","
              + "    \"results\":["
              + "        {"
              + "            \"types\":[\"street_address\"],"
              + "            \"formatted_address\":\"1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA\","
              + "            \"address_components\":["
              + "                {"
              + "                    \"long_name\":\"1600\","
              + "                    \"short_name\":\"1600\","
              + "                    \"types\":[\"invalid_address_component\"]"
              + "                }"
              + "            ],"
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

       GoogleGeocodeResponse googleGeocodeResponse = JsonGeocodeResponseParser.parse("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", new ByteArrayInputStream(jsonGeocodeResponse.getBytes()));

       assertFalse(googleGeocodeResponse.getGeocodedAddresses().get(0).addressComponentIterator().hasNext());
    }

    @Test
    public void shouldParseEmptyResults() {
        String jsonGeocodeResponse =
                "{"
              + "    \"status\":\"ZERO_RESULTS\","
              + "    \"results\":[]"
              + "}";

        GoogleGeocodeResponse googleGeocodeResponse = JsonGeocodeResponseParser.parse("there is no such address", new ByteArrayInputStream(jsonGeocodeResponse.getBytes()));

        assertTrue(googleGeocodeResponse.getGeocodedAddresses().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfOriginalQueryStringIsNull() {
        String jsonGeocodeResponse =
                "{"
              + "    \"status\":\"ZERO_RESULTS\","
              + "    \"results\":[]"
              + "}";

        JsonGeocodeResponseParser.parse(null, new ByteArrayInputStream(jsonGeocodeResponse.getBytes()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfJsonInutStreamIsNull() {
        JsonGeocodeResponseParser.parse("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", null);
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfAnIoExceptionOccursWhileReadingTheResponse() throws Exception {
        InputStream inputStream = createMock(InputStream.class);

        expect(inputStream.read((byte[]) anyObject(), anyInt(), anyInt())).andThrow(new IOException("Test I/O exception"));
        replay(inputStream);

        JsonGeocodeResponseParser.parse("Test I/O exception", inputStream);
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfTheStatusFieldIsMissingFromTheResponse() {
        String jsonGeocodeResponse =
            "{"
                + "\"results\":[]"
          + "}";

        JsonGeocodeResponseParser.parse("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", new ByteArrayInputStream(jsonGeocodeResponse.getBytes()));
    }

    @Test(expected = GeocodeException.class)
    public void shouldThrowExceptionIfTheResultsArrayIsMissingFromTheResponse() {
        String jsonGeocodeResponse =
            "{"
                + "\"status\":\"ZERO_RESULTS\""
          + "}";

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
