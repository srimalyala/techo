package com.emoolya.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.emoolya.model.Product;
import com.emoolya.model.Response;

import org.apache.camel.Body;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

/**
 * Receives {@code Product} as a json string and converts to array of products.
 * Created by srikanth on 2016/12/18.
 */
@Component("transformerBean")
public class ProductTransformerBean {

    final Gson gson = new Gson();

    public Response transform(final @Body String json) {

        final String body = "[" + json+ "]";
        final Type collectionType = new TypeToken<Collection<Product>>() {
        }.getType();
        final List<Product> products = gson.fromJson(body, collectionType);

        final Response response = new Response();
        response.setCode(HttpStatus.OK.value());
        response.setNoOfResults(products.size());
        response.setProducts(products);

        return response;

    }


}
