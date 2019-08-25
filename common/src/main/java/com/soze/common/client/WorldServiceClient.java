package com.soze.common.client;

import com.soze.common.dto.CityDTO;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

public interface WorldServiceClient {

	String WORLD_SERVICE_URL = "http://localhost:9000/world/";

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	List<CityDTO> getAllCities();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/name/{name}")
	CityDTO getCityByName(@PathParam("name") String name);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/id/{cityId}")
	CityDTO getCityById(@PathParam("cityId") String cityId);

	static WorldServiceClient createClient() {
		ResteasyClient client = (ResteasyClient) ResteasyClientBuilder.newClient();
		ResteasyWebTarget target = client.target(WORLD_SERVICE_URL);
		return target.proxy(WorldServiceClient.class);
	}
}
