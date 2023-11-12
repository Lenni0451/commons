package net.lenni0451.commons;

import lombok.SneakyThrows;
import net.lenni0451.commons.collections.enumerations.SingletonEnumeration;
import net.lenni0451.commons.io.ByteArrayURLStreamHandler;
import net.lenni0451.commons.io.IOUtils;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * A class loader with the ability to load classes from a byte array.
 */
public class ByteArrayClassLoader extends ClassLoader {

    private final Map<String, byte[]> content = new HashMap<>();
    private final Map<String, Class<?>> loadedClasses = new HashMap<>();

    public ByteArrayClassLoader(@Nullable final ClassLoader parent) {
        super(parent);
    }

    /**
     * Add a jar to the class loader.<br>
     * All entries will be iterated and added.
     *
     * @param data The jar file as byte array
     * @throws IOException If an I/O error occurs
     */
    public void addJar(final byte[] data) throws IOException {
        final JarInputStream jis = new JarInputStream(new ByteArrayInputStream(data));
        JarEntry entry;
        while ((entry = jis.getNextJarEntry()) != null) {
            if (entry.isDirectory()) continue;
            this.addEntry(entry.getName(), IOUtils.readAll(jis));
        }
    }

    /**
     * Add an entry to the class loader.<br>
     * The name is the full path to the class file:<br>
     * e.g. {@code net/lenni0451/commons/ByteArrayClassLoader.class}
     *
     * @param name The name of the entry
     * @param data The data of the entry
     */
    public void addEntry(final String name, final byte[] data) {
        this.content.put(name, data);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            return super.findClass(name);
        } catch (Throwable ignored) {
        }
        if (this.loadedClasses.containsKey(name)) return this.loadedClasses.get(name);

        byte[] data = this.content.get(name.replace(".", "/") + ".class");
        if (data == null) throw new ClassNotFoundException(name);
        Class<?> clazz = this.defineClass(name, data, 0, data.length);
        this.defineClassPackage(clazz.getName());
        this.loadedClasses.put(name, clazz);
        return clazz;
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        if (!this.content.containsKey(name)) return null;
        return new ByteArrayInputStream(this.content.get(name));
    }

    @Override
    @SneakyThrows
    public URL getResource(String name) {
        byte[] data = this.content.get(name);
        if (data == null) return super.getResource(name);
        return ByteArrayURLStreamHandler.makeURL(name, data);
    }

    @Override
    protected URL findResource(String name) {
        return this.getResource(name);
    }

    @Override
    protected Enumeration<URL> findResources(String name) throws IOException {
        URL resource = this.getResource(name);
        if (resource == null) return super.findResources(name);
        return new SingletonEnumeration<>(resource);
    }

    private void defineClassPackage(final String name) {
        if (!name.contains(".")) return;
        String packageName = name.substring(0, name.lastIndexOf("."));
        if (this.getPackage(packageName) != null) return;
        this.definePackage(packageName, null, null, null, null, null, null, null);
    }

}
