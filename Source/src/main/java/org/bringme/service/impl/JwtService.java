package org.bringme.service.impl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtService {

    private final String secretKey;

    public JwtService(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
    }


    public String generateToken(Long id, String emailOrPhone) {
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims()
                .add(claims)
                .id(Objects.requireNonNull(Long.toString(id)))
                .subject(emailOrPhone)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .and()
                .signWith(getKey())
                .compact();

    }

    /**
     * After adding username to the application, we can verify the token by username only without,
     * needing to use emailOrPhone
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String emailOrPhone = extractEmailOrPhone(token);
        return (emailOrPhone.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public Long extractUserIdAsLong(String token) {
        String out = extractClaim(token, Claims::getId);
        return Long.parseLong(out);
    }

    public String extractIdAsString(String token) {
        return extractClaim(token, Claims::getId);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parser()
                    .verifyWith(getKey())
                    .build().parseSignedClaims(token).getPayload();
        } catch (SignatureException e) {
            System.out.println("INVALID SIGNATURE\n" + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("INVALID TOKEN\n" + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("EXPIRED JWT TOKEN\n" + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT CLAIMS STRING IS EMPTY\n" + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("UNSUPPORTED JWT TOKEN\n" + e.getMessage());
        }
        return null;
    }

    public String extractEmailOrPhone(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());

    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
