package com.github.sandrasi.geocoder.google.v3;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import com.github.sandrasi.geocoder.GeocodeException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.Validate;

/**
 * {@code GoogleMapsApiPremierCredentials} holds the Google Maps API premium client ID and key used to access the
 * premium web services. The key is not revealed to the users of this class, it is only used internally to sign any
 * text.
 */
public class GoogleMapsApiPremierCredentials {

    private static final String CHARACTER_ENCODING = "UTF-8";
    private static final String SECRET_KEY_ALGORITHM = "HmacSHA1";

    private final String clientId;
    private final byte[] key;

    /**
     * @param clientId The Google Maps API Premier id to access premier benefits of the
     * Google Maps API. For more information see <a href="http://www.google.com/enterprise/earthmaps/maps.html">
     * Google Maps API Premier</a>.
     * @param key the cryptographic key used to sign the premier Google Maps API Web Services requests
     * @throws NullPointerException if either {@code clientId} or {@code key} is {@code null}
     * @throws IllegalArgumentException if either {@code clientId} or {@code key} blank
     */
    public GoogleMapsApiPremierCredentials(String clientId, String key) {
        Validate.notBlank(clientId, "clientId is required and must not be blank");
        Validate.notBlank(key, "key is required and must not be blank");

        try {
            this.clientId = clientId;
            this.key = Base64.decodeBase64(decodeModifiedBase64(key).getBytes(CHARACTER_ENCODING));
        } catch (UnsupportedEncodingException e) {
            throw new GeocodeException("The character encoding used to decode the key is not supported", e);
        }
    }

    /**
     * Returns the Google Maps API Premier client id.
     *
     * @return the client id
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Returns the signature for the given string.
     *
     * @param string the string to sign
     * @return the HMAC-SHA1 encoded signature
     * @throws NullPointerException if {@code string} is {@code null}
     */
    public String getSignatureFor(String string) {
        Validate.notNull(string, "string is required");

        try {
            SecretKeySpec secretKey = new SecretKeySpec(key, SECRET_KEY_ALGORITHM);
            Mac mac = Mac.getInstance(SECRET_KEY_ALGORITHM);

            mac.init(secretKey);

            byte[] signature = mac.doFinal(string.getBytes());

            return encodeModifiedBase64(new String(Base64.encodeBase64(signature), CHARACTER_ENCODING));
        } catch (NoSuchAlgorithmException e) {
            throw new GeocodeException("The algorithm used to sign the string does not exist", e);
        } catch (InvalidKeyException e) {
            throw new GeocodeException("The private key used to sign the string is invalid", e);
        } catch (UnsupportedEncodingException e) {
            throw new GeocodeException("The character encoding used to sign the string is not supported", e);
        }
    }

    private String decodeModifiedBase64(String string) {
        return string.replace('-', '+').replace('_', '/');
    }

    private String encodeModifiedBase64(String string) {
        return string.replace('+', '-').replace('/', '_');
    }
}
