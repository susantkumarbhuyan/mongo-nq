package io.mongonq;

import java.util.ArrayList;
import java.util.List;

import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import io.mongonq.query.QueryBuilderUtil;
import io.mongonq.query.ResultsIterator;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;

/**
 * @author susant
 *
 */
public class Aggregate {
	private final MongoCollection<BsonDocument> mongoCollection;
	private List<Bson> operations = new ArrayList<>();
	private AggregationOptions option;

	public Aggregate(MongoCollection<BsonDocument> mongoCollection) {
		this.mongoCollection = mongoCollection;
	}

	public Aggregate and(String pipelineOperation, Object... parameters) {
		BsonDocument jsonQuery = QueryBuilderUtil.buildQueryDBObject(pipelineOperation, parameters);
		operations.add(jsonQuery);
		return this;
	}

	public <T> ResultsIterator<T> as(final Class<T> clazz) {
		AggregateIterable<BsonDocument> aggregate = mongoCollection.aggregate(operations, BsonDocument.class);
		if (option != null) {
			aggregate.allowDiskUse(option.getAllowDiskUse());
//			aggregate.batchSize(option.getBatchSize());
//			aggregate.maxTime(option.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
//			aggregate.collation(option.getCollation());
//			aggregate.bypassDocumentValidation(option.getBypassDocumentValidation());
		}
		return new ResultsIterator<T>(aggregate.iterator(), clazz);
	}

	public Aggregate options(AggregationOptions option) {
		this.option = option;
		return this;
	}
}
