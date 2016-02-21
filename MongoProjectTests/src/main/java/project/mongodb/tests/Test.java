package project.mongodb.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;

import project.mongodb.tests.entities.CategoryEntity;
import project.mongodb.tests.entities.QuestionEntity;
import project.mongodb.tests.entities.TestEntity;

public class Test extends Database {
	public static void createTest(Integer id, List<QuestionEntity> questions) {
		TestEntity test = new TestEntity(id, questions);
		dataStore.save(test);
	}

	public static List<QuestionEntity> generateRandomQuestions(String categoryId, int numberOfQuestions) {
		List<QuestionEntity> questions = new ArrayList<QuestionEntity>();

		CategoryEntity category = dataStore.get(CategoryEntity.class, categoryId);
		questions = category.getQuestions();

		if (questions.size() < numberOfQuestions) {
			return questions;
		}

		List<QuestionEntity> generatedQuestions = new ArrayList<QuestionEntity>();

		Collections.shuffle(questions);

		for (int i = 0; i < numberOfQuestions; i++) {
			generatedQuestions.add(i, questions.get(i));
		}

		return generatedQuestions;
	}

	public static void printAllTests() {
		FindIterable<Document> iterator = tests.find();
		iterator.forEach(new Block<Document>() {
			public void apply(final Document document) {
				System.out.println(document);
			}
		});
	}

	public static int getNumberOfAllTest() {
		return (int) tests.count();
	}

	public static void dropTests() {
		tests.drop();
	}
}
