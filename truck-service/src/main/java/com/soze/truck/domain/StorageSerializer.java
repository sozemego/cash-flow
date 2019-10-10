package com.soze.truck.domain;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class StorageSerializer extends StdSerializer<Storage> {

	public StorageSerializer() {
		this(null);
	}

	public StorageSerializer(Class<Storage> t) {
		super(t);
	}

	@Override
	public void serialize(Storage value, JsonGenerator gen, SerializerProvider provider
											 ) throws IOException {
		gen.writeStartObject();
		gen.writeNumberField("capacity", value.getCapacity());
		gen.writeObjectField("resources", value.getResources());
		gen.writeEndObject();
	}
}
