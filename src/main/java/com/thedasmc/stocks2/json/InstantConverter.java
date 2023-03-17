package com.thedasmc.stocks2.json;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Instant;

public class InstantConverter implements JsonSerializer<Instant>, JsonDeserializer<Instant> {

    @Override
    public JsonElement serialize(Instant instant, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(instant.toEpochMilli());
    }

    @Override
    public Instant deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (json == null || json.isJsonNull())
            return null;

        return Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong());
    }
}
