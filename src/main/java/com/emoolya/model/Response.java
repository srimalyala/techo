package com.emoolya.model;

import java.util.List;

/**
 * THis is the final response object returned to the calling front end and mobile app services.
 * Created by srikanth on 2016/12/18.
 */
public class Response {

    private int code = 200;

    private String result = "Ok";

    private Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
