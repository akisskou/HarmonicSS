package utilities;

public class StringProcessing {

	
	public static int countMatches(String someString, char someChar) {

	int count = 0;
	  
	for (int i = 0; i < someString.length(); i++) {
	    if (someString.charAt(i) == someChar) {
	        count++;
	    }
	}
	
	return count;}
}
