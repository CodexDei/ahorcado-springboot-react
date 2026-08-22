package com.codexdei.crudjuegoahorcado.ahorcado.security.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.codexdei.crudjuegoahorcado.ahorcado.security.jwt.JwtService;
import com.codexdei.crudjuegoahorcado.ahorcado.services.CustomUserDetailsService;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro encargado de autenticar las peticiones mediante un JWT.
 *
 * Se ejecuta una única vez por cada petición HTTP y verifica si existe un
 * token JWT válido en la cabecera Authorization.
 *
 * Si el token es válido:
 * 
 * 
 * <li>Obtiene el nombre del usuario almacenado en el JWT.</li>
 * <li>Carga el usuario desde la base de datos.</li>
 * <li>Valida nuevamente el token.</li>
 * <li>Crea un Authentication.</li>
 * <li>Lo almacena en el SecurityContext de Spring Security.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Si el token no existe, está expirado o es inválido,
 * simplemente continúa la cadena de filtros sin autenticar
 * al usuario.
 * </p>
 */

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Servicio encargado de todas las operaciones relacionadas
     * con el JWT (generación, validación y lectura).
     */
    private final JwtService jwtService;

    /**
     * Servicio encargado de cargar los datos del usuario
     * desde la base de datos.
     */
    private final CustomUserDetailsService userDetailsService;

    // inyeccion mediante constructor
    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Método ejecutado automáticamente por Spring Security
     * una vez por cada petición HTTP.
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        /*
         * Obtiene la cabecera Authorization enviada por el cliente.
         *
         * Ejemplo:
         *
         * Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
         */

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {

            /*
             * Obtiene el username almacenado dentro del JWT.
             */

            String username = jwtService.extractUsername(token);

            /*
             * Sólo autentica si:
             *
             * - Existe un username.
             * - El usuario aún no ha sido autenticado durante esta petición.
             */

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                /*
                 * Carga el usuario de la base de datos
                 */
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                /*
                 * Verifica:
                 *
                 * - Firma del JWT.
                 * - Fecha de expiración.
                 * - Correspondencia con el usuario.
                 */

                if (jwtService.isTokenValid(token, userDetails)) {

                    /*
                     * Crea el objeto Authentication que Spring Security
                     * utilizará durante toda la petición.
                     */
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            userDetails.getAuthorities());

                    /*
                     * Agrega información adicional de la petición
                     * (IP, sesión, etc.).
                     */
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    /*
                     * Crea un contexto de seguridad vacío.
                     */
                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                    securityContext.setAuthentication(authentication);

                    /*
                     * Guarda la autenticación dentro del contexto.
                     */

                    SecurityContextHolder.setContext(securityContext);

                }

            }

        } catch (JwtException e) {

            /*
             * Cualquier excepción relacionada con JWT
             * (token expirado, firma inválida, formato incorrecto, etc.)
             * provoca que la petición continúe sin autenticación.
             *
             * El acceso será posteriormente controlado por Spring Security
             * según la configuración de los endpoints protegidos.
             */
        }

        /*
         * Continúa con el resto de filtros de Spring Security.
         */
        filterChain.doFilter(request, response);

    }

}
