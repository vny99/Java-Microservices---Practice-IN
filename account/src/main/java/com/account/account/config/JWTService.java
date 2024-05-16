package com.account.account.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.account.account.builder.AuthenticationBuider;
import com.account.account.response.AuthenticationResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

@Service
public class JWTService {
   private static String SECRET_KEY = "3F4428472B4B6250655368566D597133743677397A244326452948404D635166";

   @Autowired
   private ReactiveUserDetailsServiceImpl userDetailsService;

   public String extractUser(String token) {
      return extractClaim(token, Claims::getSubject);
   }

   public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
      final Claims claims = extractAllClaims(token);
      return claimsResolver.apply(claims);
   }

   public Claims extractAllClaims(String token) {
      return getJwtParserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
   }

   private JwtParserBuilder getJwtParserBuilder() {
      return Jwts.parserBuilder();
   }

   private Key getSigningKey() {
      return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
   }

   public String generateToken(Map<String, Object> claims, UserDetails userDetails) {

      return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 3600000)).signWith(getSigningKey()).compact();
   }

   public String generateToken(UserDetails userDetails) {
      return generateToken(new HashMap<>(), userDetails);
   }

   public boolean isTokenValid(String token, UserDetails userDetails) {
      final String username = extractUser(token);
      return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
   }

   public boolean isTokenValid(String token) {
      return !isTokenExpired(token);
   }

   private boolean isTokenExpired(String token) {
      return extractExpiration(token).before(new Date());
   }

   private Date extractExpiration(String token) {
      return extractClaim(token, Claims::getExpiration);
   }

   public Mono<AuthenticationResponse> getSecurityContext(String token) {
      if (isTokenValid(token)) {
         String username = extractUser(token);
         if (username != null) {
            return ReactiveSecurityContextHolder.getContext().map(securityContext -> {
               Authentication authentication = securityContext.getAuthentication();
               User user = (User) authentication.getPrincipal();
              if( authentication !=null && user.getUsername().equals(username)) {
                 return AuthenticationBuider.from(username, authentication.getAuthorities()).build();
              }
            return null;
            });
         }
      }
      return Mono.empty();
   }

}
