package com.example.demo.common.security;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo.common.security.exception.SecurityErrorCode;
import com.example.demo.common.security.exception.TokenException;
import com.example.demo.common.security.exception.TokenExpiredException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class TokenProvider {
	private final Duration accessTokenValidityTime;

	private final Duration refreshTokenValidityTime;

	private final Key secretKey;

	private final String TOKEN_TYPE = "tokenType";

	public TokenProvider(@Value("${spring.jwt.secret}") String secret,
		@Value("${spring.jwt.access-token-validity-time}") Duration accessTokenValidityTime,
		@Value("${spring.jwt.refresh-token-validity-time}") Duration refreshTokenValidityTime) {
		this.accessTokenValidityTime = accessTokenValidityTime;
		this.refreshTokenValidityTime = refreshTokenValidityTime;
		this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
	}

	public String createAccessToken(Long customerId) {
		return createToken(customerId, "ACCESS", accessTokenValidityTime);
	}

	public String createRefreshToken(Long customerId) {
		return createToken(customerId, "REFRESH", refreshTokenValidityTime);
	}

	public String createNewAccessTokenFromRefreshToken(String refreshToken) {
		Claims claims = validateToken(JwtType.REFRESH, refreshToken).getBody();

		Long customerId = Long.parseLong((String)claims.get(Claims.SUBJECT));
		return createAccessToken(customerId);
	}

	private String createToken(Long customerId, String tokenType, Duration tokenValidityTime) {
		Instant now = Instant.now();
		Date currentDate = Date.from(now);
		Date expiredDate = Date.from(now.plus(tokenValidityTime));

		return Jwts.builder()
			.setHeader(Map.of("typ", "JWT"))
			.setSubject(String.valueOf(customerId))
			.setIssuedAt(currentDate)
			.setExpiration(expiredDate)
			.claim(TOKEN_TYPE, tokenType)
			.signWith(secretKey, SignatureAlgorithm.HS512)
			.compact();
	}

	public Jws<Claims> validateToken(JwtType jwtType, String token) {
		try {
			Jws<Claims> claimsJws = parseClaims(token);
			validateTokenType(claimsJws, jwtType);
			return claimsJws;
		} catch (ExpiredJwtException e) {
			throw new TokenExpiredException();
		} catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
			throw new TokenException(SecurityErrorCode.INVALID_TOKEN);
		}
	}

	private Jws<Claims> parseClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token);
	}

	private void validateTokenType(Jws<Claims> claimsJws, JwtType jwtType) {
		if (!jwtType.name().equals(TOKEN_TYPE)) {
			throw new TokenException(SecurityErrorCode.DISALLOWED_TOKEN_TYPE);
		}
	}
}
