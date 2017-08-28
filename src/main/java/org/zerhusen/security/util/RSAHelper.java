package org.zerhusen.security.util;

import javax.crypto.*;
import java.security.*;
import java.util.Base64;

public class RSAHelper {

    private final static String RSA_ALGORITH = "RSA";
    private final static String UTF_8 = "UTF-8";

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(RSA_ALGORITH);

        generator.initialize(2048, new SecureRandom());

        return generator.generateKeyPair();
    }

    public static String encrypt(String text, PublicKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITH);

        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] cipherText = cipher.doFinal(text.getBytes(UTF_8));

        return Base64.getEncoder().encodeToString(cipherText);
    }

    public static String decrypt(String cipherText, PrivateKey key) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(cipherText);
        Cipher cipher = Cipher.getInstance(RSA_ALGORITH);

        cipher.init(Cipher.DECRYPT_MODE, key);

        return new String(cipher.doFinal(bytes), UTF_8);
    }

    public static void main(String[] args) {

        try {
            KeyPair keyPair = RSAHelper.generateKeyPair();
            String message = "PMS_P@ss";
            String encryptedText = RSAHelper.encrypt(message, keyPair.getPublic());

            System.out.println(encryptedText);

            String decryptedText = RSAHelper.decrypt(encryptedText, keyPair.getPrivate());

            System.out.println(decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
