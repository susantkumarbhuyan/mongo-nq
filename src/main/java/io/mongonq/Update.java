package io.mongonq;

import io.mongonq.query.QueryBuilderUtil;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;

public class Update {
	private final MongoCollection<BasicDBObject> mongoCollection;
	private final String jsonQuery;
	private static final Object[] NO_PARAMETERS = {};
	private boolean upsert = false;
	private boolean multi = false;

	public Update(MongoCollection<BasicDBObject> mongoCollection, String query, Object... parameters) {
		this.mongoCollection = mongoCollection;
		this.jsonQuery = QueryBuilderUtil.createQuery(query, parameters);
	}

	public WriteResult with(String modifier) {
		return with(modifier, NO_PARAMETERS);
	}

	public WriteResult with(String modifier, Object... parameters) {
		BasicDBObject filter = BasicDBObject.parse(jsonQuery);
		String updateWithQuery = QueryBuilderUtil.createQuery(modifier, parameters);
		BasicDBObject update = BasicDBObject.parse(updateWithQuery);

		UpdateResult updateResult = null;
		if (multi) {
			updateResult = mongoCollection.updateMany(filter, update, new UpdateOptions().upsert(upsert));
		} else {
			updateResult = mongoCollection.updateOne(filter, update, new UpdateOptions().upsert(upsert));
		}

		if (updateResult.wasAcknowledged()) {
			return new WriteResult(1, updateResult.wasAcknowledged(), updateResult.getUpsertedId());
		} else {
			return new WriteResult(0, updateResult.wasAcknowledged());
		}
	}

	public WriteResult with(Object pojo) {
		BasicDBObject filter = BasicDBObject.parse(jsonQuery);
		String updateWithQuery = QueryBuilderUtil.convertObjectToJsonString(pojo);
		BasicDBObject update = BasicDBObject.parse(updateWithQuery);
		UpdateResult updateResult = null;
		if (multi) {
			updateResult = mongoCollection.updateMany(filter, update, new UpdateOptions().upsert(upsert));
		} else {
			updateResult = mongoCollection.updateOne(filter, update, new UpdateOptions().upsert(upsert));
		}

		if (updateResult.wasAcknowledged()) {
			return new WriteResult(1, updateResult.wasAcknowledged(), updateResult.getUpsertedId());
		} else {
			return new WriteResult(0, updateResult.wasAcknowledged());
		}

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
