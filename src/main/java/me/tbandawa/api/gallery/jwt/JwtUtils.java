package me.tbandawa.api.gallery.jwt;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import me.tbandawa.api.gallery.daos.TokenDao;
import me.tbandawa.api.gallery.entities.UserToken;
import me.tbandawa.api.gallery.services.UserDetailsImpl;
import io.jsonwebtoken.*;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${gallery.jwtSecret}")
  private String jwtSecret;

  @Value("${gallery.jwtExpirationMs}")
  private int jwtExpirationMs;
  
  @Autowired
  private TokenDao tokenDao;

  public String generateJwtToken(Authentication authentication) {

    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

    return Jwts.builder()
        .setSubject((userPrincipal.getUsername()))
        .claim("user", userPrincipal.getId())
        .claim("roles", userPrincipal.getRoles().stream().map(role -> role.getName()).collect(Collectors.toList()))
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateJwtToken(String authToken) {
	  
	  final Claims claims = getAllClaimsFromToken(authToken);
		
		List<UserToken> tokenList = tokenDao.getToken(new Long((Integer)claims.get("user")));
		if (tokenList.size() == 0) {
			throw new AccessDeniedException("Invalid Verification.");
		}
		
		if (!tokenList.get(0).getToken().equals(authToken)) {
			throw new AccessDeniedException("Invalid Verification.");
		}
	  
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
      return true;
    } catch (SignatureException e) {
      logger.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }
  
  public String expireToken(String token) {
      final Claims claims = getAllClaimsFromToken(token);
      claims.setExpiration(new Date((new Date()).getTime() - jwtExpirationMs));
      return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
  }
  
  public Claims getAllClaimsFromToken(String token) {
      return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
  }
}
