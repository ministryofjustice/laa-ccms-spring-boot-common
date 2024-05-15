# LAA CCMS Spring Boot Common

Provides 2 plugins that configure plugins and apply common build logic,
and a set of starters that provide individual pieces of common functionality.

## Plugins

### `laa-ccms-java-gradle-plugin` for Java Projects
  - apply Java plugin, and configure a Java toolchain.
  - apply Jacoco plugin, and configure sensible defaults.
  - apply Versions plugin, and configure the recommended versioning strategy.
  - apply Checkstyle plugin, and configure sensible defaults.

```groovy
plugins {
    id 'uk.gov.laa.ccms.laa-ccms-java-gradle-plugin' version '<latest>'
}
```

### `laa-ccms-spring-boot-gradle-plugin` for Java + Spring Boot projects
  - apply the LAA CCMS Java Gradle plugin
  - apply the SpringBoot Gradle plugin
  - configure dependency management for the common LAA CCMS Spring Boot components (starters & libraries)

```groovy
plugins {
    id 'uk.gov.laa.ccms.laa-ccms-spring-boot-gradle-plugin' version '<latest>'
}
```

## Starters

- Exception Handling (**TBC**)
- Entity Convertors (**TBC**)
- Auth (**TBC**)
