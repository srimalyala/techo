package com.emoolya.model;

import java.util.List;

/**
 * Created by srikanth on 2016/12/18.
 */
public class Response {

    private int code;

    private int noOfResults;

    private List<Product> products;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getNoOfResults() {
        return noOfResults;
    }

    public void setNoOfResults(int noOfResults) {
        this.noOfResults = noOfResults;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
