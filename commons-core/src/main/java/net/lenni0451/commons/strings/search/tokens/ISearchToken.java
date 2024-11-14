package net.lenni0451.commons.strings.search.tokens;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface ISearchToken {

    String getKeyword();

    boolean required();

    default int matches(final String token) {
        return 0;
    }

    default int contains(final String string) {
        return 0;
    }

}
