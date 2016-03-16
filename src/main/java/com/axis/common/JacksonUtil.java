package com.axis.common;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonUtil {

	private static ObjectMapper objMapper;

	/**
	 * Json 转换成 Bean
	 * 
	 * @param json
	 * @param cls
	 * @return 相应的 Bean
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> T toBean(String json, Class<T> cls)
			throws JsonParseException, JsonMappingException, IOException {
		if (objMapper == null)
			objMapper = new ObjectMapper();
		return objMapper.readValue(json, cls);
	}

	/**
	 * Json 转换成 List
	 * 
	 * @param Json
	 * @param valueTypeRef
	 * @return 相应的 list
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> T toList(String json, TypeReference<T> valueTypeRef)
			throws JsonParseException, JsonMappingException, IOException {
		if (objMapper == null)
			objMapper = new ObjectMapper();
		return objMapper.readValue(json, valueTypeRef);
	}

	/**
	 * Bean 转换成 Json
	 * 
	 * @param obj
	 * @return 相应的 Json
	 * @throws JsonProcessingException
	 */
	public static String toJson(Object obj) throws JsonProcessingException {
		if (objMapper == null)
			objMapper = new ObjectMapper();
		return objMapper.writeValueAsString(obj);
	}
}
