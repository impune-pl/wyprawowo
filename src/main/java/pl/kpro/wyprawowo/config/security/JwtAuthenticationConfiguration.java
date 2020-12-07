package pl.kpro.wyprawowo.config.security;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.kpro.wyprawowo.security.JwtAuthenticationFilter;
import pl.kpro.wyprawowo.security.JwtTokenProvider;

/**
 * @author Krzysztof 'impune_pl' Prorok <Krzysztof1397@gmail.com>
 */
public class JwtAuthenticationConfiguration extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>
{
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationConfiguration(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void configure(HttpSecurity http) {
        JwtAuthenticationFilter customFilter = new JwtAuthenticationFilter(jwtTokenProvider);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
