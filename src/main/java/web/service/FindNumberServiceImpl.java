package web.service;

import filesearch.engine.SearchException;
import filesearch.engine.SearchResult;
import filesearch.engine.file.FileSearchEngine;
import filesearch.source.SourceException;
import org.springframework.stereotype.Service;
import search_service.FindNumberResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FindNumberServiceImpl implements FindNumberService {

    private static final String RESPONSE_CODE_OK = "00.Result.OK";

    private static final String RESPONSE_CODE_NOT_FOUND = "01.Result.NotFound";

    private static final String RESPONSE_CODE_ERROR = "02.Result.Error";

    private final FileSearchEngine fileSearchEngine;

    public FindNumberServiceImpl(FileSearchEngine fileSearchEngine) {
        this.fileSearchEngine = fileSearchEngine;
    }

    @Override
    public FindNumberResponse findNumber(int number) {
        FindNumberResponse response = new FindNumberResponse();
        List<SearchResult> searchResults;
        try {
            searchResults = fileSearchEngine.search(number);
        } catch (SourceException | SearchException e) {
            response.setCode(RESPONSE_CODE_ERROR);
            response.setError(e.getMessage());
            return response;
        }
        String errors = searchResults.stream().filter(searchResult -> !searchResult.isOk()).map(SearchResult::getError).collect(Collectors.joining(","));
        if (!errors.isEmpty()) {
            response.setCode(RESPONSE_CODE_ERROR);
            response.setError(errors);
            return response;
        }
        List<String> fileNames = searchResults.stream().map(SearchResult::getResult).collect(Collectors.toList());
        if(!fileNames.isEmpty()) {
            response.setCode(RESPONSE_CODE_OK);
            response.getFileNames().addAll(fileNames);
        } else {
            response.setCode(RESPONSE_CODE_NOT_FOUND);
        }
        return response;
    }
}
