package com.emoolya.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.emoolya.config.FlipkartConfig;
import com.emoolya.model.Product;
import com.emoolya.model.amazon.HTTPHeaders;

import org.apache.camel.Header;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;

/**
 * Created by srikanth on 2016/12/17.
 */
@Component
public class FlipkartService {

    private static final Logger log = LoggerFactory.getLogger(FlipkartService.class);

    private FlipkartConfig config = new FlipkartConfig();

    private RestTemplate restTemplate;

    private Gson gson = new Gson();

    @PostConstruct
    public void init() {

        restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            protected boolean hasError(HttpStatus statusCode) {
                return false;
            }
        });
    }

    /**
     * Requests Flipkart for the Product information by using Product bar code.
     *
     * @param id - barcode that can be UPC(12 digit)  or EAN(13 digit)
     */
    public String getProductInfo(final @Header("id") String id) {

        final String url = config.getUrl();
        final UriComponentsBuilder uri = UriComponentsBuilder.
                fromHttpUrl(url).queryParam("id", id);

        log.info("send request to flipkart - ", uri.toUriString());
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Fk-Affiliate-Id", config.getAffiliateId());
        headers.set("Fk-Affiliate-Token", config.getToken());

        final HttpEntity<String> entity = new HttpEntity<String>(headers);
        final ResponseEntity<String> exchange = restTemplate.exchange(uri.toUriString(),
                HttpMethod.GET, entity, String.class);

        if(exchange.getStatusCode() != HttpStatus.OK) {
            return "";
        }

        log.info("Response from flipkart service..", exchange.getBody());

        final Product product = buildProduct(exchange.getBody());
        return gson.toJson(product);

    }

    /**
     * builds emoolya product info
     * @param jsonLine
     * @return
     */
    private Product buildProduct(final String jsonLine) {

        if (StringUtils.isEmpty(jsonLine)) {
            return null;
        }

        final Product product = new Product();

        final JsonElement jelement = new JsonParser().parse(jsonLine);

        JsonObject jobject = jelement.getAsJsonObject();
        jobject = jobject.getAsJsonObject("productBaseInfoV1");

        final String title = jobject.get("title").getAsString();
        product.setTitle(title);

        final JsonObject imageUrlObject = jobject.get("imageUrls").getAsJsonObject();

        if(imageUrlObject.get("200x200") != null) {
            final String imageUrl = imageUrlObject.get("200x200").getAsString();
            product.setImageUrl(imageUrl);
        }

        final String pageUrl = jobject.get("productUrl").getAsString();
        product.setPageUrl(pageUrl);

        final String currency = jobject.get("flipkartSellingPrice").
                getAsJsonObject().get("currency").getAsString();

        final String amount = jobject.get("flipkartSellingPrice").
                getAsJsonObject().get("amount").getAsString();
        product.setFormattedPrice(currency + " " + amount);
        product.setSource("flipkart");

        return product;

    }
}
