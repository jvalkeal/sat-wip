package org.springframework.cloud.skipper.acceptance.core;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import com.palantir.docker.compose.DockerComposeRule;

import static com.palantir.docker.compose.connection.waiting.HealthChecks.toHaveAllPortsOpen;

public class DockerComposeExtension implements BeforeAllCallback, AfterAllCallback, ParameterResolver {

    private DockerComposeRule docker;

    public DockerComposeExtension() {
        docker = DockerComposeRule.builder()
                .pullOnStartup(true)
                .file("src/test/resources/postgres.yml")
                .saveLogsTo("target/test-docker-logs")
                .waitingForService("postgres", toHaveAllPortsOpen())
                .build();
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        docker.before();
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        docker.after();
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(DockerComposeRule.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return docker;
    }

}
