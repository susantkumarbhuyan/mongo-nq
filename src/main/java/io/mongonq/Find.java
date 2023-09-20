package io.mongonq;

import java.util.ArrayList;
import java.util.List;

import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import io.mongonq.query.QueryBuilderUtil;
import io.mongonq.query.ResultsIterator;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

public class Find {
	private final MongoCollection<BsonDocument> mongoCollection;
	private final Bson jsonQuery;
	private final List<QueryModifier> modifiers;

	public Find(MongoCollection<BsonDocument> mongoCollection, String query, Object... parameters) {
		this.mongoCollection = mongoCollection;
		this.jsonQuery = QueryBuilderUtil.buildQueryDBObject(query, parameters);
		this.modifiers = new ArrayList<>();
	}

	public <T> ResultsIterator<T> as(final Class<T> clazz) {

		FindIterable<BsonDocument> find = mongoCollection.find(jsonQuery, BsonDocument.class);
		modifiers.forEach(modifier -> modifier.modify(find));

		return new ResultsIterator<T>(find.iterator(), clazz);

	}

	public Find projection(String sortQuery, Object... params) {
		Bson project = QueryBuilderUtil.buildQueryDBObject(sortQuery, params);
		this.modifiers.add(find -> find.projection(project));
		return this;
	}

	public Find skip(final int skipFrom) {
		this.modifiers.add(find -> find.skip(skipFrom));
		return this;
	}

	public Find limit(final int limitSize) {
		this.modifiers.add(find -> find.limit(limitSize));
		return this;
	}

	public Find sort(String sort) {
		final Bson sortDBObject = BsonDocument.parse(sort);
		this.modifiers.add(find -> find.sort(sortDBObject));
		return this;
	}

	public Find min(String minQuery, Object... params) {
		final Bson minObj = QueryBuilderUtil.buildQueryDBObject(minQuery, params);
		this.modifiers.add(find -> find.min(minObj));
		return this;
	}

	public Find max(String maxQuery, Object... params) {
		final Bson maxObj = QueryBuilderUtil.buildQueryDBObject(maxQuery, params);
		this.modifiers.add(find -> find.max(maxObj));
		return this;
	}
}

interface QueryModifier {
	void modify(FindIterable<BsonDocument> find);
}
