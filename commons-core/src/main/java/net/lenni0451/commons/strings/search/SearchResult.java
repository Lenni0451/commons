package net.lenni0451.commons.strings.search;

import java.util.List;

/**
 * Represents the result of a search by the {@link SearchEngine}.
 *
 * @param <T> The type of the search results
 */
public class SearchResult<T> {

    private final String query;
    private final int queryTokens;
    private final ResultFilter resultFilter;
    private final List<T> results;
    private final boolean emptySearch;

    public SearchResult(final String query, final int queryTokens, final ResultFilter resultFilter, final List<T> results, final boolean emptySearch) {
        this.query = query;
        this.queryTokens = queryTokens;
        this.resultFilter = resultFilter;
        this.results = results;
        this.emptySearch = emptySearch;
    }

    /**
     * @return The query that was used to search
     */
    public String getQuery() {
        return this.query;
    }

    /**
     * @return The amount of tokens that were searched for
     */
    public int getQueryTokens() {
        return this.queryTokens;
    }

    /**
     * @return The filter used to filter the results
     */
    public ResultFilter getResultFilter() {
        return this.resultFilter;
    }

    /**
     * @return The sorted results of the search
     */
    public List<T> getResults() {
        return this.results;
    }

    /**
     * @return If the query or input was empty
     */
    public boolean isEmptySearch() {
        return this.emptySearch;
    }

}
