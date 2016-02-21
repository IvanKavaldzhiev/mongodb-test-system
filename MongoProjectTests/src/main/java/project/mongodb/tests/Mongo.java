package project.mongodb.tests;

import java.util.Scanner;

public class Mongo {

	public static void main(String[] args) {
		Database.setUpBase();

		UI();
	}

	public static void UI() {
		Scanner reader = new Scanner(System.in);
		String choice = "";
		System.out.println("Choose role - a for admin, u for user");
		choice = reader.nextLine();

		do {
			if (choice.equals("a")) {
				System.out.println(
						"Choose an operation - a for adding question, r for removing question, s for user statistics, q for question statistics, "
								+ "p for priting questions, f for searching questions"
								+ ", u for printing all users, qc for printing questions from a category, qsc for printing question stats for every category");
				choice = reader.nextLine();
				if (choice.equals("a")) {
					choice = addQuestion(reader, choice);
					if (choice.equals("e"))
						break;
				} else if (choice.equals("r")) {
					choice = removeQuestion(reader, choice);
					if (choice.equals("e"))
						break;
				} else if (choice.equals("s")) {
					choice = userStatistic(reader, choice);
					if (choice.equals("e"))
						break;
				} else if (choice.equals("q")) {
					choice = questionStatistic(reader, choice);
					if (choice.equals("e"))
						break;
				} else if (choice.equals("p")) {
					choice = printQuestions(reader, choice);
					if (choice.equals("e")) {
						break;
					}
				} else if (choice.equals("f")) {
					System.out.println("Enter text to match: ");
					String text = reader.nextLine();
					choice = searchForQuestions(reader, choice, text);
					if (choice.equals("e"))
						break;
				} else if (choice.equals("u")) {
					choice = printUsers(reader, choice);
					if (choice.equals("e")) {
						break;
					}
				} else if (choice.equals("qc")) {
					System.out.println("Enter category: ");
					String category = reader.nextLine();
					choice = printQuestionsFromCategory(reader, choice, category);
					if (choice.equals("e")) {
						break;
					}
				} else if (choice.equals("qsc")) {
					choice = printQuestionsStatsForEveryCategory(reader, choice);
					if (choice.equals("e")) {
						break;
					}
				}
			} else if (choice.equals("u")) {
				System.out.println("Choose operation - t for making a new test, s for statistics");
				choice = reader.nextLine();
				if (choice.equals("t")) {
					choice = makeTest(reader, choice);
					if (choice.equals("e"))
						break;
				} else if (choice.equals("s")) {
					choice = userStatistic(reader, choice);
					if (choice.equals("e"))
						break;
				}
			}
		} while (true);

		reader.close();
	}

	private static String addQuestion(Scanner reader, String choice) {
		Question.addQuestion(reader);
		System.out.println("Choose operation - e for end, m to go to main menu");
		choice = reader.nextLine();
		reader.nextLine();
		if (choice.equals("m")) {
			return printMainMenu(reader, choice);
		}
		return "e";
	}

	private static String removeQuestion(Scanner reader, String choice) {
		System.out.println("Choose id of question: ");
		String questionId = reader.nextLine();
		Question.removeQuestion(questionId);

		System.out.println("Choose operation - e for end, m to go to main menu");
		choice = reader.nextLine();
		reader.nextLine();
		if (choice.equals("m")) {
			return printMainMenu(reader, choice);
		}
		return "e";
	}

	private static String questionStatistic(Scanner reader, String choice) {
		System.out.println("Choose id of question: ");
		String questionId1 = reader.nextLine();

		Question.printQuestionStatistic(questionId1);

		System.out.println("Choose operation - e for end, m to go to main menu");
		choice = reader.nextLine();
		if (choice.equals("m")) {
			return printMainMenu(reader, choice);
		}
		return "e";

	}

	private static String makeTest(Scanner reader, String choice) {
		User.generateTestCheckItAndPrintResult();
		System.out.println("Choose operation - e for end, m to go to main menu");
		choice = reader.nextLine();
		if (choice.equals("m")) {
			return printMainMenu(reader, choice);
		}
		return "e";
	}

	private static String userStatistic(Scanner reader, String choice) {
		System.out.println("Choose id of a user: ");
		String userId = reader.nextLine();
		User.printUserStatistics(userId);

		System.out.println("Choose operation - e for end, m to go to main menu");
		choice = reader.nextLine();
		if (choice.equals("m")) {
			return printMainMenu(reader, choice);
		}
		return "e";
	}

	private static String printQuestions(Scanner reader, String choice) {
		Question.printAllQuestions();

		System.out.println("Choose operation - e for end, m to go to main menu");
		choice = reader.nextLine();
		if (choice.equals("m")) {
			return printMainMenu(reader, choice);
		}
		return "e";
	}

	private static String searchForQuestions(Scanner reader, String choice, String text) {
		Question.searchForQuestion(text);

		System.out.println("Choose operation - e for end, m to go to main menu");
		choice = reader.nextLine();
		if (choice.equals("m")) {
			return printMainMenu(reader, choice);
		}
		return "e";
	}

	private static String printUsers(Scanner reader, String choice) {
		User.printAllUsers();

		System.out.println("Choose operation - e for end, m to go to main menu");
		choice = reader.nextLine();
		if (choice.equals("m")) {
			return printMainMenu(reader, choice);
		}
		return "e";
	}

	private static String printQuestionsFromCategory(Scanner reader, String choice, String category) {
		Question.printQuestionsFromCategory(category);

		System.out.println("Choose operation - e for end, m to go to main menu");
		choice = reader.nextLine();
		if (choice.equals("m")) {
			return printMainMenu(reader, choice);
		}
		return "e";
	}

	private static String printQuestionsStatsForEveryCategory(Scanner reader, String choice) {
		Question.printQuestionsStatsForCategory();

		System.out.println("Choose operation - e for end, m to go to main menu");
		choice = reader.nextLine();
		if (choice.equals("m")) {
			return printMainMenu(reader, choice);
		}
		return "e";
	}

	private static String printMainMenu(Scanner reader, String choice) {
		System.out.println("Choose role - a for admin, u for user");
		choice = reader.nextLine();
		return choice;
	}
}