package filesearch.engine;

import filesearch.source.Source;
import filesearch.source.SourceException;
import filesearch.source.SourceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public abstract class SearchEngine<T> {

    public List<SearchResult> search(int number) throws SourceException, SearchException {
        SourceManager<T> sourceManager = createSourceManager();
        List<Future<SearchResult>> futureResultList = executeSearchTasks(number, sourceManager);
        return waitExecutionFinish(futureResultList);
    }

    private List<Future<SearchResult>> executeSearchTasks(int number, SourceManager<T> sourceManager) {
        List<Future<SearchResult>> futureResultList = new ArrayList<>();
        ExecutorService pool = Executors.newFixedThreadPool(5);
        while (sourceManager.hasNext()) {
            Source<T> source = sourceManager.next();
            Future<SearchResult> future = pool.submit(new SearchWorker(source, number));
            source.close();
            futureResultList.add(future);
        }
        return futureResultList;
    }

    private List<SearchResult> waitExecutionFinish(List<Future<SearchResult>> futureResultList) throws SearchException {
        List<SearchResult> resultList = new ArrayList<>();
        for (Future<SearchResult> stringFuture : futureResultList) {
            SearchResult searchResult;
            try {
                searchResult = stringFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new SearchException(e.getMessage(), e);
            }
            if (!searchResult.isOk() || searchResult.getResult() != null) {
                resultList.add(searchResult);
            }
        }
        return resultList;
    }

    protected abstract SourceManager<T> createSourceManager() throws SourceException;

    protected abstract Searcher createSearcher(Source<T> source);

    class SearchWorker implements Callable<SearchResult> {

        private final Source<T> source;
        private final int number;

        SearchWorker(Source<T> source, int number) {
            this.source = source;
            this.number = number;
            System.out.println("worker for " + source.getName() + " created");
        }

        @Override
        public SearchResult call() {
            System.out.println("worker for " + source.getName() + " started");
            Searcher searcher = createSearcher(source);
            boolean found;
            try {
                found = searcher.search(number);
            } catch (SearchException e) {
                return SearchResult.newBuilder()
                        .setOk(false)
                        .setError(e.getMessage())
                        .build();
            }
            System.out.println("worker for " + source.getName() + " stopped");
            SearchResult.Builder resultBuilder = SearchResult.newBuilder().setOk(true);
            if (found) {
                return resultBuilder
                        .setResult(source.getName())
                        .build();
            }
            return resultBuilder.build();
        }
    }
}
