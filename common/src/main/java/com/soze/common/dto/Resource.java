package com.soze.common.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

@JsonSerialize(using = Resource.ResourceSerializer.class)
@JsonDeserialize(using = Resource.ResourceDeserializer.class)
public enum Resource {

	WOOD(2, 6);

	private final int minPrice;
	private final int maxPrice;

	Resource(int minPrice, int maxPrice) {
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
	}

	public int getMinPrice() {
		return minPrice;
	}

	public int getMaxPrice() {
		return maxPrice;
	}

	public static class ResourceSerializer extends StdSerializer<Resource> {

		public ResourceSerializer() {
			this(null);
		}

		public ResourceSerializer(Class<Resource> t) {
			super(t);
		}

		@Override
		public void serialize(Resource value, JsonGenerator gen, SerializerProvider provider
												 ) throws IOException {
			gen.writeStartObject();
			gen.writeStringField("name", value.name());
			gen.writeNumberField("minPrice", value.getMinPrice());
			gen.writeNumberField("maxPrice", value.getMaxPrice());
			gen.writeEndObject();
		}
	}

	public static class ResourceDeserializer extends StdDeserializer<Resource> {

		public ResourceDeserializer() {
			this(null);
		}

		public ResourceDeserializer(Class<?> vc) {
			super(vc);
		}

		@Override
		public Resource deserialize(JsonParser p, DeserializationContext ctxt
															 ) throws IOException, JsonProcessingException {
			return Resource.valueOf(p.getValueAsString("name"));
		}
	}
}
