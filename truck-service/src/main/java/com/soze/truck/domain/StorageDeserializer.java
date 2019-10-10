package com.soze.truck.domain;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.soze.common.dto.Resource;

import java.io.IOException;

public class StorageDeserializer extends StdDeserializer<Storage> {

	public StorageDeserializer() {
		this(null);
	}

	protected StorageDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public Storage deserialize(JsonParser p, DeserializationContext ctxt
														) throws IOException, JsonProcessingException {
		JsonNode node = p.getCodec().readTree(p);

		int capacity = (Integer) (node.get("capacity")).numberValue();
		Storage storage = new Storage(capacity);

		ObjectNode resourcesNode = (ObjectNode) node.get("resources");
		resourcesNode.fields().forEachRemaining(e -> {
			String resource = e.getKey();
			int count = e.getValue().asInt();
			storage.addResource(Resource.valueOf(resource), count);
		});

		return storage;
	}
}
