package uk.gov.laa.ccms.springboot.metrics.config;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties.Exposure;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for setting up security rules.
 *
 * <p>This class defines a security filter chain dedicated to securing Actuator endpoints.
 * The security configurations specified in this class are applied to all requests that match the
 * given URI pattern for Actuator endpoints.</p>
 *
 * @author Jamie Briggs
 */
@AutoConfiguration
// Only load configuration if actuator Endpoint class is present
@ConditionalOnClass(Endpoint.class)
@EnableConfigurationProperties(MetricsStarterProperties.class)
public class MetricsSecurityConfig {

  /**
   * Configures security settings for Actuator endpoints.
   *
   * <p>This method defines a SecurityFilterChain that applies specific security configurations
   * for all HTTP requests targeting the URI pattern "/actuator/**". It allows public access to
   * these endpoints and disables CSRF protection, making it suitable for non-browser clients.</p>
   *
   * @param http the HttpSecurity object used to configure security behaviors
   * @return a SecurityFilterChain that applies the defined security configurations
   * @throws Exception if an error occurs while building the security configuration
   */
  @Bean
  // Ensure that this security filter applies first
  @Order(1)
  SecurityFilterChain actuatorSecurity(HttpSecurity http) throws Exception {
    return http
        .securityMatcher("/actuator/**")
        .authorizeHttpRequests(
            // Makes endpoints public
            auth -> auth.anyRequest().permitAll())
        // Prevents Spring Security from saving the request and forcing authentication.
        .sessionManagement(session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // Allows access without authentication
        .anonymous(Customizer.withDefaults())
        .build();
  }

  /**
   * Configures and returns an instance of {@link WebEndpointProperties} with specific endpoint
   * exposure settings derived from the provided {@link MetricsStarterProperties}.
   *
   * @param metricsStarterProperties the configuration properties used to determine which
   *                                 Actuator endpoints should be exposed
   * @return a configured {@link WebEndpointProperties} instance with the specified
   *                                 exposure settings
   */
  @Bean
  @Primary
  public WebEndpointProperties webEndpointProperties(
      MetricsStarterProperties metricsStarterProperties) {
    WebEndpointProperties properties = new WebEndpointProperties();

    // Programmatically include specific actuator endpoints
    Exposure exposure = new Exposure();
    exposure.setInclude(metricsStarterProperties.getIncludes());

    properties.getExposure().setInclude(exposure.getInclude());
    return properties;
  }



}
