package com.tfg.pawhope.security;

import com.tfg.pawhope.model.Usuario;
import com.tfg.pawhope.repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UsuarioRepository usuarioRepository) {
        return username -> {
            Usuario usuario = usuarioRepository.findByCorreo(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
            return org.springframework.security.core.userdetails.User.withUsername(usuario.getCorreo())
                    .password(usuario.getContrasena()) // ya hasheada en BD
                    .roles("USER")
                    .build();
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/css/**", "/js/**", "/images/**",
                                "/login", "/registro",
                                "/web/animales",           // listado público
                                "/animal/**",              // detalles público si quieres
                                "/api/**"
                        ).permitAll()
                        // Deja accesible el detalle animal solo si el id es numérico (o usa otro patrón)
                        // Esto depende de cómo lo tengas configurado, si no, puedes requerir autenticación también aquí

                        // Protege registrar
                        .requestMatchers("/web/animales/registrar").authenticated()

                        // Para todo lo demás que no está declarado explícitamente, pide autenticación
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/perform_login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                        .usernameParameter("correo")
                        .passwordParameter("contrasena")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                );
        return http.build();
    }
}
