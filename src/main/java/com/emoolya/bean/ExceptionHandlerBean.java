package com.emoolya.bean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.emoolya.model.Response;

import org.apache.camel.Exchange;
import org.apache.camel.Headers;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

/**
 * Created by srikanth on 2016/12/24.
 */
@Component
public class ExceptionHandlerBean {
    private Logger log = LoggerFactory.getLogger(ExceptionHandlerBean.class);

    private final Gson gson = new GsonBuilder().create();

    public Response handlePredicateValidationException(Exception ex, Exchange exchange,
                                                       @Headers Map<String, Object> headers) {

        log.error("Exception occurred - Bad request :",
                ExceptionUtils.getMessage(ex));

        final Response response = new Response();
        response.setCode(400);
        response.setResult("Invalid barcode - " + headers.get("code"));

        return response;
    }
}

