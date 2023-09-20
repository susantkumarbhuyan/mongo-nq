package io.mongonq;

import io.mongonq.query.QueryBuilderUtil;
import org.bson.BsonDocument;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;

public class Remove {
	private final MongoCollection<BsonDocument> mongoCollection;
	private final BsonDocument jsonQuery;
	private boolean isSingleDoc;

	public Remove(MongoCollection<BsonDocument> mongoCollection, String query, Object... parameters) {
		this.mongoCollection = mongoCollection;
		this.jsonQuery = QueryBuilderUtil.buildQueryDBObject(query, parameters);
	}

	public WriteResult remove() {
		
		DeleteResult removeResult = null;
		if (isSingleDoc) {
			removeResult = mongoCollection.deleteOne(jsonQuery);
		} else {
			removeResult = mongoCollection.deleteMany(jsonQuery);
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

