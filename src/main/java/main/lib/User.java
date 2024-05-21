package main.lib;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.SecureRandom;
import java.security.spec.*;
import javax.crypto.spec.*;
import javax.crypto.*;

public class User {
    private final int id;
    private final String login;
    private final String hash;
    private final String salt;

    public User(ResultSet row) {
        try {
            id = row.getInt("user_id");
            login = row.getString("login");
            hash = row.getString("hash");
            salt = row.getString("salt");
        } catch (SQLException ex) {
            throw new RuntimeException("Creating object from database row failed: " + ex.getMessage());
        }
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getHash() {
        return hash;
    }

    public String getSalt() {
        return salt;
    }

    public static String computeHash(char[] password, String salt) {
        try {
            // Compute the PBKDF2 hash of the combined password and salt.
            KeySpec spec = new PBEKeySpec(password, salt.getBytes("US-ASCII"), 65536, 256);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = skf.generateSecret(spec).getEncoded();

            // Convert the 256-bit hash into a hexadecimal string of length 64.
            StringBuilder hashHex = new StringBuilder(2 * hash.length);
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1)
                    hashHex.append('0');
                hashHex.append(hex);
            }
            return hashHex.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate user hash: " + e);
        }
    }

    public static String generateSalt() {
        // The salt consits of 16 printable ASCII characters.
        final int SALT_LENGTH = 16;
        final String CHARSET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!\"#$%&\'()*+,-./:;<=>?@[\\]^_`{|}~ ";

        SecureRandom sr = new SecureRandom();
        String salt = "";
        for (int i = 0; i < SALT_LENGTH; i++)
        {   
            int index = sr.nextInt(CHARSET.length());
            salt += CHARSET.charAt(index);
        }
        return salt;
    }
}