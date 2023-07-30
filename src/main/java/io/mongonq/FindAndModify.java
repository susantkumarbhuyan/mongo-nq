package io.mongonq;

import org.bson.BsonDocument;
import io.mongonq.query.QueryBuilderUtil;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndUpdateOptions;

public class FindAndModify {
	private final MongoCollection<BasicDBObject> mongoCollection;
	private final String jsonQuery;
	private boolean isUpsert;
	private boolean isRemove;

	public FindAndModify(MongoCollection<BasicDBObject> mongoCollection, String query, Object... parameters) {
		this.mongoCollection = mongoCollection;
		this.jsonQuery = QueryBuilderUtil.createQuery(query, parameters);
	}

	public FindAndModify with(String query, Object... parameters) {
		String jsonUpdateQuery = QueryBuilderUtil.createQuery(query, parameters);
		BasicDBObject filter = BasicDBObject.parse(jsonQuery);
		BasicDBObject update = BasicDBObject.parse(jsonUpdateQuery);
		mongoCollection.findOneAndUpdate(filter, update, new FindOneAndUpdateOptions().upsert(isUpsert));
		return this;
	}

	public <T> T as(final Class<T> clazz) {
		BasicDBObject filter = BasicDBObject.parse(jsonQuery);
		BsonDocument result = mongoCollection.find(filter, BsonDocument.class).first();
		if (isRemove) {
			mongoCollection.findOneAndDelete(filter);
		}
		return result == null ? null : QueryBuilderUtil.mapToPojo(result, clazz);
	}

	public FindAndModify upsert() {
		isUpsert = true;
		return this;
	}

	public FindAndModify remove() {
		isRemove = true;
		return this;
	}

}
