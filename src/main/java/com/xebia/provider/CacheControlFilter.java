package com.xebia.provider;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

public class CacheControlFilter implements ResourceFilter, ContainerResponseFilter {
    private final CacheControl cacheControl;

    public CacheControlFilter(CacheControl cacheControl) {
        this.cacheControl = cacheControl;
    }

    @Override
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
        Response responseWithHeaders = addHeaders(response, cacheControl);
        response.setResponse(responseWithHeaders);
        return response;
    }

    private Response addHeaders(ContainerResponse response, CacheControl cacheControl) {
        ResponseBuilder fromResponse = Response.fromResponse(response.getResponse());
        return fromResponse.cacheControl(cacheControl).build();
    }

    @Override
    public ContainerRequestFilter getRequestFilter() {
        return null;
    }

    @Override
    public ContainerResponseFilter getResponseFilter() {
        return this;
    }
}
