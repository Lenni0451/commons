package net.lenni0451.commons.strings.search;

public enum ResultFilter {

    /**
     * Return all results sorted by the score.<br>
     * The best result will be the first one.
     */
    ALL,
    /**
     * Filter the search results by the gap between the lowest and the highest score.<br>
     * Calculation: {@code filter(result -> result.score >= minScore + ceil((maxScore - minScore) / 2F)}
     */
    GAP,
    /**
     * Only return the highest scored results.
     */
    HIGHEST

}
