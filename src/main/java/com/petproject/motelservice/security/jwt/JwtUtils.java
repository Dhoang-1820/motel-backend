package com.petproject.motelservice.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.petproject.motelservice.security.services.UserDetailsImpl;
import com.petproject.motelservice.security.services.UserDetailsServiceImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

	private static final Log logger = LogFactory.getLog(JwtUtils.class);

	@Value("${app.jwtSecret}")
	private String jwtSecret;

	@Value("${app.jwtExpirationMs}")
	private long jwtExpirationMs;

	@Value("${app.jwtRefreshExpirationMs}")
	private long refreshTokenDurationMs;
	
	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;

	public String generateJwtToken(Authentication authentication) {
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return Jwts.builder().setSubject(authentication.getName()).claim("roles", roles).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(key(), SignatureAlgorithm.HS256).compact();
	}
	
	public String generateTokenFromUsername(String username) {
		UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());
	    return Jwts.builder().setSubject(username).claim("roles", roles).setIssuedAt(new Date())
	        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(key(), SignatureAlgorithm.HS512).compact();
	}

	public String doGenerateRefreshToken(String userName) {
		return Jwts.builder().setSubject(userName).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date((new Date()).getTime() + refreshTokenDurationMs))
				.signWith(key(), SignatureAlgorithm.HS256).compact();
	}

	private Key key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}

	public String getUserNameFromJwtToken(String token) {
		Claims claims = getClaimsFromToken(token);
		return claims.getSubject();
	}
	
	public Claims getClaimsFromToken(String token) {
		Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token); 
		return claimsJws.getBody();
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getRolresFromJwtToken(String token) {
		Claims claims = getClaimsFromToken(token);
		List<String> roles = (List<String>) claims.get("roles");
		return roles;	
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
			return true;
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}" + e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}" + e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}" + e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}" + e.getMessage());
		}

		return false;
	}
}
