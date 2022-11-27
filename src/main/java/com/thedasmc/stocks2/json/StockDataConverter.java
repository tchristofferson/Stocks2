package com.thedasmc.stocks2.json;

import com.google.gson.*;
import com.thedasmc.stocks2.requests.models.StockData;

import java.lang.reflect.Type;

import static com.thedasmc.stocks2.util.JsonTools.*;

public class StockDataConverter implements JsonSerializer<StockData>, JsonDeserializer<StockData> {

    @Override
    public JsonElement serialize(StockData src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null)
            return JsonNull.INSTANCE;

        JsonObject obj = new JsonObject();
        obj.addProperty("symbol", src.getSymbol());
        obj.addProperty("companyName", src.getCompanyName());
        obj.addProperty("calculationPrice", src.getCalculationPrice());
        obj.addProperty("open", src.getOpen());
        obj.addProperty("close", src.getClose());
        obj.addProperty("latestPrice", src.getLatestPrice());
        obj.addProperty("changePercent", src.getChangePercent());
        obj.addProperty("week52High", src.getWeek52High());
        obj.addProperty("week52Low", src.getWeek52Low());
        obj.addProperty("isUSMarketOpen", src.isUSMarketOpen());

        return obj;
    }

    @Override
    public StockData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (isNull(json))
            return null;

        JsonObject obj = json.getAsJsonObject();

        return StockData.newBuilder()
            .withSymbol(asString(obj.get("symbol")))
            .withCompanyName(asString(obj.get("companyName")))
            .withCalculationPrice(asString(obj.get("calculationPrice")))
            .withOpen(asBigDecimal(obj.get("open")))
            .withClose(asBigDecimal(obj.get("close")))
            .withLatestPrice(asBigDecimal(obj.get("latestPrice")))
            .withChangePercent(asBigDecimal(obj.get("changePercent")))
            .withWeek52High(asBigDecimal(obj.get("week52High")))
            .withWeek52Low(asBigDecimal(obj.get("week52Low")))
            .withUSMarketOpen(asBoolean(obj.get("isUSMarketOpen")))
            .build();
    }
}
