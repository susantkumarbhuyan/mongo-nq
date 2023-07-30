package io.mongonq;

import java.util.Arrays;
import java.util.List;

import io.mongonq.query.QueryBuilderUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;

public class Insert {
	private final MongoCollection<BasicDBObject> mongoCollection;

	public Insert(MongoCollection<BasicDBObject> mongoCollection) {
		this.mongoCollection = mongoCollection;
	}

	public WriteResult save(Object pojo) {
		String jsonString = QueryBuilderUtil.convertObjectToJsonString(pojo);
		BasicDBObject document = BasicDBObject.parse(jsonString);
		InsertOneResult insertResult = mongoCollection.insertOne(document);
		if (insertResult.wasAcknowledged()) {
			return new WriteResult(1, insertResult.wasAcknowledged(), insertResult.getInsertedId());
		} else {
			return new WriteResult(0, insertResult.wasAcknowledged());
		}
	}

	public WriteResult insert(Object pojos) {
		try {
			String jsonString = QueryBuilderUtil.convertObjectToJsonString(pojos);
			if (jsonString.startsWith("[")) {
				List<BasicDBObject> docList = Arrays
						.asList(new ObjectMapper().readValue(jsonString, BasicDBObject[].class));
				InsertManyResult insertResult = mongoCollection.insertMany(docList);
				if (insertResult.wasAcknowledged()) {
					return new WriteResult(1, insertResult.wasAcknowledged(), insertResult.getInsertedIds());
				} else {
					return new WriteResult(0, insertResult.wasAcknowledged());
				}
			} else {
				BasicDBObject document = BasicDBObject.parse(jsonString);
				InsertOneResult insertResult = mongoCollection.insertOne(document);
				if (insertResult.wasAcknowledged()) {
					return new WriteResult(1, insertResult.wasAcknowledged(), insertResult.getInsertedId());
				} else {
					return new WriteResult(0, insertResult.wasAcknowledged());
				}
			}

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public WriteResult insert(String query, Object... parameters) {
		WriteResult result = new WriteResult();
		try {

			String jsonQuery = QueryBuilderUtil.createQuery(query, parameters);
			if (jsonQuery.startsWith("[")) {
				List<BasicDBObject> listDoc = Arrays
						.asList(new ObjectMapper().readValue(jsonQuery.replace("'", "\""), BasicDBObject[].class));
				InsertManyResult insertResult = mongoCollection.insertMany(listDoc);
				if (insertResult.wasAcknowledged()) {
					return new WriteResult(1, insertResult.wasAcknowledged(), insertResult.getInsertedIds());
				} else {
					return new WriteResult(0, insertResult.wasAcknowledged());
				}
			} else {
				BasicDBObject document = BasicDBObject.parse(jsonQuery);
				InsertOneResult insertResult = mongoCollection.insertOne(document);
				if (insertResult.wasAcknowledged()) {
					return new WriteResult(1, insertResult.wasAcknowledged(), insertResult.getInsertedId());
				} else {
					return new WriteResult(0, insertResult.wasAcknowledged());
				}
			}

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return result;
	}

}
