package ailab; 

import abc.parser.TuneParser;

import java.util.HashMap;
import java.util.Map;

import abc.notation.Music;
public class ABCModifier {

	private static final Map<Character, Integer> base_notes = new HashMap<>();

    static {
    	base_notes.put('C', 0);
    	base_notes.put('D', 1);
    	base_notes.put('E', 2);
    	base_notes.put('F', 3);
    	base_notes.put('G', 4);
    	base_notes.put('A', 5);
    	base_notes.put('B', 6);
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
	
	private String extract(String abcNotation) {
        String[] lines = abcNotation.split("\n");
        StringBuilder melody= new StringBuilder();
        for (int i=0; i<lines.length;i++) {
            if (!lines[i].startsWith("X:") && !lines[i].startsWith("T:") && !lines[i].startsWith("M:") && !lines[i].startsWith("K:")) {
                melody.append(lines[i]);
                melody.append(" "); 
            }
        }

        return melody.toString().trim();//removing unecessary whitespace
    }

    private String[] parse(String melody) {
        String cleanmelody= melody.replace("|", " "); 
        String[] notes= cleanmelody.split("\\s+"); 
        // add later check if the element is not a note but a key or invalid chars etc..

        return notes;
    }
	
    public static int noteToInt(String note) throws Exception {
        if (note==null || note.isEmpty()) {
            throw new Exception("invalid note input");
        }
        char baseNote = Character.toUpperCase(note.charAt(0));
        if (!base_notes.containsKey(baseNote)) {
            throw new Exception("there is not a base note");
        }
        int value = base_notes.get(baseNote);

        if(Character.isLowerCase(note.charAt(0)))
        	value+=7;

        for (int i = 1; i < note.length(); i++) {// for , or ' increasing and decreasing octaves
            char mod=note.charAt(i);
            if (mod== '\'') {
                value+= 7; 
            } else if (mod==',') {
                value-= 7;
            }
        }

        return value;
    }
    
    
	
}
