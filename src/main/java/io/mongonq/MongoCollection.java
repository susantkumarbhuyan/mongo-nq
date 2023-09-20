package io.mongonq;

import org.bson.Document;
import org.bson.types.ObjectId;
import io.mongonq.query.QueryBuilderUtil;

import org.bson.BsonDocument;
import com.mongodb.client.model.IndexOptions;

public class MongoCollection {
	private static final String MONGO_DOCUMENT_ID_NAME = "_id";
	private static final Object[] NO_PARAMETERS = {};
	private static final String ALL = "{}";
	private final com.mongodb.client.MongoCollection<BsonDocument> mongoCollection;

	public MongoCollection(com.mongodb.client.MongoCollection<BsonDocument> mongoCollection) {
		this.mongoCollection = mongoCollection;
	}

	public FindOne findOne() {
		return findOne(ALL);
	}

	public FindOne findOne(String query) {
		return findOne(query, NO_PARAMETERS);
	}

	public FindOne findOne(String query, Object... parameters) {
		return new FindOne(mongoCollection, query, parameters);
	}

	public Find find() {
		return find(ALL);
	}

	public Find find(String query) {
		return find(query, NO_PARAMETERS);
	}

	public Find find(String query, Object... parameters) {
		return new Find(mongoCollection, query, parameters);
	}

	public FindAndModify findAndModify() {
		return findAndModify(ALL);
	}

	public FindAndModify findAndModify(String query) {
		return findAndModify(query, NO_PARAMETERS);
	}

	public FindAndModify findAndModify(String query, Object... parameters) {
		return new FindAndModify(mongoCollection, query, parameters);
	}

	public long count() {
		return count(ALL);
	}

	public long count(String query) {
		return count(query, NO_PARAMETERS);
	}

	public long count(String query, Object... parameters) {
		BsonDocument filter =  QueryBuilderUtil.buildQueryDBObject(query, parameters);
		return mongoCollection.countDocuments(filter);
	}

	public Update update(String query) {
		return update(query, NO_PARAMETERS);
	}

	public Update update(ObjectId id) {
		if (id == null) {
			throw new IllegalArgumentException("Object id must not be null");
		}
		return update("{_id:#}", id);
	}

	public Update update(String query, Object... parameters) {
		return new Update(mongoCollection, query, parameters);
	}

	public WriteResult save(Object pojo) {
		return new Insert(mongoCollection).save(pojo);
	}

	public WriteResult insert(Object pojo) {
		return new Insert(mongoCollection).insert(pojo);
	}

	public WriteResult insert(String query) {
		return insert(query, NO_PARAMETERS);
	}

	public WriteResult insert(String query, Object... parameters) {
		return new Insert(mongoCollection).insert(query, parameters);
	}

	public WriteResult remove(ObjectId id) {
		return remove("{" + MONGO_DOCUMENT_ID_NAME + ":#}", id);
	}

	public WriteResult remove() {
		return remove(ALL);
	}

	public WriteResult remove(String query) {
		return remove(query, NO_PARAMETERS);
	}

	public WriteResult remove(String query, Object... parameters) {
		return new Remove(mongoCollection, query, parameters).remove();
	}

	public Distinct distinct(String key) {
		return new Distinct(mongoCollection, key);
	}

	public Aggregate aggregate(String pipelineOperator) {
		return aggregate(pipelineOperator, NO_PARAMETERS);
	}

	public Aggregate aggregate(String pipelineOperator, Object... parameters) {
		return new Aggregate(mongoCollection).and(pipelineOperator, parameters);
	}

	public void drop() {
		mongoCollection.drop();
	}

	public void dropIndex(String keys) {
		mongoCollection.dropIndex(keys);
	}

	public void dropIndexes() {
		mongoCollection.dropIndexes();
	}

	public void ensureIndex(String keys) {
		mongoCollection.createIndex(Document.parse(keys));
	}

	public void ensureIndex(String keys, IndexOptions options) {
		mongoCollection.createIndex(Document.parse(keys), options);
	}
}
