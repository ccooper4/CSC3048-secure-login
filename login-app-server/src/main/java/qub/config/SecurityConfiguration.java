package qub.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Java based configuration file for spring security.
 */
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            // Use HTTP Basic
            .httpBasic()
            .and()
                // Allow login and console
                .authorizeRequests()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/console/**").permitAll()
            .and()
                // Secure others
                .authorizeRequests()
                    .anyRequest()
                    .authenticated();

        // Needed to view h2 console
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }
}
