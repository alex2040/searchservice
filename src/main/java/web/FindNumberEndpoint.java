package web;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import search_service.FindNumberRequest;
import search_service.FindNumberResponse;
import web.service.FindNumberService;

@Endpoint
public class FindNumberEndpoint {

    private FindNumberService findNumberService;

    public FindNumberEndpoint(FindNumberService findNumberService) {
        this.findNumberService = findNumberService;
    }

    @PayloadRoot(namespace = "search-service", localPart = "findNumberRequest")
    @ResponsePayload
    public FindNumberResponse findNumber(@RequestPayload FindNumberRequest request) {
        return findNumberService.findNumber(request.getNumber());
    }

}