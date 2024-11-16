package net.lenni0451.commons.asm.provider;

import net.lenni0451.commons.asm.ASMUtils;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static net.lenni0451.commons.asm.ASMUtils.dot;
import static net.lenni0451.commons.asm.ASMUtils.slash;

/**
 * A class provider that loads classes from a map.<br>
 * The name format can be {@link NameFormat#SLASH}, {@link NameFormat#DOT}, {@link NameFormat#SLASH_CLASS} or {@link NameFormat#DOT_CLASS}.
 */
public class MapClassProvider implements ClassProvider {

    private final Map<String, byte[]> classes;
    private final NameFormat nameFormat;

    public MapClassProvider(final Map<String, byte[]> classes, final NameFormat nameFormat) {
        this.classes = classes;
        this.nameFormat = nameFormat;
    }

    public Map<String, byte[]> getClasses() {
        return this.classes;
    }

    @Nonnull
    @Override
    public byte[] getClass(String name) throws ClassNotFoundException {
        String formattedName = this.nameFormat.format(name);
        byte[] clazz = this.classes.get(formattedName);
        if (clazz != null) return clazz;
        throw new ClassNotFoundException(name);
    }

    @Nonnull
    @Override
    public Map<String, ClassSupplier> getAllClasses() {
        Map<String, ClassSupplier> classes = new HashMap<>();
        for (Map.Entry<String, byte[]> entry : this.classes.entrySet()) {
            classes.put(this.nameFormat.toSlash(entry.getKey()), entry::getValue);
        }
        return classes;
    }


    public enum NameFormat {
        SLASH(ASMUtils::slash, ASMUtils::dot),
        DOT(ASMUtils::dot, Function.identity()),
        SLASH_CLASS(name -> slash(name) + ".class", name -> dot(name).substring(0, name.length() - 6)),
        DOT_CLASS(name -> dot(name) + ".class", name -> name.substring(0, name.length() - 6));

        private final Function<String, String> formatter;
        private final Function<String, String> toSlash;

        NameFormat(final Function<String, String> formatter, final Function<String, String> toSlash) {
            this.formatter = formatter;
            this.toSlash = toSlash;
        }

        public String format(final String name) {
            return this.formatter.apply(name);
        }

        public String toSlash(final String name) {
            return this.toSlash.apply(name);
        }
    }

}
