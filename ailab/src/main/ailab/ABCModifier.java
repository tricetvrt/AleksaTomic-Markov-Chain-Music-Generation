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

    static {
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
    	
    
    public static int noteToInt(String note) throws Exception { 
        if (note==null || note.isEmpty()) {
            throw new Exception("invalid note input");
        }
        if(note.contains("z")) // in the same time fixing the error if somehow the note is z with modifiers (z')
        	return 200;
        String baseNote = note.charAt(0) + "";
        int flatsharp = 0;
        if (baseNote.equals("^") || baseNote.equals("_")) {
            baseNote += Character.toUpperCase(note.charAt(1));
            flatsharp=1;
        }
        else
        	baseNote= Character.toUpperCase(note.charAt(0)) + "";
   
        if (!base_notes.containsKey(baseNote)) {
            throw new Exception("there is not a base note : " + baseNote);
        }
        int value = base_notes.get(baseNote);

        if(flatsharp == 0 && Character.isLowerCase(note.charAt(0)))
        	value+=12;
        if(flatsharp == 1 && Character.isLowerCase(note.charAt(1)))
        	value+=12;

        for (int i = 1; i < note.length(); i++) {// for , or ' increasing and decreasing octaves
            char mod=note.charAt(i);
            if (mod== '\'') {
                value+= 12; 
            } else if (mod==',') {
                value-= 12;
            }
        }

        return value;
    }
    
    public String intToNote(int value) throws Exception {
    	String note = "";
    	String help = "";

    	if(value < 0 || value > 200)// fix maybe -1000 if the value for z is changed
    		throw new Exception ("invalid integer value to convert to a note");
    	
    	while(!base_notes.containsValue(value)) {
    		if(value<36) {
    			value+= 12;
    			help+= ",";
    		}
    		else {
    			value-= 12;
    			help += "'";
    		}
    	}
    	
    	 for (Map.Entry<String, Integer> entry : base_notes.entrySet()) {
             if (value == entry.getValue()) {
                 note = entry.getKey();
             }}
    	 if(note.equals(""))
    		 throw new Exception("couldnt get the base note from a value");


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
    
    
	

