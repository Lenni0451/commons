package net.lenni0451.commons.asm.mappings.loader.formats;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.lenni0451.commons.asm.mappings.Mappings;
import net.lenni0451.commons.asm.mappings.loader.MappingsLoader;
import net.lenni0451.commons.asm.mappings.meta.ClassMetaMapping;
import net.lenni0451.commons.asm.mappings.meta.FieldMetaMapping;
import net.lenni0451.commons.asm.mappings.meta.MethodMetaMapping;
import net.lenni0451.commons.asm.mappings.meta.ParameterMetaMapping;

import javax.annotation.Nullable;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A loader for TinyV2 mappings.<br>
 * This loader supports extra metadata for classes, fields, methods and parameters.
 */
public class TinyV2MappingsLoader extends MappingsLoader {

    private static final String[] EMPTY_JAVADOC = new String[0];

    private final String fromNamespace;
    private final String toNamespace;
    private final List<ClassMetaMapping> metaMappings = new ArrayList<>();
    private final List<Property> properties = new ArrayList<>();
    private boolean parseMeta = false;
    private ClassMetaMapping currentClassMeta = null;
    private FieldMetaMapping currentFieldMeta = null;
    private MethodMetaMapping currentMethodMeta = null;
    private ParameterMetaMapping currentParameterMeta = null;

    public TinyV2MappingsLoader(final InputStream inputStream, final String fromNamespace, final String toNamespace) {
        super(inputStream);
        this.fromNamespace = fromNamespace;
        this.toNamespace = toNamespace;
    }

    public TinyV2MappingsLoader(final File file, final String fromNamespace, final String toNamespace) {
        super(file);
        this.fromNamespace = fromNamespace;
        this.toNamespace = toNamespace;
    }

    public TinyV2MappingsLoader(final Path path, final String fromNamespace, final String toNamespace) {
        super(path);
        this.fromNamespace = fromNamespace;
        this.toNamespace = toNamespace;
    }

    /**
     * Enable parsing of metadata for classes, fields, methods and parameters.<br>
     * Disabled by default.
     *
     * @return This loader
     */
    public TinyV2MappingsLoader enableMetaParsing() {
        this.parseMeta = true;
        return this;
    }

    /**
     * Get the parsed metadata mappings.<br>
     * Make sure to enable parsing of metadata before calling this method.<br>
     * <b>This loader will be initialized if this method is called. See {@link #getMappings()} for more information.</b>
     *
     * @return The parsed metadata mappings
     * @throws IllegalStateException If metadata parsing is disabled
     */
    @Override
    public List<ClassMetaMapping> getMetaMappings() {
        if (!this.parseMeta) throw new IllegalStateException("Meta parsing is disabled");
        this.getMappings(); //Ensure mappings are loaded
        return this.metaMappings;
    }

    /**
     * Get the properties defined in the tiny mappings.<br>
     * Properties without a value will have {@code null} as value.<br>
     * <b>This loader will be initialized if this method is called. See {@link #getMappings()} for more information.</b>
     *
     * @return The properties
     */
    public List<Property> getProperties() {
        this.getMappings(); //Ensure mappings are loaded
        return this.properties;
    }

