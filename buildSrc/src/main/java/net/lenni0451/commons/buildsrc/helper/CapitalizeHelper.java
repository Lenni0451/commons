package net.lenni0451.commons.buildsrc.helper;

import org.trimou.handlebars.AbstractHelper;
import org.trimou.handlebars.Options;

import java.util.Locale;

public class CapitalizeHelper extends AbstractHelper {

    @Override
    public void execute(Options options) {
        if (options.getParameters().size() != 1) throw new IllegalArgumentException("{{lowercase}} helper must have exactly one parameter");
        String s = this.convertValue(options.getParameters().get(0));
        s = s.substring(0, 1).toUpperCase(Locale.ROOT) + s.substring(1).toLowerCase(Locale.ROOT);
        this.append(options, s);
    }

}
