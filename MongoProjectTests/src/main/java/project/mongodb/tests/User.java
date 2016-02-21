package project.mongodb.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;

import project.mongodb.tests.entities.QuestionEntity;
import project.mongodb.tests.entities.TestEntity;
import project.mongodb.tests.entities.UserEntity;

public class User extends Database {
	public static void generateTestCheckItAndPrintResult() {
		Scanner input = new Scanner(System.in);
		System.out.println("Enter name: ");
		String name = input.nextLine();

		System.out.println("Enter faculty number: ");
		String facultyNumber = input.nextLine();

		List<String> listOfCategories = new ArrayList<String>();
		System.out.println("Enter list of categories: ");
		String categories = input.nextLine();

		splitStringAndFillList(listOfCategories, categories);

		List<String> numberOfQuestionsForEachCategory = new ArrayList<String>();
		System.out.println("Enter corresponding number of questions: ");
		String numberOfQuestions = input.nextLine();

		splitStringAndFillList(numberOfQuestionsForEachCategory, numberOfQuestions);

		List<QuestionEntity> testQuestions = new ArrayList<QuestionEntity>();
		for (int i = 0; i < listOfCategories.size(); i++) {
			if (listOfCategories.size() != numberOfQuestionsForEachCategory.size()) {
				System.out.println("Wrong input! Try again.");
			}
			testQuestions.addAll(Test.generateRandomQuestions(listOfCategories.get(i),
					Integer.valueOf(numberOfQuestionsForEachCategory.get(i))));
		}

		int testId = Test.getNumberOfAllTest() + 1;

		List<String> answers = enterAnswers(testQuestions, input);
		UserEntity foundUser = dataStore.get(UserEntity.class, facultyNumber);

		if (foundUser == null) {
			Map<Integer, List<String>> testWithAnswers = new HashMap<Integer, List<String>>();
			testWithAnswers.put(testId, answers);
			createUser(facultyNumber, name, testWithAnswers);

		} else {
			testId = numberOfTestsOfUser(facultyNumber) + Integer.valueOf(facultyNumber) * 2;
			foundUser.updateMap(testId, answers);
			dataStore.save(foundUser);
		}

		Test.createTest(testId, testQuestions);

		showResult(answers, testQuestions);
	}

	public static List<String> enterAnswers(List<QuestionEntity> questions, Scanner input) {
		List<String> answers = new ArrayList<String>();

		for (int i = 0; i < questions.size(); i++) {
			System.out.println(questions.get(i).getText());
			String answer = input.nextLine();
			answers.add(answer);
		}
		return answers;
	}

	public static void createUser(String facultyNumber, String name, Map<Integer, List<String>> testWithAnswers) {
		UserEntity user = new UserEntity(facultyNumber, name, testWithAnswers);
		dataStore.save(user);
	}

	public static void printUserStatistics(String userId) {
		printNumberOfAllCorrectAnswers(userId);

		printNumberOfAllWrongAnswers(userId);

		printBestCategory(userId);

		printWorstCategory(userId);

		printAverageCorrectAnswers(userId);

		System.out.println();
	}

	public static void showResult(List<String> answers, List<QuestionEntity> questionsOfTest) {
		int[] results = results(answers, questionsOfTest);
		System.out.println("Your result is : ");
		System.out.printf("%d correct answers, %d wrong answers", results[0], results[1]);
		System.out.println();
	}

	public static int calculateNumberOfAnswers(String userId, Integer index) {
		int totalCorrectAnswers = 0, totalWrongAnswers = 0;
		int[] results;
		int[] totalResults = { totalCorrectAnswers, totalWrongAnswers };
		UserEntity user = dataStore.get(UserEntity.class, userId);
		Map<Integer, List<String>> testWithAnswers = user.getMap();
		for (Map.Entry<Integer, List<String>> entry : testWithAnswers.entrySet()) {
			Integer testId = entry.getKey();
			List<String> answers = entry.getValue();
			TestEntity test = dataStore.get(TestEntity.class, testId);
			List<QuestionEntity> questionsOfTest = test.getQuestions();
			results = results(answers, questionsOfTest);
			totalResults[0] += results[0];
			totalResults[1] += results[1];
		}

		return totalResults[index];
	}

