package oe.util;

import oe.ui.LoginController;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryptor {
    private static Logger log = Logger.getLogger(LoginController.class.getName());
    private static String key = "OeEncryptKey9792"; // 128 bit key
    private static String initVector = "OeEncryptionInit"; // 16 bytes IV

    @Nullable
    public static String encrypt(String value) {
        return encrypt(key, initVector, value);
    }

    @Nullable
    public static String encrypt(String key, String initVector, String value) {

        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());

            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            log.error(ex);
        }

        return null;
    }


    @Nullable
    public static String decrypt(String encrypted) {
        return decrypt(key, initVector, encrypted);
    }

    @Nullable
    public static String decrypt(String key, String initVector, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            return new String(original);
        } catch (Exception ex) {
            log.error(ex);
        }

        return null;
    }
}