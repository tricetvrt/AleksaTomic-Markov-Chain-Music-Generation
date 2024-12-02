package ailab; 



import java.util.HashMap;
import java.util.Map;


public class ABCModifier {

	private static final Map<String, Integer> base_notes = new HashMap<>();

    static {
    	base_notes.put("z", -1000);
    	base_notes.put("C", 0);
    	base_notes.put("^C", 1);
    	base_notes.put("_D", 1);
    	base_notes.put("D", 2);
    	base_notes.put("^D", 3);
    	base_notes.put("_E", 3);
    	base_notes.put("E", 4);
    	base_notes.put("F", 5);
    	base_notes.put("^F", 6);
    	base_notes.put("_G", 6);
    	base_notes.put("G", 7);
    	base_notes.put("^G", 8);
    	base_notes.put("_A", 8);
    	base_notes.put("A", 9);
    	base_notes.put("^A", 10);
    	base_notes.put("_B", 10);
    	base_notes.put("B", 11);
    }

	
	public int[] abcToInt(String abcNotation) throws Exception {
        String melody = extract(abcNotation);
        String[] components = parse(melody);
        int[] components2 = new int[components.length];
        for( int i=0 ; i<components.length ; i++) {
        	components2[i] = noteToInt(components[i]);
        }
        return components2;
	}
	
	public String extract(String abcNotation) {
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
        	return -1000;
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

    	if(value!= -1000 && (value < -120 || value > 144))
    		throw new Exception ("invalid integer value to convert to a note");
    	
    	while(!base_notes.containsValue(value)) {
    		if(value<0) {
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
    
    
	

