package com.fernandocanabarro.fullstack_ecommerce_app.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class ProjectSecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
        http.csrf(csrf -> csrf.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                .ignoringRequestMatchers("/auth/**")
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
            .addFilterBefore(new CsrfCookieFilter(), BasicAuthenticationFilter.class) 
            .authorizeHttpRequests(requests -> requests
                .requestMatchers(HttpMethod.GET, "/admin/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/admin/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/carts/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/carts/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/wishlists/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/wishlists/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/my-account/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/addresses/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/addresses/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/credit-cards/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/credit-cards/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/orders/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/orders/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/newsletter-subscribe/**").authenticated()
                .anyRequest().permitAll())
            .formLogin(form -> form.loginPage("/login")
                .defaultSuccessUrl("/")
                .loginProcessingUrl("/login")
                .permitAll())
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .permitAll());
        return http.build();  
    }
}
