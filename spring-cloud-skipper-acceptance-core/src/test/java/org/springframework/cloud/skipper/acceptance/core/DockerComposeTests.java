package org.springframework.cloud.skipper.acceptance.core;

import org.junit.jupiter.api.Test;

@DockerCompose(locations = {"docker-compose-1.yml", "docker-compose-2.yml"})
public class DockerComposeTests  {

	@DockerCompose(locations = {"docker-compose-3.yml", "docker-compose-4.yml"})
	//@Test
	public void testCompose1(DockerComposeInfo dockerComposeInfo) {
		System.out.println("hello1 " + dockerComposeInfo);
	}

	@DockerCompose(locations = {"docker-compose-5.yml", "docker-compose-6.yml"})
	//@Test
	public void testCompose2(DockerComposeInfo dockerComposeInfo) {
		System.out.println("hello2 " + dockerComposeInfo);
	}
	
}
