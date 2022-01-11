package com.dev.prepaid.util;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


import java.security.Key;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.dev.prepaid.model.invocation.InstanceContext;
import com.dev.prepaid.model.invocation.InvocationRequest;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Data
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = -2550185165626007488L;

	public static final long JWT_TOKEN_VALIDITY = 2 * 60 * 60;

	@Value("${security.jwt.token.secret-key}")
	private String secret;
	@Value("${security.jwt.token.key}")
	private String key;

	
//////////////////////////////////////////////// VALIDATING SIGNATURE //////////////////////////////////////////////////////
	
	//retrieve username from jwt token
	public String getSubjectFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
    //for retrieveing any information from token we will need the secret key
	private Claims getAllClaimsFromToken(String token) {
        byte[] base64secretByte     = Base64.encodeBase64(secret.getBytes());
        String base64secretString   = new String(base64secretByte);
		
		return Jwts.parserBuilder().setSigningKey(base64secretString).build().parseClaimsJws(token).getBody();
	}
	
//////////////////////////////////////////////// VALIDATING SIGNATURE //////////////////////////////////////////////////////
	
//////////////////////////////////////////////// VALIDATING EXPIRY //////////////////////////////////////////////////////
	
	//validate token
	public Boolean validateToken(String token) {
		return (!isTokenExpired(token));
	}
		
	//check if the token has expired
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	//retrieve expiration date from jwt token
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}
	
//////////////////////////////////////////////// VALIDATING EXPIRY //////////////////////////////////////////////////////	
	
	
	
	private Key getSigningKey() {        
		byte[] keyBytes = secret.getBytes();
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	//generate token APP -> AMS
	public String generateTokenAppToAms(InvocationRequest invocation, InstanceContext instantContext) {
			Map<String, Object> claims = new HashMap<>();
			return doGenerateTokenToAms(claims, invocation, instantContext);
	}
	private String doGenerateTokenToAms(Map<String, Object> claims, InvocationRequest invocation, InstanceContext instantContext) {
//		iss	The issuer of the token.					Set to the app's Token Key.
//		aud	The audience of the token.					Set to "AMS".
//		iat	The date and time the JWT was issued,		Set to the current time.
//		exp	The date and time the token will expire,	Set to the time the JWT should expire.
//		jti	The unique identifier for the JWT token.	Set to a random UUID.
			return Jwts.builder()
					.setClaims(claims)
					.setIssuer(key) // iss
					.setAudience("AMS") // aud
					.setIssuedAt(new Date(System.currentTimeMillis())) // iat
					.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000)) // exp
					.setId(GUIDUtil.generateGUID()) // jti Set to a random UUID.
					.signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
	}
	
	//generate token APP -> ExportProduct
	public String generateTokenExportProduct(InvocationRequest invocation, InstanceContext instantContext) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateTokenExportProduct(claims, invocation, instantContext);
	}	
	private String doGenerateTokenExportProduct(Map<String, Object> claims, InvocationRequest invocation, InstanceContext instantContext) {
//		iss	The issuer of the token.				Set to the app's UUID. or ams
//		aud	The audience of the token.				Set to the installed app's service instance UUID.
//		iat	The date and time the JWT was issued,	Set to the current time.
//		exp	The date and time the JWT will expire, 	Set to the time the JWT should expire.
//		o.a.p.ctenantId	The tenant Id.				Set to the id of the tenant as identified by the product.
		byte[] keyBytes = instantContext.getSecret().getBytes();
		Key signingKey = Keys.hmacShaKeyFor(keyBytes);
		return Jwts.builder()
				.setClaims(claims)
				.setIssuer("ams") //Set to the app's UUID.
				.setAudience(instantContext.getInstanceId()) //Set to the installed app's service instance UUID.
				.setIssuedAt(new Date(System.currentTimeMillis())) //iat
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000)) //exp
				.claim("o.a.p.ctenantId", instantContext.getTenantId()) //Set to the id of the tenant as identified by the product.
				.signWith(signingKey, SignatureAlgorithm.HS256).compact();
	}
	
	//generate token APP -> OnCompletionProduct or ProductImportEndpoint
	public String generateTokenProduct(InvocationRequest invocation, InstanceContext instantContext) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateTokenProduct(claims, invocation, instantContext);
	}
	private String doGenerateTokenProduct(Map<String, Object> claims, InvocationRequest invocation, InstanceContext instantContext) {
//		iss	The issuer of the token.				Set to the app's UUID.
//		sub	The subject of the token.				Set to the installed app's install UUID.
//		aud	The audience of the token.				Set to the installed app's service instance UUID.
//		iat	The date and time the JWT was issued,	Set to the current time.
//		exp	The date and time the JWT will expire, 	Set to the time the JWT should expire.
//		o.a.p.ctenantId	The tenant Id.				Set to the id of the tenant as identified by the product.
		byte[] keyBytes = instantContext.getSecret().getBytes();
		Key signingKey = Keys.hmacShaKeyFor(keyBytes);
		return Jwts.builder()
				.setClaims(claims)
				.setIssuer(instantContext.getAppId()) //Set to the app's UUID.
				.setSubject(instantContext.getInstallId()) //Set to the installed app's install UUID.
				.setAudience(instantContext.getInstanceId()) //Set to the installed app's service instance UUID.
				.setIssuedAt(new Date(System.currentTimeMillis())) //iat
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000)) //exp
				.claim("o.a.p.ctenantId", instantContext.getTenantId()) //Set to the id of the tenant as identified by the product.
				.signWith(signingKey, SignatureAlgorithm.HS256).compact();
	}

	//generate token for user
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userDetails.getUsername());
	}

	private String doGenerateToken(Map<String, Object> claims, String subject) {
		System.out.println("===="+subject);
		
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.claim("o.a.p.ctenantId", "607")
				.signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
	}
	
	

}
