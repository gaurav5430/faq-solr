package utils;

public class Test {
	
	public static void main(String[] args) {
		String queryText = "is 2.7 version of rasa nlu ok";
		String question = "which version of rasa nlu am i working on";
		
		double result = Utils.similarity(
				Utils.removeStopwords(Utils.tokenize(queryText)),
				Utils.removeStopwords(Utils.tokenize(question)));
		
		System.out.println(result);
	}

}
