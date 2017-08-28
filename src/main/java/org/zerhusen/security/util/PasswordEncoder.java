package org.zerhusen.security.util;

import java.security.KeyPair;

public class PasswordEncoder {

    private KeyPair keyPair;

    public PasswordEncoder() {
        try {
            keyPair = RSAHelper.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String encrypt(String text) throws Exception {
        return RSAHelper.encrypt(text, keyPair.getPublic());
    }

    public String decrypt(String text) throws Exception {
        return RSAHelper.decrypt(text, keyPair.getPrivate());
    }
}
