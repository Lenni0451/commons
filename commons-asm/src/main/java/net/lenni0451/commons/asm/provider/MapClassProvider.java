package net.lenni0451.commons.asm.provider;

import net.lenni0451.commons.asm.ASMUtils;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.function.Function;

import static net.lenni0451.commons.asm.ASMUtils.dot;
import static net.lenni0451.commons.asm.ASMUtils.slash;

public class MapClassProvider implements ClassProvider {

    private final Map<String, byte[]> classes;
    private final NameFormat nameFormat;

    public MapClassProvider(final Map<String, byte[]> classes, final NameFormat nameFormat) {
        this.classes = classes;
        this.nameFormat = nameFormat;
    }

    @Nonnull
    @Override
    public byte[] getClass(String name) throws ClassNotFoundException {
        String formattedName = this.nameFormat.format(name);
        byte[] clazz = this.classes.get(formattedName);
        if (clazz != null) return clazz;
        throw new ClassNotFoundException(name);
    }


    public enum NameFormat {
        SLASH(ASMUtils::slash),
        DOT(ASMUtils::dot),
        SLASH_CLASS(name -> slash(name) + ".class"),
        DOT_CLASS(name -> dot(name) + ".class");

        private final Function<String, String> formatter;

        NameFormat(final Function<String, String> formatter) {
            this.formatter = formatter;
        }

        public String format(final String name) {
            return this.formatter.apply(name);
        }
    }

}
