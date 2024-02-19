package net.lenni0451.commons.brigadier;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

@UtilityClass
public class SuggestionBuilder {

    public static <S> SuggestionProvider<S> suggest(final Consumer<List<String>> consumer) {
        return (ctx, builder) -> {
            String remaining = builder.getRemaining().toLowerCase(Locale.ROOT);
            List<String> suggestions = new ArrayList<>();
            consumer.accept(suggestions);
            for (String suggestion : suggestions) {
                if (suggestion.toLowerCase(Locale.ROOT).startsWith(remaining)) builder.suggest(suggestion);
            }
            return builder.buildFuture();
        };
    }

}
