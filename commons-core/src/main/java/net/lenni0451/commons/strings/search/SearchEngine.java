package net.lenni0451.commons.strings.search;

import net.lenni0451.commons.math.MathUtils;
import net.lenni0451.commons.strings.search.tokens.ISearchToken;
import net.lenni0451.commons.strings.search.tokens.QueryTokenizer;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class SearchEngine<T> {

    private final Supplier<? extends Iterable<T>> supplier;
    private final Function<T, String> toStringFunction;

    public SearchEngine(final Supplier<? extends Iterable<T>> supplier, final Function<T, String> toStringFunction) {
        this.supplier = supplier;
        this.toStringFunction = toStringFunction;
    }

    /**
     * Searches for objects in the supplier that match the given query.<br>
     * The query can contain:
     * <ul>
     *     <li>Keywords: Words that are separated by spaces</li>
     *     <li>Required Words: Words that are surrounded by single or double quotes</li>
     *     <li>OR: The keyword {@code OR} to combine two keywords with a binary or operator</li>
     * </ul>
     *
     * @param filter The filter that should be applied to the results
     * @param query  The query to search for
     * @return The search result containing the results and metadata
     */
    public SearchResult<T> search(final ResultFilter filter, final String query) {
        List<SearchScore<T>> results = new ArrayList<>();
        List<ISearchToken> tokens = QueryTokenizer.tokenize(query);
        if (tokens.isEmpty()) return new SearchResult<>(query, 0, filter, Collections.emptyList(), true);

        Iterator<T> it = this.supplier.get().iterator();
        boolean hasElements = false;
        while (it.hasNext()) {
            hasElements = true;
            T object = it.next();
            SearchScore<T> result = this.calculateScore(object, tokens);
            if (result.score > 0) results.add(result);
        }
        if (!hasElements) return new SearchResult<>(query, tokens.size(), filter, Collections.emptyList(), true);
        results.sort((o1, o2) -> Integer.compare(o2.score, o1.score));
        switch (filter) {
            case ALL:
                break;
            case GAP:
                this.filterScoreGap(results);
                break;
            case HIGHEST:
                if (!results.isEmpty()) {
                    int maxScore = results.get(0).score;
                    results.removeIf(result -> result.score < maxScore);
                }
                break;
        }
        List<T> sortedResults = new ArrayList<>();
        for (SearchScore<T> result : results) sortedResults.add(result.object);
        return new SearchResult<>(query, tokens.size(), filter, sortedResults, false);
    }

    private SearchScore<T> calculateScore(final T object, final List<ISearchToken> tokens) {
        String string = this.toStringFunction.apply(object).toLowerCase(Locale.ROOT);
        int score = 0;
        for (ISearchToken token : tokens) {
            int tokenScore = 0;
            tokenScore += token.matches(string);
            tokenScore += token.contains(string);
            if (token.required() && tokenScore == 0) return new SearchScore<>(object, 0);
            score += tokenScore;
        }
        return new SearchScore<>(object, score);
    }

    private void filterScoreGap(final List<SearchScore<T>> results) {
        if (results.size() <= 1) return;
        int maxScore = results.get(0).score;
        int minScore = results.get(results.size() - 1).score;
        int gap = maxScore - minScore;
        if (gap <= 0) return;
        int minGap = MathUtils.ceilInt(gap / 2F);
        for (int i = results.size() - 1; i >= 0; i--) {
            if (results.get(i).score < minScore + minGap) results.remove(i);
        }
    }


    private static class SearchScore<T> {
        private final T object;
        private final int score;

        private SearchScore(final T object, final int score) {
            this.object = object;
            this.score = score;
        }
    }

}
