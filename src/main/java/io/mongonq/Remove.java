package io.mongonq;

import io.mongonq.query.QueryBuilderUtil;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;

public class Remove {
	private final MongoCollection<BasicDBObject> mongoCollection;
	private final String jsonQuery;
	private boolean isSingleDoc;

	public Remove(MongoCollection<BasicDBObject> mongoCollection, String query, Object... parameters) {
		this.mongoCollection = mongoCollection;
		this.jsonQuery = QueryBuilderUtil.createQuery(query, parameters);
	}

	public WriteResult remove() {
		BasicDBObject filter = BasicDBObject.parse(jsonQuery);
		DeleteResult removeResult = null;
		if (isSingleDoc) {
			removeResult = mongoCollection.deleteOne(filter);
		} else {
			removeResult = mongoCollection.deleteMany(filter);
		}
		if (removeResult.getDeletedCount() > 0) {
			return new WriteResult(1, removeResult.wasAcknowledged(), removeResult.getDeletedCount());
		} else {
			return new WriteResult(0, removeResult.wasAcknowledged());
		}
	}

	public Remove single() {
		this.isSingleDoc = true;
		return this;
	}

}
