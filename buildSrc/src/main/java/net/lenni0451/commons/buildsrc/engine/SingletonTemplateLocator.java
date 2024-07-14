package net.lenni0451.commons.buildsrc.engine;

import org.trimou.engine.locator.TemplateLocator;

import java.io.Reader;
import java.io.StringReader;

public class SingletonTemplateLocator implements TemplateLocator {

    private final String template;

    public SingletonTemplateLocator(final String template) {
        this.template = template;
    }

    @Override
    public Reader locate(String name) {
        return new StringReader(this.template);
    }

}
