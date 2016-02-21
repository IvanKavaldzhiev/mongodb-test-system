package project.mongodb.tests;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.bson.Document;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;

import project.mongodb.tests.entities.CategoryEntity;
import project.mongodb.tests.entities.QuestionEntity;

public class Question extends Database {
	public static void addQuestion(Scanner input) {
		System.out.println("Enter content of the answer: ");
		String content = input.nextLine();

		List<String> listOfAnswers = new ArrayList<String>();

		System.out.println("Enter answers: ");
		String answers = input.nextLine();
		String[] splitAnswers = answers.split(" ");
		for (String answer : splitAnswers) {
			listOfAnswers.add(answer);
		}

		System.out.println("Enter correct answer: ");
		String correctAnswer = input.nextLine();

		List<String> listOfCategories = new ArrayList<String>();

		System.out.println("Enter categories: ");
		String categoriesForQuestion = input.nextLine();
		String[] splitCategories = categoriesForQuestion.split(" ");
		for (String category : splitCategories) {
			listOfCategories.add(category);
		}

		System.out.println("Enter difficulty between 1 and 5: ");
		int difficulty = input.nextInt();
		input.nextLine();
		System.out.println("Enter date of adding: ");
		String date = input.nextLine();

		String identificator = "";
		for (int i = 0; i < listOfCategories.size(); i++) {
			identificator = identificator + String.valueOf(numberOfQuestionsInCategory(listOfCategories.get(i)) + 1)
					+ getFirstLetter(listOfCategories.get(i));
		}

		QuestionEntity question = new QuestionEntity(content, listOfAnswers, correctAnswer, listOfCategories,
				difficulty, date, identificator);

		addQuestionToCategories(listOfCategories, question);

		dataStore.save(question);
	}

	public static void removeQuestion(String questionId) {
		QuestionEntity question = dataStore.get(QuestionEntity.class, questionId);
		List<String> listOfCategories = question.getListOfCategories();

		for (int i = 0; i < listOfCategories.size(); i++) {
			Query<CategoryEntity> updateQuery = dataStore.createQuery(CategoryEntity.class).field(Mapper.ID_KEY)
					.equal(listOfCategories.get(i));

			UpdateOperations<CategoryEntity> ops = dataStore.createUpdateOperations(CategoryEntity.class);
			ops.disableValidation();
			ops.removeAll("questions", question);
			dataStore.update(updateQuery, ops);
		}
		dataStore.delete(QuestionEntity.class, questionId);
	}

	public static void printAllQuestions() {
		FindIterable<Document> iterator = questions.find();
		iterator.forEach(new Block<Document>() {
			public void apply(final Document document) {
				String content = document.getString("text");
				String identificator = document.getString("_id");
				System.out.println(content + " " + identificator);
			}
		});
	}

	public static void printQuestionsFromCategory(String name) {
		CategoryEntity category = Category.getCategory(name);
		List<QuestionEntity> questions = category.getQuestions();
		for (int i = 0; i < questions.size(); i++) {
			System.out.println(questions.get(i).toString());
		}
	}

	public static void searchForQuestion(String text) {
		dataStore.ensureIndexes();
		Query<QuestionEntity> query = dataStore.createQuery(QuestionEntity.class).search(text).order("dateOfCreation");
		Iterator<QuestionEntity> iterator = query.iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}
	}

	public static void printQuestionsStatsForCategory() {
		FindIterable<Document> iterator = categories.find();
		iterator.forEach(new Block<Document>() {
			public void apply(final Document document) {
				System.out.printf("The category " + document.getString("_id") + " has %d questions. \n",
						numberOfQuestionsInCategory(document.getString("_id")));

				System.out.printf("The average difficulty of the questions in category " + document.getString("_id")
						+ " is %f. \n", averageDifficultyOfQuestions(document.getString("_id")));

			}
		});
	}

	public static int numberOfQuestionsInCategory(String categoryId) {
		CategoryEntity category = dataStore.get(CategoryEntity.class, categoryId);
		if (category == null) {
			return 0;
		}
		List<QuestionEntity> questions = category.getQuestions();
		return questions.size();
	}

	public static double averageDifficultyOfQuestions(String categoryId) {
		int numberOfQuestions = 0, sumOfDifficulties = 0;
		CategoryEntity category = dataStore.get(CategoryEntity.class, categoryId);
		List<QuestionEntity> questions = category.getQuestions();
		numberOfQuestions = questions.size();
		for (int i = 0; i < questions.size(); i++) {
			sumOfDifficulties = sumOfDifficulties + questions.get(i).getDifficulty();
		}
		return sumOfDifficulties / numberOfQuestions;
	}

	public static void printFullQuestions() {
		FindIterable<Document> iterator = questions.find();
		iterator.forEach(new Block<Document>() {
			public void apply(final Document document) {
				System.out.println(document);
			}
		});
	}

	public static void dropQuestions() {
		questions.drop();
	}

	public static void printQuestionStatistic(String questionId) {
		printNumberOfUniqueUsersWithCorrectAnswer(questionId);

		printNumberOfUniqueUsersWithWrongAnswer(questionId);
	}

	public static void printNumberOfUniqueUsersWithCorrectAnswer(String questionId) {
		int[] numberOfUniqueUsers = User.numbersOfUniqueUsersWithCorrectAndWrongAnswer(questionId);
		System.out.printf("The number of unique users selected the correct answer is %d. \n", numberOfUniqueUsers[0]);
	}

	public static void printNumberOfUniqueUsersWithWrongAnswer(String questionId) {
		int[] numberOfUniqueUsers = User.numbersOfUniqueUsersWithCorrectAndWrongAnswer(questionId);
		System.out.printf("The number of unique users selected the wrong answer is %d. \n", numberOfUniqueUsers[1]);
	}

	public static String getCorrectAnswer(String identificator) {
		QuestionEntity question = dataStore.get(QuestionEntity.class, identificator);
		return question.getCorrectAnswer();
	}

	private static String getFirstLetter(String category) {
		String firstLetter = "";
		firstLetter = category.substring(0, 1).toUpperCase();
		return firstLetter;
	}

	private static void addQuestionToCategories(List<String> listOfCategories, QuestionEntity question) {
		List<QuestionEntity> questions = new ArrayList<QuestionEntity>();
		questions.add(question);

		for (int i = 0; i < listOfCategories.size(); i++) {
			Query<CategoryEntity> updateQuery = dataStore.createQuery(CategoryEntity.class).field(Mapper.ID_KEY)
					.equal(listOfCategories.get(i));

			if (updateQuery.countAll() == 0) {
				Category.createCategory(listOfCategories.get(i), questions);
				System.out.println("dfdf");
			}

			UpdateOperations<CategoryEntity> ops = dataStore.createUpdateOperations(CategoryEntity.class);
			ops.disableValidation();
			ops.add("questions", question);
			dataStore.update(updateQuery, ops);
		}
	}
}