    @Override
    protected Mappings load(List<String> lines) {
        Mappings mappings = new Mappings();
        Mappings baseToSource = new Mappings();
        Mappings baseToTarget = new Mappings();
        List<UnmappedMember> unmappedMembers = new ArrayList<>();

        int fromIndex = -1;
        int toIndex = -1;

        String currentClass = null;
        for (String line : lines) {
            String[] parts = line.replaceAll("^\\s+", "").split("\t", -1);
            if (fromIndex == -1) { //Header
                if (!parts[0].equals("tiny")) throw new IllegalStateException("Invalid tiny header (expected 'tiny', got '" + parts[0] + "')");
                if (!parts[1].equals("2")) throw new IllegalStateException("Invalid tiny major version (expected '2', got '" + parts[1] + "')");
                if (!parts[2].equals("0")) throw new IllegalStateException("Invalid tiny minor version (expected '0', got '" + parts[2] + "')");
                if (parts.length < 5) throw new IllegalStateException("Invalid tiny header (missing namespaces)");

                List<String> namespaces = Arrays.asList(Arrays.copyOfRange(parts, 3, parts.length));
                fromIndex = namespaces.indexOf(this.fromNamespace);
                toIndex = namespaces.indexOf(this.toNamespace);
                if (fromIndex == -1) throw new IllegalStateException("Namespace '" + this.fromNamespace + "' not found in tiny mappings (available: " + namespaces + ")");
                if (toIndex == -1) throw new IllegalStateException("Namespace '" + this.toNamespace + "' not found in tiny mappings (available: " + namespaces + ")");
            } else if (currentClass == null && line.startsWith("\t")) { //Property
                if (parts.length == 1) {
                    this.properties.add(new Property(parts[0]));
                } else if (parts.length == 2) {
                    this.properties.add(new Property(parts[0], parts[1]));
                } else {
                    throw new IllegalStateException("Property with too many parts: " + line);
                }
            } else if (line.startsWith("c\t")) { //Class mapping
                String baseName = parts[1];
                currentClass = parts[1 + fromIndex];
                String toName = parts[1 + toIndex];
                if (toName.isEmpty()) toName = currentClass;

                baseToSource.addClassMapping(baseName, currentClass);
                baseToTarget.addClassMapping(baseName, toName);
                mappings.addClassMapping(currentClass, toName);
                if (this.parseMeta) {
                    this.updateMeta(UpdateLevel.CLASS);
                    this.currentClassMeta = new ClassMetaMapping(toName, EMPTY_JAVADOC, new ArrayList<>(), new ArrayList<>());
                }
            } else if (line.startsWith("\tf\t")) { //Field mapping
                if (currentClass == null) throw new IllegalStateException("Field mapping without class mapping");
                String descriptor = parts[1];
                String fromName = parts[2 + fromIndex];
                String toName = parts[2 + toIndex];
                if (toName.isEmpty()) toName = fromName;

                unmappedMembers.add(new UnmappedMember(false, currentClass, fromName, descriptor, toName));
                if (this.parseMeta) {
                    this.updateMeta(UpdateLevel.FIELD);
                    this.currentFieldMeta = new FieldMetaMapping(toName, descriptor, EMPTY_JAVADOC);
                }
            } else if (line.startsWith("\tm\t")) { //Method mapping
                if (currentClass == null) throw new IllegalStateException("Method mapping without class mapping");
                String descriptor = parts[1];
                String fromName = parts[2 + fromIndex];
                String toName = parts[2 + toIndex];
                if (toName.isEmpty()) toName = fromName;

                unmappedMembers.add(new UnmappedMember(true, currentClass, fromName, descriptor, toName));
                if (this.parseMeta) {
                    this.updateMeta(UpdateLevel.METHOD);
                    this.currentMethodMeta = new MethodMetaMapping(toName, descriptor, EMPTY_JAVADOC, new ArrayList<>());
                }
            } else if (line.startsWith("\t\tp")) { //Parameter mapping
                if (!this.parseMeta) continue;
                if (this.currentMethodMeta == null) throw new IllegalStateException("Parameter mapping without method mapping");
                int index = Integer.parseInt(parts[1]);
                String name = parts[2];

                this.updateMeta(UpdateLevel.PARAMETER);
                this.currentParameterMeta = new ParameterMetaMapping(index, name, EMPTY_JAVADOC);
            } else if (line.startsWith("\tc")) { //Class comment
                if (!this.parseMeta) continue;
                if (this.currentClassMeta == null) throw new IllegalStateException("Comment without class mapping");
                String comment = String.join("\t", Arrays.copyOfRange(parts, 1, parts.length));
                this.currentClassMeta = this.currentClassMeta.withJavadoc(comment.split(Pattern.quote("\\n")));
            } else if (line.startsWith("\t\tc")) { //Field/Method comment
                if (!this.parseMeta) continue;
                if (this.currentFieldMeta != null && this.currentMethodMeta != null) {
                    throw new IllegalStateException("Field and method meta at the same time");
                } else if (this.currentFieldMeta != null) {
                    String comment = String.join("\t", Arrays.copyOfRange(parts, 1, parts.length));
                    this.currentFieldMeta = this.currentFieldMeta.withJavadoc(comment.split(Pattern.quote("\\n")));
                } else if (this.currentMethodMeta != null) {
                    String comment = String.join("\t", Arrays.copyOfRange(parts, 1, parts.length));
                    this.currentMethodMeta = this.currentMethodMeta.withJavadoc(comment.split(Pattern.quote("\\n")));
                } else {
                    throw new IllegalStateException("Comment without field or method mapping");
                }
            } else if (line.startsWith("\t\t\tc")) { //Parameter comment
                if (!this.parseMeta) continue;
                if (this.currentParameterMeta == null) throw new IllegalStateException("Comment without parameter mapping");
                String comment = String.join("\t", Arrays.copyOfRange(parts, 1, parts.length));
                this.currentParameterMeta = this.currentParameterMeta.withJavadoc(comment.split(Pattern.quote("\\n")));
            } else {
                throw new IllegalStateException("Unknown line: " + line);
            }
        }
        this.updateMeta(UpdateLevel.CLASS);
        this.finalizeMemberMappings(mappings, baseToSource, unmappedMembers);
        if (this.parseMeta) this.finalizeMetaMappings(baseToTarget);
        return mappings;
    }

