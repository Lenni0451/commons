package net.lenni0451.commons.buildsrc.model;

import java.util.Map;

public class TemplateConfig {

    private String template;
    private String target;
    private Map<String, String>[] variables;

    public String getTemplate() {
        return this.template;
    }

    public String getTarget() {
        return this.target;
    }

    public Map<String, String>[] getVariables() {
        return this.variables;
    }

}
