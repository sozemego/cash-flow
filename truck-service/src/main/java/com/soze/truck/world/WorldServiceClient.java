package com.soze.truck.world;

import com.soze.common.dto.CityDTO;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

public interface WorldServiceClient {

	String WORLD_SERVICE_URL = "http://localhost:9000/world/";

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	List<CityDTO> getAllCities();

	static WorldServiceClient createClient() {
		ResteasyClient client = (ResteasyClient) ResteasyClientBuilder.newClient();
		ResteasyWebTarget target = client.target(WORLD_SERVICE_URL);
		WorldServiceClient proxy = target.proxy(WorldServiceClient.class);
		return proxy;
	}

}
