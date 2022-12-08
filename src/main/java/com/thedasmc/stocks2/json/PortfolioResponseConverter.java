package com.thedasmc.stocks2.json;

import com.google.gson.*;
import com.thedasmc.stocks2.requests.response.PortfolioResponse;
import com.thedasmc.stocks2.requests.response.StockResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.thedasmc.stocks2.common.Tools.isNull;

public class PortfolioResponseConverter implements JsonSerializer<PortfolioResponse>, JsonDeserializer<PortfolioResponse> {

    @Override
    public JsonElement serialize(PortfolioResponse src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null)
            return JsonNull.INSTANCE;

        JsonObject obj = new JsonObject();
        obj.addProperty("page", src.getPage());
        obj.addProperty("pages", src.getPages());

        JsonArray stocks = new JsonArray();
        src.getStocks().forEach(stock -> stocks.add(context.serialize(stock)));
        obj.add("stocks", stocks);

        return obj;
    }


    @Override
    public PortfolioResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (isNull(json))
            return null;

        JsonObject obj = json.getAsJsonObject();
        JsonArray stocks = obj.getAsJsonArray("stocks");

        List<StockResponse> stockList = new ArrayList<>(stocks.size());
        stocks.forEach(element -> {
            StockResponse stockResponse = context.deserialize(element, StockResponse.class);
            stockList.add(stockResponse);
        });

        return new PortfolioResponse(stockList, obj.get("page").getAsInt(), obj.get("pages").getAsInt());
    }
}
