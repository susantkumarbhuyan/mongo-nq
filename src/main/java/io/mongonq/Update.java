package io.mongonq;

import io.mongonq.query.QueryBuilderUtil;

import org.bson.BsonDocument;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;

public class Update {
	private final MongoCollection<BsonDocument> mongoCollection;
	private final BsonDocument jsonQuery;
	private static final Object[] NO_PARAMETERS = {};
	private boolean upsert = false;
	private boolean multi = false;

	public Update(MongoCollection<BsonDocument> mongoCollection, String query, Object... parameters) {
		this.mongoCollection = mongoCollection;
		this.jsonQuery = QueryBuilderUtil.buildQueryDBObject(query, parameters);
	}

	public WriteResult with(String modifier) {
		return with(modifier, NO_PARAMETERS);
	}

	public WriteResult with(Object pojo) {
		String updateWithQuery = QueryBuilderUtil.convertObjectToJsonString(pojo);
		BsonDocument update = new BsonDocument().append("$set", BsonDocument.parse(updateWithQuery));
		return with(update.toJson(), NO_PARAMETERS);
	}

	public WriteResult with(String modifier, Object... parameters) {

		BsonDocument update = QueryBuilderUtil.buildQueryDBObject(modifier, parameters);
		UpdateResult updateResult = multi
				? mongoCollection.updateMany(jsonQuery, update, new UpdateOptions().upsert(upsert))
				: mongoCollection.updateOne(jsonQuery, update, new UpdateOptions().upsert(upsert));

		return updateResult.wasAcknowledged()
				? new WriteResult(1, updateResult.wasAcknowledged(), updateResult.getUpsertedId())
				: new WriteResult();
	}

	public Update upsert() {
		this.upsert = true;
		return this;
	}

	public Update multi() {
		this.multi = true;
		return this;
	}
}
