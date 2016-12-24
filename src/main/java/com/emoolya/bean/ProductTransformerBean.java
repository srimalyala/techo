package com.emoolya.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.emoolya.model.Product;
import com.emoolya.model.ProductLookupResponse;
import com.emoolya.model.Response;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import static org.apache.camel.model.rest.RestBindingMode.json;

/**
 * Receives {@code Product} as a json string and converts to array of products.
 * Created by srikanth on 2016/12/18.
 */
@Component("transformerBean")
public class ProductTransformerBean {

    final Gson gson = new Gson();

    public Response transform(final @Body String json,
                              final @Header("code") String code) {

        final String body = "[" + json+ "]";
        final Type collectionType = new TypeToken<Collection<Product>>() {
        }.getType();
        final List<Product> products = gson.fromJson(body, collectionType);

        final Response response = new Response();

        final ProductLookupResponse productLookupResponse = new ProductLookupResponse();
        productLookupResponse.setBarcode(code);
        productLookupResponse.setNoOfResults(products.size());
        productLookupResponse.setProducts(products);

        response.setData(productLookupResponse);

        return response;

    }


}
