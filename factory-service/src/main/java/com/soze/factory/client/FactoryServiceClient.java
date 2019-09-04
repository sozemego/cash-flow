package com.soze.factory.client;

import com.soze.common.dto.FactoryDTO;
import com.soze.factory.domain.SellResult;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

public interface FactoryServiceClient {

	String FACTORY_SERVICE_URL = "http://localhost:9001/factory/";

	static FactoryServiceClient createClient() {
		ResteasyClient client = (ResteasyClient) ResteasyClientBuilder.newClient();
		ResteasyWebTarget target = client.target(FACTORY_SERVICE_URL);
		return target.proxy(FactoryServiceClient.class);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/sell")
	SellResult sell(@QueryParam("factoryId") String factoryId, @QueryParam("resource") String resource,
									@QueryParam("count") int count
								 );

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/single/{factoryId}")
	FactoryDTO getFactory(@PathParam("factoryId") String factoryId);
}
