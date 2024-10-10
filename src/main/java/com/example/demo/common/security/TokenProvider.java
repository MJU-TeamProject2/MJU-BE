package com.example.demo.common.security;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.example.demo.common.security.exception.SecurityErrorCode;
import com.example.demo.common.security.exception.TokenException;
import com.example.demo.common.security.exception.TokenExpiredException;
import com.example.demo.common.util.Role;

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

	public TokenProvider(@Value("${spring.jwt.secret}") String secret,
		@Value("${spring.jwt.access-token-validity-time}") Duration accessTokenValidityTime,
		@Value("${spring.jwt.refresh-token-validity-time}") Duration refreshTokenValidityTime) {
		this.accessTokenValidityTime = accessTokenValidityTime;
		this.refreshTokenValidityTime = refreshTokenValidityTime;
		this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
	}

	public String createAccessToken(Long customerId, Role role) {
		return createToken(customerId, role, "ACCESS", accessTokenValidityTime);
	}

	public String createRefreshToken(Long customerId, Role role) {
		return createToken(customerId, role, "REFRESH", refreshTokenValidityTime);
	}

	public String createNewAccessTokenFromRefreshToken(String refreshToken) {
		Claims claims = validateToken(JwtType.REFRESH, refreshToken).getBody();

		Long customerId = Long.parseLong((String)claims.get(Claims.SUBJECT));
		Role role = Role.valueOf((String) claims.get(CustomClaims.ROLE));
		return createAccessToken(customerId, role);
	}

	private String createToken(Long customerId, Role role, String tokenType, Duration tokenValidityTime) {
		Instant now = Instant.now();
		Date currentDate = Date.from(now);
		Date expiredDate = Date.from(now.plus(tokenValidityTime));

		String TOKEN_TYPE = "tokenType";
		return Jwts.builder()
			.setHeader(Map.of("typ", "JWT"))
			.setSubject(String.valueOf(customerId))
			.setIssuedAt(currentDate)
			.setExpiration(expiredDate)
			.claim(CustomClaims.ROLE, role)
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
		String tokenType = String.valueOf(claimsJws.getBody().get(CustomClaims.TOKEN_TYPE));
		if (!jwtType.name().equals(tokenType)) {
			throw new TokenException(SecurityErrorCode.DISALLOWED_TOKEN_TYPE);
		}
	}

	public Authentication getCustomerIdFromToken(String token) {
		Claims claims = parseClaims(token).getBody();
		Long id = Long.parseLong(claims.get(Claims.SUBJECT).toString());
		Collection<? extends GrantedAuthority> authorities = getAuthorities(claims);
		return new JwtAuthenticationToken(id, authorities);
	}

	private Collection<? extends GrantedAuthority> getAuthorities(Claims claims) {
		String role = claims.get(CustomClaims.ROLE).toString();
		return List.of(new SimpleGrantedAuthority(role));
	}

}
