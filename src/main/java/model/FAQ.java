package model;

import java.util.ArrayList;

public class FAQ {

	private String question;
	private String answer;
	private ArrayList<String> concept;
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public ArrayList<String> getConcept() {
		return concept;
	}
	public void setConcept(ArrayList<String> concept) {
		this.concept = concept;
	}
}
