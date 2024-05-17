# LAA CCMS Spring Boot Common

Provides 2 plugins that configure plugins and apply common build logic,
and a set of starters that provide individual pieces of common functionality.

## Plugins

### `laa-ccms-java-gradle-plugin` for Java Projects
  - apply [Java](https://docs.gradle.org/current/userguide/java_plugin.html) plugin, and configure a Java toolchain.
  - apply [Jacoco](https://docs.gradle.org/current/userguide/jacoco_plugin.html) plugin, and configure sensible defaults.
  - apply [Versions](https://github.com/ben-manes/gradle-versions-plugin) plugin, and configure the recommended versioning strategy.
  - apply [Checkstyle](https://docs.gradle.org/current/userguide/checkstyle_plugin.html) plugin, and configure sensible defaults.
  - apply [Maven Publish](https://docs.gradle.org/current/userguide/publishing_maven.html) plugin
  - apply [Gradle Release](https://github.com/researchgate/gradle-release) plugin

```groovy
plugins {
    id 'uk.gov.laa.ccms.laa-ccms-java-gradle-plugin' version '<latest>'
}
```

### `laa-ccms-spring-boot-gradle-plugin` for Java + Spring Boot projects
  - apply the LAA CCMS Java Gradle plugin
  - apply the [SpringBoot](https://plugins.gradle.org/plugin/org.springframework.boot) plugin
  - apply the [Dependency Management](https://plugins.gradle.org/plugin/io.spring.dependency-management) plugin, and configure dependency management for the common LAA CCMS Spring Boot components (starters & libraries)

```groovy
plugins {
    id 'uk.gov.laa.ccms.laa-ccms-spring-boot-gradle-plugin' version '<latest>'
}
```

## Starters

- _**[TODO]**_ Exception Handling
- _**[TODO]**_ Entity Convertors
- _**[TODO]**_ Auth
