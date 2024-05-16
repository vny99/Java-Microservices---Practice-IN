package com.order.purchase.response;

import org.springframework.http.HttpStatus;

public class Resp {
    private Object response;
    private HttpStatus httpStatus;

    public Resp(Object response, HttpStatus httpStatus) {
        this.response = response;
        this.httpStatus = httpStatus;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public String toString() {
        return "Resp [response=" + response + ", httpStatus=" + httpStatus + "]";
    }

}
