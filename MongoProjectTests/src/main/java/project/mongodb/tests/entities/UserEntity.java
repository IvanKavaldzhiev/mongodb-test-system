package project.mongodb.tests.entities;

import java.util.List;
import java.util.Map;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("users")
public class UserEntity {
	public UserEntity(String facultyNumber, String name, Map<Integer, List<String>> testWithMarkedAnswers) {
		this.facultyNumber = facultyNumber;
		this.name = name;
		this.testWithMarkedAnswers = testWithMarkedAnswers;
	}

	public UserEntity() {

	}

	public void updateMap(Integer testNumber, List<String> answers) {
		this.testWithMarkedAnswers.put(testNumber, answers);
	}

	public Map<Integer, List<String>> getMap() {
		return this.testWithMarkedAnswers;
	}

	public String getName() {
		return name;
	}

	@Id
	private String facultyNumber;

	private String name;
	private Map<Integer, List<String>> testWithMarkedAnswers;
}
