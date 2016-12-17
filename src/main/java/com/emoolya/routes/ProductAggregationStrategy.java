package com.emoolya.routes;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import com.emoolya.model.Product;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by srikanth on 2016/12/17.
 */
class ProductAggregationStrategy implements AggregationStrategy {

    Gson gson = new Gson();
    @Override
    public org.apache.camel.Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
         if(oldExchange == null)  {
             return newExchange;
         }

         if(newExchange == null) {
             return oldExchange;
         }

        final Object newExchangebody =
                newExchange.getIn().getBody();

        if(newExchangebody == null) {
            return oldExchange;
        }

        final String oldExhangeData = gson.toJson(oldExchange.getIn().getBody());
        final String newExhangeData = gson.toJson(newExchangebody);

        final String mergeExchangeData = "[" + oldExhangeData +"," + newExhangeData + "]";

        final Type listType = new TypeToken<ArrayList<Product>>(){}.getType();

        List<Product> products = new Gson().fromJson(mergeExchangeData, listType);
        oldExchange.getIn().setBody(products);

        return oldExchange;
    }
}