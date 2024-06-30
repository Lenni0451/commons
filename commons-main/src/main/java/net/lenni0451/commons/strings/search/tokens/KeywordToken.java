package net.lenni0451.commons.strings.search.tokens;

import org.jetbrains.annotations.ApiStatus;

import java.util.Locale;

@ApiStatus.Internal
public class KeywordToken implements ISearchToken {

    private final String keyword;

    public KeywordToken(final String keyword) {
        this.keyword = keyword.toLowerCase(Locale.ROOT);
    }

    @Override
    public String getKeyword() {
        return this.keyword;
    }

    @Override
    public boolean required() {
        return false;
    }

    @Override
    public int matches(String token) {
        if (this.keyword.equalsIgnoreCase(token)) return 2;
        else if (token.contains(this.keyword)) return 1;
        else return 0;
    }

}