	public static int[] results(List<String> answers, List<QuestionEntity> questionsOfTest) {
		int numberOfCorrectAnswers = 0, numberOfWrongAnswers = 0;
		for (int i = 0; i < questionsOfTest.size(); i++) {
			if (questionsOfTest.get(i).getCorrectAnswer().equals(answers.get(i))) {
				numberOfCorrectAnswers++;
			} else {
				numberOfWrongAnswers++;
			}
		}
		int[] results = { numberOfCorrectAnswers, numberOfWrongAnswers };
		return results;
	}

	public static void printNumberOfAllCorrectAnswers(String userId) {
		System.out.printf("The number of all corect answers is %d \n", calculateNumberOfAnswers(userId, 0));
	}

	public static void printNumberOfAllWrongAnswers(String userId) {
		System.out.printf("The number of all wrong answers is %d \n", calculateNumberOfAnswers(userId, 1));
	}

	public static void printBestCategory(String userId) {
		System.out.println("The student's best category is " + printCategory(userId, 0));
	}

	public static void printWorstCategory(String userId) {
		System.out.println("The student's worst category is " + printCategory(userId, 1));
	}

	public static String printCategory(String userId, int index) {
		UserEntity user = dataStore.get(UserEntity.class, userId);
		Map<Integer, List<String>> testWithAnswers = user.getMap();
		if (testWithAnswers.isEmpty()) {
			return "The student hasn't made any tests yet.";
		}

		String[] bestAndWorstCategory = getBestAndWorstCategory(userId);
		return bestAndWorstCategory[index];
	}

	public static void printAverageCorrectAnswers(String name) {
		UserEntity question = dataStore.get(UserEntity.class, name);
		Map<Integer, List<String>> testWithAnswers = question.getMap();

		for (Map.Entry<Integer, List<String>> entry : testWithAnswers.entrySet()) {
			Integer testId = entry.getKey();
			List<String> answers = entry.getValue();
			TestEntity test = dataStore.get(TestEntity.class, testId);
			List<QuestionEntity> questionsOfTest = test.getQuestions();
			System.out.printf("Average percent of correct answer for test number %d is %.2f \n", testId,
					averagePercent(answers, questionsOfTest));
		}
	}

	public static double averagePercent(List<String> answers, List<QuestionEntity> questionsOfTest) {
		int[] results = results(answers, questionsOfTest);
		double[] convertedResults = new double[2];
		convertedResults[0] = Integer.valueOf(results[0]).doubleValue();
		convertedResults[1] = Integer.valueOf(results[1]).doubleValue();
		return (convertedResults[0] / ((convertedResults[0] + convertedResults[1])));
	}

	public static Map<Integer, List<String>> getTestWithAnswers(String userId) {
		UserEntity user = dataStore.get(UserEntity.class, userId);
		return user.getMap();
	}

	public static List<String> getAllUniqueUserIds() {
		final List<String> userIds = new ArrayList<String>();
		FindIterable<Document> iterator = questions.find();
		iterator.forEach(new Block<Document>() {
			public void apply(final Document document) {
				String id = document.getString("_id");
				userIds.add(id);
			}
		});
		return userIds;
	}

	public static int[] numbersOfUniqueUsersWithCorrectAndWrongAnswer(String identificatorOfQuestion) {
		int usersWithCorrectAnswer = 0, usersWithWrongAnswer = 0;
		String correctAnswer = Question.getCorrectAnswer(identificatorOfQuestion);
		Map<List<QuestionEntity>, List<String>> allAnswersWithQuestions = getAllAnswersWithQuestionsFromAllUsers();
		for (Map.Entry<List<QuestionEntity>, List<String>> entry : allAnswersWithQuestions.entrySet()) {
			List<QuestionEntity> questions = entry.getKey();
			List<String> answers = entry.getValue();
			for (int i = 0; i < questions.size(); i++) {
				if (questions.get(i).getIdentificator().equals(identificatorOfQuestion)) {
					if (answers.get(i).equals(correctAnswer)) {
						usersWithCorrectAnswer++;
					} else {
						usersWithWrongAnswer++;
					}
				}
			}
		}
		int[] usersWithCorrectAndWrongAnswer = new int[2];
		usersWithCorrectAndWrongAnswer[0] = usersWithCorrectAnswer;
		usersWithCorrectAndWrongAnswer[1] = usersWithWrongAnswer;
		return usersWithCorrectAndWrongAnswer;
	}

