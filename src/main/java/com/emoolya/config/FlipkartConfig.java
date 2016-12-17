package com.emoolya.config;

/**
 * Created by srikanth on 2016/12/17.
 */
public class FlipkartConfig {

    private String token = "4f9ba7778bcb472db8a47e0d52624c1a";

    private String affiliateId = "srireddym";

    private String url = "https://affiliate-api.flipkart.net/affiliate/1.0/product.json";

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAffiliateId() {
        return affiliateId;
    }

    public void setAffiliateId(String affiliateId) {
        this.affiliateId = affiliateId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
