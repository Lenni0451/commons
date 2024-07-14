package net.lenni0451.commons.buildsrc.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TemplateConfig {

    public String template;
    public String target;
    public JsonObject globals;
    public JsonElement[] variants;
    public JsonElement variables;

}
