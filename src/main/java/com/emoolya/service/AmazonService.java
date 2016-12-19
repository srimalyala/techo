package com.emoolya.service;

import com.google.gson.Gson;

import com.emoolya.config.AmazonConfig;
import com.emoolya.model.Product;
import com.emoolya.model.amazon.Item;
import com.emoolya.model.amazon.ItemAttributes;
import com.emoolya.model.amazon.ItemLookupResponse;
import com.emoolya.model.amazon.Items;

import org.apache.camel.Header;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

/**
 * Created by srikanth on 2016/12/10.
 */
@Component
public class AmazonService {

    private static final Logger log = LoggerFactory.getLogger(AmazonService.class);

    private final RestTemplate restTemplate = new RestTemplate();

    private final Base64 base64Encoder = new Base64();

    private JAXBContext jaxbContext;
    private Unmarshaller unmarshaller;

    private final Gson gson = new Gson();


    Mac sha256_HMAC = null;

    SecretKeySpec secret_key = null;


    @PostConstruct
    public void init() {

        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            protected boolean hasError(HttpStatus statusCode) {
                return false;
            }
        });
        try {
            AmazonConfig config = new AmazonConfig();
            final String awsSecretKey = config.getSecretKey();
            secret_key = new SecretKeySpec(awsSecretKey.getBytes("UTF-8"), "HmacSHA256");

            sha256_HMAC = Mac.getInstance("HmacSHA256");
            sha256_HMAC.init(secret_key);

            jaxbContext = JAXBContext.newInstance(ItemLookupResponse.class);
            unmarshaller = jaxbContext.createUnmarshaller();
        } catch (final Exception e) {
            log.error("error occurred ", e);
        }
    }

    /**
     * Requests Amazon for the Product information by using Product bar code.
     *
     * @param id - barcode that can be UPC(12 digit)  or EAN(13 digit)
     */
    public final String getProductInfo(final @Header("countryCode") String countryCode,
                                       final @Header("id") String id) {

        final AmazonConfig config = new AmazonConfig(countryCode);

        log.info("Sending request to amazon to get Product info of having barcode {}", id);

        final MultiValueMap<String, String> queryValueMap = new LinkedMultiValueMap();
        queryValueMap.set("AWSAccessKeyId", config.getAwsAccessKeyId());
        queryValueMap.set("AssociateTag", config.getAssociateTag());

        if (StringUtils.length(id) == 12) {
            queryValueMap.set("IdType", "UPC");
        } else {
            queryValueMap.set("IdType", "EAN");
        }

        queryValueMap.set("ItemId", id);
        queryValueMap.set("Operation", "ItemLookup");
        queryValueMap.set("ResponseGroup", "ItemAttributes,OfferSummary,Images");
        queryValueMap.set("SearchIndex", "All");
        queryValueMap.set("Service", "AWSECommerceService");
        queryValueMap.set("Timestamp", timestamp());

        final SortedMap<String, String> sortedMap = new TreeMap<>(queryValueMap.toSingleValueMap());
        final String canonicalize = canonicalize(sortedMap);
        final String signature = getSignature(config.getDomain(),
                config.getUri(), canonicalize);

        try {

            final String url = "http://" + config.getDomain() + config.getUri() + "?" + canonicalize
                    + "&Signature=" + signature;
            final URI uri = URI.create(url);
            final ResponseEntity<String> response = restTemplate.getForEntity(
                    uri,
                    String.class);

            log.info(response.getBody());

            final StringReader responseXML = new StringReader(response.getBody());
            final ItemLookupResponse itemLookupResponse = (ItemLookupResponse)
                    unmarshaller.unmarshal(responseXML);

            final List<Items> items = itemLookupResponse.getItems();

            if(CollectionUtils.isEmpty(items) ||
                    CollectionUtils.isEmpty(items.get(0).getItem())) {
                return "";
            }

            final Item item = items.get(0).getItem().get(0);

            final Product product = buildItem(item);

            return gson.toJson(product);


        } catch (final Exception exception) {
            log.error("Error occurred accessing amazon service ", exception);
        }
        return null;
    }


    /**
     * Builds {@code product} i.e used by FE's
     * @param amazonItem
     * @return
     */
    private Product buildItem(final Item amazonItem) {

        final Product product = new Product();
        final ItemAttributes itemAttributes = amazonItem.getItemAttributes();

        product.setSource("amazon");
        product.setFormattedPrice(itemAttributes.getListPrice().getFormattedPrice());

        final String title = itemAttributes.getTitle().split("0")[0];
        product.setTitle(title);
        product.setPageUrl(amazonItem.getDetailPageURL());
        product.setImageUrl(amazonItem.getSmallImage().getURL());

        return product;
    }

    /**
     * A signature is created by using the request type, domain, the URI,
     * and a sorted string of every parameter in the request (except the Signature parameter itself)
     * with the following format <parameter>=<value>&. After it's properly formatted, create a
     * base64-encoded HMAC-SHA256 signature with your AWS secret key. For more information, see
     * Example REST
     */

    private final String getSignature(final String domain, final String uri,
                                      final String canonicalize) {

        final String newline = System.getProperty("line.separator");
        String signature = HttpMethod.GET + newline +
                domain + newline + uri + newline +
                canonicalize;
        try {

            final byte[] signatureBytes = sha256_HMAC.doFinal(signature.getBytes("UTF-8"));
            signature = new String(Base64.encodeBase64(signatureBytes));
            return percentEncodeRfc3986(signature);
        } catch (final Exception exception) {
            log.error("Error creating signature for input {}", signature);
        }

        return null;
    }

    /**
     * Takes {@code sortedMap} and returns String key=value seperated by ampersands (&)
     */
    private String canonicalize(final SortedMap<String, String> sortedParamMap) {
        if (sortedParamMap.isEmpty()) {
            return "";
        }
        final StringBuffer buffer = new StringBuffer();
        final Iterator<Map.Entry<String, String>> iter =
                sortedParamMap.entrySet().iterator();
        while (iter.hasNext()) {
            final Map.Entry<String, String> kvpair = iter.next();
            buffer.append(percentEncodeRfc3986(kvpair.getKey()));
            buffer.append("=");
            buffer.append(percentEncodeRfc3986(kvpair.getValue()));
            if (iter.hasNext()) {
                buffer.append("&");
            }
        }
        return buffer.toString();
    }

    /**
     * URL encodes the string
     */
    private String percentEncodeRfc3986(final String queryString) {
        String out;
        try {
            out = URLEncoder.encode(queryString, "UTF-8")
                    .replace("+", "%20")
                    .replace("*", "%2A")
                    .replace("%7E", "~");
        } catch (final UnsupportedEncodingException e) {
            out = queryString;
        }
        return out;
    }

    /**
     * returns timestamo in GMT format
     */
    private String timestamp() {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return LocalDateTime.now().format(formatter);
    }
}
