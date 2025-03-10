package com.juan.springcloud_msvcOauth.msvc_oauth.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.UUID;
import java.util.stream.Collectors;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

@Configuration
public class SecurityConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

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
                .csrf(csrf -> csrf.disable())
                .formLogin(Customizer.withDefaults());

        // Construye y retorna la cadena de filtros de seguridad
        return http.build();
    }
//    COMENTAMOS PARA USAR USUARIOOS DE BASE DE DATOS
//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails userDetails = User.builder()
//                .username("user")
//                .password("{noop}password")
//                .roles("USER")
//                .build();
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password("{noop}password")
//                .roles("USER", "ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(userDetails, admin);
//    }

    @Bean
    //CLIENTE: Aplicación que solicita autorización solo desarrollo
    public RegisteredClientRepository registeredClientRepository() {
        // Crea cliente registrado para OAuth
        RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
                // ID del cliente (gateway) SE PONE EN EL GATEWAY!!!!
                .clientId("gateway")
                .clientSecret(passwordEncoder.encode("secret")) //ENCRIPTO
//                .clientSecret("{noop}secret") //se pasa al yml de gatway
                // Método de autenticación del cliente
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                // Tipos de concesión de autorización
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                //PROTEGEREMOS GATEWAY YA QUE ES LA ENTRADA DE TODOS, estos datos van en el yml
                .redirectUri("http://127.0.0.1:8090/login/oauth2/code/client-app") //client-app ES EL NOMBRE DEL CLIENTE
                .redirectUri("http://127.0.0.1:8090/authorized") //ENDPOINTS 
                .postLogoutRedirectUri("http://127.0.0.1:8090/logout")
                .scope(OidcScopes.OPENID)//ROLES
//                .scope("write")
//                .scope("read")
                .scope(OidcScopes.PROFILE) //MARCAR ABAJO COMO FALSE
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
                //CONFIGURACION DEL TOKEN
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofHours(3)) //tiempo de vida del token
                        .refreshTokenTimeToLive(Duration.ofHours(6)) //Sirve para refrescar el acces token
                        .build())
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

    @Bean
    OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return context -> {
            //VERIFICAMOS QUE EL TOKEN SEA EL DE ACCESO, NO EL DE REFRESH
            if (context.getTokenType().getValue() == OAuth2TokenType.ACCESS_TOKEN.getValue()) {
                //oBTENGO EL USUARIO
                Authentication principal = context.getPrincipal();
                //A{adimos a los claims los roles HACIENDO EL CAST YA QUE VIENEN COMO grathed auth y necesito string
                context.getClaims().claim("roles", principal.getAuthorities()
                        .stream().
                        map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()));
            }
        };
    }

}