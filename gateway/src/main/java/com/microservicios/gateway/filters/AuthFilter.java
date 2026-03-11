package com.microservicios.gateway.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicios.gateway.dtos.TokenDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Map;

@Component
@Slf4j
public class AuthFilter implements GatewayFilter {
    private final WebClient webClient;

    private static final String AUTH_VALIDATE_URI="http://localhost:4040/auth-server/auth/jwt";
    private static final String ACCES_TOKEN_HEADER_NAME = "accessToken";

    public AuthFilter(){
        this.webClient = WebClient.builder().build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
            return this.onError(exchange);
        }
        final  var tokenHeader = exchange
                .getRequest()
                .getHeaders()
                .get(HttpHeaders.AUTHORIZATION).get(0);
        final var chunks = tokenHeader.split(" ");

        if (chunks.length != 2  || !chunks[0].equals("Bearer")){
           return  this.onError(exchange);
        }

        final var token = chunks[1];

        System.out.println(ACCES_TOKEN_HEADER_NAME + token);
        return this.webClient
                .post()
                .uri(AUTH_VALIDATE_URI)
                .header(ACCES_TOKEN_HEADER_NAME, token)
                .retrieve()
                .bodyToMono(TokenDto.class)
                .map(response -> exchange)
                .flatMap(chain::filter);
    }


    private Mono<Void> onError(ServerWebExchange exchange)  {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");

        Map<String, Object> error = Map.of(
                "status", 401,
                "error", "No authorizado",
                "message", "Error en la autenticación"
        );

        ObjectMapper mapper = new ObjectMapper();
        byte[] bytes = null;
        try {
            bytes = mapper.writeValueAsBytes(error);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        var buffer = exchange.getResponse()
                .bufferFactory()
                .wrap(bytes);

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}
