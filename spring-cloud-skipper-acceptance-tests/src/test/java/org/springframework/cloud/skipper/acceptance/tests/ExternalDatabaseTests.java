package org.springframework.cloud.skipper.acceptance.tests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.cloud.skipper.acceptance.core.DockerComposeExtension;

import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.connection.DockerPort;

@ExtendWith(DockerComposeExtension.class)
public class ExternalDatabaseTests {

	@Test
	public void testFoo(DockerComposeRule docker) {
		DockerPort container = docker.containers().container("postgres").port(5432);	
		assertThat(container).isNotNull();
	}
	
}
