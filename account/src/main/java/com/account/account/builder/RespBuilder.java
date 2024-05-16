package com.account.account.builder;

import org.springframework.http.HttpStatus;

import com.account.account.response.Resp;


public class RespBuilder {
    private Object response;
    private HttpStatus httpStatus;

    public static RespBuilder from(Object response, HttpStatus httpStatus){
        return new RespBuilder().withResponse(response).withHttpStatus(httpStatus);
    }
    
    private RespBuilder withResponse(Object response) {
       this.response = response;
       return this;
    }

    private RespBuilder withHttpStatus(HttpStatus httpStatus){
        this.httpStatus = httpStatus;
        return this;
    }
    
    public Resp build(){
        return new Resp(response, httpStatus);
    }
}
