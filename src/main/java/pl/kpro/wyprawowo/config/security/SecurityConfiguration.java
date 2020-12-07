package pl.kpro.wyprawowo.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import pl.kpro.wyprawowo.security.JwtTokenProvider;
import pl.kpro.wyprawowo.data.service.UserService;

/**
 * @author Krzysztof 'impune_pl' Prorok <Krzysztof1397@gmail.com>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
    private final PasswordEncoder passwordEncoder;
    private final UserService userDetailsService;
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SecurityConfiguration(PasswordEncoder passwordEncoder, UserService userDetailsService, JwtTokenProvider jwtTokenProvider)
    {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        super.configure(auth);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected UserDetailsService userDetailsService()
    {
        return this.userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeRequests(authorize -> authorize
                .antMatchers("/api/hike").authenticated()
                .antMatchers("/api/hike/**").permitAll()
                .antMatchers("/api/video").authenticated()
                .antMatchers("/api/video/**").permitAll()
                .antMatchers("/api/image").authenticated()
                .antMatchers("/api/image/**").permitAll()
                .antMatchers("/api/logout").authenticated()
                .antMatchers("/api/login").permitAll()
                .antMatchers("/api/register").permitAll()
                .antMatchers("/**").permitAll()
            ).csrf(csrf->csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
            .apply(new JwtAuthenticationConfiguration(jwtTokenProvider));
    }
}
