package com.account.account.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.pattern.PathPatternParser;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter implements WebFilter {
    @Autowired
    private JWTService jwtService;

    @Autowired
    private ReactiveUserDetailsServiceImpl userDetailsService;

    private static final String WHITE_LISTED = "/auth/**";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String token = null;

        if (request.getCookies().containsKey("token")) {
            token = request.getCookies().getFirst("token").getValue();
        }

        if (new PathPatternParser().parse(WHITE_LISTED).matches(request.getPath().pathWithinApplication())) {
            return chain.filter(exchange);
        }

        if (token != null && !token.isEmpty()) {
            String username = jwtService.extractUser(token);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                return userDetailsService.findByUsername(username).flatMap(userDetails -> {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                    SecurityContext securityContext = new SecurityContextImpl(usernamePasswordAuthenticationToken);
                    return chain.filter(exchange).contextWrite(
                            ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
                });
            }
        }
        return chain.filter(exchange);
    }

}
