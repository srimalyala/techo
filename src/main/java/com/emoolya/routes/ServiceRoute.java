package com.emoolya.routes;

import com.emoolya.service.AmazonService;
import com.emoolya.service.FlipkartService;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by srikanth on 2016/09/27.
 */
@Component
public class ServiceRoute extends RouteBuilder {

    @Autowired
    private AmazonService amazonService;

    @Autowired
    private FlipkartService flipkartService;

    final AggregationStrategy myAggregationStrategy = new ProductAggregationStrategy();

    @Override
    public void configure() throws Exception {

        restConfiguration().component("servlet").bindingMode(RestBindingMode.json)
        .dataFormatProperty("prettyPrint", "true");

        rest("/").get("barcode/{id}").to("direct:processService").produces("application/json");

        from("direct:processService").multicast().parallelProcessing().
                to("bean:amazonService?method=getProductInfo",
                        "bean:flipkartService?method=getProductInfo").
                aggregationStrategy(myAggregationStrategy);

    }
}
