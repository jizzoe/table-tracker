package com.swingtech.app.tabletracker.service.util;

import java.io.File;
import java.net.URL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

public class JsonUtil {
	private final static ObjectMapper MAPPER = new ObjectMapper();

	static {
		MAPPER.registerModule(new JodaModule());
		MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		MAPPER.configure(com.fasterxml.jackson.databind.SerializationFeature.
			    WRITE_DATES_AS_TIMESTAMPS , false);
	}

	/**
	 * DOCME - JavaDoc this constructor: JsonUtil
	 *
	 */
	private JsonUtil() {

	}

	public static String marshalObjectToJson(Object obj) {
		try {
			return MAPPER.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error trying to marshall Object to Json", e);
		}
	}

	public static <T> T unmarshalJsonToObject(String json, Class<T> valueType) {
		try {
			return MAPPER.readValue(json, valueType);
		} catch (Exception e) {
			throw new RuntimeException("Error trying to unmarshall Object to Json", e);
		}
	}

	public static <T> T unmarshalJsonToObject(URL jsonRul, Class<T> valueType) {
		try {
			return MAPPER.readValue(jsonRul, valueType);
		} catch (Exception e) {
			throw new RuntimeException("Error trying to marshall json to object", e);
		}
	}

	public static <T> T unmarshalJsonToObject(File jsonFile, Class<T> valueType) {
		try {
			return MAPPER.readValue(jsonFile, valueType);
		} catch (Exception e) {
			throw new RuntimeException("Error trying to marshall json to object", e);
		}
	}
}
