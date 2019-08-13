package com.soze.common.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonUtils {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	public static <T> T parse(String json, Class<T> clazz) {
		try {
			return MAPPER.readValue(json, clazz);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static <T> List<T> parseList(String json, Class<T> clazz) {
		try {
			return MAPPER
				.readValue(json, MAPPER.getTypeFactory()
															 .constructCollectionType(List.class, clazz));
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}


	public static <T> List<T> parseList(File json, Class<T> clazz) {
		try {
			return MAPPER
				.readValue(json, MAPPER.getTypeFactory()
															 .constructCollectionType(List.class, clazz));
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static String serialize(Object object) {
		try {
			return MAPPER.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
