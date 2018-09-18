package web.service;

import search_service.FindNumberResponse;

public interface PersistenceService {

    void storeResult(Integer number, FindNumberResponse response);

}
