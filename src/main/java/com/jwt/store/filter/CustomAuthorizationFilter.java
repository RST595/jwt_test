package com.jwt.store.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals("/user/login") || request.getServletPath().equals("/user/refresh")){
            filterChain.doFilter(request, response); //let go through to login page without authorization
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION); // "Authorization" - name of key for token
            //set "Bearer " before token.
            //Means no further authentication for user with this token because he is bearer of this token.
            //A security token with the property that any party in possession of the token (a "bearer") can use
            // the token in any way that any other party in possession of it can. Using a bearer token does not require
            // a bearer to prove possession of cryptographic key material (proof-of-possession).
            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
                //tell to spring this is user with email and authorities
                try{
                    String token = authorizationHeader.substring("Bearer ".length());
                    //secret word should be the same as in CustomAuthenticationFilter.successfulAuthentication();
                    Algorithm algorithm = com.auth0.jwt.algorithms.Algorithm.HMAC256("secretForEncryption".getBytes());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedToken = verifier.verify(token);
                    String email = decodedToken.getSubject();
                    //Claim word should be the same as in CustomAuthenticationFilter.successfulAuthentication();
                    String[] roles = decodedToken.getClaim("roles").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    stream(roles).forEach(role -> 
                        authorities.add(new SimpleGrantedAuthority(role))
                    );
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(email, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    filterChain.doFilter(request, response);
                } catch(Exception e){
                    log.error("Error logging in: {}", e.getMessage());
                    response.setHeader("error", e.getMessage());
                    response.setStatus(FORBIDDEN.value());
                    //response.sendError(FORBIDDEN.value());
                    Map<String, String> error = new HashMap<>(); //for tokens be represented in body, only for easy testing through postman
                    error.put("error_message", e.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }

            } else {
                filterChain.doFilter(request, response);
            }

        }
    }
}
