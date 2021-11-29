package sk.qpp.poc.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import sk.qpp.poc.user.CustomOAuth2UserService;
import sk.qpp.poc.user.CustomUserDetailsService;

/**
 * Configuration, which is mutually exclusive to its parent, {@link SecurityConfig}, configuration. This configuration
 * should be loaded only while <b>dev</b> profile is active. Configuration allows:
 * <ul>
 *     <li>h2-console to work (due its authentication mechanism),</li>
 *     <li>unauthorized access to all /actuator/** endpoints and</li>
 *     <li>unauthorized access to /api/** (because web authorization using headers (Bearer token) is non-trivial, HAL Browser issue)</li>
 * </ul>
 */
@Profile("dev")
@Configuration
public class SecurityConfigDevProfileExtension extends SecurityConfig {
    public SecurityConfigDevProfileExtension(CustomUserDetailsService customUserDetailsService, CustomOAuth2UserService customOAuth2UserService, OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler, OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler, HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository, TokenProvider tokenProvider) {
        super(customUserDetailsService, customOAuth2UserService, oAuth2AuthenticationSuccessHandler, oAuth2AuthenticationFailureHandler, httpCookieOAuth2AuthorizationRequestRepository, tokenProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/h2-console/**");
        super.configure(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(
                        "/api/**",
                        "/actuator/**",
                        "/h2-console/**")
                .permitAll();
        super.configure(http);
    }
}
