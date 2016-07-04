package com.swingtech.app.tabletracker.service.util;

import java.io.File;
import java.net.URL;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import com.swingtech.app.tabletracker.model.TableTrackEvent;

public class JsonUtil {
	private final static ObjectMapper MAPPER = new ObjectMapper();

	static {
		// Set Up Jackson for object mapping
		MAPPER.registerModule(new JodaModule());
		MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		MAPPER.configure(com.fasterxml.jackson.databind.SerializationFeature.
			    WRITE_DATES_AS_TIMESTAMPS , false);
		
		// Set up JsonPath for json path stuff
		Configuration.setDefaults(new Configuration.Defaults() 
		{
		    private final JsonProvider jsonProvider = new JacksonJsonProvider(MAPPER);
		    private final MappingProvider mappingProvider = new JacksonMappingProvider(MAPPER);

		    @Override
		    public JsonProvider jsonProvider() {
		        return jsonProvider;
		    }

		    @Override
		    public MappingProvider mappingProvider() {
		        return mappingProvider;
		    }

		    @Override
		    public Set<Option> options() {
		        return EnumSet.noneOf(Option.class);
		    }
		});
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
	
	public static <T> T unmarshalJsonToObjectUsingJsonPath(String json, String jsonPath, Class<T> valueType) {
		try {
			return JsonPath.parse(json).read(jsonPath, valueType);
		} catch (Exception e) {
			throw new RuntimeException("Error trying to unmarshall Object to Json", e);
		}
	}
	
	public static Object unmarshalJsonToObjectUsingJsonPath(String json, String jsonPath) {
		try {
			return JsonPath.parse(json).read(jsonPath);
		} catch (Exception e) {
			throw new RuntimeException("Error trying to unmarshall Object to Json", e);
		}
	}

	public static <T> T unmarshalJsonToObjectUsingJsonPath(Object parsedJsonDocument, String jsonPath, Class<T> valueType) {
		try {
			return JsonPath.parse(parsedJsonDocument).read(jsonPath, valueType);
		} catch (Exception e) {
			throw new RuntimeException("Error trying to unmarshall Object to Json", e);
		}
	}

	public static <T> T unmarshalJsonToObjectUsingJsonPath(Object parsedJsonDocument, String jsonPath, TypeRef<T> valueType) {
		try {
			return JsonPath.parse(parsedJsonDocument).read(jsonPath, valueType);
		} catch (Exception e) {
			throw new RuntimeException("Error trying to unmarshall Object to Json", e);
		}
	}
	
	public static Object unmarshalJsonToObjectUsingJsonPath(Object parsedJsonDocument, String jsonPath) {
		try {
			return JsonPath.read(parsedJsonDocument, jsonPath);
		} catch (Exception e) {
			throw new RuntimeException("Error trying to unmarshall Object to Json", e);
		}
	}

	public static <T> T unmarshalJsonToObjectUsingJsonPath(String json, String jsonPath, TypeRef<T> valueType) {
		try {
			return JsonPath.parse(json).read(jsonPath, valueType);
		} catch (Exception e) {
			throw new RuntimeException("Error trying to unmarshall Object to Json", e);
		}
	}

	public static <T> T unmarshalJsonToObject(String json, TypeReference<T> valueType) {
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
	
	public static ObjectMapper getObjectMapper() {
		return MAPPER;
	}
	
	public static Object parseJson(String json) {
		return Configuration.defaultConfiguration().jsonProvider().parse(json);
	}
}
