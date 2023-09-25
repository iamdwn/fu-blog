package tech.fublog.FuBlog.hash;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;

@Component
public class SHA256Hash implements Hashing{
    @Override
    public String hasdPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());
            StringBuilder hexHash = new StringBuilder();
            for (byte b : hashBytes) {
                hexHash.append(String.format("%02x", b));
            }
            return hexHash.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
