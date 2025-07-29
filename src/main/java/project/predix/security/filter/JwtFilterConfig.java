package project.predix.security.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import project.predix.auth.JwtProvider;

@Configuration
public class JwtFilterConfig {

    @Bean
    JwtAuthenticationFilter jwtFilter(JwtProvider provider, UserDetailsService userDetailsService){
        return new JwtAuthenticationFilter(provider, userDetailsService);
    }

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> disableServletRegistration(
            JwtAuthenticationFilter jwtFilter
    ){
        FilterRegistrationBean<JwtAuthenticationFilter> reg = new FilterRegistrationBean<>(jwtFilter);
        reg.setEnabled(false);
        return reg;
    }
}
