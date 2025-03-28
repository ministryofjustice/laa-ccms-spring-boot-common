package uk.gov.laa.ccms.springboot.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * Configuration of security filter chains to determine authentication behavior per endpoint
 * (group). See <a
 * href="https://docs.spring.io/spring-security/reference/servlet/configuration/java.html#jc-httpsecurity">HTTP
 * Security Configuration</a>.
 */
@Slf4j
@AutoConfiguration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@ComponentScan
@EnableConfigurationProperties(AuthenticationProperties.class)
public class SecurityFilterChainAutoConfiguration {

  private final ApiAuthenticationService apiAuthenticationService;

  private final ApiAuthenticationContextHolder apiAuthenticationContextHolder;

  private final ObjectMapper objectMapper;

  /**
   * Constructs a {@code SecurityFilterChainAutoConfiguration} to handle security configuration.
   *
   * @param apiAuthenticationService to construct a {@link ApiAuthenticationFilter}.
   * @param apiAuthenticationContextHolder holding client, role and URI details configured by
   *                                       the application.
   * @param objectMapper to construct components which manipulate a http response object.
   */
  @Autowired
  public SecurityFilterChainAutoConfiguration(
      ApiAuthenticationService apiAuthenticationService,
      ApiAuthenticationContextHolder apiAuthenticationContextHolder,
      ObjectMapper objectMapper) {
    this.apiAuthenticationService = apiAuthenticationService;
    this.apiAuthenticationContextHolder = apiAuthenticationContextHolder;
    this.objectMapper = objectMapper;
  }

  /**
   * First security filter chain to allow requests to unprotected URLs regardless of whether
   * authentication credentials have been provided.
   *
   * @param httpSecurity web based security configuration customizer
   * @return The {@link SecurityFilterChain} to continue with successive security filters.
   * @throws Exception -
   */
  @Bean
  @Order(1)
  public SecurityFilterChain filterUnprotectedUris(HttpSecurity httpSecurity) throws Exception {

    httpSecurity
        .securityMatcher(apiAuthenticationContextHolder.getUnprotectedUris())
        .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
        .csrf(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable)
        .sessionManagement(
            sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return httpSecurity.build();
  }

  /**
   * Second security filter chain to authenticate against endpoints based on roles configured in
   * application properties.
   *
   * @param httpSecurity web based security configuration customizer
   * @return The {@link SecurityFilterChain} to continue with successive security filters.
   * @throws Exception -
   */
  @Bean
  @Order(2)
  public SecurityFilterChain filterProtectedUris(HttpSecurity httpSecurity) throws Exception {

    httpSecurity.authorizeHttpRequests(
        customizer -> {
          for (AuthorizedRole authorizedRole :
              apiAuthenticationContextHolder.getAuthorizedRoles()) {
            customizer.requestMatchers(authorizedRole.uris()).hasRole(authorizedRole.name());
          }
          // Deny requests to any other endpoint by default
          customizer.anyRequest().denyAll();
        });

    ApiAuthenticationFilter apiAuthenticationFilter =
        new ApiAuthenticationFilter(apiAuthenticationService, objectMapper);

    httpSecurity
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable)
        .sessionManagement(
            sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(apiAuthenticationFilter, BasicAuthenticationFilter.class)
        .exceptionHandling(
            exceptionHandling ->
                exceptionHandling.accessDeniedHandler(new ApiAccessDeniedHandler(objectMapper)));

    return httpSecurity.build();
  }
}
