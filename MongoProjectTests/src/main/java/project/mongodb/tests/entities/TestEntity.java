package project.mongodb.tests.entities;

import java.util.List;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("tests")
public class TestEntity {
	public TestEntity(Integer id, List<QuestionEntity> questions) {
		this.id = id;
		this.questions = questions;
	}

	public TestEntity() {

	}

	public List<QuestionEntity> getQuestions() {
		return this.questions;
	}

	@Id
	Integer id;

	@Embedded
	List<QuestionEntity> questions;
}
