package com.udacity.pricing;

import org.apache.commons.collections.map.MultiValueMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PricingServiceApplicationTests {
	@LocalServerPort
	private int port;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testFindPriceForVehicle(){
		LinkedMultiValueMap map = new LinkedMultiValueMap();
		String query = "{\"query\":\"{findPriceForVehicleId(vehicleid:2) {price}}\"}";
		map.add("query", query);
		WebClient client = WebClient.create();
		String resourceUrl = "http://localhost:"+this.port+"/graphql";
		String response = client.post()
				.uri(resourceUrl)
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromMultipartData("query","{findPriceForVehicleId(vehicleid:2) {price}}"))
				.retrieve()
				.bodyToMono(String.class)
				.block();
		Assertions.assertEquals(response.contains("600"), true);
	}
}
