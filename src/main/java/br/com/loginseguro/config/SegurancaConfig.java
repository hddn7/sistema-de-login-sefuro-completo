package br.com.loginseguro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SegurancaConfig {

    @Bean
    public SecurityFilterChain filtroDeSeguranca(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(autorizacao -> autorizacao
                        .requestMatchers("/", "/login", "/cadastro", "/css/**").permitAll()
                        .requestMatchers("/admin", "/admin/**").hasRole("ADMIN")
                        .requestMatchers("/professor", "/professor/**").hasRole("PROFESSOR")
                        .requestMatchers("/aluno", "/aluno/**").hasRole("ALUNO")
                        .anyRequest().authenticated())
                .formLogin(formulario -> formulario
                        .loginPage("/login")
                        .defaultSuccessUrl("/painel", true)
                        .failureUrl("/login?erro")
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?saiu")
                        .invalidateHttpSession(true)
                        .deleteCookies("SESSION")
                        .permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
