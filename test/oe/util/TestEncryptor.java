package oe.util;

import org.junit.jupiter.api.Test;

import static oe.util.Encryptor.decrypt;
import static oe.util.Encryptor.encrypt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TestEncryptor {

    @Test
    void testEncryptAndDecrypt() {
        String key = "Bar12345Bar12345"; // 128 bit key
        String initVector = "RandomInitVector"; // 16 bytes IV

        String expected = "asdfghjkl;qwertyuiopzxcvbnm12334!@#<>?=_)((*";
        String encryptedStr = encrypt(key, initVector, expected);
        String decryptedStr = decrypt(key, initVector, encryptedStr);

        assertEquals(expected, decryptedStr);
        assertNotEquals(expected, encryptedStr);
    }
}