    private void finalizeMemberMappings(final Mappings mappings, final Mappings baseToSource, final List<UnmappedMember> unmappedMembers) {
        for (UnmappedMember member : unmappedMembers) {
            if (member.method) {
                mappings.addMethodMapping(member.owner, member.name, baseToSource.mapMethodDesc(member.descriptor), member.toName);
            } else {
                mappings.addFieldMapping(member.owner, member.name, baseToSource.mapDesc(member.descriptor), member.toName);
            }
        }
    }

    private void finalizeMetaMappings(final Mappings baseToTarget) {
        List<ClassMetaMapping> remappedMetaMappings = new ArrayList<>();
        for (ClassMetaMapping classMeta : this.metaMappings) {
            remappedMetaMappings.add(classMeta);

            List<FieldMetaMapping> fieldMetas = new ArrayList<>();
            for (FieldMetaMapping field : classMeta.getFields()) {
                fieldMetas.add(field.withDescriptor(baseToTarget.mapDesc(field.getDescriptor())));
            }
            classMeta.getFields().clear();
            classMeta.getFields().addAll(fieldMetas);

            List<MethodMetaMapping> methodMetas = new ArrayList<>();
            for (MethodMetaMapping method : classMeta.getMethods()) {
                methodMetas.add(method.withDescriptor(baseToTarget.mapMethodDesc(method.getDescriptor())));
            }
            classMeta.getMethods().clear();
            classMeta.getMethods().addAll(methodMetas);
        }
        this.metaMappings.clear();
        this.metaMappings.addAll(remappedMetaMappings);
    }

    private void updateMeta(final UpdateLevel level) {
        UpdateLevel[] updates;
        switch (level) {
            case CLASS:
                updates = new UpdateLevel[]{UpdateLevel.PARAMETER, UpdateLevel.METHOD, UpdateLevel.FIELD, UpdateLevel.CLASS};
                break;
            case FIELD:
            case METHOD:
                updates = new UpdateLevel[]{UpdateLevel.PARAMETER, UpdateLevel.METHOD, UpdateLevel.FIELD};
                break;
            case PARAMETER:
                updates = new UpdateLevel[]{UpdateLevel.PARAMETER};
                break;
            default:
                throw new IllegalArgumentException("Unknown update level: " + level);
        }
        for (UpdateLevel update : updates) {
            switch (update) {
                case CLASS:
                    if (this.currentClassMeta != null && !this.currentClassMeta.isEmpty()) {
                        this.metaMappings.add(this.currentClassMeta);
                    }
                    this.currentClassMeta = null;
                    break;
                case FIELD:
                    if (this.currentFieldMeta != null && !this.currentFieldMeta.isEmpty()) {
                        this.currentClassMeta.getFields().add(this.currentFieldMeta);
                    }
                    this.currentFieldMeta = null;
                    break;
                case METHOD:
                    if (this.currentMethodMeta != null && !this.currentMethodMeta.isEmpty()) {
                        this.currentClassMeta.getMethods().add(this.currentMethodMeta);
                    }
                    this.currentMethodMeta = null;
                    break;
                case PARAMETER:
                    if (this.currentParameterMeta != null) {
                        this.currentMethodMeta.getParameters().add(this.currentParameterMeta);
                    }
                    this.currentParameterMeta = null;
                    break;
            }
        }
    }


    @AllArgsConstructor
    private static class UnmappedMember {
        private final boolean method;
        private final String owner;
        private final String name;
        private final String descriptor;
        private final String toName;
    }

    private enum UpdateLevel {
        CLASS,
        FIELD,
        METHOD,
        PARAMETER
    }

    @Getter
    @ToString
    @EqualsAndHashCode
    @AllArgsConstructor
    public static class Property {
        private final String name;
        @Nullable
        private final String value;

        public Property(final String name) {
            this(name, null);
        }
    }

}
