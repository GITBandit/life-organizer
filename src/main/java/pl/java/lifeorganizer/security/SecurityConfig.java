package pl.java.lifeorganizer.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity httpSecurity) {
        try {
            httpSecurity.
                    authorizeRequests()
                        .antMatchers("/").permitAll()
                        .antMatchers("/register").permitAll()
                        .antMatchers("/test").hasRole("USER")
                        .antMatchers("/profile").hasAnyRole("USER", "ADMIN")
                        .antMatchers("/password").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
            .and()
                .formLogin()
            ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder)  {
        try {
            authenticationManagerBuilder.jdbcAuthentication().dataSource(dataSource)
                    .usersByUsernameQuery("SELECT username, password, enabled FROM user WHERE username = ?")
                    .authoritiesByUsernameQuery("SELECT username, role FROM user_role WHERE username = ?")
                    .passwordEncoder(new BCryptPasswordEncoder())
            ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
