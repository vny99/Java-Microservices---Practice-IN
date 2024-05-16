package com.gateway.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.pattern.PathPatternParser;

import com.gateway.gateway.response.AuthenticationResponse;

import reactor.core.publisher.Mono;

@Component
public class CustomAuthenticationGatewayFilterFactory
        extends AbstractGatewayFilterFactory<CustomAuthenticationGatewayFilterFactory.Config> {

    private static final String WHITE_LISTED = "/auth/**";

    private static final String BASE_URL = "http://localhost:8083/account";

    @Autowired
    private WebClient.Builder webClientBuilder;

    public CustomAuthenticationGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (new PathPatternParser().parse(WHITE_LISTED).matches(request.getPath().pathWithinApplication())) {
                return chain.filter(exchange);
            }
            String token = null;
            if (request.getCookies().containsKey("token")) {
                token = request.getCookies().getFirst("token").getValue();
            }
            if (token == null) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            return Mono.justOrEmpty(token).flatMap(key -> {
                return isTokenValid(key).flatMap(isValid -> {
                    if (isValid) {
                        return Mono.justOrEmpty(key).flatMap(this::getSecurityContext)
                                .flatMap(authenticationResponse -> {
                                    exchange.getRequest().mutate()
                                            .header("X-Username", authenticationResponse.getUserName())
                                            .header("X-Authorities",
                                                    String.join(",", authenticationResponse.getAuthorities()))
                                            .build();
                                    return chain.filter(exchange);
                                }).switchIfEmpty(Mono.defer(() -> chain.filter(exchange))).onErrorResume(e -> {
                                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                                    return exchange.getResponse().setComplete();
                                });
                    }
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                });
            });
        };

    }

    public Mono<AuthenticationResponse> getSecurityContext(String token) {
        return webClientBuilder.build().get()
                .uri(BASE_URL,
                        uriBuilder -> uriBuilder.path("/getSecurityContext").queryParam("token", token).build())
                .cookie("token", token)
                .retrieve().bodyToMono(AuthenticationResponse.class);
    }

    public Mono<Boolean> isTokenValid(String token) {
        return webClientBuilder.build().get()
                .uri(BASE_URL,
                        uriBuilder -> uriBuilder.path("/isTokenValid").queryParam("token", token).build())
                .cookie("token", token)
                .retrieve().bodyToMono(Boolean.class);
    }

    public static class Config {
    }

}
