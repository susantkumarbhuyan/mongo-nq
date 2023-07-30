package io.mongonq;

import java.util.ArrayList;
import java.util.List;

import org.bson.conversions.Bson;
import io.mongonq.query.QueryBuilderUtil;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;

public class Distinct {
	private final MongoCollection<BasicDBObject> mongoCollection;
	private final String key;
	private String query;
	private static final Object[] NO_PARAMETERS = {};

	public Distinct(MongoCollection<BasicDBObject> mongoCollection, String key) {
		this.mongoCollection = mongoCollection;
		this.key = key;
	}

	public Distinct query(String query) {
		return query(query, NO_PARAMETERS);

	}

	public Distinct query(String query, Object... parameters) {
		this.query = QueryBuilderUtil.createQuery(query, parameters);
		return this;
	}

	public <T> List<T> as(final Class<T> clazz) {
		Bson filter = BasicDBObject.parse(query);
		List<T> distinct = mongoCollection.distinct(key, filter, clazz).into(new ArrayList<>());
		return distinct;
	}
}
