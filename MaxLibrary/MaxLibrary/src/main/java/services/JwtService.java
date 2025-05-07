package services;

import javax.crypto.SecretKey;

public interface JwtService {
    String generateToken(String email);
    SecretKey getKey(String key);
    String extractEmail(String token);
    boolean validateToken(String token);
}
