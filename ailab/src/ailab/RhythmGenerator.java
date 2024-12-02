package ailab;

import java.util.HashMap;
import java.util.Map;

public class RhythmGenerator {
	private static final Map<Integer, Integer[]> patterns_4_4 = new HashMap<>();
	private static final Map<Integer, Integer[]> patterns_3_4 = new HashMap<>();
	private static final Map<Integer, Integer[]> patterns_2_4 = new HashMap<>();
	private static final Map<Integer, Integer[]> patterns_7_8 = new HashMap<>();
	private static final Map<Integer, Integer[]> patterns_6_8 = new HashMap<>();
	private static final Map<Integer, Integer[]> patterns_12_8 = new HashMap<>();//maybe no need

    static {
    	patterns_4_4.put(0, new Integer[]{4,4,4,4});
    	patterns_4_4.put(1, new Integer[]{8,8});
    	patterns_4_4.put(2, new Integer[]{2,4,2,8});
    	patterns_4_4.put(2, new Integer[]{2,2,4,8});
    	patterns_4_4.put(2, new Integer[]{4,2,2,8});
    	patterns_4_4.put(3, new Integer[]{2,2,2,2,2,2,2,2});
    	patterns_4_4.put(3, new Integer[]{2,2,4,2,2,4});
    	patterns_4_4.put(3, new Integer[]{2,2,2,4,2,4});
    	patterns_4_4.put(4, new Integer[]{6,2,4,4});
    	patterns_4_4.put(5, new Integer[]{1,1,1,1});
    	
    	
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
    
    public int[] barToLengthArray(String[] barLengths, int noteLengthDenominator) throws Exception {
    	//ill just need to separate the lengths to barsbased on | and then according to time signature add pattern to hashmap f its not there and add value to int array
    	int[] lengths = new int[barLengths.length];
    	double helper;
    	int mod = 16/ noteLengthDenominator;
    	for(int i = 0; i< barLengths.length ; i++) {
    		if(barLengths[i].contains("/")) {
    			String[] lengthParts = barLengths[i].split("/");
    			try {
                    helper =  Double.parseDouble(lengthParts[0])/ Double.parseDouble(lengthParts[1]);
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
        throw new Exception("no note length found for: " + lines[0]);
    }
}
