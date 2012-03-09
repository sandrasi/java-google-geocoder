package com.github.sandrasi.geocoder.components;

/**
 * {@code AddressComponentType} contains enumerated values to indicate the type of
 * a particular address component.
 */
public enum AddressComponentType {

    /**
     * Indicates a first-order civil entity below the country level. For example within
     * the United States, these administrative levels are states.
     */
    ADMINISTRATIVE_AREA_LEVEL_1,

    /**
     * Indicates a second-order civil entity below the country level. For example within
     * the United States, these administrative levels are counties.
     */
    ADMINISTRATIVE_AREA_LEVEL_2,

    /**
     * Indicates a third-order civil entity below the country level. This type indicates
     * a minor civil division.
     */
    ADMINISTRATIVE_AREA_LEVEL_3,

    /**
     * Indicates an airport.
     */
    AIRPORT,

    /**
     * Indicates a bus station.
     */
    BUS_STATION,

    /**
     * Indicates a church.
     */
    CHURCH,

    /**
     * Indicates a city hall.
     */
    CITY_HALL,

    /**
     * Indicates a commonly-used alternative name for the entity.
     */
    COLLOQUIAL_AREA,

    /**
     * Indicates the national political entity, and is typically the highest order type returned
     * by a geocoder service.
     */
    COUNTRY,

    /**
     * Indicates a courthouse.
     */
    COURTHOUSE,

    /**
     * Indicates a named establishment.
     */
    ESTABLISHMENT,

    /**
     * Indicates the floor of a building address.
     */
    FLOOR,

    /**
     * Indicates a healthcare location.
     */
    HEALTH,

    /**
     * Indicates a hospital.
     */
    HOSPITAL,

    /**
     * Indicates a major intersection, usually of two major roads.
     */
    INTERSECTION,

    /**
     * Indicates a library.
     */
    LIBRARY,

    /**
     * Indicates an incorporated city or town political entity.
     */
    LOCALITY,

    /**
     * Indicates a local government office.
     */
    LOCAL_GOVERNMENT_OFFICE,

    /**
     * Indicates a museum.
     */
    MUSEUM,

    /**
     * Indicates a prominent natural feature.
     */
    NATURAL_FEATURE,

    /**
     * Indicates a named neighborhood.
     */
    NEIGHBORHOOD,

    /**
     * Indicates a named park.
     */
    PARK,

    /**
     * Indicates a place of worship.
     */
    PLACE_OF_WORSHIP,

    /**
     * Indicates a named point of interest. Typically, these "POI"s are prominent local
     * entities that don't easily fit in another category such as "Empire State Building"
     * or "Statue of Liberty."
     */
    POINT_OF_INTEREST,

    /**
     * Indicates a political entity. Usually, this type indicates a polygon of some civil
     * administration.
     */
    POLITICAL,

    /**
     * Indicates a specific postal box.
     */
    POST_BOX,

    /**
     * Indicates a postal code.
     */
    POSTAL_CODE,

    /**
     * Indicates a postal code prefix.
     */
    POSTAL_CODE_PREFIX,

    /**
     * Indicates a postal town. Normally its main function is to distinguish between locality
     * or street names in addresses not including a postcode.
     */
    POSTAL_TOWN,

    /**
     * Indicates a post office.
     */
    POST_OFFICE,

    /**
     * Indicates a named location, usually a building or collection of buildings with a common name.
     */
    PREMISE,

    /**
     * Indicates the room of a building address.
     */
    ROOM,

    /**
     * Indicates a named route (such as "US 101").
     */
    ROUTE,

    /**
     * Indicates a school.
     */
    SCHOOL,

    /**
     * Indicates a precise street address.
     */
    STREET_ADDRESS,

    /**
     * Indicates the precise street number.
     */
    STREET_NUMBER,

    /**
     * Indicates an first-order civil entity below a locality.
     */
    SUBLOCALITY,

    /**
     * Indicates a first-order entity below a named location, usually a singular building within
     * a collection of buildings with a common name.
     */
    SUBPREMISE,

    /**
     * Indicates a subway station.
     */
    SUBWAY_STATION,

    /**
     * Indicates a train station.
     */
    TRAIN_STATION,

    /**
     * Indicates a transit station.
     */
    TRANSIT_STATION,

    /**
     * Inidicates a university.
     */
    UNIVERSITY
}
