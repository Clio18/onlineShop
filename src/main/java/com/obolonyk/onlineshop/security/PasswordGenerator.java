package com.obolonyk.onlineshop.security;

import org.apache.commons.codec.digest.DigestUtils;

public class PasswordGenerator {

    public static String generateEncrypted(String password, String salt) {
        return DigestUtils.md5Hex(password + salt);
    }
}
