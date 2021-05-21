package com.gennelloa0.project1.beans;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {
	private static final long serialVersionUID = 2624585471973830930L;
	private Integer id;
	private String question;
	private Forms subject;
	private List<String> answers;
	private String correctAnswer;
	private int points;
	
	public Question() {
		super();
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public Forms getSubject() {
		return subject;
	}
	public void setSubject(Forms subject) {
		this.subject = subject;
	}
	public List<String> getAnswers() {
		return answers;
	}
	public void setAnswers(List<String> answers) {
		this.answers = answers;
	}
	public String getCorrectAnswer() {
		return correctAnswer;
	}
	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((answers == null) ? 0 : answers.hashCode());
		result = prime * result + ((correctAnswer == null) ? 0 : correctAnswer.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + points;
		result = prime * result + ((question == null) ? 0 : question.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Question other = (Question) obj;
		if (answers == null) {
			if (other.answers != null)
				return false;
		} else if (!answers.equals(other.answers))
			return false;
		if (correctAnswer == null) {
			if (other.correctAnswer != null)
				return false;
		} else if (!correctAnswer.equals(other.correctAnswer))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (points != other.points)
			return false;
		if (question == null) {
			if (other.question != null)
				return false;
		} else if (!question.equals(other.question))
			return false;
		if (subject != other.subject)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Question [id=" + id + ", question=" + question + ", subject=" + subject + ", answers=" + answers
				+ ", correctAnswer=" + correctAnswer + ", points=" + points + "]";
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
}
