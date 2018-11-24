package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Utils {
	
	static public List<String> tokenize(String input){
		return Arrays.asList(input.split(" "));
	}
	
	public static double similarity(List<String> a, List<String> b) {
	    Set<String> s1 = new HashSet<String>();
	    for (int i = 0; i < a.size(); i++) {
	        s1.add(a.get(i));
	    }
	    Set<String> s2 = new HashSet<String>();
	    for (int i = 0; i < b.size(); i++) {
	        s2.add(b.get(i));
	    }

	    final int sa = s1.size();
	    final int sb = s2.size();
	    s1.retainAll(s2);
	    final int intersection = s1.size();
	    return 1d / (sa + sb - intersection) * intersection;
	}
	public static List<String> removeStopwords(List<String> tokens) {
		String[] stopwords = {
		                       "a",
		                       "an",
		                       "and",
		                       "are",
		                       "as",
		                       "at",
		                       "be",
		                       "but",
		                       "by",
		                       "can",
		                       "for","if",
		                       "in",
		                       "into",
		                       "is",
		                       "it",
		                       "no",
		                       "not",
		                       "of",
		                       "on",
		                       "or",
		                       "such",
		                       "that",
		                       "the",
		                       "their",
		                       "then",
		                       "there",
		                       "these",
		                       "they",
		                       "this",
		                       "to",
		                       "was",
		                       "will",
		                       "with"
		};
		List<String> swList = Arrays.asList(stopwords);
		List<String> swRemovedTokens = new ArrayList<String>();
		for(String token : tokens) {
			if(!swList.contains(token)) {
				swRemovedTokens.add(token);
			}
		}
		
		return swRemovedTokens;
	}
}
