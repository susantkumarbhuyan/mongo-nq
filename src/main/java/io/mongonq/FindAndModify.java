package io.mongonq;

import org.bson.BsonDocument;
import io.mongonq.query.QueryBuilderUtil;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndUpdateOptions;

public class FindAndModify {
	private final MongoCollection<BsonDocument> mongoCollection;
	private final BsonDocument jsonQuery;
	private boolean isUpsert;
	private boolean isRemove;
	private BsonDocument jsonUpdateQuery;

	public FindAndModify(MongoCollection<BsonDocument> mongoCollection, String query, Object... parameters) {
		this.mongoCollection = mongoCollection;
		this.jsonQuery = QueryBuilderUtil.buildQueryDBObject(query, parameters);
	}

	public FindAndModify with(String query, Object... parameters) {
		jsonUpdateQuery = QueryBuilderUtil.buildQueryDBObject(query, parameters);
		return this;
	}

	public <T> T as(final Class<T> clazz) {

		BsonDocument result = new BsonDocument();
		if (isRemove) {
			result = mongoCollection.findOneAndDelete(jsonQuery);
		} else {
			result = mongoCollection.findOneAndUpdate(jsonQuery, jsonUpdateQuery,
					new FindOneAndUpdateOptions().upsert(isUpsert));
		}
		return QueryBuilderUtil.mapToPojo(result.toBsonDocument(), clazz);
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
