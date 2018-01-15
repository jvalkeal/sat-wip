package org.springframework.cloud.skipper.acceptance.core;

import static com.palantir.docker.compose.connection.waiting.HealthChecks.toHaveAllPortsOpen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.cloud.skipper.acceptance.core.DockerComposeManager.DockerComposeData;

import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.DockerComposeRule.Builder;
import com.palantir.docker.compose.ImmutableDockerComposeRule;
import com.palantir.docker.compose.configuration.DockerComposeFiles;

public class DockerComposeManager {
	
	private final Map<String, DockerComposeData> classLocations = new HashMap<>();
	private final Map<String, DockerComposeData> methodLocations = new HashMap<>();
	private final Map<String, DockerComposeRule> rules = new HashMap<>();

	public DockerComposeManager(Class<?> testClass) {
	}

	public void addClassDockerComposeData(String key, DockerComposeData dockerComposeData) {		
		classLocations.put(key, dockerComposeData);
	}

	public void addMethodDockerComposeData(String key, DockerComposeData dockerComposeData) {		
		methodLocations.put(key, dockerComposeData);
	}
		
	public List<String> getLocations(String classKey, String methodKey) {
		ArrayList<String> locations = new ArrayList<>();
		DockerComposeData dockerComposeData = classLocations.get(classKey);
		if (dockerComposeData != null) {
			locations.addAll(Arrays.asList(dockerComposeData.getLocations()));
		}
		dockerComposeData = methodLocations.get(methodKey);
		if (dockerComposeData != null) {
			locations.addAll(Arrays.asList(dockerComposeData.getLocations()));
		}
		return locations;
	}

	public List<String> getServices(String classKey, String methodKey) {
		ArrayList<String> services = new ArrayList<>();
		DockerComposeData dockerComposeData = classLocations.get(classKey);
		if (dockerComposeData != null) {
			services.addAll(Arrays.asList(dockerComposeData.getServices()));
		}
		dockerComposeData = methodLocations.get(methodKey);
		if (dockerComposeData != null) {
			services.addAll(Arrays.asList(dockerComposeData.getServices()));
		}
		return services;
	}
	
	public DockerComposeRule get(String classKey, String methodKey) {
		return rules.get(classKey + methodKey);
	}
	
	public void start(String classKey, String methodKey) {
		List<String> locations = getLocations(classKey, methodKey);
		Builder builder = DockerComposeRule.builder();
		builder.files(DockerComposeFiles.from(locations.toArray(new String[0])));
		for (String service : getServices(classKey, methodKey)) {
			builder.waitingForService(service, toHaveAllPortsOpen());
		}
		builder.saveLogsTo("build/test-docker-logs");
		DockerComposeRule rule = builder.build();
		try {
			rule.before();
		} catch (Exception e) {
			e.printStackTrace();
		}
		rules.put(classKey + methodKey, rule);
	}

	public void stop(String classKey, String methodKey) {
		DockerComposeRule rule = rules.remove(classKey + methodKey);
		if (rule != null) {
			rule.after();
		}
	}
	
	public static class DockerComposeData {

		private final String[] locations;
		private final String[] services;	
		private final String log;
		
		public DockerComposeData(String[] locations, String[] services, String log) {
			this.locations = locations;
			this.services = services;
			this.log = log;
		}

		public String[] getLocations() {
			return locations;
		}

		public String[] getServices() {
			return services;
		}

		public String getLog() {
			return log;
		}
	}		
}
