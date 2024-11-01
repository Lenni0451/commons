package net.lenni0451.commons.asm.mappings;

import org.objectweb.asm.commons.Remapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class Mappings extends Remapper {

    protected final Supplier<Map<String, String>> mapInitializer;
    /**
     * Package names are separated by {@code /}.<br>
     * If a package equals {@code .} it is the default package.<br>
     * {@code org/example} {@literal ->} {@code org/example2}
     */
    protected final Map<String, String> packageMappings;
    /**
     * Class names are separated by {@code /}.<br>
     * {@code org/example/SomeClass} {@literal ->} {@code org/example/NewName}
     */
    protected final Map<String, String> classMappings;
    /**
     * Field names are separated by {@code .} from the owner and with {@code :} from the descriptor.<br>
     * The descriptor is optional but highly recommended to avoid conflicts.<br>
     * {@code org/example/SomeClass.fieldName:I} {@literal ->} {@code newFieldName}
     */
    protected final Map<String, String> fieldMappings;
    /**
     * Method names are separated by {@code .} from the owner. The descriptor is directly appended to the name.<br>
     * {@code org/example/SomeClass.methodName(I)V} {@literal ->} {@code newMethodName}
     */
    protected final Map<String, String> methodMappings;
    Mappings reverse;

    public Mappings() {
        this(HashMap::new);
    }

    public Mappings(final Supplier<Map<String, String>> mapInitializer) {
        this.mapInitializer = mapInitializer;
        this.packageMappings = mapInitializer.get();
        this.classMappings = mapInitializer.get();
        this.fieldMappings = mapInitializer.get();
        this.methodMappings = mapInitializer.get();
    }

    public Mappings addPackageMapping(final String from, final String to) {
        return this.addPackageMapping(from, to, false);
    }

    public Mappings addPackageMapping(String from, String to, final boolean skipExisting) {
        if (from.length() > 1 && from.contains(".")) throw new IllegalArgumentException("Package mappings must not contain '.'");
        if (to.length() > 1 && to.contains(".")) throw new IllegalArgumentException("Package mappings must not contain '.'");
        if (!from.equals(".") && !from.endsWith("/")) from += "/";
        if (!to.equals(".") && !to.endsWith("/")) to += "/";
        if (skipExisting && this.packageMappings.containsKey(from)) return this;
        this.packageMappings.put(from, to);
        if (this.reverse != null) {
            this.reverse.packageMappings.put(to, from);
            Reverser.recalculateClasses(this, this.reverse);
        }
        return this;
    }

    @Override
    public String mapPackageName(String name) {
        if (name.isEmpty()) return this.packageMappings.getOrDefault(".", "");
        String[] parts = name.split(Pattern.quote("."));
        for (int i = parts.length - 1; i >= 0; i--) {
            String current = String.join("/", Arrays.copyOfRange(parts, 0, i + 1)) + "/";
            if (this.packageMappings.containsKey(current)) {
                String mapped = this.packageMappings.get(current).replace('/', '.');
                mapped = mapped.substring(0, mapped.length() - 1);
                if (!mapped.isEmpty() && i != parts.length - 1) mapped += ".";
                mapped += String.join(".", Arrays.copyOfRange(parts, i + 1, parts.length));
                if (name.endsWith(".")) mapped += ".";
                return mapped;
            }
        }
        return name;
    }

    public String mapClassPackage(final String internalName) {
        String packageName = internalName.substring(0, internalName.lastIndexOf('/') + 1).replace('/', '.');
        String className = internalName.substring(internalName.lastIndexOf('/') + 1);
        return this.mapPackageName(packageName).replace('.', '/') + className;
    }

    public Mappings addClassMapping(final String from, final String to) {
        return this.addClassMapping(from, to, false);
    }

    public Mappings addClassMapping(final String from, final String to, final boolean skipExisting) {
        if (from.contains(".")) throw new IllegalArgumentException("Class mappings must not contain '.'");
        if (to.contains(".")) throw new IllegalArgumentException("Class mappings must not contain '.'");
        if (skipExisting && this.classMappings.containsKey(from)) return this;
        this.classMappings.put(from, to);
        if (this.reverse != null) {
            this.reverse.classMappings.put(this.map(from), from);
            Reverser.recalculateFields(this, this.reverse);
            Reverser.recalculateMethods(this, this.reverse);
        }
        return this;
    }

    @Nonnull
    @Override
    public String map(String internalName) {
        String mapped = this.classMappings.getOrDefault(internalName, internalName);
        if (this.packageMappings.isEmpty()) return mapped;
        return this.mapClassPackage(mapped);
    }

    public Mappings addFieldMapping(final String owner, final String name, @Nullable final String descriptor, final String newName) {
        return this.addFieldMapping(owner, name, descriptor, newName, false);
    }

    public Mappings addFieldMapping(final String owner, final String name, @Nullable final String descriptor, final String newName, final boolean skipExisting) {
        if (owner.contains(".")) throw new IllegalArgumentException("Field mappings must not contain '.'");
        String key = owner + "." + name + (descriptor != null ? ":" + descriptor : "");
        if (skipExisting && this.fieldMappings.containsKey(key)) return this;
        this.fieldMappings.put(key, newName);
        if (this.reverse != null) this.reverse.addFieldMapping(this.map(owner), newName, this.mapDesc(descriptor), name);
        return this;
    }

    @Override
    public String mapFieldName(String owner, String name, String descriptor) {
        String mappedName = this.fieldMappings.get(owner + "." + name + ":" + descriptor);
        if (mappedName != null) return mappedName;
        return this.fieldMappings.getOrDefault(owner + "." + name, name);
    }

    public Mappings addMethodMapping(final String owner, final String name, final String descriptor, final String newName) {
        return this.addMethodMapping(owner, name, descriptor, newName, false);
    }

    public Mappings addMethodMapping(final String owner, final String name, final String descriptor, final String newName, final boolean skipExisting) {
        if (owner.contains(".")) throw new IllegalArgumentException("Method mappings must not contain '.'");
        String key = owner + "." + name + descriptor;
        if (skipExisting && this.methodMappings.containsKey(key)) return this;
        this.methodMappings.put(key, newName);
        if (this.reverse != null) this.reverse.addMethodMapping(this.map(owner), newName, this.mapMethodDesc(descriptor), name);
        return this;
    }

    @Override
    public String mapMethodName(String owner, String name, String descriptor) {
        return this.methodMappings.getOrDefault(owner + "." + name + descriptor, name);
    }

    public Mappings emptyCopy() {
        return this.emptyCopy(this.mapInitializer);
    }

    public Mappings emptyCopy(final Supplier<Map<String, String>> mapInitializer) {
        return new Mappings(mapInitializer);
    }

    public Mappings copy() {
        return this.copy(this.mapInitializer);
    }

    public Mappings copy(final Supplier<Map<String, String>> mapInitializer) {
        Mappings copy = this.emptyCopy(mapInitializer);
        copy.packageMappings.putAll(this.packageMappings);
        copy.classMappings.putAll(this.classMappings);
        copy.fieldMappings.putAll(this.fieldMappings);
        copy.methodMappings.putAll(this.methodMappings);
        return copy;
    }

    public void isolate() {
        if (this.reverse != null) {
            this.reverse.reverse = null;
            this.reverse = null;
        }
    }

    public Mappings reverse() {
        if (this.reverse != null) return this.reverse;
        Reverser.init(this);
        return this.reverse;
    }

    @Override
    public String toString() {
        return "Mappings{" +
                "packageMappings=" + this.packageMappings +
                ", classMappings=" + this.classMappings +
                ", fieldMappings=" + this.fieldMappings +
                ", methodMappings=" + this.methodMappings +
                '}';
    }

}
