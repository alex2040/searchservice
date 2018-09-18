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
            response.setCode("02");
            response.setError(e.getMessage());
            return response;
        }
        String errors = searchResults.stream().filter(searchResult -> !searchResult.isOk()).map(SearchResult::getError).collect(Collectors.joining(","));
        if (!errors.isEmpty()) {
            response.setCode("02");
            response.setError(errors);
            return response;
        }
        List<String> fileNames = searchResults.stream().map(SearchResult::getResult).collect(Collectors.toList());
        if(!fileNames.isEmpty()) {
            response.setCode("00");
            response.getFileNames().addAll(fileNames);
        } else {
            response.setCode("01");
        }
        return response;
    }
}
