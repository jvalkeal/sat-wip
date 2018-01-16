package org.springframework.cloud.skipper.acceptance.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.cloud.skipper.acceptance.core.DockerCompose;
import org.springframework.cloud.skipper.acceptance.core.DockerComposeInfo;
import org.springframework.web.client.RestTemplate;

import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.connection.DockerPort;

import static com.jayway.awaitility.Awaitility.with;

public class ExternalDatabaseTests {

	@Test
	@DockerCompose(locations = { "src/test/resources/skipper-postgres.yml" }, services = { "postgres", "skipper" })
	public void testSkipperWithPostgres(DockerComposeInfo dockerComposeInfo) throws Exception {
		DockerComposeRule docker = dockerComposeInfo.getRule();
		DockerPort container1 = docker.containers().container("postgres").port(5432);	
		assertThat(container1).isNotNull();
		DockerPort container2 = docker.containers().container("skipper").port(7577);	
		assertThat(container2).isNotNull();
				
		String url = "http://" + container2.getIp() + ":" + container2.getExternalPort() + "/api/about";
		RestTemplate template = new RestTemplate();
		
		with()
			.pollInterval(1, TimeUnit.SECONDS)
			.and()
		.await()
			.ignoreExceptions()
			.atMost(20, TimeUnit.SECONDS)
			.until(() -> template.getForObject(url, String.class).contains("Spring Cloud Skipper Server"));
		
	}

	@Test
	@DockerCompose(locations = { "src/test/resources/skipper-mysql.yml" }, services = { "mysql", "skipper" })
	public void testSkipperWithMysql(DockerComposeInfo dockerComposeInfo) throws Exception {
		DockerComposeRule docker = dockerComposeInfo.getRule();
		DockerPort container1 = docker.containers().container("mysql").port(3306);	
		assertThat(container1).isNotNull();		
		DockerPort container2 = docker.containers().container("skipper").port(7577);	
		assertThat(container2).isNotNull();

		String url = "http://" + container2.getIp() + ":" + container2.getExternalPort() + "/api/about";
		RestTemplate template = new RestTemplate();
		
		with()
			.pollInterval(1, TimeUnit.SECONDS)
			.and()
		.await()
			.ignoreExceptions()
			.atMost(20, TimeUnit.SECONDS)
			.until(() -> template.getForObject(url, String.class).contains("Spring Cloud Skipper Server"));
	}	
}
