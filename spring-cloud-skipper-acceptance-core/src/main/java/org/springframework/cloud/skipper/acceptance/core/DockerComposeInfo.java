package org.springframework.cloud.skipper.acceptance.core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.palantir.docker.compose.DockerComposeRule;

public interface DockerComposeInfo {
	
	DockerComposeRule getRule();
}
