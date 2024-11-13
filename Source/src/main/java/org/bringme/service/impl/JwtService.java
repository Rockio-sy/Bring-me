package org.bringme.service.impl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.bringme.model.Person;
import org.bringme.service.exceptions.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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


    public String generateToken(Person personToInclude, String emailOrPhone) {
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims()
                .add(claims)
                .id(Objects.requireNonNull(Long.toString(personToInclude.getId())))
                .subject(emailOrPhone)
                .add("role", personToInclude.getRole())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .and()
                .signWith(getKey())
                .compact();

    }

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
            throw new SignatureException("Invalid Signature");
        } catch (MalformedJwtException e) {
            throw new MalformedJwtException("Invalid token");
        } catch (ExpiredJwtException e) {
            throw new CustomException("Expired token", HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Claims are empty");
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtException("Unsupported token");
        }
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
