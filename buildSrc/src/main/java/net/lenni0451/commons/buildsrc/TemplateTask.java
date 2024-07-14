package net.lenni0451.commons.buildsrc;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import net.lenni0451.commons.buildsrc.helper.LowerCaseHelper;
import net.lenni0451.commons.buildsrc.helper.VarHelper;
import net.lenni0451.commons.buildsrc.model.TemplateConfig;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.trimou.Mustache;
import org.trimou.engine.MustacheEngine;
import org.trimou.engine.MustacheEngineBuilder;
import org.trimou.engine.config.EngineConfigurationKey;
import org.trimou.engine.locator.FileSystemTemplateLocator;
import org.trimou.engine.resolver.MapResolver;
import org.trimou.handlebars.HelpersBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public abstract class TemplateTask extends DefaultTask {

    private static final Gson GSON = new Gson();

    @InputDirectory
    public abstract DirectoryProperty getTemplateDir();

    @OutputDirectory
    public abstract DirectoryProperty getOutputDir();

    @TaskAction
    public void run() throws Throwable {
        List<TemplateConfig> templates = this.loadTemplates();
        if (templates.isEmpty()) return;
        System.out.println("Loaded " + templates.size() + " templates");

        for (TemplateConfig templateConfig : templates) {
            MustacheEngine templateEngine = this.buildTemplateEngine(templateConfig.globals);
            Mustache template = templateEngine.getMustache(templateConfig.template);
            Mustache variantTemplate = templateEngine.compileMustache(templateConfig.target);
            for (JsonElement variant : templateConfig.variants) {
                String relativeTarget = variantTemplate.render(variant);
                File target = new File(this.getOutputDir().get().getAsFile(), relativeTarget);
                target.getParentFile().mkdirs();
                long time = System.nanoTime();
                try (FileWriter writer = new FileWriter(target)) {
                    template.render(writer, this.merge(variant, templateConfig.variables));
                }
                time = System.nanoTime() - time;
                System.out.println("Rendered template '" + templateConfig.template + "' to '" + target.getAbsolutePath() + "' in " + (time / 1_000_000) + "ms");
            }
        }
    }

    private List<TemplateConfig> loadTemplates() throws IOException {
        File templateDir = this.getTemplateDir().get().getAsFile();
        if (!templateDir.exists() || !templateDir.isDirectory()) return Collections.emptyList();
        File[] files = templateDir.listFiles();
        if (files == null) return Collections.emptyList();

        List<TemplateConfig> templates = new ArrayList<>();
        for (File file : files) {
            if (!file.isFile() || !file.getName().toLowerCase(Locale.ROOT).endsWith(".json")) continue;
            try (JsonReader reader = new JsonReader(new FileReader(file))) {
                TemplateConfig templateConfig = GSON.fromJson(reader, TemplateConfig.class);
                if (templateConfig == null) throw new IllegalStateException("The template file '" + file.getName() + "' could not be parsed");

                if (templateConfig.template == null) throw new IllegalStateException("The template file '" + file.getName() + "' does not contain a 'template' field");
                if (templateConfig.target == null) throw new IllegalStateException("The template file '" + file.getName() + "' does not contain a 'target' field");
                if (templateConfig.globals == null) templateConfig.globals = new JsonObject();
                if (templateConfig.variants == null) templateConfig.variants = new JsonElement[]{new JsonObject()};
                if (templateConfig.variables == null) templateConfig.variables = new JsonObject();

                templates.add(templateConfig);
            }
        }
        return templates;
    }

    private JsonElement merge(final JsonElement variant, final JsonElement variables) {
        if (variant == null && variables == null) return new JsonObject();
        if (variables == null) return variant;

        if (variant instanceof JsonObject && variables instanceof JsonObject) {
            JsonObject merged = new JsonObject();
            for (Map.Entry<String, JsonElement> entry : ((JsonObject) variant).entrySet()) merged.add(entry.getKey(), entry.getValue());
            for (Map.Entry<String, JsonElement> entry : ((JsonObject) variables).entrySet()) merged.add(entry.getKey(), entry.getValue());
            return merged;
        }
        return variables;
    }

    private MustacheEngine buildTemplateEngine(final JsonObject globals) {
        MustacheEngineBuilder builder = MustacheEngineBuilder
                .newBuilder()
                .addTemplateLocator(new FileSystemTemplateLocator(0, this.getTemplateDir().get().getAsFile().getAbsolutePath()))
                .addResolver(new MapResolver())
                .registerHelpers(
                        HelpersBuilder
                                .all()
                                .add("lower", new LowerCaseHelper())
                                .add("var", new VarHelper())
                                .build()
                )
                .setProperty(EngineConfigurationKey.SKIP_VALUE_ESCAPING, true);
        for (Map.Entry<String, JsonElement> entry : globals.entrySet()) builder.addGlobalData(entry.getKey(), entry.getValue());
        return builder.build();
    }

}
