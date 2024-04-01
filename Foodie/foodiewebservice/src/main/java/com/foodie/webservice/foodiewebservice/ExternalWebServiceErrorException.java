package com.foodie.webservice.foodiewebservice;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ExternalWebServiceErrorException extends ResponseStatusException {
    public ExternalWebServiceErrorException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
