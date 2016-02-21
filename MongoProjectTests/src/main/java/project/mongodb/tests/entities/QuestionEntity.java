package project.mongodb.tests.entities;

import java.util.List;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.utils.IndexType;

@Entity("questions")

@Indexes(@Index(fields = { @Field(value = "text", type = IndexType.TEXT),
		@Field(value = "listOfAnswers", type = IndexType.TEXT) }))
public class QuestionEntity {
	public QuestionEntity(String text, List<String> listOfAnswers, String correctAnswer, List<String> listOfCategories,
			Integer difficulty, String dateOfCreation, String identificator) {
		this.setText(text);
		this.setListOfAnswers(listOfAnswers);
		this.setCorrectAnswer(correctAnswer);
		this.setListOfCategories(listOfCategories);
		this.setDifficulty(difficulty);
		this.setDateOfCreation(dateOfCreation);
		this.identificator = identificator;
	}

	QuestionEntity() {

	}

	@Override
	public String toString() {
		return "QuestionEntity [identificator=" + identificator + ", listOfCategories=" + listOfCategories
				+ ", listOfAnswers=" + listOfAnswers + ", text=" + text + ", correctAnswer=" + correctAnswer
				+ ", difficulty=" + difficulty + ", dateOfCreation=" + dateOfCreation + "]";
	}

	public List<String> getListOfCategories() {
		return listOfCategories;
	}

	public void setListOfCategories(List<String> listOfCategories) {
		this.listOfCategories = listOfCategories;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<String> getListOfAnswers() {
		return listOfAnswers;
	}

	public void setListOfAnswers(List<String> listOfAnswers) {
		this.listOfAnswers = listOfAnswers;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public Integer getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Integer difficulty) {
		this.difficulty = difficulty;
	}

	public String getDateOfCreation() {
		return dateOfCreation;
	}

	public void setDateOfCreation(String dateOfCreation) {
		this.dateOfCreation = dateOfCreation;
	}

	public String getIdentificator() {
		return this.identificator;
	}

	@Id
	private String identificator;

	private List<String> listOfCategories;
	private List<String> listOfAnswers;
	private String text;
	private String correctAnswer;
	private Integer difficulty;
	private String dateOfCreation;
}
