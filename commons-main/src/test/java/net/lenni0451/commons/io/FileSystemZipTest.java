package net.lenni0451.commons.io;

import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FileSystemZipTest {

    private static final byte[] TEST_STRING = "Hello World".getBytes(StandardCharsets.UTF_8);
    private static final byte[] TEST_PLACEHOLDER = new byte[]{1, 2, 3};
    private static File tempFile;
    private static FileSystemZip zip;

    @BeforeAll
    static void setUp() throws IOException {
        tempFile = File.createTempFile("FileSystemZipTest", ".zip");
        tempFile.delete(); //Delete the file again to let the file system create a new one
        zip = new FileSystemZip(tempFile);
    }

    @AfterAll
    static void tearDown() throws Exception {
        if (zip != null) zip.close();
        tempFile.delete();
    }


    @Test
    @Order(1)
    void addEntry() {
        assertDoesNotThrow(() -> zip.addEntry("test.txt", TEST_STRING));
    }

    @Test
    @Order(1)
    void addDirectory() {
        assertDoesNotThrow(() -> zip.addDirectory("test2"));
    }

    @Test
    @Order(2)
    void hasEntry() {
        assertTrue(zip.hasEntry("test.txt"));
        assertTrue(zip.hasEntry("test2"));
        assertFalse(zip.hasEntry("test3"));
    }

    @Test
    @Order(2)
    void hasFile() {
        assertTrue(zip.hasFile("test.txt"));
        assertFalse(zip.hasFile("test2"));
    }

    @Test
    @Order(2)
    void hasDirectory() {
        assertFalse(zip.hasDirectory("test.txt"));
        assertTrue(zip.hasDirectory("test2"));
    }

    @Test
    @Order(3)
    void getEntry() {
        FileSystemZip.Entry fileEntry = assertDoesNotThrow(() -> zip.getEntry("test.txt"));
        assertEquals("test.txt", fileEntry.getRelativePath());
        assertEquals("test.txt", fileEntry.getName());
        assertTrue(fileEntry.exists());
        assertTrue(fileEntry.isFile());
        assertFalse(fileEntry.isDirectory());
        assertArrayEquals(TEST_STRING, assertDoesNotThrow(fileEntry::getData));
        assertDoesNotThrow(() -> fileEntry.setData(TEST_PLACEHOLDER));
        assertArrayEquals(TEST_PLACEHOLDER, assertDoesNotThrow(fileEntry::getData));

        FileSystemZip.Entry folderEntry = assertDoesNotThrow(() -> zip.getEntry("test2"));
        assertEquals("test2", folderEntry.getRelativePath());
        assertEquals("test2", folderEntry.getName());
        assertTrue(folderEntry.exists());
        assertFalse(folderEntry.isFile());
        assertTrue(folderEntry.isDirectory());
        assertThrows(IllegalStateException.class, folderEntry::getData);
        assertThrows(IllegalStateException.class, () -> folderEntry.setData(TEST_PLACEHOLDER));
    }

    @Test
    @Order(4)
    void forEach() {
        assertDoesNotThrow(() -> zip.forEach(entry -> {
            if (entry.getName().isEmpty()) assertTrue(entry.isDirectory());
            else if (entry.getName().equals("test.txt")) assertArrayEquals(TEST_PLACEHOLDER, assertDoesNotThrow(entry::getData));
            else if (entry.getName().equals("test2/") || entry.getName().equals("test2")) assertThrows(IllegalStateException.class, entry::getData);
            else fail("Unknown entry: " + entry.getName());
        }));
    }

    @Test
    @Order(5)
    void delete() {
        assertDoesNotThrow(() -> zip.forEach(entry -> {
            if (!entry.getName().isEmpty()) assertDoesNotThrow(entry::delete);
        }));
        assertDoesNotThrow(() -> zip.forEach(entry -> {
            if (!entry.getName().isEmpty()) fail("Entry not deleted: " + entry.getName());
        }));
    }

}
