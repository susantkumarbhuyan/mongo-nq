package io.mongonq.query;

import java.io.IOException;

import org.bson.BsonDocument;
import org.bson.json.JsonWriterSettings;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class QueryBuilderUtil {

	private static final String DEFAULT_TOKEN = "#";

	public static String createQuery(String query, Object... parameters) {
		if (!isValidQuery(query, parameters)) {
			String message = String.format("Parameters missing %s", query);
			throw new IllegalArgumentException(message);
		}
		String newQuery = query.replace("#", "%s");
		return String.format(newQuery, stringQueryFormat(parameters));
	}

	public static String convertObjectToJsonString(Object parameters) {
		String jsonString = null;
		try {
			jsonString = new ObjectMapper().writeValueAsString(parameters);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonString;
	}

	static boolean isValidQuery(String query, Object... parameters) {
		int count = query.split(DEFAULT_TOKEN).length - 1;
		if (count == parameters.length) {
			return true;
		}
		return false;
	}

	private static Object[] stringQueryFormat(Object... parameters) {
		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i] instanceof String) {
				parameters[i] = "'" + parameters[i] + "'";
			}
		}
		return parameters;
	}

	public static <T> T mapToPojo(BsonDocument document, final Class<T> clazz) {
		try {
			JsonWriterSettings settings = JsonWriterSettings.builder()
					.int64Converter((value, writer) -> writer.writeNumber(value.toString())).build();
			String string = document.toJson(settings);
			JsonMapper build = JsonMapper.builder().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
					.build();
			return build.readValue(string, clazz);
		} catch (IOException e) {
			String message = String.format("Unable to unmarshall result to %s from content %s", clazz,
					document.toString());
			throw new MarshallingException(message, e);
		}
	}
}
