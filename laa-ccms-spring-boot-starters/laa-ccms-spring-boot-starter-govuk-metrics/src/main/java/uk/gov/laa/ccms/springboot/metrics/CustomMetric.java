package uk.gov.laa.ccms.springboot.dialect;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

/**
 * CustomMetric is a component responsible for managing and incrementing
 * a custom metric counter. This class utilizes the Micrometer library
 * to register and update metrics.
 *
 */
@Component
public class CustomMetric {
  private final Counter customMetricCounter;

  /**
   * Constructs a new instance of CustomMetric and initializes a custom metric counter.
   * The counter is registered with the provided MeterRegistry, configured with a specified
   * name, description, and tags.
   *
   * @param meterRegistry the MeterRegistry instance used to register the custom metric counter
   */
  public CustomMetric(MeterRegistry meterRegistry) {
    customMetricCounter = Counter.builder("custom_metric_name")
        .description("Description of custom metric")
        .tags("environment", "development")
        .register(meterRegistry);
  }

  /**
   * Increments the custom metric counter by one. This method updates the
   * value of the custom metric registered using the MeterRegistry. It is
   * typically used to record and track events or operations associated
   * with the custom metric.
   */
  public void incrementCustomMetric() {
    customMetricCounter.increment();
  }
}
