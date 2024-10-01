package com.example.teamcity.api.requests;

import com.example.teamcity.api.enums.Endpoint;
import io.restassured.specification.RequestSpecification;
/*
* Request is a class that specifies changeable parameters of requests:
* specification, endpoint (relative URL, model)
 */
public class Request {

    private final RequestSpecification spec;
    private final Endpoint endpoint;

    public Request(RequestSpecification spec, Endpoint endpoint) {
        this.spec = spec;
        this.endpoint = endpoint;
    }
}
