package io.mongonq.query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.BsonDocument;
import org.bson.json.JsonWriterSettings;
import org.json.JSONArray;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class QueryBuilderUtil {

	private static final String DEFAULT_TOKEN = "#";

	public static BsonDocument buildQueryDBObject(String query, Object... parameters) {
		try {
			String jsonQuery = queryFormater(query, parameters);
			// JSONObject for convert proper JSON product.price -> 'product.price'
			// String strQuery = new JSONObject(jsonQuery).toString();

			return BsonDocument.parse(jsonQuery);
		} catch (Exception e) {
			e.printStackTrace();
			return new BsonDocument();
		}

	}

	public static List<BsonDocument> buildQueryDBObjectList(String query, Object... parameters) {
		try {
			String jsonQuery = queryFormater(query, parameters);
			String jsonObjectArrayStr = new JSONArray(jsonQuery).toString();
			ObjectMapper objectMapper = new ObjectMapper()
					.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
			String respData = objectMapper.writeValueAsString(jsonObjectArrayStr);
			return new ObjectMapper().readValue(respData, new TypeReference<List<BsonDocument>>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	private static String queryFormater(String query, Object... parameters) {
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

	private static boolean isValidQuery(String query, Object... parameters) {
		int count = query.split(DEFAULT_TOKEN).length - 1;
		return count == parameters.length;
	}

	private static Object[] stringQueryFormat(Object... parameters) {
		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i] instanceof String) {
				parameters[i] = "'" + parameters[i] + "'";
			} else if (parameters[i] instanceof Object) {
				parameters[i] = convertObjectToJsonString(parameters[i]);
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
