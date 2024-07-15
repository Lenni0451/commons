package net.lenni0451.commons.buildsrc;

public class OutputCleaner {

    public static final String CLEAN_PLACEHOLDER = "\u0008";

    public static String clean(final String output) {
        StringBuilder out = new StringBuilder();
        String[] lines = output.split("\n", -1);
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line.contains(CLEAN_PLACEHOLDER)) {
                line = line.replace(CLEAN_PLACEHOLDER, "");
                if (line.trim().isEmpty()) line = null;
            }
            if (line != null) out.append(line).append(i == lines.length - 1 ? "" : "\n");
        }
        return out.toString();
    }

}
