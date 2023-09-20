package io.mongonq;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.BsonDocument;
import io.mongonq.query.QueryBuilderUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;

public class Distinct {
	private final MongoCollection<BsonDocument> mongoCollection;
	private final String key;
	private BsonDocument query;
	private static final Object[] NO_PARAMETERS = {};

	public Distinct(MongoCollection<BsonDocument> mongoCollection, String key) {
		this.mongoCollection = mongoCollection;
		this.key = key;
	}

	public Distinct query(String query) {
		return query(query, NO_PARAMETERS);

	}

	public Distinct query(String query, Object... parameters) {
		this.query = QueryBuilderUtil.buildQueryDBObject(query, parameters);
		return this;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> as(final Class<T> clazz) {
		try {
			Class<T[]> class1 = (Class<T[]>) Array.newInstance(clazz, 0).getClass();

			List<BsonDocument> distinct = mongoCollection.distinct(key, query, BsonDocument.class)
					.into(new ArrayList<>());

			List<T> docList = Arrays.asList(new ObjectMapper().readValue(
					QueryBuilderUtil.convertObjectToJsonString(distinct).replace("\"", "").replace("'", "\""), class1));
			return docList;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
