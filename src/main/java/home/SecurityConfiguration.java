package home;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {


    @Order(1)
    @Configuration
    public static class OAuth2SecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            DefaultBearerTokenResolver tokenResolver = new DefaultBearerTokenResolver();
            tokenResolver.setAllowFormEncodedBodyParameter(true);
            http.requestMatchers()
                    .antMatchers("/persons/**", "/associations/**", "/oauth2/offerings/**")
                    .and()
                    .authorizeRequests(authz -> authz
                            .antMatchers(HttpMethod.GET)
                            .hasAuthority("SCOPE_openid")
                            .anyRequest().authenticated())
                    .sessionManagement(sessionManagement ->
                            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .csrf().disable()
                    .oauth2ResourceServer().bearerTokenResolver(tokenResolver).opaqueToken();
        }

    }

    @Order
    @Configuration
    public static class BasicSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        @Value("${security.user.name}")
        private String user;

        @Value("${security.user.password}")
        private String password;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication().withUser(user).password(String.format("{noop}%s",password)).authorities("ROLE_USER");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher("/basic/offerings/**")
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .csrf().disable()
                    .addFilterBefore(new BasicAuthenticationFilter(authenticationManager()), BasicAuthenticationFilter.class)
                    .authorizeRequests()
                    .antMatchers("/**").hasRole("USER");
        }
    }
}
