package com.emoolya.routes;

import com.emoolya.bean.ExceptionHandlerBean;
import com.emoolya.bean.ProductTransformerBean;
import com.emoolya.bean.RecipientListBean;
import com.emoolya.predicate.BarcodePredicate;
import com.emoolya.service.AmazonService;
import com.emoolya.service.FlipkartService;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.camel.processor.validation.PredicateValidationException;
import org.apache.camel.spi.DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * This route filters recipients by country code and  does parallel processing and aggrgates data.
 *
 * Created by srikanth on 2016/09/27.
 */
@Component
public class ServiceRoute extends RouteBuilder {

    @Autowired
    private AmazonService amazonService;

    @Autowired
    private FlipkartService flipkartService;

    @Autowired
    private ProductTransformerBean transformerBean;

    @Autowired
    private AggregationStrategy aggregationStrategy;

    @Autowired
    private BarcodePredicate barcodePredicate;

    @Autowired
    private ExceptionHandlerBean exceptionBean;

    @Override
    public void configure() throws Exception {

        onException(PredicateValidationException.class)
                .to("bean:exceptionHandlerBean?method=handlePredicateValidationException")
                .handled(true).stop();

        restConfiguration().component("servlet").bindingMode(RestBindingMode.json)
        .dataFormatProperty("prettyPrint", "true");

        rest("/").get("barcode/{code}/countrycode/{countryCode}").to("direct:processRequest").
                produces("application/json");

        from("direct:processRequest").validate(barcodePredicate).process(new RecipientListBean()).recipientList(header("recipients"))
                .aggregationStrategy(aggregationStrategy)
                .parallelProcessing()
                .to("bean:transformerBean");


    }
}
