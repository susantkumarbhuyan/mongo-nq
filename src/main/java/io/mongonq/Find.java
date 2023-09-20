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
	private final MongoCollection<BasicDBObject> mongoCollection;
	private final String jsonQuery;
	private String sortJsonQuery = "{}";
	private String projectJsonQuery = "{}";
	private boolean isSort;
	private boolean isProjection;
	private boolean isSkip;
	private boolean isLimit;
	private int skipFrom;
	private int limitSize;
	private boolean isMin;
	private String minJsonQuery;
	private String maxJsonQuery;
	private boolean isMax;

	public Find(MongoCollection<BasicDBObject> mongoCollection, String query, Object... parameters) {
		this.mongoCollection = mongoCollection;
		this.jsonQuery = QueryBuilderUtil.createQuery(query, parameters);
	}

	public Find sort(String sortQuery, Object... params) {
		isSort = true;
		sortJsonQuery = QueryBuilderUtil.createQuery(sortQuery, params);
		return this;
	}

	public Find projection(String sortQuery, Object... params) {
		isProjection = true;
		projectJsonQuery = QueryBuilderUtil.createQuery(sortQuery, params);
		return this;
	}

	/**
	 * @param minQuery
	 * @param params
	 * @return
	 */
	public Find min(String minQuery, Object... params) {
		isMin = true;
		minJsonQuery = QueryBuilderUtil.createQuery(minQuery, params);
		return this;
	}

	public Find max(String maxQuery, Object... params) {
		isMax = true;
		maxJsonQuery = QueryBuilderUtil.createQuery(maxQuery, params);
		return this;
	}

	public <T> ResultsIterator<T> as(final Class<T> clazz) {
		BasicDBObject filter = BasicDBObject.parse(jsonQuery);
		FindIterable<BsonDocument> find = mongoCollection.find(filter, BsonDocument.class);

		if (isProjection) {
			BasicDBObject project = BasicDBObject.parse(projectJsonQuery);
			find = find.projection(project);
		}
		if (isSort) {
			BasicDBObject sort = BasicDBObject.parse(sortJsonQuery);
			find = find.sort(sort);
		}
		if (isSkip) {
			find = find.skip(skipFrom);
		}
		if (isLimit) {
			find = find.limit(limitSize);
		}
		if (isMin) {
			BasicDBObject minObj = BasicDBObject.parse(minJsonQuery);
			find.min(minObj);
		}
		if (isMax) {
			BasicDBObject maxObj = BasicDBObject.parse(maxJsonQuery);
			find.max(maxObj);
		}

		return new ResultsIterator<T>(find.iterator(), clazz);

	}

	/**
	 * @param from
	 * @return this
	 */
	public Find skip(int from) {
		skipFrom = from;
		isSkip = true;
		return this;
	}

	public Find limit(int size) {
		limitSize = size;
		isLimit = true;
		return this;
	}

}
