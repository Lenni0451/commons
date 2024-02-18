package net.lenni0451.commons.brigadier;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LineArgumentBuilderTest implements CommandBuilder<CommandExecutor> {

    @Test
    void test() {
        CommandExecutor executor = new CommandExecutor();
        CommandDispatcher<CommandExecutor> dispatcher = new CommandDispatcher<>();
        dispatcher.register(
                line(literal("test"), line -> line
                        .argument("test", BoolArgumentType.bool()).defaultValue(false)
                        .argument("i", IntegerArgumentType.integer()).defaultValue(10)
                        .literal("lel")
                        .execute(ctx -> {
                            System.out.println(ctx.getArgument("test", boolean.class) + " Hi " + ctx.getArgument("i", int.class) + " Kevin");
                        }))
        );

        assertDoesNotThrow(() -> dispatcher.execute("test", executor));
        assertDoesNotThrow(() -> dispatcher.execute("test true", executor));
        assertThrows(CommandSyntaxException.class, () -> dispatcher.execute("test true 5", executor));
        assertDoesNotThrow(() -> dispatcher.execute("test true 5 lel", executor));
    }

}
