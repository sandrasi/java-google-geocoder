package com.github.sandrasi.geocoder.google.v3;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.github.sandrasi.geocoder.GeocodeException;
import com.github.sandrasi.geocoder.components.*;
import org.apache.commons.lang3.Validate;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A parser to convert a JSON-string into {@link GoogleGeocodeResponse}.
 * The JSON output format of the Google geocoding response is described
 * <a href="http://code.google.com/apis/maps/documentation/geocoding/#JSON">here</a>.
 */
final class JsonGeocodeResponseParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonGeocodeResponseParser.class);

    private static final String FIELD_STATUS = "status";
    private static final String FIELD_RESULTS = "results";
    private static final String FIELD_TYPES = "types";
    private static final String FIELD_FORMATTED_ADDRESS = "formatted_address";
    private static final String FIELD_GEOMETRY = "geometry";
    private static final String FIELD_ADDRESS_COMPONENTS = "address_components";
    private static final String FIELD_LOCATION = "location";
    private static final String FIELD_LOCATION_TYPE = "location_type";
    private static final String FIELD_VIEWPORT = "viewport";
    private static final String FIELD_BOUNDS = "bounds";
    private static final String FIELD_LAT = "lat";
    private static final String FIELD_LNG = "lng";
    private static final String FIELD_SOUTHWEST = "southwest";
    private static final String FIELD_NORTHEAST = "northeast";
    private static final String FIELD_LONG_NAME = "long_name";
    private static final String FIELD_SHORT_NAME = "short_name";

    private JsonGeocodeResponseParser() {
    }

    /**
     * Parses the JSON-string representing a Google geocode response and converts it into
     * {@link GoogleGeocodeResponse}.
     *
     * @param originalQueryString the original query sent to the Google geocoding service
     * @param jsonInputStream the streamed geocode response in JSON format
     * @throws NullPointerException if {@code orignalQueryString} or {@code jsonInputStream} is {@code null}
     * @throws GeocodeException if an I/O exception occurs during the parsing
     * @return a Google geocode response
     */
    public static GoogleGeocodeResponse parse(String originalQueryString, InputStream jsonInputStream) {
        Validate.notNull(jsonInputStream, "jsonInputStream is required");

        try {
            JsonNode geocodeResponseRoot = new ObjectMapper().readValue(jsonInputStream, JsonNode.class);

            validateJsonFieldExists(geocodeResponseRoot, FIELD_STATUS);
            validateJsonFieldExists(geocodeResponseRoot, FIELD_RESULTS);

            return GoogleGeocodeResponse.newBuilder(originalQueryString)
                    .setGeocodeStatus(parseGeocodeStatus(geocodeResponseRoot))
                    .addGeocodedAddresses(parseGeocodedAddresses(geocodeResponseRoot))
                    .build();
        } catch (IOException e) {
            LOGGER.error("An I/O exception occured while parsing Google's geocode response");

            throw new GeocodeException("An I/O exception occured while parsing Google's geocode response", e);
        }
    }

    private static void validateJsonFieldExists(JsonNode jsonNode, String fieldName) {
        if (!jsonNode.has(fieldName)) {
            LOGGER.error("Mandatory field [" + fieldName + "] is missing from the response");

            throw new GeocodeException("Mandatory field [" + fieldName + "] is missing from the response");
        }
    }

    private static GeocodeStatus parseGeocodeStatus(JsonNode geocodeResponseRoot) {
        return GeocodeStatus.valueOf(geocodeResponseRoot.path(FIELD_STATUS).getTextValue().toUpperCase());
    }

    private static List<GeocodedAddress> parseGeocodedAddresses(JsonNode geocodeResponseRoot) {
        List<GeocodedAddress> geocodedAddresses = new ArrayList<>();

        for (JsonNode jsonGeocodedAddress : geocodeResponseRoot.path(FIELD_RESULTS)) {
            validateJsonFieldExists(jsonGeocodedAddress, FIELD_TYPES);
            validateJsonFieldExists(jsonGeocodedAddress, FIELD_FORMATTED_ADDRESS);
            validateJsonFieldExists(jsonGeocodedAddress, FIELD_ADDRESS_COMPONENTS);
            validateJsonFieldExists(jsonGeocodedAddress, FIELD_GEOMETRY);

            geocodedAddresses.add(GeocodedAddress.newBuilder(parseFormattedAddress(jsonGeocodedAddress.path(FIELD_FORMATTED_ADDRESS)))
                    .addAddressTypes(parseAddressComponentTypes(jsonGeocodedAddress.path(FIELD_TYPES)))
                    .addAddressComponents(parseAddressComponents(jsonGeocodedAddress.path(FIELD_ADDRESS_COMPONENTS)))
                    .setGeometry(parseGeometry(jsonGeocodedAddress.path(FIELD_GEOMETRY)))
                    .build());
        }

        return geocodedAddresses;
    }

    private static List<AddressComponentType> parseAddressComponentTypes(JsonNode jsonAddressComponentTypes) {
        List<AddressComponentType> addressComponentTypes = new ArrayList<>();

        for (JsonNode addressType : jsonAddressComponentTypes) {
            try {
                addressComponentTypes.add(AddressComponentType.valueOf(addressType.getTextValue().toUpperCase()));
            } catch (IllegalArgumentException e) {
                LOGGER.warn("Unknown address component type [" + addressType.getTextValue().toUpperCase() + "] has been found. Ignoring it.");
            }
        }

        return addressComponentTypes;
    }

    private static String parseFormattedAddress(JsonNode jsonFormattedAddress) {
        return jsonFormattedAddress.getTextValue();
    }

    private static Geometry parseGeometry(JsonNode jsonGeometry) {
        validateJsonFieldExists(jsonGeometry, FIELD_LOCATION);
        validateJsonFieldExists(jsonGeometry, FIELD_LOCATION_TYPE);
        validateJsonFieldExists(jsonGeometry, FIELD_VIEWPORT);

        Geometry.Builder geometryBuilder = Geometry.newBuilder(parseGeographicCoordinates(jsonGeometry.path(FIELD_LOCATION)))
                .setLocationType(parseLocationType(jsonGeometry.path(FIELD_LOCATION_TYPE)))
                .setViewport(parseGeographicArea(jsonGeometry.path(FIELD_VIEWPORT)));

        if (jsonGeometry.has(FIELD_BOUNDS)) {
            geometryBuilder.setBounds(parseGeographicArea(jsonGeometry.path(FIELD_BOUNDS)));
        }

        return geometryBuilder.build();
    }

    private static GeographicLocation parseGeographicCoordinates(JsonNode jsonGeographicCoordinates) {
        validateJsonFieldExists(jsonGeographicCoordinates, FIELD_LAT);
        validateJsonFieldExists(jsonGeographicCoordinates, FIELD_LNG);

        return GeographicLocation.fromValues(jsonGeographicCoordinates.path(FIELD_LAT).getDoubleValue(), jsonGeographicCoordinates.path(FIELD_LNG).getDoubleValue());
    }

    private static LocationType parseLocationType(JsonNode jsonLocationType) {
        return LocationType.valueOf(jsonLocationType.getTextValue().toUpperCase());
    }

    private static GeographicArea parseGeographicArea(JsonNode jsonGeographicArea) {
        validateJsonFieldExists(jsonGeographicArea, FIELD_SOUTHWEST);
        validateJsonFieldExists(jsonGeographicArea, FIELD_NORTHEAST);

        return new GeographicArea(parseGeographicCoordinates(jsonGeographicArea.path(FIELD_SOUTHWEST)), parseGeographicCoordinates(jsonGeographicArea.path(FIELD_NORTHEAST)));
    }

    private static List<AddressComponent> parseAddressComponents(JsonNode jsonAddressComponents) {
        List<AddressComponent> addressComponents = new ArrayList<>();

        for (JsonNode jsonAddressComponent : jsonAddressComponents) {
            validateJsonFieldExists(jsonAddressComponent, FIELD_LONG_NAME);
            validateJsonFieldExists(jsonAddressComponent, FIELD_SHORT_NAME);
            validateJsonFieldExists(jsonAddressComponent, FIELD_TYPES);

            List<AddressComponentType> addressComponentTypes = parseAddressComponentTypes(jsonAddressComponent.path(FIELD_TYPES));

            if (!addressComponentTypes.isEmpty()) {
                AddressComponent.Builder addressComponentBuilder = AddressComponent.newBuilder(addressComponentTypes.get(0))
                        .setLongName(parseLongName(jsonAddressComponent.path(FIELD_LONG_NAME)))
                        .setShortName(parseShortName(jsonAddressComponent.path(FIELD_SHORT_NAME)));

                if (addressComponentTypes.size() > 1) {
                    addressComponentBuilder.addAddressComponentTypes(addressComponentTypes.subList(1, addressComponentTypes.size()));
                }

                addressComponents.add(addressComponentBuilder.build());
            }
        }

        return addressComponents;
    }

    private static String parseLongName(JsonNode jsonLongName) {
        return jsonLongName.getTextValue();
    }

    private static String parseShortName(JsonNode jsonShortName) {
        return jsonShortName.getTextValue();
    }
}
