package com.thedasmc.stocks2.common;

import com.google.gson.JsonElement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;

public class JsonTools {

    public static String asString(JsonElement element) {
        return isNull(element) ? null : element.getAsString();
    }

    public static BigDecimal asBigDecimal(JsonElement element) {
        return isNull(element) ? null : element.getAsBigDecimal();
    }

    public static Boolean asBoolean(JsonElement element) {
        return isNull(element) ? null : element.getAsBoolean();
    }

    public static boolean isNull(JsonElement element) {
        return element == null || element.isJsonNull();
    }

    public static String readJson(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder jsonBuilder = new StringBuilder();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            jsonBuilder.append(line);
        }

        return jsonBuilder.toString();
    }

}
