package io.mongonq;

import org.bson.BsonDocument;
import io.mongonq.query.QueryBuilderUtil;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;

public class FindOne {
	private final MongoCollection<BasicDBObject> mongoCollection;
	private final String jsonQuery;
	private String projectJsonQuery = "{}";

	public FindOne(MongoCollection<BasicDBObject> mongoCollection, String query, Object... parameters) {
		this.mongoCollection = mongoCollection;
		this.jsonQuery = QueryBuilderUtil.createQuery(query, parameters);
	}

	public FindOne projection(String sortQuery, Object... params) {
		projectJsonQuery = QueryBuilderUtil.createQuery(sortQuery, params);
		return this;
	}

	public <T> T as(final Class<T> clazz) {
		BasicDBObject filter = BasicDBObject.parse(jsonQuery);
		BasicDBObject project = BasicDBObject.parse(projectJsonQuery);

		BsonDocument result = mongoCollection.find(filter, BsonDocument.class).projection(project).first();
		return result == null ? null : QueryBuilderUtil.mapToPojo(result, clazz);
	}
}
