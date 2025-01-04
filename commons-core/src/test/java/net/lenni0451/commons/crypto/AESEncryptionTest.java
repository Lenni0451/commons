package net.lenni0451.commons.crypto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class AESEncryptionTest {

    @Test
    void test() throws Throwable {
        byte[] key = "Test".getBytes();
        byte[] secretData = "Hello World".getBytes();

        AESEncryption encryption = new AESEncryption();
        byte[] encryptedData = encryption.encrypt(key, secretData);
        byte[] decryptedData = encryption.decrypt(key, encryptedData);
        assertArrayEquals(secretData, decryptedData);
    }

}
