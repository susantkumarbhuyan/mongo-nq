package io.mongonq;

import java.util.List;

import org.bson.BsonDocument;
import io.mongonq.query.QueryBuilderUtil;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;

public class Insert {
	private final MongoCollection<BsonDocument> mongoCollection;

	public Insert(MongoCollection<BsonDocument> mongoCollection) {
		this.mongoCollection = mongoCollection;
	}

	public WriteResult save(Object pojo) {
		String jsonString = QueryBuilderUtil.convertObjectToJsonString(pojo);
		BsonDocument document = BsonDocument.parse(jsonString);
		if (document.containsKey("_id")) {
			document = new BsonDocument().append("_id", document.getString("_id"));
		}
		BsonDocument insert = new BsonDocument();
		insert.append("$set", document);
		UpdateResult updateResult = mongoCollection.updateOne(document, insert, new UpdateOptions().upsert(true));
		if (updateResult.wasAcknowledged()) {
			return new WriteResult(1, true);
		}
		return new WriteResult(0, false);
	}

	public WriteResult insert(Object pojos) {
		String jsonString = QueryBuilderUtil.convertObjectToJsonString(pojos);
		return insert(jsonString);
	}

	public WriteResult insert(String query, Object... parameters) {
		WriteResult result = new WriteResult(0, false);
		if (query.startsWith("[")) {
			List<BsonDocument> docList = QueryBuilderUtil.buildQueryDBObjectList(query, parameters);
			InsertManyResult insertMany = mongoCollection.insertMany(docList);
			if (insertMany.wasAcknowledged()) {
				result = new WriteResult(docList.size(), true);
			}
		} else {
			BsonDocument document = QueryBuilderUtil.buildQueryDBObject(query, parameters);
			InsertOneResult insertOne = mongoCollection.insertOne(document);
			if (insertOne.wasAcknowledged()) {
				result = new WriteResult(1, true);
			}
		}
		return result;
	}
}
