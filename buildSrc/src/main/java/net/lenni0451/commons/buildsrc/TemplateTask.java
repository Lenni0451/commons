package net.lenni0451.commons.buildsrc;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
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
import org.trimou.handlebars.LogHelper;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

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

        MustacheEngine engine = this.buildEngine();
        for (TemplateConfig templateConfig : templates) {
            Mustache template = engine.getMustache(templateConfig.getTemplate());
            File target = new File(this.getOutputDir().get().getAsFile(), templateConfig.getTarget());
            target.getParentFile().mkdirs();
            long time = System.nanoTime();
            try (FileWriter writer = new FileWriter(target)) {
                template.render(writer, templateConfig.getVariables());
            }
            time = System.nanoTime() - time;
            System.out.println("Rendered template '" + templateConfig.getTemplate() + "' to '" + target.getAbsolutePath() + "' in " + (time / 1_000_000) + "ms");
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
                if (templateConfig == null) throw new IllegalStateException("The template file '" + file.getName() + "' is invalid");
                templates.add(templateConfig);
            }
        }
        return templates;
    }

    private MustacheEngine buildEngine() {
        return MustacheEngineBuilder
                .newBuilder()
                .addTemplateLocator(new FileSystemTemplateLocator(0, this.getTemplateDir().get().getAsFile().getAbsolutePath()))
                .addResolver(new MapResolver())
                .registerHelpers(
                        HelpersBuilder.extra()
                                .add("log", LogHelper.builder().setDefaultLevel(LogHelper.Level.INFO).build())
                                .build()
                )
                .setProperty(EngineConfigurationKey.SKIP_VALUE_ESCAPING, true)
                .build();
    }

}
