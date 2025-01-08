package com.juan.springcloud.gateway.app.api_gateway.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class SampleGlobalFilter implements GlobalFilter, Ordered {
    //sirve para ver lo que esta pasando en consola
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //el orden es muy importante para indicar que filtros correran primero

    @Override //exchange maneja request y response, chain es la cadena de filtros, mono es un objeto rectivo, flux son varios
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("Global filter executing PRE...");
        exchange.getRequest().mutate().headers(h -> h.add("token","5k4lel")); //el mutate simepre debe ir ya que el exange es inmutable, SE HCE PORQUE ESTAMOS TOCANDO REQUEST Y NO RESPONSE
        return chain.filter(exchange).then(Mono.fromRunnable(()->{
            logger.info("Global filter executing POST...");
            String token = exchange.getRequest().getHeaders().getFirst("token");
            logger.info("token " + token);

            Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("token")).ifPresent(t -> {
                logger.info("token " + t);
            });
            //Aparte de tirar logs podemos modificar la respuesta que dara usando el exchange
            exchange.getResponse().getCookies().add("color", ResponseCookie.from("color","red").build());
            exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
        })); //RECORDAR LO QUE ESTA EN EL EXCHANGE
    }

    @Override
    public int getOrder() {
        return 100;
    }
}
