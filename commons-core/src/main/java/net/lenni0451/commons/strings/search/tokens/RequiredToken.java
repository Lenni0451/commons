package net.lenni0451.commons.strings.search.tokens;

import org.jetbrains.annotations.ApiStatus;

import java.util.Locale;

@ApiStatus.Internal
public class RequiredToken implements ISearchToken {

    private final String keyword;

    public RequiredToken(final String keyword) {
        this.keyword = keyword.toLowerCase(Locale.ROOT);
    }

    @Override
    public String getKeyword() {
        return this.keyword;
    }

    @Override
    public boolean required() {
        return true;
    }

    @Override
    public int contains(String string) {
        int index = string.indexOf(this.keyword);
        if (index == -1) return 0;

        char before = index > 0 ? string.charAt(index - 1) : ' ';
        char after = index + this.keyword.length() < string.length() ? string.charAt(index + this.keyword.length()) : ' ';
        if (before == ' ' && after == ' ') return 2;
        else return 1;
    }

}
