package net.lenni0451.commons.strings.search.tokens;

import net.lenni0451.commons.strings.StringReader;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;

@ApiStatus.Internal
public class QueryTokenizer {

    public static List<ISearchToken> tokenize(final String query) {
        List<ISearchToken> tokens = new ArrayList<>();
        StringReader reader = new StringReader(query).skipWhitespace();

        while (reader.hasNext()) {
            int start = reader.getIndex();
            if (reader.peek() == '"' || reader.peek() == '\'') {
                char quote = reader.next();
                String remaining = reader.peekRemaining();
                reader.setIndex(start);

                if (remaining.indexOf(quote) == -1) {
                    //If the quote is not closed, just read it as a keyword
                    tokens.add(new KeywordToken(reader.readWord()));
                } else {
                    tokens.add(new RequiredToken(reader.readQuoted()));
                }
            } else {
                tokens.add(new KeywordToken(reader.readWord()));
            }
        }

        return mergeOr(tokens);
    }

    private static List<ISearchToken> mergeOr(final List<ISearchToken> tokens) {
        List<ISearchToken> merged = new ArrayList<>();
        for (int i = 0; i < tokens.size(); i++) {
            ISearchToken last = merged.isEmpty() ? null : merged.get(merged.size() - 1);
            ISearchToken current = tokens.get(i);
            ISearchToken next = i == tokens.size() - 1 ? null : tokens.get(i + 1);

            if (last != null && next != null && current.getKeyword().equals("OR")) {
                merged.remove(merged.size() - 1);
                merged.add(new BinaryOrToken(last, next));
                i++;
            } else {
                merged.add(current);
            }
        }
        return merged;
    }

}
