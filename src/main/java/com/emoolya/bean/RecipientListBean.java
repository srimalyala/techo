package com.emoolya.bean;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Created by srikanth on 2016/12/19.
 */
@Component
public class RecipientListBean implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {

        final String countryCode = (String) exchange.getIn().getHeader("countryCode");
        String recipients;

        if (StringUtils.equalsIgnoreCase(countryCode, "IN")) {
            recipients = "bean:amazonService?method=getProductInfo, bean:flipkartService?method=getProductInfo";
        } else {
            recipients = "bean:amazonService?method=getProductInfo";
        }

        exchange.getIn().setHeader("recipients", recipients);

    }
}
