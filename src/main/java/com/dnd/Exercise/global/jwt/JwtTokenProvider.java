package com.dnd.Exercise.global.jwt;

import com.dnd.Exercise.domain.auth.repository.RefreshTokenRepository;
import com.dnd.Exercise.domain.auth.service.CustomUserDetailsService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-valid-minute}")
    private long accessTokenValidTime;
    @Value("${jwt.refresh-token-valid-minute}")
    private long refreshTokenValidTime;

    private final CustomUserDetailsService userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        accessTokenValidTime = accessTokenValidTime * 60 * 1000L;
        refreshTokenValidTime = refreshTokenValidTime * 60 * 1000L;
    }

    public String createAccessToken(Long id) {
        Claims claims = Jwts.claims().setSubject(id.toString());
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createRefreshToken(Long id) {
        Claims claims = Jwts.claims().setSubject(id.toString());
        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        RefreshToken refreshToken = new RefreshToken(token, id);
        refreshTokenRepository.save(refreshToken);
        return refreshToken.getRefreshToken();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserId(token));
        return new UsernamePasswordAuthenticationToken(Long.parseLong(userDetails.getUsername()), null, AuthorityUtils.NO_AUTHORITIES);
    }

    public String getUserId(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveAccessToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null) {
            return null;
        }
        return authorization.substring("Bearer ".length());
    }

    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (SignatureException | MalformedJwtException e) {
            log.info("JWT 토큰이 유효하지 않습니다.");
            // TODO: 예외처리
        } catch (ExpiredJwtException e) {
            log.info("JWT 토큰이 만료되었습니다.");
            // TODO: 예외처리
        } catch (UnsupportedJwtException e) {
            log.info("지원하지 않는 JWT 토큰입니다.");
            // TODO: 예외처리
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}
