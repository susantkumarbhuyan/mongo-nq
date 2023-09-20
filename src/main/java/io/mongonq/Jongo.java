package io.mongonq;

import org.bson.BsonDocument;

import com.mongodb.client.MongoDatabase;

/**
 * 
 *  @author Susant Kumar
 *  @since   1.0
 *  
 **/
public class Jongo {
	private final MongoDatabase database;

	public Jongo(MongoDatabase database) {
		this.database = database;
	}

	public MongoCollection getCollection(String mongoCollection) {
		return new MongoCollection(database.getCollection(mongoCollection, BsonDocument.class));
	}

	public MongoDatabase getDatabase() {
		return database;
	}

}
