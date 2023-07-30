package io.mongonq;

import java.util.ArrayList;
import java.util.List;

import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import io.mongonq.query.QueryBuilderUtil;
import io.mongonq.query.ResultsIterator;

import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;


/**
 * @author susant
 *
 */
public class Aggregate {
	private final MongoCollection<BasicDBObject> mongoCollection;
	private List<Bson> operations = new ArrayList<>();
	private String jsonQuery;
	private AggregationOptions option;

	public Aggregate(MongoCollection<BasicDBObject> mongoCollection) {
		this.mongoCollection = mongoCollection;
	}

	public Aggregate and(String pipelineOperator, Object... parameters) {

		jsonQuery = QueryBuilderUtil.createQuery(pipelineOperator, parameters);
		operations.add(BasicDBObject.parse(jsonQuery));
		return this;
	}

	public <T> ResultsIterator<T> as(final Class<T> clazz) {
		AggregateIterable<BsonDocument> aggregate = mongoCollection.aggregate(operations, BsonDocument.class);
		if (option != null) {
			aggregate.allowDiskUse(option.getAllowDiskUse());
//			if(option.getBatchSize() != null)
//			aggregate.batchSize(option.getBatchSize());
//			if(option.getMaxTime(TimeUnit.MILLISECONDS) )
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
