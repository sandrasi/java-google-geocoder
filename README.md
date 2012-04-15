Java Google Geocoder
====================
Yet another Java wrapper around the Google Geocoder API.

Usage
-----
The simplest way to use the Google Geocoder is to create a default <code>GoogleGeocoder</code> instance by the <code>GoogleGeocoderFactory</code>.

```java
GoogleGeocoder googleGeocoder = GoogleGeocoderFactory.createDefaultGoogleGeocoder();
```

This will create a geocoder that can send geocoding requests to the Google Geocoding API. To geocode an address use

```java
GeocodeResponse geocodeResponse = googleGeocoder.geocodeAddress("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA");
```

and to look up a location use

```java
GeocodeResponse geocodeResponse = googleGeocoder.lookupAddress(37.422782, -122.085099);
```

Since no Google Maps API Premier credentials are used the use of the geocoding service is subject to a query limit of 2,500 requests per day and additionally a request rate limit is also enforced (see <a href="https://developers.google.com/maps/documentation/geocoding/#Limits">Usage Limits</a> of the Google Geocoding API). To prevent exceeding the query limit a timed geocoder can be instantiated:

```java
long requestRateInMilliseconds = 34560;
GoogleGeocoder googleGeocoder = GoogleGeocoderFactory.createTimedGoogleGeocoder(requestRateInMilliseconds);
```

The argument of the <code>GoogleGeocoderFactory.createTimedGoogleGeocoder(long)</code> method is a number that specifies the time in milliseconds that must elapse between two consecutive geocoding requests. If the second request happens earlier than the time given, the thread calling the geocodeAddress(String) or lookupAddress(double, double) method will sleep until the necessary time elapses and only after then it makes the geocoding request. The timing is very basic, only the request rate is controlled by it. Limiting the number of requests per day must be implemented externally, or - like in the above example - if the specified time period is long enough, it can also ensure that no more than a given number of requests are performed per day.

If you are subscribed to the Google Maps API Premier web services then you must have a client ID and a key. Using these credentials every request is signed by the geocoder. To use the premier geocoding services create an instance of the <code>GoogleGeocoder</code> as follows:

```java
GoogleGeocoder googleGeocoder = GoogleGeocoderFactory.createTimedPremierGoogleGeocoder("your-client-id", "your-key");
```

To geocode an address and to look up a location use the same <code>geocodeAddress(String)</code> and <code>lookupAddress(double, double)</code> methods mentioned previously.

