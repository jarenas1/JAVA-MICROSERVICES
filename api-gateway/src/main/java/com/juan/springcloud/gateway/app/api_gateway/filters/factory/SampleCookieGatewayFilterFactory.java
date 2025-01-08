package com.juan.springcloud.gateway.app.api_gateway.filters.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component   //LA CLASE SE DEBE PONER ASI, CON , YA QUE ESTA DENTRO DE ESTA MISMA CLASE
public class SampleCookieGatewayFilterFactory extends AbstractGatewayFilterFactory<SampleCookieGatewayFilterFactory.ConfigurationCookie> {

    private final Logger logger = LoggerFactory.getLogger(SampleCookieGatewayFilterFactory.class);
    //PASAMOS LA CONFIGURACION DE LA COOKIE, A LA CLASE PADRE
    public SampleCookieGatewayFilterFactory() {
        super(ConfigurationCookie.class);
    }
    @Override
    public GatewayFilter apply(ConfigurationCookie config) {
        return new OrderedGatewayFilter((exchange, chain) -> {
            //FILTROS PRE
            logger.info("fltro pre del gateway especifico " + config.getMessage());
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                //FILTROS POST
                Optional.ofNullable(config.value).ifPresent(cookie -> {
                    exchange.getResponse().addCookie(ResponseCookie.from(config.getName(),cookie).build());
                });
                logger.info("fltro post del gateway especifico " + config.getMessage());
            }));
        },100);
    }

    //ESTA CLASE SERA LA QUE AÑADURENOS COMO GENERICO EN LA CLASE ABSTRACTA QUE EXTENDIMOS ACA

    public static class ConfigurationCookie{
        private String name;
        private String value;
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
