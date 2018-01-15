package org.springframework.cloud.skipper.acceptance.core;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.util.AnnotationUtils;
import org.springframework.cloud.skipper.acceptance.core.DockerComposeManager.DockerComposeData;

import com.palantir.docker.compose.DockerComposeRule;

public class DockerComposeExtension
		implements BeforeAllCallback, BeforeEachCallback, AfterAllCallback, AfterEachCallback, ParameterResolver {

	private static final Namespace NAMESPACE = Namespace.create(DockerComposeExtension.class);
	
	@Override
	public void beforeAll(ExtensionContext extensionContext) throws Exception {
		DockerComposeManager dockerComposeManager = getDockerComposeManager(extensionContext);
		
		Class<?> testClass = extensionContext.getRequiredTestClass();
		Optional<DockerCompose> optional = AnnotationUtils.findAnnotation(testClass, DockerCompose.class);
		
		if (optional.isPresent()) {
			String classKey = extensionContext.getRequiredTestClass().toString();
			DockerCompose dockerCompose = optional.get();
			DockerComposeData dockerComposeData = new DockerComposeData(dockerCompose.locations(),
					dockerCompose.services(), dockerCompose.log());
			dockerComposeManager.addClassDockerComposeData(classKey, dockerComposeData);
		}		
	}

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		DockerComposeManager dockerComposeManager = getDockerComposeManager(context);
		
		String classKey = context.getRequiredTestClass().toString();
		String methodKey = context.getRequiredTestMethod().toString();
		Method testMethod = context.getRequiredTestMethod();
		Optional<DockerCompose> optional = AnnotationUtils.findAnnotation(testMethod, DockerCompose.class);
		
		if (optional.isPresent()) {
			DockerCompose dockerCompose = optional.get();
			DockerComposeData dockerComposeData = new DockerComposeData(dockerCompose.locations(),
					dockerCompose.services(), dockerCompose.log());
			dockerComposeManager.addMethodDockerComposeData(methodKey, dockerComposeData);			
		}
		
		dockerComposeManager.start(classKey, methodKey);
	}

	@Override
	public void afterEach(ExtensionContext context) throws Exception {
		DockerComposeManager dockerComposeManager = getDockerComposeManager(context);
		String classKey = context.getRequiredTestClass().toString();
		String methodKey = context.getRequiredTestMethod().toString();
		dockerComposeManager.stop(classKey, methodKey);
	}

	@Override
	public void afterAll(ExtensionContext context) throws Exception {
		DockerComposeManager dockerComposeManager = getDockerComposeManager(context);
	}
	
	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {
		return (parameterContext.getParameter().getType() == DockerComposeInfo.class);
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {
		String classKey = extensionContext.getRequiredTestClass().toString();
		String methodKey = extensionContext.getRequiredTestMethod().toString();
		DockerComposeManager dockerComposeManager = getDockerComposeManager(extensionContext);
		return new DefaultDockerComposeInfo(dockerComposeManager.get(classKey, methodKey));
	}
	
	private static DockerComposeManager getDockerComposeManager(ExtensionContext context) {
		Class<?> testClass = context.getRequiredTestClass();
		Store store = getStore(context);
		return store.getOrComputeIfAbsent(testClass, DockerComposeManager::new, DockerComposeManager.class);
	}
	
	private static Store getStore(ExtensionContext context) {
		return context.getRoot().getStore(NAMESPACE);
	}
	
	private static class DefaultDockerComposeInfo implements DockerComposeInfo {		
		private final DockerComposeRule rule;

		public DefaultDockerComposeInfo(DockerComposeRule rule) {
			this.rule = rule;
		}

		@Override
		public DockerComposeRule getRule() {
			return rule;
		}
	}
	
}
