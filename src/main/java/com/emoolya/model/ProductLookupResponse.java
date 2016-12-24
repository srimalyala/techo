package com.emoolya.model;

import java.util.List;

/**
 * Created by srikanth on 2016/12/21.
 */
public class ProductLookupResponse {

    private String barcode;

    private int noOfResults;

    private List<Product> products;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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
