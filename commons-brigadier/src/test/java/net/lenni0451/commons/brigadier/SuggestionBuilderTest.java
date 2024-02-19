package net.lenni0451.commons.brigadier;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SuggestionBuilderTest implements CommandBuilder<CommandExecutor> {

    private static final CommandExecutor executor = new CommandExecutor();

    @Test
    void test() {
        CommandDispatcher<CommandExecutor> dispatcher = new CommandDispatcher<>();
        dispatcher.register(
                literal("test").then(
                        argument("test", StringArgumentType.string())
                                .suggests(SuggestionBuilder.suggest(suggestions -> {
                                    suggestions.add("hello");
                                    suggestions.add("world");
                                    suggestions.add("test");
                                    suggestions.add("test2");
                                }))
                                .executes(ctx -> 1)
                )
        );

        this.checkCompletions(dispatcher, "test ", "hello", "world", "test", "test2");
        this.checkCompletions(dispatcher, "test h", "hello");
        this.checkCompletions(dispatcher, "test w", "world");
        this.checkCompletions(dispatcher, "test te", "test", "test2");
    }

    private void checkCompletions(final CommandDispatcher<CommandExecutor> dispatcher, final String command, final String... expectedCompletions) {
        ParseResults<CommandExecutor> results = dispatcher.parse(command, executor);
        Suggestions suggestions = assertDoesNotThrow(() -> dispatcher.getCompletionSuggestions(results).get());
        List<String> completions = suggestions.getList().stream().map(Suggestion::getText).collect(Collectors.toList());
        for (String expectedCompletion : expectedCompletions) {
            assertTrue(completions.remove(expectedCompletion), "Missing completion: " + expectedCompletion + " in " + completions);
        }
        assertTrue(completions.isEmpty(), "Unexpected completions: " + completions);
    }

}
