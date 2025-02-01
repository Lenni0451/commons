package net.lenni0451.commons.logging;

import lombok.Value;

import javax.annotation.Nullable;
import java.util.Arrays;

public interface MessageFormat {

    MessageFormat CURLY_BRACKETS = (message, args) -> {
        MessageArgs messageArgs = split(args);
        StringBuilder builder = new StringBuilder();

        String left = message;
        int index = 0;
        while (left.contains("{}")) {
            int i = left.indexOf("{}");
            builder.append(left, 0, i);
            builder.append(messageArgs.getArg(index++, "{}"));
            left = left.substring(i + 2);
        }
        builder.append(left);

        return new Result(builder.toString(), messageArgs.getThrowable());
    };
    MessageFormat STRING_FORMAT = (message, args) -> {
        MessageArgs messageArgs = split(args);
        return new Result(String.format(message, messageArgs.getArgs()), messageArgs.getThrowable());
    };

    /**
     * Split logging arguments into message arguments and throwable.
     *
     * @param args The logging arguments
     * @return The message arguments and throwable
     */
    static MessageArgs split(final Object[] args) {
        if (args.length > 0 && args[args.length - 1] instanceof Throwable) {
            return new MessageArgs(Arrays.copyOfRange(args, 0, args.length - 1), (Throwable) args[args.length - 1]);
        } else {
            return new MessageArgs(args, null);
        }
    }

    /**
     * Format a message with argument placeholders.
     *
     * @param message The message to format
     * @param args    The arguments for the message
     * @return The formatted message
     */
    Result format(final String message, final Object... args);


    @Value
    class MessageArgs {
        private final Object[] args;
        @Nullable
        private final Throwable throwable;

        public Object getArg(final int index, final String def) {
            if (index < 0 || index >= this.args.length) return def;
            return this.args[index];
        }
    }

    /**
     * A formatted message with an optional throwable.
     */
    @Value
    class Result {
        private final String message;
        @Nullable
        private final Throwable throwable;
    }

}
