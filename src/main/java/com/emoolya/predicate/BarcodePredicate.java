package com.emoolya.predicate;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.commons.validator.routines.checkdigit.EAN13CheckDigit;
import org.springframework.stereotype.Component;

/**
 * Created by srikanth on 2016/12/24.
 */
@Component
public class BarcodePredicate implements Predicate {

    final EAN13CheckDigit checkDigit = new EAN13CheckDigit();

    @Override
    public boolean matches(final Exchange exchange) {
        final String code = (String) exchange.getIn().getHeader("code");
        return checkDigit.isValid(code);
    }
}

