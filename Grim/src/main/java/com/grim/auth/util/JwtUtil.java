package com.grim.auth.util;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {
	

		@Value("${jwt.secret}")
		private String secretKey;

		private SecretKey key;
		
		private long ACCESS_TOKEN_EXPIRED = 3600000L * 24; // 토근 유호기간 1일
		private long REFRESH_TOKEN_EXPIRED = 3600000L * 72; // 3일 리프레쉬 토큰은 DB에 따로 저장해야한다
		
		@PostConstruct 
		public void init() { 
			byte [] keyArr = Base64.getDecoder().decode(secretKey);
			this.key = Keys.hmacShaKeyFor(keyArr);
			
		}
		
		private Date buildExpirationDate(long date) {
			long now = System.currentTimeMillis();
			return new Date(now + date);
		}
		
		public String getAccessToken(String username) {
			
			return Jwts.builder()
					   .subject(username)     
					   .issuedAt(new Date())  
					   .expiration(buildExpirationDate(ACCESS_TOKEN_EXPIRED)) 
					   .signWith(key)
					   .compact();	
		}
		
		public String getRefreshToken(String username) {
			return Jwts.builder().subject(username)
					   .issuedAt(new Date())
					   .expiration(buildExpirationDate(REFRESH_TOKEN_EXPIRED))
					   .signWith(key)
					   .compact();
		}
		
		// 내가 만든 토큰이 맞니? 체크하는 메서드
		public Claims parseJwt(String token) {
			return Jwts.parser()
					   .verifyWith(key)
					   .build()
					   .parseSignedClaims(token)
					   .getPayload();
			
		}

}
