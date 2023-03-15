package com.example.familybudget.config;

import com.example.familybudget.entity.Permission;
import com.example.familybudget.security.JwtConfigurer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtConfigurer jwtConfigurer;

    private static final String SWAGGER = "/swagger-ui**/**";
    private static final String USER_ENDPOINT = "/api/v*/user/**";
    private static final String ADMIN_ENDPOINT = "/api/v*/admin/**";
    private static final String LOGIN_ENDPOINT = "/api/v*/auth/**";
    private static final String ACTIVATE_ENDPOINT = "/api/v*/auth/activate/*";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(SWAGGER).permitAll()
                .antMatchers(ACTIVATE_ENDPOINT).permitAll()
                .antMatchers(LOGIN_ENDPOINT).permitAll()
                .antMatchers(USER_ENDPOINT).hasAnyAuthority(Permission.LEVEL_LOW.getPermission(), Permission.LEVEL_MIDDLE.getPermission(), Permission.LEVEL_HIGH.getPermission())
                .antMatchers(ADMIN_ENDPOINT).hasAnyAuthority(Permission.LEVEL_MIDDLE.getPermission(), Permission.LEVEL_HIGH.getPermission())
                .anyRequest().authenticated()
                .and()
                .apply(jwtConfigurer);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
