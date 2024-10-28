package com.studentgroup.app;

import java.security.SecureRandom;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Misc {

    public static final int SALT_SIZE = 16;
    public static final int TOKEN_SIZE = 32;

    public static String genString(int len) {
        final String syms = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        SecureRandom r1;
        try {
            r1 = SecureRandom.getInstance("SHA1PRNG", "SUN");
        } catch (Exception e) {
            r1 = new SecureRandom();
            System.out.println("");
        }
        String res = "";
        for (int i = 0; i < len; i++) {
            res += syms.charAt(r1.nextInt(0, syms.length() - 1));
        }
        return res;
    }

    public static String genSalt() {
        return genString(SALT_SIZE);
    }

    public static String genToken() {
        return genString(TOKEN_SIZE);
    }

    public static String hashPassword(String password, String salt) throws NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance("SHA-512");
        sha.update(salt.getBytes());
        byte[] bytes = sha.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
