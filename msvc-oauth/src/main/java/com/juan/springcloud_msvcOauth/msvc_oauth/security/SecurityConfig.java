package com.juan.springcloud_msvcOauth.msvc_oauth.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        // Configurador específico para el servidor de autorización
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                OAuth2AuthorizationServerConfigurer.authorizationServer();

        http
                // Define que esta configuración solo se aplica a los endpoints de OAuth
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(authorizationServerConfigurer, (authorizationServer) ->
                        authorizationServer
                                // Habilita OpenID Connect con configuración por defecto OpenID Connect (OIDC) es un protocolo de autenticación construido sobre OAuth 2.0 que permite:
                                //
                                //Verificación de identidad del usuario
                                //Obtención de información básica del perfil
                                //Inicio de sesión único (SSO) entre múltiples servicios
                                //
                                //Características clave:
                                //
                                //Agrega una capa de identidad a OAuth 2.0
                                //Usa tokens JWT para transportar información del usuario
                                //Permite autenticación descentralizada
                                //Más seguro que OAuth 2.0 standalone
                                //
                                //Principales componentes:
                                //
                                //ID Token: Contiene información del usuario
                                //UserInfo Endpoint: Recupera datos adicionales del perfil
                                //Scopes estándar: openid, profile, email
                                .oidc(Customizer.withDefaults())
                )
                // Configuración de autorización de solicitudes HTTP
                .authorizeHttpRequests((authorize) ->
                        authorize
                                // Requiere autenticación para cualquier solicitud
                                .anyRequest().authenticated()
                )
                // Manejo de excepciones de autenticación
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                );

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
            throws Exception {
        http
                // Configuración de autorización para solicitudes HTTP
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().authenticated()
                )
                //  // Configuración de autorización para solicitudes HTTP
                .formLogin(Customizer.withDefaults());
        // Construye y retorna la cadena de filtros de seguridad
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails userDetails = User.builder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password("password")
                .roles("USER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(userDetails, admin);
    }

    @Bean
    //CLIENTE: Aplicación que solicita autorización solo desarrollo
    public RegisteredClientRepository registeredClientRepository() {
        // Crea cliente registrado para OAuth
        RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
                // ID del cliente (gateway)
                .clientId("gateway")
                .clientSecret("{noop}secret")
                // Método de autenticación del cliente
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                // Tipos de concesión de autorización
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                //PROTEGEREMOS GATEWAY YA QUE ES LA ENTRADA DE TODOS
                .redirectUri("http://127.0.0.1:8090/login/oauth2/code/gateway")
                .redirectUri("http://127.0.0.1:8090/authorized")
                .postLogoutRedirectUri("http://127.0.0.1:8090/logout")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE) //MARCAR ABAJO COMO FALSE
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
                .build();
        // Devuelve repositorio de clientes en memoria
        return new InMemoryRegisteredClientRepository(oidcClient);
    }

    @Bean //genera 2 llaves para por medio de estas generar la firma del token
    public JWKSource<SecurityContext> jwkSource() {
        // Genera par de claves RSA
        KeyPair keyPair = generateRsaKey();
        // Obtiene claves pública y privada
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // Construye clave RSA con identificador único
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        // Crea conjunto de claves JWK
        JWKSet jwkSet = new JWKSet(rsaKey);
        // Retorna fuente de claves inmutable
        return new ImmutableJWKSet<>(jwkSet);
    }
    // Método privado para generar claves RSA
    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            // Genera par de claves RSA de 2048 bits
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        }
        catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

}