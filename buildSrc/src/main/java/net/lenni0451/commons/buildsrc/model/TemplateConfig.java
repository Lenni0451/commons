package net.lenni0451.commons.buildsrc.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TemplateConfig {

    public String parent;
    public String baseDir;
    public String template;
    public String target;
    public JsonObject globals;
    public JsonElement[] variants;
    public JsonElement variables;

    public void copyFrom(final TemplateConfig other) {
        if (this.baseDir == null) this.baseDir = other.baseDir;
        if (this.template == null) this.template = other.template;
        if (this.target == null) this.target = other.target;
        if (this.globals == null) this.globals = other.globals;
        if (this.variants == null) this.variants = other.variants;
        if (this.variables == null) this.variables = other.variables;
    }

}
