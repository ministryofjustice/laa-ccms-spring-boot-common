package uk.gov.laa.ccms.springboot.auth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.assertj.AssertableApplicationContext;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class OpenApiConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withBean(OpenApiConfiguration.class)
            .withPropertyValues("laa.ccms.springboot.starter.auth.authentication-header=Authorization");

    private final String OPEN_API_CONFIGURATION_BEAN = "openApiConfiguration";
    private final String OPEN_API_BEAN = "openAPI";
    private final String SECURITY_SCHEME_NAME = "ApiKeyAuth";

    @Test
    void testOpenApiBeanIsCreatedWhenApplicationPropertyOmitted() {
        contextRunner.run((context) -> {
            assertThat(context).hasBean(OPEN_API_CONFIGURATION_BEAN);
            assertSecuritySchemeApplied(context);
        });
    }

    @Test
    void testOpenApiBeanIsCreatedWhenApplicationPropertyEnabled() {
        contextRunner.withPropertyValues("laa.ccms.springboot.starter.open-api.security-scheme.enabled=true").run((context) -> {
            assertThat(context).hasBean(OPEN_API_CONFIGURATION_BEAN);
            assertSecuritySchemeApplied(context);
        });
    }

    @Test
    void testNoOpenApiBeanIsCreatedWhenApplicationPropertyDisabled() {
        contextRunner.withPropertyValues("laa.ccms.springboot.starter.open-api.security-scheme.enabled=false").run((context) -> {
            assertThat(context).doesNotHaveBean(OPEN_API_CONFIGURATION_BEAN);
            assertThat(context).doesNotHaveBean(OPEN_API_BEAN);
        });
    }

    private void assertSecuritySchemeApplied(AssertableApplicationContext context) {
        OpenAPI openApiSpec = context.getBean(OPEN_API_BEAN, OpenAPI.class);
        assertThat(openApiSpec.getComponents().getSecuritySchemes()).isEqualTo(Map.of(SECURITY_SCHEME_NAME, getSecurityScheme()));
    }

    private SecurityScheme getSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");
    }

}
