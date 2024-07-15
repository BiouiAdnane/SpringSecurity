package org.example.securiteback.Security;

import java.security.SecureRandom;
import java.util.Base64;

public class JwtSecretGenerator {
    public static void main(String[] args) {
        // SecureRandom provides a cryptographically strong random number generator
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[64]; // 256-bit key
        secureRandom.nextBytes(key);

        // Encode the byte array to a Base64 string
        String jwtSecret = Base64.getEncoder().encodeToString(key);

        System.out.println("Generated JWT Secret: " + jwtSecret);
    }
}