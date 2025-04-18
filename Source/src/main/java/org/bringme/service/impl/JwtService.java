package org.bringme.service.impl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.bringme.exceptions.CustomException;
import org.bringme.exceptions.NullTokenGeneratedException;
import org.bringme.model.Person;
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

    /**
     * Generates a JWT token with the given person's information and email/phone.
     * The token contains claims such as the person's ID, role, and the subject (email or phone).
     * It also includes the issue time and an expiration time of 10 hours.
     *
     * @param personToInclude The {@link Person} object containing user information.
     * @param emailOrPhone    The email or phone of the person to be set as the subject of the token.
     * @return {@link String} The generated JWT token.
     */
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

    /**
     * Validates the provided JWT token by checking if the subject (email/phone) matches the user's username
     * and if the token has not expired.
     *
     * @param token       The JWT token to validate.
     * @param userDetails The {@link UserDetails} of the user to compare the token's subject with.
     * @return {@code true} if the token is valid; {@code false} otherwise.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String emailOrPhone = extractEmailOrPhone(token);
        return (emailOrPhone.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Retrieves the secret key used for signing the JWT tokens.
     *
     * @return {@link SecretKey} The key used to sign the token.
     */
    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * Extracts and returns the user ID from the JWT token as a {@link Long}.
     *
     * @param token The JWT token.
     * @return {@link Long} The user ID extracted from the token.
     */
    public Long extractUserIdAsLong(String token) {
        String out = extractClaim(token, Claims::getId);
        return Long.parseLong(out);
    }

    /**
     * Extracts and returns the user ID from the JWT token as a {@link String}.
     *
     * @param token The JWT token.
     * @return {@link String} The user ID extracted from the token.
     */
    public String extractIdAsString(String token) {
        return extractClaim(token, Claims::getId);
    }

    /**
     * Extracts a specific claim from the JWT token using the provided claim resolver.
     *
     * @param token         The JWT token.
     * @param claimResolver A function to extract the desired claim from the token's payload.
     * @param <T>           The type of the claim.
     * @return {@link T} The extracted claim.
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    /**
     * Extracts all claims from the JWT token and returns them as a {@link Claims} object.
     *
     * @param token The JWT token.
     * @return {@link Claims} The claims from the token.
     * @throws CustomException If there is an error during token processing, an exception is thrown with HTTP status
     *                         {@code 401 UNAUTHORIZED}.
     */
    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parser()
                    .verifyWith(getKey())
                    .build().parseSignedClaims(token).getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            String errorMessage = "JWT Processing error";
            if (e instanceof SignatureException) {
                errorMessage = "Invalid Signature";
            } else if (e instanceof MalformedJwtException) {
                errorMessage = "Invalid token";
            } else if (e instanceof ExpiredJwtException) {
                errorMessage = "Expired token";
            } else if (e instanceof UnsupportedJwtException) {
                errorMessage = "Unsupported token";
            } else if (e instanceof IllegalArgumentException) {
                errorMessage = "Claims are empty";
            }
            throw new JwtException(errorMessage);
        }
    }

    /**
     * Extracts the subject (email or phone) from the JWT token.
     *
     * @param token The JWT token.
     * @return {@link String} The subject (email or phone) of the token.
     */
    public String extractEmailOrPhone(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    /**
     * Checks whether the JWT token has expired.
     *
     * @param token The JWT token.
     * @return {@code true} if the token has expired; {@code false} otherwise.
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());

    }
    /**
     * Extracts the expiration date of the JWT token.
     *
     * @param token The JWT token.
     * @return {@link Date} The expiration date of the token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
