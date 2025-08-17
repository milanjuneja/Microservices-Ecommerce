package com.api_gateway.util;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JwtWebFilter implements WebFilter {
    
    private final JwtAuthFilter jwtAuthFilter;

    public JwtWebFilter(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return jwtAuthFilter.filter(exchange, new GatewayFilterChain() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange) {
                return chain.filter(exchange);
            }
        });
    }
}