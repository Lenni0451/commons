package net.lenni0451.commons.classloader;

import net.lenni0451.commons.io.IOUtils;
import org.junit.jupiter.api.*;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.Enumeration;
import java.util.function.Supplier;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ByteArrayClassLoaderTest {

    private static final String CLASS_NAME = "net/lenni0451/common/TestClass";
    private static ByteArrayClassLoader classLoader;
    private static byte[] classBytes;

    @BeforeAll
    static void init() {
        classLoader = new ByteArrayClassLoader(null);

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, CLASS_NAME, null, "java/lang/Object", new String[]{"java/util/function/Supplier"});
        MethodVisitor constructor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        constructor.visitCode();
        constructor.visitVarInsn(Opcodes.ALOAD, 0);
        constructor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        constructor.visitInsn(Opcodes.RETURN);
        constructor.visitMaxs(1, 1);
        constructor.visitEnd();

        MethodVisitor get = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "get", "()Ljava/lang/Object;", null, null);
        get.visitCode();
        get.visitLdcInsn("Test");
        get.visitInsn(Opcodes.ARETURN);
        get.visitMaxs(1, 1);
        get.visitEnd();
        classWriter.visitEnd();

        classBytes = classWriter.toByteArray();
    }

    @Test
    @Order(1)
    void addJar() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JarOutputStream jos = new JarOutputStream(baos);
        jos.putNextEntry(new ZipEntry(CLASS_NAME + ".class"));
        jos.write(classBytes);
        jos.closeEntry();
        jos.close();

        classLoader.addJar(baos.toByteArray());
    }

    @Test
    @Order(2)
    void getResourceStream() throws IOException {
        InputStream is = classLoader.getResourceAsStream(CLASS_NAME + ".class");
        assertNotNull(is);
        byte[] data = IOUtils.readAll(is);
        assertArrayEquals(classBytes, data);
    }

    @Test
    @Order(2)
    void getResource() {
        assertNotNull(classLoader.getResource(CLASS_NAME + ".class"));
    }

    @Test
    @Order(2)
    void findResource() {
        assertNotNull(classLoader.findResource(CLASS_NAME + ".class"));
    }

    @Test
    @Order(2)
    void findResources() throws IOException {
        Enumeration<URL> resources = classLoader.findResources(CLASS_NAME + ".class");
        assertNotNull(resources);
        assertNotNull(resources.nextElement());
    }

    @Test
    @Order(3)
    void findClass() {
        Class<?> clazz = Assertions.assertDoesNotThrow(() -> classLoader.findClass(CLASS_NAME.replace('/', '.')));
        assertEquals(CLASS_NAME.replace('/', '.'), clazz.getName());

        Constructor<?> constructor = assertDoesNotThrow(() -> clazz.getDeclaredConstructor());
        Object instance = assertDoesNotThrow(() -> constructor.newInstance());
        assertInstanceOf(Supplier.class, instance);
        Supplier<String> supplier = (Supplier<String>) instance;
        assertEquals("Test", supplier.get());
    }

}
