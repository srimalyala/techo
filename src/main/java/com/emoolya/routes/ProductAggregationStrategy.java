package com.emoolya.routes;

import com.google.gson.Gson;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Concatenates 2 json Strings of {@code ExchNGE}
 * Created by srikanth on 2016/12/17.
 */
@Component("aggregationStrategy")
public class ProductAggregationStrategy implements AggregationStrategy {

    final Gson gson = new Gson();

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {

        if (oldExchange == null) {
            return newExchange;
        }

        final String oldExchangeBody = (String) oldExchange.getIn().getBody();

        if (StringUtils.isEmpty(oldExchangeBody)) {
            return newExchange;
        }

        if (newExchange == null) {
            return oldExchange;
        }

        final String newExchangeBody = (String) newExchange.getIn().getBody();

        if (StringUtils.isEmpty(newExchangeBody)) {
            return oldExchange;
        }

        oldExchange.getIn().setBody(oldExchangeBody + "," + newExchangeBody);

        return oldExchange;
    }
}