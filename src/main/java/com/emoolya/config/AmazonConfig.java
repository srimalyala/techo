package com.emoolya.config;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by srikanth on 2016/12/10.
 */
public class AmazonConfig {

    private String domain;

    private String AssociateTag;

    private String uri = "/onca/xml";

    private String awsAccessKeyId = "AKIAJ7NHWB4XOVV6LFEA";

    private String secretKey = "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";

    public AmazonConfig(String countryCode) {
        if (StringUtils.equalsIgnoreCase(countryCode, "IN")) {
            this.domain = "webservices.amazon.in";
            AssociateTag = "emoolya04-21";
        } else {
            this.domain = "webservices.amazon.com";
            AssociateTag = "emulya-20";
        }
    }

    public AmazonConfig() {
        this.domain = "amazon.webservices.com";
        AssociateTag = "emulya-20";
    }

    public String getAwsAccessKeyId() {
        return awsAccessKeyId;
    }

    public void setAwsAccessKeyId(String awsAccessKeyId) {
        this.awsAccessKeyId = awsAccessKeyId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getAssociateTag() {
        return AssociateTag;
    }

    public void setAssociateTag(String associateTag) {
        AssociateTag = associateTag;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
