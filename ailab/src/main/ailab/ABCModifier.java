package main.ailab;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ABCModifier {

	private static final Map<String, Integer> base_notes = new HashMap<>();

    static { // base notes and their values used for converting the abc notation to integer arrays
    	base_notes.put("z", 200);
    	base_notes.put("C", 36);
    	base_notes.put("^C", 37);
    	base_notes.put("_D", 37);
    	base_notes.put("D", 38);
    	base_notes.put("^D", 39);
    	base_notes.put("_E", 39);
    	base_notes.put("E", 40);
    	base_notes.put("F", 41);
    	base_notes.put("^F", 42);
    	base_notes.put("_G", 42);
    	base_notes.put("G", 43);
    	base_notes.put("^G", 44);
    	base_notes.put("_A", 44);
    	base_notes.put("A", 45);
    	base_notes.put("^A", 46);
    	base_notes.put("_B", 46);
    	base_notes.put("B", 47);
    }

	public int[][] abcToIntDatabase(String dataPath) throws Exception{
		String content = "";
		String[] notations = null;
		
		try {
			content = Files.readString(Paths.get(dataPath));
			notations = content.split("\\$\\$\\$"); // splitting into individual notations
		} catch (IOException e) {
			throw new IOException("could not read file at" + dataPath, e);
		}
		int[][] result = new int[notations.length][];
		for(int i=0; i<notations.length; i++) {
			// processing each notation and storing the result int array
			result[i] = abcToInt(notations[i]);
		}
		return result;
	}
	public int[] abcToInt(String abcNotation) throws Exception {
        String melody = extract(abcNotation);
        String[] components = parse(melody);
        List<Integer> components2 = new ArrayList<>();
        for( int i=0 ; i<components.length ; i++) {
        	try {
                // try to convert each note to an integer
                components2.add(noteToInt(components[i]));
            } catch (Exception e) {
                // if an exception occurs, just skip the note and continue with the next one
                System.out.println("Skipping invalid note: " + components[i]);
            }
        }
        int[] componentsArray = new int[components2.size()];
        for (int i = 0; i < components2.size(); i++) {
            componentsArray[i] = components2.get(i);
        }
        return componentsArray;
	}
	
	
	// a method which only extracts the melody part of the ABC notation code
	public String extract(String abcNotation) {
		abcNotation = abcNotation.replaceAll("\r", "");// deals with carriage return characters
        String[] lines = abcNotation.split("\n");
        StringBuilder melody= new StringBuilder();
        for (int i=0; i<lines.length;i++) {
        	if (!lines[i].matches("^[A-Z]:.*")) // checks if the line matches  a capital letter followed by : and more possible content
        	{
        		melody.append(lines[i]);
                melody.append(" "); 
            }
        }

        return melody.toString().trim();//removing unecessary whitespace
    }

	
	// a method used for parsing the extracted melody from ABC notation
	// it keeps only the notes and modifiers, and puts them into a String array
    public String[] parse(String melody) {
        String cleanmelody= melody.replace("|", " "); //add C2 is C 
        cleanmelody = cleanmelody.replaceAll("\"[A-Ga-g](m|#)?(m)?\"", "");
        cleanmelody = cleanmelody.replaceAll("[^A-Ga-gz_^',\\s]", "");
        cleanmelody = cleanmelody.replaceAll("([A-Ga-gz',])([A-Ga-gz^_])", "$1 $2");
        cleanmelody = cleanmelody.replaceAll("([A-Ga-gz])([A-Ga-gz^_])", "$1 $2");
        cleanmelody = cleanmelody.replaceAll("\\s+", " ").trim();
        String[] notes= cleanmelody.split("\\s+"); 

        return notes;
    }
    	
    
    
    // a method for converting a note String into an corresponding value
    public static int noteToInt(String note) throws Exception { 
        if (note==null || note.isEmpty()) {
            throw new Exception("invalid note input");
        }
        if(note.contains("z")) // in the same time fixing the error if somehow the note is z with modifiers (z')
        	return 200;
        String baseNote = note.charAt(0) + ""; // adding the first character of the note to the baseNote String
        int flatsharp = 0;
        // if the first character is a symbol representing a flat note or a sharp note, we should add the second character to the baseNote
        if (baseNote.equals("^") || baseNote.equals("_")) {
            baseNote += Character.toUpperCase(note.charAt(1));
            flatsharp=1;
        }
        else
        	baseNote= Character.toUpperCase(note.charAt(0)) + "";
   
        if (!base_notes.containsKey(baseNote)) {
            throw new Exception("there is not a base note : " + baseNote);
        }
        int value = base_notes.get(baseNote); // getting the value of the base note

        // if the note is written in lowercase, add 12 to the integer representing a higher octave
        if(flatsharp == 0 && Character.isLowerCase(note.charAt(0)))
        	value+=12;
        if(flatsharp == 1 && Character.isLowerCase(note.charAt(1)))
        	value+=12;

        for (int i = 1; i < note.length(); i++) {// for , or ' increasing and decreasing octaves
            char mod=note.charAt(i);
            if (mod== '\'') {
                value+= 12; // adding 12 for every modifier ' representing a higher octave
            } else if (mod==',') {
                value-= 12; // reducing 12 for every modifier ,
            }
        }

        return value;
    }
    
    
    // a function for converting an integer value to a String of the according note as if its written in ABC notation
    public String intToNote(int value) throws Exception {
    	String note = "";
    	String help = ""; // used for saving the modifiers

    	if(value < 0 || value > 200)
    		throw new Exception ("invalid integer value to convert to a note");
    	
    	while(!base_notes.containsValue(value)) { // modifying the int value until it is a value of a base note
    		if(value<36) { 
    			// if the value is smaller then 36, the note is of a lower octave than the base one
    			value+= 12;
    			help+= ","; // the modifier used for lowering an octave
    		}
    		else {
    			// if its higher than the base notes, we should lower the octave to the base one
    			value-= 12;
    			help += "'"; // the modifier used for increasing by an octave
    		}
    	}
    	
    	 for (Map.Entry<String, Integer> entry : base_notes.entrySet()) {
             if (value == entry.getValue()) {
                 note = entry.getKey(); // getting the base note String
             }}
    	 if(note.equals(""))
    		 throw new Exception("couldnt get the base note from a value");


    	// if the first character of the help String is ', we should remove it and instead write the note as a lower case (ABC notation rules)
         if(!(help.equals("")) && help.charAt(0)=='\'') { 
        	  if(note.length()==1)
        		  note= Character.toLowerCase(note.charAt(0))+"";
        	  
        	  else if(note.charAt(1)=='A'||note.charAt(1)=='G'||note.charAt(1)=='F'||note.charAt(1)=='E'||note.charAt(1)=='D'||note.charAt(1)=='C'||note.charAt(1)=='B')
        		  note = note.charAt(0) +"" + Character.toLowerCase(note.charAt(1)) + note.substring(2);
        	  
        	  else
        		  throw new Exception("There is a problem while conversing value to lowercase note");
        	  
        	  if (help.length() > 0) {
        		    help = help.substring(1);
        		}
        	  }
         
          note+=help;
          return note;
          
          
          
          }
    }
    
    
	

