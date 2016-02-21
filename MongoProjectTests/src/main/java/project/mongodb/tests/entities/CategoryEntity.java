package project.mongodb.tests.entities;

import java.util.List;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("categories")
public class CategoryEntity {
	public CategoryEntity(String name, List<QuestionEntity> questions) {
		this.name = name;
		this.questions = questions;
	}

	public CategoryEntity() {

	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<QuestionEntity> getQuestions() {
		return questions;
	}

	public void setQuestions(List<QuestionEntity> questions) {
		this.questions = questions;
	}

	@Id
	String name;

	@Embedded
	List<QuestionEntity> questions;
}
