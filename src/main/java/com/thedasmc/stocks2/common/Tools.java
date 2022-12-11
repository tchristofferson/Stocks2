package com.thedasmc.stocks2.common;

import com.google.gson.JsonElement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;

public class Tools {

    public static String asString(JsonElement element) {
        return isNull(element) ? null : element.getAsString();
    }

    public static BigDecimal asBigDecimal(JsonElement element) {
        return isNull(element) ? null : element.getAsBigDecimal();
    }

    public static BigInteger toCents(BigDecimal value) {
        return value
            .multiply(BigDecimal.valueOf(100), new MathContext(0, RoundingMode.DOWN))
            .toBigInteger();
    }

    public static Boolean asBoolean(JsonElement element) {
        return isNull(element) ? null : element.getAsBoolean();
    }

    public static boolean isNull(JsonElement element) {
        return element == null || element.isJsonNull();
    }

    public static String readInputStream(InputStream inputStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        inputStream.close();
        return builder.toString();
    }

    public static HttpURLConnection getHttpConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Accept", "application/json");

        return connection;
    }

    public static HttpURLConnection getHttpGetConnection(URL url) throws IOException {
        HttpURLConnection connection = getHttpConnection(url);
        connection.setRequestMethod("GET");

        return connection;
    }

    public static HttpURLConnection getHttpPostConnection(URL url) throws IOException {
        HttpURLConnection connection = getHttpConnection(url);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");

        return connection;
    }

    public static boolean canConvertToDouble(BigDecimal bigDecimal) {
        return bigDecimal.compareTo(BigDecimal.valueOf(Double.MAX_VALUE)) <= 0;
    }

}
