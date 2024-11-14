package net.lenni0451.commons.strings.search.tokens;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class BinaryOrToken implements ISearchToken {

    private final ISearchToken token1;
    private final ISearchToken token2;
    private boolean required;
    private boolean matched;

    public BinaryOrToken(final ISearchToken token1, final ISearchToken token2) {
        this.token1 = token1;
        this.token2 = token2;
    }

    @Override
    public String getKeyword() {
        return this.token1.getKeyword() + " OR " + this.token2.getKeyword();
    }

    @Override
    public boolean required() {
        return this.required;
    }

    @Override
    public int matches(String token) {
        if (this.matched) return 0;
        return this.match(this.token1.matches(token), this.token2.matches(token));
    }

    @Override
    public int contains(String string) {
        if (this.matched) return 0;
        return this.match(this.token1.contains(string), this.token2.contains(string));
    }

    private int match(final int matches1, final int matches2) {
        if (matches1 > matches2) this.required = this.token1.required();
        else if (matches2 > matches1) this.required = this.token2.required();
        else this.required = this.token1.required() && this.token2.required();

        this.matched = matches1 > 0 || matches2 > 0;
        return Math.max(matches1, matches2);
    }

}
