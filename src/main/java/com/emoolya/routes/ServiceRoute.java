package com.emoolya.routes;

import com.emoolya.service.AmazonService;

import org.apache.camel.builder.RouteBuilder;
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
       rest("/").get("barcode/{id}").to("bean:amazonService?method=getProductInfo");
    }

}
