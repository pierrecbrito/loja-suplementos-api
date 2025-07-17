package com.suplementos.lojasuplementosapi.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    @Autowired
    private AuthEntryPointJWT unauthorizedHandler;
    
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> 
                auth
                    // Endpoints públicos de autenticação
                    .requestMatchers("/api/v1/auth/**").permitAll()
                    
                    // Endpoints públicos - apenas operações GET (consulta)
                    .requestMatchers(HttpMethod.GET, "/api/v1/suplementos/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/v1/categorias/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/v1/avaliacoes/**").permitAll()
                    
                    // Swagger (se habilitado)
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                    
                    // Endpoints específicos do carrinho - apenas usuários autenticados
                    .requestMatchers("/api/carrinho/**").hasAnyRole("CLIENTE", "ADMIN")
                    
                    // Endpoints administrativos - apenas administradores
                    .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                    
                    // Operações de escrita em suplementos - apenas administradores
                    .requestMatchers(HttpMethod.POST, "/api/v1/suplementos/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/v1/suplementos/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/v1/suplementos/**").hasRole("ADMIN")
                    
                    // Operações de escrita em categorias - apenas administradores
                    .requestMatchers(HttpMethod.POST, "/api/v1/categorias/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/v1/categorias/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/v1/categorias/**").hasRole("ADMIN")
                    
                    // Operações de escrita em avaliações - usuários autenticados
                    .requestMatchers(HttpMethod.POST, "/api/v1/avaliacoes/**").hasAnyRole("CLIENTE", "ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/v1/avaliacoes/**").hasAnyRole("CLIENTE", "ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/v1/avaliacoes/**").hasAnyRole("CLIENTE", "ADMIN")
                    
                    // Endpoints de usuário - usuários autenticados
                    .requestMatchers("/api/v1/usuarios/**").hasAnyRole("CLIENTE", "ADMIN")
                    .requestMatchers("/api/v1/pedidos/**").hasAnyRole("CLIENTE", "ADMIN")
                    
                    .anyRequest().authenticated()
            );
        
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}