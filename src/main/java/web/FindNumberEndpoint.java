package web;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import search_service.FindNumberRequest;
import search_service.FindNumberResponse;
import web.service.FindNumberService;
import web.service.PersistenceService;

@Endpoint
public class FindNumberEndpoint {

    private FindNumberService findNumberService;

    private PersistenceService persistenceService;

    public FindNumberEndpoint(FindNumberService findNumberService, PersistenceService persistenceService) {
        this.findNumberService = findNumberService;
        this.persistenceService = persistenceService;
    }

    @PayloadRoot(namespace = "search-service", localPart = "findNumberRequest")
    @ResponsePayload
    public FindNumberResponse findNumber(@RequestPayload FindNumberRequest request) {
        FindNumberResponse response = findNumberService.findNumber(request.getNumber());
        persistenceService.storeResult(request.getNumber(), response);
        return response;
    }

}