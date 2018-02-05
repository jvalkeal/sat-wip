/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.cloud.skipper.acceptance.tests;

import org.junit.jupiter.api.Test;
import org.springframework.cloud.skipper.acceptance.core.DockerCompose;
import org.springframework.cloud.skipper.acceptance.core.DockerComposeInfo;
import org.springframework.cloud.skipper.acceptance.tests.support.AssertUtils;

import com.palantir.docker.compose.connection.DockerPort;

/**
 * Tests going through start of skipper servers with databases and verifying
 * server works with initial schema creation.
 * 
 * @author Janne Valkealahti
 *
 */
public class SkipperServerInitialBootstrapTests {

	@Test
	@DockerCompose(locations = { "src/test/resources/skipper-postgres.yml" }, services = { "postgres", "skipper" })
	public void testSkipperWithPostgres(DockerComposeInfo dockerComposeInfo) throws Exception {
		DockerPort port = dockerComposeInfo.id("").getRule().containers().container("skipper").port(7577);
		String url = "http://" + port.getIp() + ":" + port.getExternalPort() + "/api/about";
		AssertUtils.assertServerRunning(url);
	}

	@Test
	@DockerCompose(locations = { "src/test/resources/skipper-mysql.yml" }, services = { "mysql", "skipper" })
	public void testSkipperWithMysql(DockerComposeInfo dockerComposeInfo) throws Exception {
		DockerPort port = dockerComposeInfo.id("").getRule().containers().container("skipper").port(7577);
		String url = "http://" + port.getIp() + ":" + port.getExternalPort() + "/api/about";
		AssertUtils.assertServerRunning(url);
	}

	@Test
	@DockerCompose(locations = { "src/test/resources/skipper-oracle.yml" }, services = { "oracle", "skipper" })
	public void testSkipperWithOracle(DockerComposeInfo dockerComposeInfo) throws Exception {
		DockerPort port = dockerComposeInfo.id("").getRule().containers().container("skipper").port(7577);
		String url = "http://" + port.getIp() + ":" + port.getExternalPort() + "/api/about";
		AssertUtils.assertServerRunning(url);
	}
}
