package com.gtasterix.E_Commerce.Util;


import lombok.Data;

@Data
public class Response {

    public String message;
    public Object object;
    public Boolean hasError;

    public Response(String message, Object object, Boolean hasError) {
        this.message = message;
        this.object = object;
        this.hasError = hasError;
    }
}
