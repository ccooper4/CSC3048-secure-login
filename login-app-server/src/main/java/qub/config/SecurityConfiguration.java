package qub.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

/**
 * Java based configuration file for spring security.
 */
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder authentication) throws Exception {

        String usersQuery = "SELECT LOGIN_ID, PASSWORD, 'true' FROM USER WHERE LOGIN_ID=?";
        String authoritiesQuery = "SELECT LOGIN_ID, DTYPE from USER where LOGIN_ID=?";

        authentication.jdbcAuthentication()
                        .dataSource(dataSource)
                        .usersByUsernameQuery(usersQuery)
                        .authoritiesByUsernameQuery(authoritiesQuery);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            // Use HTTP Basic
            .httpBasic()
            .and()
                // Allow login / logout by default + console
                .authorizeRequests()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/console/**").permitAll()
                .antMatchers("/signOut/**").permitAll()
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
