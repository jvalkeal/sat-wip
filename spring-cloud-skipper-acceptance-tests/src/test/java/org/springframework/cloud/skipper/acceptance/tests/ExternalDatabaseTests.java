package org.springframework.cloud.skipper.acceptance.tests;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.cloud.skipper.acceptance.core.DockerCompose;
import org.springframework.cloud.skipper.acceptance.core.DockerComposeInfo;
import org.springframework.web.client.RestTemplate;

import com.palantir.docker.compose.connection.DockerPort;

import static com.jayway.awaitility.Awaitility.with;

public class ExternalDatabaseTests {

	@Test
	@DockerCompose(locations = { "src/test/resources/skipper-postgres.yml" }, services = { "postgres", "skipper" })
	public void testSkipperWithPostgres(DockerComposeInfo dockerComposeInfo) throws Exception {
		DockerPort port = dockerComposeInfo.getRule().containers().container("skipper").port(7577);
		String url = "http://" + port.getIp() + ":" + port.getExternalPort() + "/api/about";
		assertServerRunning(url);
	}

	@Test
	@DockerCompose(locations = { "src/test/resources/skipper-mysql.yml" }, services = { "mysql", "skipper" })
	public void testSkipperWithMysql(DockerComposeInfo dockerComposeInfo) throws Exception {
		DockerPort port = dockerComposeInfo.getRule().containers().container("skipper").port(7577);
		String url = "http://" + port.getIp() + ":" + port.getExternalPort() + "/api/about";
		assertServerRunning(url);
	}

	@Test
	@DockerCompose(locations = { "src/test/resources/skipper-oracle.yml" }, services = { "oracle", "skipper" })
	public void testSkipperWithOracle(DockerComposeInfo dockerComposeInfo) throws Exception {
		DockerPort port = dockerComposeInfo.getRule().containers().container("skipper").port(7577);
		String url = "http://" + port.getIp() + ":" + port.getExternalPort() + "/api/about";
		assertServerRunning(url);
	}
	
	private void assertServerRunning(String url) {
		RestTemplate template = new RestTemplate();
		with()
			.pollInterval(1, TimeUnit.SECONDS)
			.and()
			.await()
				.ignoreExceptions()
				.atMost(120, TimeUnit.SECONDS)
				.until(() -> template.getForObject(url, String.class).contains("Spring Cloud Skipper Server"));
	}
}
