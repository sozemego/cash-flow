package com.soze.clock.client;

import com.soze.clock.domain.Clock;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public interface ClockServiceClient {

	String CLOCK_SERVICE_URL = "http://localhost:9004/clock/";

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Clock getClock();

	default ClockServiceClient createClient() {
		ResteasyClient client = (ResteasyClient) ResteasyClientBuilder.newClient();
		ResteasyWebTarget target = client.target(CLOCK_SERVICE_URL);
		return target.proxy(ClockServiceClient.class);
	}

}