	public static void printAllUsers() {
		FindIterable<Document> iterator = users.find();
		iterator.forEach(new Block<Document>() {
			public void apply(final Document document) {
				System.out.println(document);
			}
		});
	}

	public static void dropAllUsers() {
		users.drop();
	}

	public static Map<List<QuestionEntity>, List<String>> getAllAnswersWithQuestionsFromAllUsers() {
		final Map<List<QuestionEntity>, List<String>> allAnswersWithQuestionsFromAllUsers = new HashMap<List<QuestionEntity>, List<String>>();
		FindIterable<Document> iterator = users.find();
		iterator.forEach(new Block<Document>() {
			public void apply(final Document document) {
				String identificator = document.getString("_id");
				allAnswersWithQuestionsFromAllUsers.putAll(getAllAnswersWithQuestionsFromUser(identificator));
			}
		});
		return allAnswersWithQuestionsFromAllUsers;
	}

	private static void splitStringAndFillList(List<String> list, String stringToSplit) {
		String[] splitNumberOfQuestions = stringToSplit.split(" ");
		for (String number : splitNumberOfQuestions) {
			list.add(number);
		}
	}

	private static int numberOfTestsOfUser(String id) {
		UserEntity user = dataStore.get(UserEntity.class, id);
		Map<Integer, List<String>> testWithAnswers = user.getMap();
		return testWithAnswers.size();
	}

	private static Map<List<QuestionEntity>, List<String>> getAllAnswersWithQuestionsFromUser(String userId) {
		Map<Integer, List<String>> testWithAnswers = getTestWithAnswers(userId);
		Map<List<QuestionEntity>, List<String>> allAnswersWithQuestions = new HashMap<List<QuestionEntity>, List<String>>();
		for (Map.Entry<Integer, List<String>> entry : testWithAnswers.entrySet()) {
			Integer testId = entry.getKey();
			List<String> answers = entry.getValue();
			TestEntity test = dataStore.get(TestEntity.class, testId);
			List<QuestionEntity> questionsOfTest = test.getQuestions();
			allAnswersWithQuestions.put(questionsOfTest, answers);
		}

		return allAnswersWithQuestions;
	}

	private static void initializeMap(Map<String, Integer> hashMap, Map<Integer, List<String>> testWithAnswers) {
		for (Map.Entry<Integer, List<String>> entry : testWithAnswers.entrySet()) {
			Integer testId = entry.getKey();
			TestEntity test = dataStore.get(TestEntity.class, testId);

			List<QuestionEntity> testQuestions = test.getQuestions();
			for (int i = 0; i < testQuestions.size(); i++) {
				List<String> categories = testQuestions.get(i).getListOfCategories();

				for (int j = 0; j < categories.size(); j++) {
					hashMap.put(categories.get(j), 0);
				}
			}
		}
	}

	private static String[] getBestAndWorstCategory(String userId) {
		UserEntity user = dataStore.get(UserEntity.class, userId);
		final Map<String, Integer> categoryCounter = new HashMap<String, Integer>();

		Map<Integer, List<String>> testWithAnswers = user.getMap();

		initializeMap(categoryCounter, testWithAnswers);

		for (Map.Entry<Integer, List<String>> entry : testWithAnswers.entrySet()) {
			Integer testId = entry.getKey();
			List<String> answers = entry.getValue();

			TestEntity test = dataStore.get(TestEntity.class, testId);

			List<QuestionEntity> testQuestions = test.getQuestions();
			for (int i = 0; i < testQuestions.size(); i++) {

				List<String> categories = testQuestions.get(i).getListOfCategories();
				for (int j = 0; j < categories.size(); j++) {
					if (testQuestions.get(i).getCorrectAnswer().equals(answers.get(i))) {
						categoryCounter.put(categories.get(j), categoryCounter.get(categories.get(j)) + 1);
					}
				}
			}
		}

		String bestCategory = null;
		String worstCategory = null;
		int comparator = 0;

		for (Map.Entry<String, Integer> entry : categoryCounter.entrySet()) {
			int value = entry.getValue();
			if (value > comparator) {
				bestCategory = entry.getKey();
			}
			worstCategory = entry.getKey();

			comparator = value;
		}

		if (bestCategory == null) {
			bestCategory = "none";
		}

		String[] bestAndWorstCategories = new String[] { bestCategory, worstCategory };
		return bestAndWorstCategories;
	}
}
