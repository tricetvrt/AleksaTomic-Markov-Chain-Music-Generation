package test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import ailab.Combine;
import ailab.RhythmGenerator;

public class CombineTest {

	
	private Combine modifier = new Combine();
    @Test
    public void testAddIntsToStrings() throws Exception {
        String[] notes = {"C", "D", "^C","C", "D", "b","_D'", "D", "^C"};
        Integer[] lengths = {2, 2, 4, 1, 1, 8, 4, 1, 2};
        String[] expected = {"C2", "D2", "^C4","C", "D", "b8","_D'4", "D", "^C2"};
        String[] result = modifier.addIntsToStrings(notes, lengths);

        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testCombineMelodyRhythm() throws Exception {
    	Map<Integer, Integer[]> dict =  RhythmGenerator.patterns_4_4;
    	int[] notes = {36,36,36,36,36,36,36,36};
        int[] patterns = {0,0};
        String[][] expected = {
        		{"C4", "C4", "C4", "C4"},
        		{"C4", "C4", "C4", "C4"}};
        String[][] result = modifier.combineMelodyRhythm(notes, patterns, dict);
        assertArrayEquals(expected, result);
    	
    }
    @Test
    public void testCombineMelodyRhythm2() throws Exception {
    	Map<Integer, Integer[]> dict =  RhythmGenerator.patterns_4_4;
    	int[] notes = {36,36,36,36,36,36,36,36,36};
        int[] patterns = {0,0,0};
        String[][] expected = {
        		{"C4", "C4", "C4", "C4"},
        		{"C4", "C4", "C4", "C4"},
        		{"C4"}};
        String[][] result = modifier.combineMelodyRhythm(notes, patterns, dict);
        assertArrayEquals(expected, result);
    	
    }
}
