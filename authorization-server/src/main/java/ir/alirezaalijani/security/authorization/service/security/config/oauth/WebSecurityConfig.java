package ir.alirezaalijani.security.authorization.service.security.config.oauth;

import ir.alirezaalijani.security.authorization.service.security.config.AttemptFilterConfig;
import ir.alirezaalijani.security.authorization.service.security.config.RecaptchaFilterConfig;
import ir.alirezaalijani.security.authorization.service.security.config.SecurityBeanConfigs;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@EnableWebSecurity
public class WebSecurityConfig {

    protected static final String LOGIN_PGE = "/auth/login";
    protected static final String LOGIN_PROCESSING_Url = "/auth/login";
    protected static final String LOGIN_FAILURE_URL = "/auth/login?error=true";

    private final UserDetailsService userDetailsService;
    private final AttemptFilterConfig attemptFilterConfig;
    private final RecaptchaFilterConfig recaptchaFilterConfig;

    public WebSecurityConfig(UserDetailsService userDetailsService,
                             PasswordEncoder passwordEncoder,
                             AttemptFilterConfig attemptFilterConfig,
                             AuthenticationManagerBuilder authenticationManagerBuilder,
                             RecaptchaFilterConfig recaptchaFilterConfig) throws Exception {

        this.attemptFilterConfig = attemptFilterConfig;
        this.recaptchaFilterConfig = recaptchaFilterConfig;
        this.userDetailsService= userDetailsService;
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    private AuthenticationEntryPoint authenticationEntryPoint() {
        return ((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: UNAUTHORIZED"));
    }

    private AccessDeniedHandler accessDeniedHandler() {
        return ((request, response, accessDeniedException) -> response.sendError(HttpServletResponse.SC_FORBIDDEN, "Error: FORBIDDEN"));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler())
                .and()
                // session config
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and()
                // requests config
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .antMatchers(SecurityBeanConfigs.publicPaths).permitAll()
                        .antMatchers(SecurityBeanConfigs.registerPaths).permitAll()
                        .antMatchers("/api/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                // login config
                .formLogin(form -> form
                        .loginPage(LOGIN_PGE)
                        .loginProcessingUrl(LOGIN_PROCESSING_Url)
                        .usernameParameter("uname")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/home")
                        .failureUrl(LOGIN_FAILURE_URL)
                )
                // logout config
                .logout()
                .logoutUrl("/auth/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", HttpMethod.GET.name()))
                .logoutSuccessUrl(LOGIN_PGE)
                .and()
                .rememberMe(remember -> remember.key("aslfdGGHsfk8LMKLLKldc65")
                        .rememberMeParameter("remember-me")
                        .userDetailsService(this.userDetailsService)
                        .tokenValiditySeconds(86400));

        // custom filters config
        http.addFilterBefore(attemptFilterConfig, SecurityContextPersistenceFilter.class);
        http.addFilterAfter(recaptchaFilterConfig, AttemptFilterConfig.class);

        return http.build();
    }

}
