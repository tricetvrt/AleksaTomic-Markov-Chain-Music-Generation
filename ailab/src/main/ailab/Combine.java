package main.ailab;

import java.util.Arrays;
import java.util.Map;

public class Combine {

		// a function that combines the notes and their lengths into another array
	 	public String[] addIntsToStrings(String[] notes, Integer[] lengths) throws Exception {// adds the lengths to the genarated notes
	        if (notes.length < lengths.length) {
	        	// finish the rhythms with the last melody note
	        	// border case if the length of notes in the patterns generated is longer than the melody generated
	            lengths = Arrays.copyOfRange(lengths, 0, notes.length);
	        }
	        else if(notes.length != lengths.length)
	        	throw new Exception("The generated sequences' lengths do not match");
	        String[] result = new String[notes.length];
	        for (int i = 0; i < notes.length; i++) {
	        	if(lengths[i]>1)
	        		result[i] = notes[i] + lengths[i];
	        	else
	        		result[i] = notes[i];
	        }

	        return result;
	    }
	    
	 	// a function which combines the generated melody and rhythm into a matrix where every row is one bar
	    public String[][] combineMelodyRhythm(int[] generatedMelody, int[] generatedRhythm, Map<Integer, Integer[]> patterns ) throws Exception {
	    	 int helper = 0;
	    	 String[][] result = new String[generatedRhythm.length][];
	    	 String[] generatedMelodyString = new String[generatedMelody.length];
	    	 ABCModifier a  = new ABCModifier();
	    	 
	    	 for(int i=0;i<generatedMelodyString.length ; i++) {
	    		// converting the generated melody from an int array into a String array of notes
	    		 generatedMelodyString[i] = a.intToNote(generatedMelody[i]); 
	    	 }
	    	 
	    	 
	    	 // adds the lengths of notes to the notes
	    	 // takes care of pattern lengths, and ensures that every note is a part of one pattern, even if the last pattern is not fully completed
	    	for(int i =0; i < generatedRhythm.length ; i++ ) {
	    		if(patterns.containsKey(generatedRhythm[i])) {
	    			int patternLength = patterns.get(generatedRhythm[i]).length;
	    			if (helper + patternLength > generatedMelodyString.length) {
	                    patternLength = generatedMelodyString.length - helper;
	                }
	    			String[] var = Arrays.copyOfRange(generatedMelodyString, helper, helper + patternLength);
	    			helper += patternLength;
	    			result[i]= addIntsToStrings(var, patterns.get(generatedRhythm[i])); // we get the row (bar) by using the addIntsToStrings method

	    		}
	    		else
	    			throw new Exception("invalid rhythm pattern key");
	    		
	    	}
	    	return result; 
	    }
	    
	    
	    // returns an ABC notation code based on the generated melody and the generated rhythm, uses the previous functions
	    public String returnAbcNotation(int[] generatedMelody, int[] generatedRhythm, String timeSignature ) throws Exception {
	    	RhythmGenerator rhythm = new RhythmGenerator();
	    	Map<Integer, Integer[]> patterns = rhythm.getPatternDictionary(timeSignature);
	    	String[][] bars = combineMelodyRhythm(generatedMelody, generatedRhythm, patterns);
	    	String result = "T: Generated melody\n"
	    			+ "M: " + timeSignature + " \n" // putting the chosen time signature
	        		+ "L: 1/16\r\n"
	        		+ "K: Cm\r\n" 
	        		+ "| ";
	    	for(int i=0;i< bars.length;i++) {
	    		for(int j=0; j < bars[i].length ; j++) {
	    			result += bars[i][j] + " ";
	    		}
	    		result+= "| "; // separating bars
	    		if( (i+1)%3==0)
	    			result += "\n";
	    	}
	    	return result;
	    }
}
