package uk.gov.laa.ccms.springboot.metrics.config;

import java.util.HashSet;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Configuration properties for customizing the metrics starter behavior and what endpoints
 * are exposed.
 *
 * <p>These properties are primarily used in conjunction with the
 * {@link MetricsSecurityConfig} class to control the Actuator endpoint exposure settings.</p>
 *
 * @author Jamie Briggs
 */
@ConfigurationProperties(prefix = "laa.ccms.metrics")
public record MetricsStarterProperties(
    @DefaultValue(value = "true")
    boolean healthExposed,
    @DefaultValue(value = "true")
    boolean infoExposed,
    @DefaultValue(value = "true")
    boolean metricsExposed,
    @DefaultValue(value = "true")
    boolean prometheusExposed,
    @DefaultValue(value = "false")
    boolean mappingsExposed,
    @DefaultValue(value = "false")
    boolean envExposed) {

  /**
   * Retrieves a set of exposed Actuator endpoints based on the current configuration properties.
   *
   * <p>The method checks the boolean flags for each endpoint type and includes those
   * configured as exposed in the returned set.</p>
   *
   * @return a set of strings representing the names of the exposed Actuator endpoints
   */
  public Set<String> getIncludes() {
    Set<String> includes = new HashSet<>();
    if (healthExposed) {
      includes.add("health");
    }
    if (infoExposed) {
      includes.add("info");
    }
    if (metricsExposed) {
      includes.add("metrics");
    }
    if (prometheusExposed) {
      includes.add("prometheus");
    }
    if (mappingsExposed) {
      includes.add("mappings");
    }
    if (envExposed) {
      includes.add("env");
    }
    return includes;
  }
}