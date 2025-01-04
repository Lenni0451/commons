package net.lenni0451.commons.crypto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.annotation.WillNotClose;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class AESEncryption {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int BUFFER_SIZE = 1024;

    private final Settings settings;

    public AESEncryption() {
        this(new Settings());
    }

    public AESEncryption(final Settings settings) {
        this.settings = settings;
    }

    /**
     * Encrypt the data with the given key.
     *
     * @param key  The key to encrypt the data
     * @param data The data to encrypt
     * @return The encrypted data
     * @throws NoSuchAlgorithmException           If the algorithm is not supported
     * @throws InvalidKeySpecException            If the key specification is invalid
     * @throws NoSuchPaddingException             If the padding is not supported
     * @throws InvalidAlgorithmParameterException If the algorithm parameters are invalid
     * @throws InvalidKeyException                If the key is invalid
     * @throws IOException                        If an I/O error occurs
     * @throws IllegalBlockSizeException          If the block size is invalid
     * @throws BadPaddingException                If the padding is invalid
     */
    public byte[] encrypt(final byte[] key, final byte[] data) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        this.encrypt(key, in, out);
        return out.toByteArray();
    }

    /**
     * Read and encrypt the data from the input stream and write it to the output stream.
     *
     * @param key The key to encrypt the data
     * @param in  The input stream to read the data from
     * @param out The output stream to write the encrypted data to
     * @throws NoSuchAlgorithmException           If the algorithm is not supported
     * @throws InvalidKeySpecException            If the key specification is invalid
     * @throws NoSuchPaddingException             If the padding is not supported
     * @throws InvalidAlgorithmParameterException If the algorithm parameters are invalid
     * @throws InvalidKeyException                If the key is invalid
     * @throws IOException                        If an I/O error occurs
     * @throws IllegalBlockSizeException          If the block size is invalid
     * @throws BadPaddingException                If the padding is invalid
     */
    public void encrypt(final byte[] key, @WillNotClose final InputStream in, @WillNotClose final OutputStream out) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
        byte[] salt = new byte[this.settings.saltLength];
        RANDOM.nextBytes(salt);
        byte[] iv = new byte[this.settings.ivLength];
        RANDOM.nextBytes(iv);

        SecretKey secretKey = this.generateSecretKey(key, salt);
        Cipher cipher = Cipher.getInstance(this.settings.algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));

        out.write(salt);
        out.write(iv);

        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            byte[] output = cipher.update(buffer, 0, bytesRead);
            if (output != null) {
                out.write(output);
            }
        }
        byte[] outputBytes = cipher.doFinal();
        if (outputBytes != null) {
            out.write(outputBytes);
        }
    }

    /**
     * Decrypt the data with the given key.
     *
     * @param key  The key to decrypt the data
     * @param data The data to decrypt
     * @return The decrypted data
     * @throws IOException                        If an I/O error occurs
     * @throws NoSuchAlgorithmException           If the algorithm is not supported
     * @throws InvalidKeySpecException            If the key specification is invalid
     * @throws NoSuchPaddingException             If the padding is not supported
     * @throws InvalidAlgorithmParameterException If the algorithm parameters are invalid
     * @throws InvalidKeyException                If the key is invalid
     * @throws IllegalBlockSizeException          If the block size is invalid
     * @throws BadPaddingException                If the padding is invalid
     */
    public byte[] decrypt(final byte[] key, final byte[] data) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        this.decrypt(key, in, out);
        return out.toByteArray();
    }

    /**
     * Read and decrypt the data from the input stream and write it to the output stream.
     *
     * @param key The key to decrypt the data
     * @param in  The input stream to read the data from
     * @param out The output stream to write the decrypted data to
     * @throws IOException                        If an I/O error occurs
     * @throws NoSuchAlgorithmException           If the algorithm is not supported
     * @throws InvalidKeySpecException            If the key specification is invalid
     * @throws NoSuchPaddingException             If the padding is not supported
     * @throws InvalidAlgorithmParameterException If the algorithm parameters are invalid
     * @throws InvalidKeyException                If the key is invalid
     * @throws IllegalBlockSizeException          If the block size is invalid
     * @throws BadPaddingException                If the padding is invalid
     */
    public void decrypt(final byte[] key, @WillNotClose final InputStream in, @WillNotClose final OutputStream out) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] salt = this.readFully(in, this.settings.saltLength);
        byte[] iv = this.readFully(in, this.settings.ivLength);

        SecretKey secretKey = this.generateSecretKey(key, salt);
        Cipher cipher = Cipher.getInstance(this.settings.algorithm);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            byte[] output = cipher.update(buffer, 0, bytesRead);
            if (output != null) {
                out.write(output);
            }
        }
        byte[] outputBytes = cipher.doFinal();
        if (outputBytes != null) {
            out.write(outputBytes);
        }
    }

    private SecretKey generateSecretKey(final byte[] key, final byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(this.settings.keyAlgorithm);
        char[] keyChars = new char[key.length];
        for (int i = 0; i < key.length; i++) {
            keyChars[i] = (char) (key[i] & 0xFF);
        }
        KeySpec spec = new PBEKeySpec(keyChars, salt, this.settings.iterations, this.settings.keyLength);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }

    private byte[] readFully(final InputStream in, final int length) throws IOException {
        byte[] buffer = new byte[length];
        int read = 0;
        while (read < length) {
            int bytesRead = in.read(buffer, read, length - read);
            if (bytesRead == -1) {
                throw new IOException("Unexpected end of stream");
            }
            read += bytesRead;
        }
        return buffer;
    }


    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Settings {
        @Builder.Default
        private final String algorithm = "AES/CBC/PKCS5Padding";
        @Builder.Default
        private final String keyAlgorithm = "PBKDF2WithHmacSHA1";
        @Builder.Default
        private final int keyLength = 256;
        @Builder.Default
        private final int iterations = 32767;
        @Builder.Default
        private final int saltLength = 8;
        @Builder.Default
        private final int ivLength = 16;
    }

}
