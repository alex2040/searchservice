package filesearch.engine;

public class SearchResult {
    private boolean ok;
    private String result;
    private String error;

    private SearchResult() {
    }

    public boolean isOk() {
        return ok;
    }

    public String getResult() {
        return result;
    }

    public String getError() {
        return error;
    }

    static Builder newBuilder() {
        return new SearchResult().new Builder();
    }

    class Builder {

        private Builder() {
        }

        Builder setOk(boolean ok) {
            SearchResult.this.ok = ok;
            return this;
        }

        Builder setResult(String result) {
            SearchResult.this.result = result;
            return this;
        }

        Builder setError(String error) {
            SearchResult.this.error = error;
            return this;
        }

        SearchResult build() {
            return SearchResult.this;
        }
    }
}
