package io.mongonq;

import org.bson.BsonDocument;
import io.mongonq.query.QueryBuilderUtil;

import com.mongodb.client.MongoCollection;

public class FindOne {
	private final MongoCollection<BsonDocument> mongoCollection;
	private final BsonDocument jsonQuery;
	private BsonDocument projectJsonQuery = new BsonDocument();

	public FindOne(MongoCollection<BsonDocument> mongoCollection, String query, Object... parameters) {
		this.mongoCollection = mongoCollection;
		this.jsonQuery = QueryBuilderUtil.buildQueryDBObject(query, parameters);
	}

	public FindOne projection(String sortQuery, Object... params) {
		projectJsonQuery = QueryBuilderUtil.buildQueryDBObject(sortQuery, params);
		return this;
	}

	public <T> T as(final Class<T> clazz) {
		BsonDocument result = mongoCollection.find(jsonQuery, BsonDocument.class).projection(projectJsonQuery).first();
		return result != null ? QueryBuilderUtil.mapToPojo(result, clazz)
				: QueryBuilderUtil.mapToPojo(new BsonDocument(), clazz);

	}
}
