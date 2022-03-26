package ir.alirezaalijani.security.authorization.service.security.config.oauth;

import ir.alirezaalijani.security.authorization.service.security.config.AttemptFilterConfig;
import ir.alirezaalijani.security.authorization.service.security.config.RecaptchaFilterConfig;
import ir.alirezaalijani.security.authorization.service.security.config.SecurityBeanConfigs;
import ir.alirezaalijani.security.authorization.service.security.util.HttpUtil;
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
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
public class WebSecurityConfig {

    protected static final String LOGIN_PGE = "/auth/login";
    public static final String LOGIN_PROCESSING_Url = "/auth/login";
    protected static final String LOGIN_FAILURE_URL = "/auth/login?error=true";

    private final AttemptFilterConfig attemptFilterConfig;
    private final RecaptchaFilterConfig recaptchaFilterConfig;
    private final RememberMeServices rememberMeServices;

    public WebSecurityConfig(UserDetailsService userDetailsService,
                             PasswordEncoder passwordEncoder,
                             AttemptFilterConfig attemptFilterConfig,
                             AuthenticationManagerBuilder authenticationManagerBuilder,
                             RecaptchaFilterConfig recaptchaFilterConfig,
                             RememberMeServices rememberMeServices) throws Exception {

        this.attemptFilterConfig = attemptFilterConfig;
        this.recaptchaFilterConfig = recaptchaFilterConfig;
        this.rememberMeServices = rememberMeServices;
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    private AuthenticationEntryPoint authenticationEntryPoint() {
        return ((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: UNAUTHORIZED"));
    }

    private AccessDeniedHandler accessDeniedHandler() {
        return ((request, response, accessDeniedException) -> response.sendError(HttpServletResponse.SC_FORBIDDEN, "Error: FORBIDDEN"));
    }

    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) ->{
            response.setHeader("Location", HttpUtil.getForwardedHost(request).concat("/home"));
            response.setStatus(302);
        };
    }

    private AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, authentication) ->{
            response.setHeader("Location", HttpUtil.getForwardedHost(request).concat(LOGIN_FAILURE_URL));
            response.setStatus(302);
        };
    }

    private LogoutSuccessHandler logoutSuccessHandler(){
        return (request, response, authentication) -> {
            response.setHeader("Location", HttpUtil.getForwardedHost(request).concat(LOGIN_PGE));
            response.setStatus(302);
        };
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
//                        .failureUrl(LOGIN_FAILURE_URL)
//                                .successHandler(authenticationSuccessHandler())
                                .failureHandler(authenticationFailureHandler())
                )
                // logout config
                .logout()
                .logoutUrl("/auth/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", HttpMethod.GET.name()))
//                .logoutSuccessUrl(LOGIN_PGE)
                .logoutSuccessHandler(logoutSuccessHandler())
                .and()
                // remember me config
                .rememberMe(remember -> remember.rememberMeParameter("remember-me")
                        .rememberMeServices(this.rememberMeServices)
                        .tokenValiditySeconds(86400));

        // custom filters config
        http.addFilterBefore(attemptFilterConfig, SecurityContextPersistenceFilter.class);
        http.addFilterAfter(recaptchaFilterConfig, AttemptFilterConfig.class);

        return http.build();
    }

}
