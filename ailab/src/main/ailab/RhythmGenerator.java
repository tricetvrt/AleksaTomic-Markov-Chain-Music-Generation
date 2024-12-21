package main.ailab;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RhythmGenerator {
	public static final Map<Integer, Integer[]> patterns_4_4 = new HashMap<>();
	private static final Map<Integer, Integer[]> patterns_3_4 = new HashMap<>();
	private static final Map<Integer, Integer[]> patterns_2_4 = new HashMap<>();
	public static final Map<Integer, Integer[]> patterns_7_8 = new HashMap<>();
	private static final Map<Integer, Integer[]> patterns_6_8 = new HashMap<>();
	private static final Map<Integer, Integer[]> patterns_12_8 = new HashMap<>();//maybe no need

    static {
    	patterns_4_4.put(0, new Integer[]{4,4,4,4});
    	patterns_4_4.put(1, new Integer[]{8,8});
    	patterns_4_4.put(2, new Integer[]{2,4,2,8});
    	patterns_4_4.put(3, new Integer[]{2,2,4,8});
    	patterns_4_4.put(4, new Integer[]{4,2,2,8});
    	patterns_4_4.put(5, new Integer[]{2,2,2,2,2,2,2,2});
    	
    	patterns_4_4.put(6, new Integer[]{2,2,4,2,2,4});
    	patterns_4_4.put(7, new Integer[]{2,2,2,4,2,4});
    	patterns_4_4.put(8, new Integer[]{6,2,4,4});
    	patterns_4_4.put(9, new Integer[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1});
    	patterns_4_4.put(10, new Integer[]{16});
    	patterns_4_4.put(11, new Integer[]{2,2,2,2,2,2,4});
    	patterns_4_4.put(12, new Integer[]{2,4,2,4,4});
    	patterns_4_4.put(13, new Integer[]{2,2,2,2,4,4});
    	patterns_4_4.put(13, new Integer[]{4,2,2,4,4});
    	
    	
    }
    
    static {
    	patterns_7_8.put(0, new Integer[]{4,2,2,2,2,2});
    	patterns_7_8.put(1, new Integer[]{4,2,8});
    	patterns_7_8.put(2, new Integer[]{4, 2, 1, 1, 1, 1, 4});
    	patterns_7_8.put(3, new Integer[]{4, 2, 4, 4});
    	patterns_7_8.put(4, new Integer[]{2, 2, 2, 4, 4});
    	patterns_7_8.put(5, new Integer[]{6, 8});
    	patterns_7_8.put(6, new Integer[]{2, 4, 4, 4});
    	patterns_7_8.put(7, new Integer[]{10, 4});
    	patterns_7_8.put(8, new Integer[]{14});
    	patterns_7_8.put(9, new Integer[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1});
    	patterns_7_8.put(10, new Integer[]{1,1,2,2,2,1,1,4});
    	patterns_7_8.put(11, new Integer[]{1, 1, 2, 1, 1, 2, 2, 1, 1, 1, 1});
    	patterns_7_8.put(12, new Integer[]{2,2,2,2,2,2,2});
    	patterns_7_8.put(13, new Integer[]{1,1,1,1,1,1,1,1,1,1,2,2});
    	patterns_7_8.put(14, new Integer[]{2, 2, 2, 2, 1, 1, 1, 1, 2});
    	patterns_7_8.put(15, new Integer[]{6, 2, 2, 2, 2});
    	patterns_7_8.put(16, new Integer[]{2,2,2, 4, 2,2});
    	patterns_7_8.put(17, new Integer[]{2, 4, 4, 4});
    	
    	
    	
    }
    
    
    public int[][] abcToIntRhythmDatabase(String dataPath, String timeSignature) throws Exception{
		String content = "";
		String[] notations = null;
		
		try {
			content = Files.readString(Paths.get(dataPath));
			notations = content.split("\\$\\$\\$"); // splitting into individual notations
		} catch (IOException e) {
			throw new IOException("could not read file at" + dataPath, e);
		}
		List<int[]> result = new ArrayList<>();
		for(int i=0; i<notations.length; i++) {
			if(getTimeSignature(notations[i]).equals(timeSignature)){// filtering the dataset based on the given time signature
				result.add(abcToIntRhythm(notations[i], timeSignature));
				}// adding the pattern sequences to the list
		}
		int[][] result2 = new int[result.size()][]; // converting to int matrix so that training is possible
		for( int i = 0 ; i< result2.length ; i++) {
			result2[i] = result.get(i);
		}
		return result2;
	}
    
    
    
    public int[] abcToIntRhythm(String abcNotation, String timeSignature) throws Exception {
    	    String melody = extract(abcNotation);
    	    if (melody.isEmpty()) {
    	        throw new Exception("melody is empty after extraction");
    	    }
    	    String[] components = parseRhythm(melody);

    	    if (components.length == 0) {
    	        throw new Exception("components empty after parsing");
    	    }

    	    String[][] splitLengths = splitByBar(components);
    	    if (splitLengths.length == 0) {
    	        throw new Exception("bars empty after splitting by bars");
    	    }

    	    Integer[][] intLengths = new Integer[splitLengths.length][];
    	    int noteDem = getNoteLength(abcNotation);
    	    for (int i = 0; i < intLengths.length; i++) {
    	        intLengths[i] = barToLengthArray(splitLengths[i], noteDem);
    	    }

    	    
    	    return patternReader(intLengths, getPatternDictionary(timeSignature)); 


        
	}
    
    public String extract(String abcNotation) {
        String[] lines = abcNotation.split("\n");
        StringBuilder melody= new StringBuilder();
        for (int i=0; i<lines.length;i++) {
        	if (!lines[i].matches("^[A-Z]:.*")) // checks if the line matches  a capital letter followed by : and more possible content
        	{
        		melody.append(lines[i].trim());
                melody.append(" "); 
            }
        }

        return melody.toString().trim();//removing unecessary whitespace
    }
    
    
    public String[] parseRhythm(String melody) {
        String cleanmelody= melody.replace("|", " | "); 
        cleanmelody = cleanmelody.replaceAll("\"[A-Ga-g](m|#)?(m)?\"", "");
        cleanmelody = cleanmelody.replaceAll("[^A-Ga-gz0-9/|\\s]", "");
        cleanmelody = cleanmelody.replaceAll("([A-Ga-gz0-9/])([A-Ga-gz])", "$1 $2");
        cleanmelody = cleanmelody.replaceAll("([A-Ga-gz])([A-Ga-gz])", "$1 $2");
        cleanmelody = cleanmelody.replaceAll("(?<=^|\\s)[A-Ga-gz](?=\\s|$)", "1"); // if a letter is surrounded by spaces or at the start/end        
        cleanmelody = cleanmelody.replaceAll("[A-Ga-gz]", "");
        cleanmelody = cleanmelody.replaceAll(" / ", " 1/2 ");
        cleanmelody = cleanmelody.replaceAll(" /2 ", " 1/2 ");
        cleanmelody = cleanmelody.replaceAll(" / ", " 1/2 ");// i repeated the lines because of the edge case " / / / / / " it detects only every second " / "
        cleanmelody = cleanmelody.replaceAll(" /2 ", " 1/2 ");// find a more optimal version later without repeating the same lines
        cleanmelody = cleanmelody.replaceAll("\\s+", " ").trim();
        String[] notes= cleanmelody.split("\\s+"); 

        return notes;
    }
    
    public Integer[] barToLengthArray(String[] barLengths, int noteLengthDenominator) throws Exception {
    	//ill just need to separate the lengths to barsbased on | and then according to time signature add pattern to hashmap f its not there and add value to int array
    	Integer[] lengths = new Integer[barLengths.length];
    	double helper;
    	int mod = 16/ noteLengthDenominator;
    	for(int i = 0; i< barLengths.length ; i++) {
    		if(barLengths[i].contains("/")) {
    			String[] lengthParts = barLengths[i].split("/");
    			try {
    				double numerator = lengthParts[0].isEmpty() ? 1 : Double.parseDouble(lengthParts[0]); // if the numerator is missing, it is 1
                    helper =  numerator/ Double.parseDouble(lengthParts[1]);
                    if(mod>1)
                    	lengths[i] = (int) (helper*mod);
                    else
                    	lengths[i] = 1;
                } catch (NumberFormatException e) {
                    throw new Exception("invalid note length format: " + barLengths[i]);
                }
    		}
    		else {
    			try {
    				lengths[i] = Integer.parseInt(barLengths[i]) * mod;
    			} catch(NumberFormatException e){
    				throw new Exception("invalid note length format: " + barLengths[i]);
    			}
    		}
    	}
		return lengths;}
    
    
    public String getTimeSignature(String abcNotation) throws Exception {
    	abcNotation = abcNotation.replaceAll("\r","");
        String[] lines = abcNotation.split("\n");
        for (String line : lines) {
            if (line.startsWith("M:")) {
                return line.replace("M:", "").trim();
            }
        }
        throw new Exception("no time signature found for: " + lines[0]);
    }
    
    
    public int getNoteLength(String abcNotation) throws Exception {
        String[] lines = abcNotation.split("\n");
        for (String line : lines) {
            if (line.startsWith("L:")) {
                line = line.replace("L:", "").trim();
                if(line.startsWith("1/")) {
                	String[] noteParts = line.split("/"); 
                    if (noteParts.length == 2) {
                    	return Integer.parseInt(noteParts[1]);
                    }
                	
                }
            }
        }
        return 8;
    }
    
    
    
    public static String[][] splitByBar(String[] noteLengths) throws Exception {
    	
    	if (noteLengths == null || noteLengths.length == 0) {
            throw new Exception("input lengths are empty or null");
        }
    	
    	ArrayList<ArrayList<String>> bars = new ArrayList<>();
    	ArrayList<String> currentBar = new ArrayList<>();

    	for (String note : noteLengths) {
            if (note.equals("|")) { // bar separator
                // if current bar has notes, add it to bars and clear for the next
                if (!currentBar.isEmpty()) {
                    bars.add(new ArrayList<>(currentBar));
                    currentBar.clear();
                } 
            } else {
                // add the note length to the current bar
                currentBar.add(note);
            }
        }

        // add the last bar if not empty
        if (!currentBar.isEmpty()) {
            bars.add(new ArrayList<>(currentBar));
        }
        
        if (bars.isEmpty()) {
            throw new Exception("no bars were detected in the input melody");
        }
        
        // converting to a String matrix
        String[][] result = new String[bars.size()][];
        for (int i = 0; i < bars.size(); i++) {
            result[i] = bars.get(i).toArray(new String[0]); 
        }
        return result;
    }
    
    public static int[] patternReader(Integer[][] bars, Map<Integer, Integer[]> patterns) {
    	ArrayList<Integer> patternkeys = new ArrayList<>();

        for (int i = 0; i < bars.length; i++) {
            for (Map.Entry<Integer, Integer[]> entry : patterns.entrySet()) {
                if (java.util.Arrays.equals(entry.getValue(), bars[i])) {
                    patternkeys.add(entry.getKey());
                    break;
                }
            }

        }
        int[] result = new int[patternkeys.size()];
        for (int i = 0; i < patternkeys.size(); i++) {
            result[i] = patternkeys.get(i); // doing this to prevent empty spots in an array
        }
        return result;// returns an array of keys to patterns for a specific hashmap
    }
   
    
    public Map<Integer, Integer[]> getPatternDictionary(String timeSignature) throws Exception{
    	
    	Map<Integer, Integer[]> dictionary;
    	timeSignature = timeSignature.replaceAll("[^0-9/]", "");
    	switch (timeSignature) {
        case "4/4":
            dictionary = patterns_4_4;
            break;
        case "3/4":
            dictionary = patterns_3_4;
            break;
        case "2/4":
            dictionary = patterns_2_4;
            break;
        case "7/8":
            dictionary = patterns_7_8;
            break;
        case "6/8":
            dictionary = patterns_6_8;
            break;
        case "12/8":
            dictionary = patterns_12_8;
            break;
        default:
            throw new Exception("Chosen time signature is not a given option");
    }
    	
    	return dictionary;
    }
    
    


    
    public static void main(String[] args) throws Exception {

	}
}
