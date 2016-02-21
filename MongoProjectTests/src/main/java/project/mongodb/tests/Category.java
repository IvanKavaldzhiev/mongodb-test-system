package project.mongodb.tests;

import java.util.List;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;

import project.mongodb.tests.entities.CategoryEntity;
import project.mongodb.tests.entities.QuestionEntity;

public class Category extends Database {
	public static void printFullCategories() {
		FindIterable<Document> iterator2 = categories.find();
		iterator2.forEach(new Block<Document>() {
			public void apply(final Document document) {
				System.out.println(document);
			}
		});
	}

	public static CategoryEntity getCategory(String name) {
		CategoryEntity category = dataStore.get(CategoryEntity.class, name);
		return category;
	}

	public static void createCategory(String name, List<QuestionEntity> questions) {
		CategoryEntity category = new CategoryEntity(name, questions);
		dataStore.save(category);
	}

	public static void deleteCategories() {
		categories.drop();
	}
}
