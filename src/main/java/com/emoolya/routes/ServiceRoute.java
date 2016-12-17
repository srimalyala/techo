package com.emoolya.routes;

import com.emoolya.service.AmazonService;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by srikanth on 2016/09/27.
 */
@Component
public class ServiceRoute extends RouteBuilder {

    @Autowired
    private AmazonService amazonService;

    @Override
    public void configure() throws Exception {
        restConfiguration().component("servlet").bindingMode(RestBindingMode.json)
        .dataFormatProperty("prettyPrint", "true");

        rest("/").get("barcode/{id}").
                to("bean:amazonService?method=getProductInfo").produces("application/json");
    }

}
