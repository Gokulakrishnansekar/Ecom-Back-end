package com.gokul.ecom_website.config;

import com.gokul.ecom_website.service.AuthService;
import com.gokul.ecom_website.service.JWTService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class Config {
    @Autowired
    private AuthService authService;
    @Autowired
    private JWTService jwtService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(c->c.disable());
        http.authorizeHttpRequests(r->r.requestMatchers("/h2-console/**","/login/**","/dummy").permitAll()
                .anyRequest().authenticated());

               http.headers(h->h.frameOptions(f->f.disable()));
        http.cors(Customizer.withDefaults());//to alow cors request
    http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtService, UsernamePasswordAuthenticationFilter.class);
        http.sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

@Bean
public AuthenticationManager authenticationMangerfn(AuthenticationConfiguration configuration) throws Exception {
    return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
       provider.setPasswordEncoder(new BCryptPasswordEncoder());
       provider.setUserDetailsService(authService);
       return provider;
    }
}
