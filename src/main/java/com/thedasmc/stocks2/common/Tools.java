package com.thedasmc.stocks2.common;

import com.google.gson.*;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Tools {

    public static String formatStockSymbol(String symbol) {
        return symbol.trim().toUpperCase();
    }

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

    public static String readErrorStream(InputStream errorStream) throws IOException {
        String jsonError = readInputStream(errorStream);
        JsonElement jsonElement;

        try {
            jsonElement = new JsonParser().parse(jsonError);
        } catch (JsonParseException e) {
            return jsonError;
        }

        if (!jsonElement.isJsonObject())
            return jsonError;

        JsonObject obj = jsonElement.getAsJsonObject();

        if (obj.has("message"))
            return obj.get("message").getAsString();

        if (obj.has("error"))
            return obj.get("error").getAsString();

        return jsonError;
    }

    public static HttpURLConnection getHttpConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Accept", "application/json");
        connection.setConnectTimeout(3000);
        connection.setReadTimeout(5000);

        return connection;
    }

    public static HttpURLConnection getHttpDeleteConnection(URL url) throws IOException {
        HttpURLConnection connection = getHttpConnection(url);
        connection.setRequestMethod("DELETE");

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

    public static void writeBody(HttpURLConnection connection, String body) throws IOException {
        byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);
        connection.setFixedLengthStreamingMode(bodyBytes.length);

        try (OutputStream os =  connection.getOutputStream()) {
            os.write(bodyBytes);
        }
    }

    public static boolean canConvertToDouble(BigDecimal bigDecimal) {
        return bigDecimal.compareTo(BigDecimal.valueOf(Double.MAX_VALUE)) <= 0;
    }

}
