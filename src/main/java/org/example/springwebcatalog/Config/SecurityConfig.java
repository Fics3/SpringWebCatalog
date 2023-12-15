package org.example.springwebcatalog.Config;

import lombok.extern.slf4j.Slf4j;
import org.example.springwebcatalog.Services.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Slf4j
class SecurityConfig {

    private final UserManager userManager;

    public SecurityConfig(UserManager userManager) {
        this.userManager = userManager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        try {
            httpSecurity
                    .authorizeRequests(requests -> requests
                            .requestMatchers("/",
                                    "/registration",
                                    "/login",
                                    "/images/**",
                                    "/product/**",
                                    "/search/**",
                                    "/css/**")
                            .permitAll()
                            .anyRequest()
                            .authenticated())
                    .formLogin(form ->
                            form.loginPage("/login").permitAll())
                    .logout(LogoutConfigurer::permitAll);
            return httpSecurity.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userManager).passwordEncoder(new BCryptPasswordEncoder());
    }

}