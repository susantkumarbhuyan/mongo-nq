package io.mongonq;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoDatabase;

/**
 * @author Susant Kumar
 * 
 * */
public class Jongo {
	private final MongoDatabase database;

	public Jongo(MongoDatabase database) {
		this.database = database;
	}

	public MongoCollection getCollection(String mongoCollection) {
		return new MongoCollection(database.getCollection(mongoCollection, BasicDBObject.class));
	}

	public MongoDatabase getDatabase() {
		return database;
	}

}
