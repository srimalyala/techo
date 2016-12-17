package com.emoolya.config;

/**
 * Created by srikanth on 2016/12/10.
 */
public class AmazonConfig {

    private String serviceUrl = "http://webservices.amazon.in/onca/xml";

    private String awsAccessKeyId = "AKIAJ7NHWB4XOVV6LFEA";

    private String secretKey = "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";

    private String AssociateTag = "emoolya04-21";

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
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

}
