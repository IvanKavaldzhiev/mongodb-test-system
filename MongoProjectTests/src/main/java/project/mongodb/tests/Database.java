package project.mongodb.tests;

import org.bson.Document;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import project.mongodb.tests.entities.QuestionEntity;

public class Database {
	public final static void setUpBase() {
		mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase database = mongoClient.getDatabase(DATABASE);
		questions = database.getCollection("questions");
		categories = database.getCollection("categories");
		users = database.getCollection("users");
		tests = database.getCollection("tests");

		morphia = new Morphia();
		morphia.map(QuestionEntity.class);
		morphia.mapPackage("package project.mongodb.tests.entities");

		dataStore = morphia.createDatastore(mongoClient, DATABASE);
		dataStore.ensureIndexes();
	}

	protected final static String DATABASE = "testsProject";
	protected static MongoClient mongoClient;
	protected static MongoDatabase database;
	protected static Datastore dataStore;
	protected static Morphia morphia;
	protected static MongoCollection<Document> questions;
	protected static MongoCollection<Document> categories;
	protected static MongoCollection<Document> users;
	protected static MongoCollection<Document> tests;
}
