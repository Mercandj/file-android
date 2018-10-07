package com.mercandalli.sdk.files.api.online.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Static methods for dealing with hash.
 */
@SuppressWarnings("unused")
public final class HashUtils {

    private static final String TAG = "HashUtils";

    /**
     * Non-instantiable.
     */
    private HashUtils() {
        // Non-instantiable.
    }

    /**
     * Hash a {@link String} with the sha1 method.
     *
     * @param text The input {@link String}.
     * @return The sha1 hash.
     */
    public static String sha1(final String text) {
        if (text == null) {
            return null;
        }
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(text.getBytes("iso-8859-1"), 0, text.length());
            return convertToHex(messageDigest.digest());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
        }
        return null;
    }

    /**
     * Hash a {@link String} with the sha1 method.
     *
     * @param text The input {@link String}.
     * @param time Do "time" hash consecutively.
     * @return The sha1 hash.
     */
    public static String sha1(final String text, final int time) {
        if (text == null) {
            return null;
        }
        String result = text;
        final MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        for (int i = 0; i < time; i++) {
            result = hash(messageDigest, result);
        }
        return result;
    }

    public static String hash(
            final MessageDigest messageDigest,
            final String text) {
        if (text == null) {
            return null;
        }
        try {
            messageDigest.update(text.getBytes("iso-8859-1"), 0, text.length());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return convertToHex(messageDigest.digest());
    }

    /**
     * Hash a {@link String} with the sha256 method.
     *
     * @param text The input {@link String}.
     * @return The sha1 hash.
     */
    public static String sha256(final String text) {
        if (text == null) {
            return null;
        }
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(text.getBytes("iso-8859-1"), 0, text.length());
            return convertToHex(messageDigest.digest());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
        }
        return null;
    }

    public static String sha256(final String text, final int time) {
        if (text == null) {
            return null;
        }
        String result = text;
        final MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        for (int i = 0; i < time; i++) {
            result = hash(messageDigest, result);
        }
        return result;
    }

    private static String convertToHex(final byte[] data) {
        final StringBuilder buf = new StringBuilder();
        for (final byte b : data) {
            int halfByte = (b >>> 4) & 0x0F;
            int twoHalf = 0;
            do {
                buf.append((0 <= halfByte) && (halfByte <= 9) ? (char) ('0' + halfByte)
                        : (char) ('a' + (halfByte - 10)));
                halfByte = b & 0x0F;
            } while (twoHalf++ < 1);
        }
        return buf.toString();
    }
}

