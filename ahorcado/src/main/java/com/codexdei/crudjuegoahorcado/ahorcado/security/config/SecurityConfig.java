package com.codexdei.crudjuegoahorcado.ahorcado.security.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.codexdei.crudjuegoahorcado.ahorcado.security.filter.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationConfiguration authenticationConfiguration;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
            AuthenticationConfiguration authenticationConfiguration) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    /*
     * Define el algoritmo utilizado para almacenar
     * y verificar las contraseñas.
     */
    @Bean
    PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager() {

        /*
         * Obtiene el AuthenticationManager que Spring Security
         * construye a partir de la configuración de autenticación
         * de la aplicación.
         *
         * Este componente será el encargado de procesar las
         * solicitudes de autenticación, como el login con
         * username y password.
         */
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        /*
         * Define las reglas CORS que utilizará la aplicación.
         *
         * CORS controla desde qué orígenes puede el navegador
         * realizar peticiones hacia nuestro backend.
         */

        CorsConfiguration config = new CorsConfiguration();

        /*
         * Permite únicamente al frontend React que se ejecuta
         * en localhost:5173 realizar peticiones CORS.
         *
         * En producción debe sustituirse por el dominio real
         * del frontend.
         */

        config.setAllowedOrigins(List.of("http://localhost:5173"));

        /*
         * Define los métodos HTTP que el frontend puede utilizar.
         *
         * OPTIONS es necesario para las peticiones preflight
         * que realiza el navegador como parte del mecanismo CORS.
         */

        config.setAllowedMethods(List.of(

                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS"));

        /*
         * Define los headers que el frontend puede enviar.
         *
         * Authorization es necesario para enviar el JWT mediante:
         *
         * Authorization: Bearer <token>
         *
         * Content-Type permite indicar, por ejemplo,
         * que el cuerpo de la petición contiene JSON.
         */

        config.setAllowedHeaders(List.of("Authorization", "Content-type"));

        /*
         * Permite el envío de credenciales en peticiones CORS,
         * como cookies.
         *
         * Si la aplicación utiliza exclusivamente JWT mediante
         * el header Authorization y no utiliza cookies para
         * autenticación, esta opción puede no ser necesaria.
         */

        // config.setAllowCredentials(true);

        /*
         * Crea el origen de configuraciones CORS.
         *
         * Este objeto permite asociar diferentes configuraciones
         * CORS con diferentes rutas de la aplicación.
         */

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        /*
         * Aplica la configuración anterior a todas las rutas
         * de la aplicación.
         */

        source.registerCorsConfiguration("/**", config);

        return source;

    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                /*
                 * Habilita la integración de CORS
                 * con Spring Security.
                 */
                .cors(cors -> {
                })

                /*
                 * Deshabilitamos CSRF porque nuestra API
                 * utiliza JWT y no una sesión basada
                 * en cookies.
                 */

                .csrf(csrf -> csrf.disable())

                /*
                 * No utilizamos HttpSession para almacenar
                 * la autenticación.
                 *
                 * Cada petición protegida debe enviar
                 * su JWT.
                 */

                .sessionManagement(session -> session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))

                /*
                 * Define los endpoints públicos
                 * y protegidos.
                 */

                .authorizeHttpRequests(auth -> auth

                        /*
                         * Login y otros endpoints relacionados
                         * con autenticación.
                         */

                        .requestMatchers("/auth/**").permitAll()

                        /*
                         * Todo lo demás requiere
                         * autenticación.
                         */

                        .anyRequest()
                        .authenticated())

                /*
                 * Ejecutamos nuestro filtro JWT antes del
                 * filtro estándar de username/password.
                 */

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
