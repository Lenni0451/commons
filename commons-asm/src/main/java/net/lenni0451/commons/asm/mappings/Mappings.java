package net.lenni0451.commons.asm.mappings;

import org.objectweb.asm.commons.Remapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
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
    ReverseCacheMode reverseCacheMode;
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

    public Mappings(final Mappings mappings, final Function<Map<String, String>, Map<String, String>> mapCopier) {
        this.mapInitializer = mappings.mapInitializer;
        this.packageMappings = mapCopier.apply(mappings.packageMappings);
        this.classMappings = mapCopier.apply(mappings.classMappings);
        this.fieldMappings = mapCopier.apply(mappings.fieldMappings);
        this.methodMappings = mapCopier.apply(mappings.methodMappings);
    }

    /**
     * @return The package mappings
     */
    public Map<String, String> getPackageMappings() {
        return Collections.unmodifiableMap(this.packageMappings);
    }

    /**
     * Add a package mapping.<br>
     * If a package is {@code .} it is the default package.<br>
     * {@code org/example} {@literal ->} {@code org/example2}
     *
     * @param from The source package
     * @param to   The mapped package
     * @return This instance
     * @see #addPackageMapping(String, String, boolean)
     */
    public Mappings addPackageMapping(final String from, final String to) {
        return this.addPackageMapping(from, to, false);
    }

    /**
     * Add a package mapping.<br>
     * If a package is {@code .} it is the default package.<br>
     * {@code org/example} {@literal ->} {@code org/example2}
     *
     * @param from         The source package
     * @param to           The mapped package
     * @param skipExisting If the mapping should be skipped if it already exists
     * @return This instance
     */
    public Mappings addPackageMapping(String from, String to, final boolean skipExisting) {
        if (from.length() > 1 && from.contains(".")) throw new IllegalArgumentException("Package mappings must not contain '.'");
        if (to.length() > 1 && to.contains(".")) throw new IllegalArgumentException("Package mappings must not contain '.'");
        if (!from.equals(".") && !from.endsWith("/")) from += "/";
        if (!to.equals(".") && !to.endsWith("/")) to += "/";
        if (skipExisting && this.packageMappings.containsKey(from)) return this;
        this.packageMappings.put(from, to);
        if (this.reverse != null) {
            if (this.reverseCacheMode.equals(ReverseCacheMode.UPDATE)) {
                this.reverse.packageMappings.put(to, from);
                Reverser.recalculateClasses(this, this.reverse);
            } else if (this.reverseCacheMode.equals(ReverseCacheMode.RECREATE)) {
                this.reverse.reverseCacheMode = null;
                this.reverse.reverse = null;
                this.reverseCacheMode = null;
                this.reverse = null;
            }
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

    /**
     * Map the package of a class name.
     *
     * @param internalName The internal name of the class
     * @return The mapped class name
     */
    public String mapClassPackage(final String internalName) {
        String packageName = internalName.substring(0, internalName.lastIndexOf('/') + 1).replace('/', '.');
        String className = internalName.substring(internalName.lastIndexOf('/') + 1);
        return this.mapPackageName(packageName).replace('.', '/') + className;
    }

    /**
     * @return The class mappings
     */
    public Map<String, String> getClassMappings() {
        return Collections.unmodifiableMap(this.classMappings);
    }

    /**
     * Add a class mapping.<br>
     * {@code org/example/SomeClass} {@literal ->} {@code org/example/NewName}
     *
     * @param from The source class
     * @param to   The mapped class
     * @return This instance
     */
    public Mappings addClassMapping(final String from, final String to) {
        return this.addClassMapping(from, to, false);
    }

    /**
     * Add a class mapping.<br>
     * {@code org/example/SomeClass} {@literal ->} {@code org/example/NewName}
     *
     * @param from         The source class
     * @param to           The mapped class
     * @param skipExisting If the mapping should be skipped if it already exists
     * @return This instance
     */
    public Mappings addClassMapping(final String from, final String to, final boolean skipExisting) {
        if (from.contains(".")) throw new IllegalArgumentException("Class mappings must not contain '.'");
        if (to.contains(".")) throw new IllegalArgumentException("Class mappings must not contain '.'");
        if (skipExisting && this.classMappings.containsKey(from)) return this;
        this.classMappings.put(from, to);
        if (this.reverse != null) {
            if (this.reverseCacheMode.equals(ReverseCacheMode.UPDATE)) {
                this.reverse.classMappings.put(this.map(from), from);
                Reverser.recalculateFields(this, this.reverse);
                Reverser.recalculateMethods(this, this.reverse);
            } else if (this.reverseCacheMode.equals(ReverseCacheMode.RECREATE)) {
                this.reverse.reverseCacheMode = null;
                this.reverse.reverse = null;
                this.reverseCacheMode = null;
                this.reverse = null;
            }
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

    /**
     * @return The field mappings
     */
    public Map<String, String> getFieldMappings() {
        return Collections.unmodifiableMap(this.fieldMappings);
    }

    /**
     * Add a field mapping.<br>
     * The descriptor is optional but highly recommended to avoid conflicts.<br>
     * {@code org/example/SomeClass.fieldName:I} {@literal ->} {@code newFieldName}
     *
     * @param owner      The owner of the field
     * @param name       The name of the field
     * @param descriptor The descriptor of the field
     * @param newName    The new name of the field
     * @return This instance
     * @see #addFieldMapping(String, String, String, String, boolean)
     */
    public Mappings addFieldMapping(final String owner, final String name, @Nullable final String descriptor, final String newName) {
        return this.addFieldMapping(owner, name, descriptor, newName, false);
    }

    /**
     * Add a field mapping.<br>
     * The descriptor is optional but highly recommended to avoid conflicts.<br>
     * {@code org/example/SomeClass.fieldName:I} {@literal ->} {@code newFieldName}
     *
     * @param owner        The owner of the field
     * @param name         The name of the field
     * @param descriptor   The descriptor of the field
     * @param newName      The new name of the field
     * @param skipExisting If the mapping should be skipped if it already exists
     * @return This instance
     */
    public Mappings addFieldMapping(final String owner, final String name, @Nullable final String descriptor, final String newName, final boolean skipExisting) {
        if (owner.contains(".")) throw new IllegalArgumentException("Field mappings must not contain '.'");
        String key = owner + "." + name + (descriptor != null ? ":" + descriptor : "");
        if (skipExisting && this.fieldMappings.containsKey(key)) return this;
        this.fieldMappings.put(key, newName);
        if (this.reverse != null) {
            if (this.reverseCacheMode.equals(ReverseCacheMode.UPDATE)) {
                this.reverse.addFieldMapping(this.map(owner), newName, this.mapDesc(descriptor), name);
            } else if (this.reverse.equals(ReverseCacheMode.RECREATE)) {
                this.reverse.reverseCacheMode = null;
                this.reverse.reverse = null;
                this.reverseCacheMode = null;
                this.reverse = null;
            }
        }
        return this;
    }

    @Override
    public String mapFieldName(String owner, String name, String descriptor) {
        String mappedName = this.fieldMappings.get(owner + "." + name + ":" + descriptor);
        if (mappedName != null) return mappedName;
        return this.fieldMappings.getOrDefault(owner + "." + name, name);
    }

    /**
     * @return The method mappings
     */
    public Map<String, String> getMethodMappings() {
        return Collections.unmodifiableMap(this.methodMappings);
    }

    /**
     * Add a method mapping.<br>
     * {@code org/example/SomeClass.methodName(I)V} {@literal ->} {@code newMethodName}
     *
     * @param owner      The owner of the method
     * @param name       The name of the method
     * @param descriptor The descriptor of the method
     * @param newName    The new name of the method
     * @return This instance
     * @see #addMethodMapping(String, String, String, String, boolean)
     */
    public Mappings addMethodMapping(final String owner, final String name, final String descriptor, final String newName) {
        return this.addMethodMapping(owner, name, descriptor, newName, false);
    }

    /**
     * Add a method mapping.<br>
     * {@code org/example/SomeClass.methodName(I)V} {@literal ->} {@code newMethodName}
     *
     * @param owner        The owner of the method
     * @param name         The name of the method
     * @param descriptor   The descriptor of the method
     * @param newName      The new name of the method
     * @param skipExisting If the mapping should be skipped if it already exists
     * @return This instance
     */
    public Mappings addMethodMapping(final String owner, final String name, final String descriptor, final String newName, final boolean skipExisting) {
        if (owner.contains(".")) throw new IllegalArgumentException("Method mappings must not contain '.'");
        String key = owner + "." + name + descriptor;
        if (skipExisting && this.methodMappings.containsKey(key)) return this;
        this.methodMappings.put(key, newName);
        if (this.reverse != null) {
            if (this.reverseCacheMode.equals(ReverseCacheMode.UPDATE)) {
                this.reverse.addMethodMapping(this.map(owner), newName, this.mapMethodDesc(descriptor), name);
            } else if (this.reverseCacheMode.equals(ReverseCacheMode.RECREATE)) {
                this.reverse.reverseCacheMode = null;
                this.reverse.reverse = null;
                this.reverseCacheMode = null;
                this.reverse = null;
            }
        }
        return this;
    }

    @Override
    public String mapMethodName(String owner, String name, String descriptor) {
        return this.methodMappings.getOrDefault(owner + "." + name + descriptor, name);
    }

    /**
     * @return The size of all mappings
     */
    public int size() {
        return this.packageMappings.size() + this.classMappings.size() + this.fieldMappings.size() + this.methodMappings.size();
    }

    /**
     * @return An empty copy of these mappings
     */
    public Mappings emptyCopy() {
        return this.emptyCopy(this.mapInitializer);
    }

    /**
     * Get an empty copy of these mappings with a custom map initializer.<br>
     * This should be overridden by subclasses to return the correct type.
     *
     * @param mapInitializer The map initializer
     * @return An empty copy of these mappings
     */
    public Mappings emptyCopy(final Supplier<Map<String, String>> mapInitializer) {
        return new Mappings(mapInitializer);
    }

    /**
     * @return A copy of these mappings
     */
    public Mappings copy() {
        return this.copy(this.mapInitializer);
    }

    /**
     * Get a copy of these mappings with a custom map initializer.<br>
     * This should be overridden by subclasses to return the correct type.
     *
     * @param mapInitializer The map initializer
     * @return A copy of these mappings
     */
    public Mappings copy(final Supplier<Map<String, String>> mapInitializer) {
        Mappings copy = this.emptyCopy(mapInitializer);
        copy.packageMappings.putAll(this.packageMappings);
        copy.classMappings.putAll(this.classMappings);
        copy.fieldMappings.putAll(this.fieldMappings);
        copy.methodMappings.putAll(this.methodMappings);
        return copy;
    }

    /**
     * Isolate this instance from the reversed one.<br>
     * This will remove the reverse reference from this instance and the reverse instance.
     *
     * @return This instance
     */
    public Mappings isolate() {
        if (this.reverse != null) {
            this.reverse.reverse = null;
            this.reverse = null;
        }
        return this;
    }

    /**
     * Reverse the mappings using the {@link ReverseCacheMode#UPDATE} mode.
     *
     * @return The reversed mappings
     */
    public Mappings reverse() {
        return this.reverse(ReverseCacheMode.UPDATE);
    }

    /**
     * Reverse the mappings using the given mode.<br>
     * If the reverse mappings are already cached with the given mode, they will be returned.<br>
     * Reversing the mappings is an expensive operation and should be cached if used multiple times.
     *
     * @param mode The mode to use
     * @return The reversed mappings
     */
    public Mappings reverse(final ReverseCacheMode mode) {
        if (this.reverse != null && this.reverseCacheMode.equals(mode)) return this.reverse;
        Mappings reverse = Reverser.init(this);
        if (mode.equals(ReverseCacheMode.STANDALONE)) return reverse;
        this.reverseCacheMode = mode;
        this.reverse = reverse;
        reverse.reverseCacheMode = mode;
        reverse.reverse = this;
        if (mode.equals(ReverseCacheMode.IMMUTABLE)) {
            this.reverse = new Mappings(this.reverse, Collections::unmodifiableMap);
        }
        return this.reverse;
    }


    public enum ReverseCacheMode {
        /**
         * Update the reverse mappings if new mappings are added.<br>
         * This reduces the performance overhead of fully reversing the mappings if fields or methods are added.
         * Class or package mappings will still require a nearly full reverse.
         */
        UPDATE,
        /**
         * Recreate the reverse mappings if any new mappings are added.<br>
         * This will cause overhead if new mappings are added.
         */
        RECREATE,
        /**
         * Create an immutable reverse copy of the mappings.<br>
         * If any new mappings are added, the reverse will not be updated.<br>
         * This causes no overhead if new mappings are added, but the reversed mappings will be outdated.
         */
        IMMUTABLE,
        /**
         * Create a standalone reverse copy of the mappings.<br>
         * They will not be linked and changes to either side will not affect the other.
         */
        STANDALONE
    }

}
