package qub.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // Authenticate all requests using http basic authentication
        http
            .authorizeRequests()
                .anyRequest().authenticated()
                .and()
            .httpBasic();

        // Manually override login
        http
            .authorizeRequests()
            .antMatchers("/login")
                .permitAll();
    }
}
