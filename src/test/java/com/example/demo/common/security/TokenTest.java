package com.example.demo.common.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.demo.common.security.exception.TokenException;
import com.example.demo.common.security.exception.TokenExpiredException;
import com.example.demo.common.util.auth.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import org.springframework.security.core.Authentication;

class TokenTest {

  private TokenProvider tokenProvider;

  @Mock
  private JwtParser jwtParser;

  private static final String SECRET_KEY = "c3ByaW5nLWJvb3Qtc2VjdXJpdHktand0LXR1dG9yaWFsLWppd29vbi1zcHJpbmctYm9vdC1zZWN1cml0eS1qd3QtdHV0b3JpYWwK";

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    tokenProvider = new TokenProvider(
        SECRET_KEY,
        Duration.ofHours(2),
        Duration.ofDays(7)
    );
  }

  @Test
  void Access_토큰을_생성하고_유효성을_검증한다() {
    // given
    Long memberId = 1L;
    Role role = Role.CUSTOMER;
    Claims claims = mock(Claims.class);
    Jws<Claims> jws = mock(Jws.class);

    given(jwtParser.parseClaimsJws(anyString())).willReturn(jws);
    given(jws.getBody()).willReturn(claims);
    given(claims.get(CustomClaims.TOKEN_TYPE)).willReturn("ACCESS");
    given(claims.getSubject()).willReturn(memberId.toString());
    given(claims.get(CustomClaims.ROLE)).willReturn(role.name());

    // when
    String token = tokenProvider.createAccessToken(memberId, role);

    // then
    assertThat(token).isNotNull();
    Authentication authentication = tokenProvider.getCustomerIdFromToken(token);
    assertThat(authentication.getPrincipal()).isEqualTo(memberId);
    assertThat(authentication.getAuthorities())
        .anyMatch(authority -> authority.getAuthority().equals(role.name()));
  }

  @Test
  void Refresh_토큰을_생성하고_유효성을_검증한다() {
    // given
    Long memberId = 1L;
    Role role = Role.CUSTOMER;
    Claims claims = mock(Claims.class);
    Jws<Claims> jws = mock(Jws.class);

    given(jwtParser.parseClaimsJws(anyString())).willReturn(jws);
    given(jws.getBody()).willReturn(claims);
    given(claims.get(CustomClaims.TOKEN_TYPE)).willReturn("REFRESH");

    // when
    String token = tokenProvider.createRefreshToken(memberId, role);

    // then
    assertThat(token).isNotNull();
    tokenProvider.validateToken(JwtType.REFRESH, token);
  }

  @Test
  void 만료된_토큰인_경우_TokenExpiredException_에러를_던진다() {
    // given
    TokenProvider shortLivedTokenProvider = new TokenProvider(
        SECRET_KEY,
        Duration.ofMillis(1),
        Duration.ofMillis(1)
    );
    String expiredToken = shortLivedTokenProvider.createAccessToken(1L, Role.CUSTOMER);

    try {
      Thread.sleep(10);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    // when & then
    assertThrows(TokenExpiredException.class,
        () -> tokenProvider.validateToken(JwtType.ACCESS, expiredToken));
  }

  @Test
  void 유효하지_않은_토큰인_경우_TokenException_에러를_던진다() {
    // given
    String invalidToken = "invalid.token.string";
    given(jwtParser.parseClaimsJws(anyString()))
        .willThrow(new io.jsonwebtoken.security.SignatureException("Invalid token"));

    // when & then
    assertThrows(TokenException.class,
        () -> tokenProvider.validateToken(JwtType.ACCESS, invalidToken));
  }

  @Test
  void Refresh_토큰으로_새로운_Access_토큰을_생성한다() {
    // given
    Long memberId = 1L;
    Role role = Role.CUSTOMER;
    Claims claims = mock(Claims.class);
    Jws<Claims> jws = mock(Jws.class);

    given(jwtParser.parseClaimsJws(anyString())).willReturn(jws);
    given(jws.getBody()).willReturn(claims);
    given(claims.get(CustomClaims.TOKEN_TYPE)).willReturn("REFRESH");
    given(claims.getSubject()).willReturn(memberId.toString());
    given(claims.get(CustomClaims.ROLE)).willReturn(role.name());

    String refreshToken = tokenProvider.createRefreshToken(memberId, role);

    // when
    String newAccessToken = tokenProvider.createNewAccessTokenFromRefreshToken(refreshToken);

    // then
    assertThat(newAccessToken).isNotNull();
    Authentication authentication = tokenProvider.getCustomerIdFromToken(newAccessToken);
    assertThat(authentication.getPrincipal()).isEqualTo(memberId);
    assertThat(authentication.getAuthorities())
        .anyMatch(authority -> authority.getAuthority().equals(role.name()));
  }

  @Test
  void 토큰_타입이_일치하지_않는_경우_TokenException_에러를_던진다() {
    // given
    Claims claims = mock(Claims.class);
    Jws<Claims> jws = mock(Jws.class);

    given(jwtParser.parseClaimsJws(anyString())).willReturn(jws);
    given(jws.getBody()).willReturn(claims);
    given(claims.get(CustomClaims.TOKEN_TYPE)).willReturn("ACCESS");

    String accessToken = tokenProvider.createAccessToken(1L, Role.CUSTOMER);

    // when & then
    assertThrows(TokenException.class,
        () -> tokenProvider.validateToken(JwtType.REFRESH, accessToken));
  }
}