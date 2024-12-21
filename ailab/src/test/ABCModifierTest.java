package test;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import main.ailab.ABCModifier;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

class ABCModifierTest {

    private ABCModifier abcModifier;

    @BeforeEach
    void setUp() {
        abcModifier = new ABCModifier();
    }

    @Test
    public void testAbcToIntDatabaseValid1() throws Exception {
        // creating a proper temporary file to use in testing with 3 abc notations
        String sampleData = 
            "X: 1\nT: Sample\nM: 4/4\nK: C\n|: C2 C2 C2 C2 :|\n$$$" +
            "X: 2\nT: Another Sample\nM: 3/4\nK: C\n|: D2 D2 D2 :| C4 C4 :| \n$$$" +
            "X: 3\nT: Third Sample\nM: 4/4\nK: C\n|: E2 E2 E2 E2 :| C2 C2 C2 C2 |";
        Path tempFile = Files.createTempFile("abc_test", ".abc");
        Files.writeString(tempFile, sampleData);

        int[][] result = abcModifier.abcToIntDatabase(tempFile.toString());

        // the output should contain all of the notes' expected int values
        int[][] expected = {
            {36,36,36,36}, 
            {38,38,38,36,36},
            {40,40,40,40,36,36,36,36}
        };
        assertTrue(Arrays.deepEquals(result, expected));
        Files.delete(tempFile);
    }

    @Test
    public void testAbcToIntDatabaseInvalid() {

        // ensuring the exception is thrown for the invalid file path
        Exception exception = assertThrows(IOException.class, () -> { abcModifier.abcToIntDatabase("invalid/horrible/awful/path/file.abc"); });
        assertTrue(exception.getMessage().contains("could not read file"));
    }
    
    
    
    @Test
    void testAbcToIntValid() throws Exception {
        String abcNotation = "X: 1\nT: Example\nC: Composer\nG: Key\nM: 4/4\nL: 1/4\nK: C\nC D E F G A b";
        int[] expected = {36, 38, 40, 41, 43, 45, 59};
        int[] result = abcModifier.abcToInt(abcNotation);

        assertArrayEquals(expected, result, "The ABC notation should be correctly converted to integers.");
    }

//    @Test   
//    void testAbcToIntInvalid1() {
//        String abcNotation = "X Y Z"; // invalid notes
//
//        assertThrows(Exception.class, ()-> abcModifier.abcToInt(abcNotation), "an exception should be thrown for invalid notes.");
//    }
//    
//    @Test
//    void testAbcToIntInvalid2() {
//        String abcNotation = "A C D ^E F"; // Invalid note E^
//
//        assertThrows(Exception.class, ()->abcModifier.abcToInt(abcNotation), "an exception should be thrown for invalid notes.");
//    }
// CHANGED TO SKIPPING INVALID NOTES
    @Test
    void testExtract() {
        String abcNotation = "X: 1\nT: Example\nC: Composer\nG: Key\nM: 4/4\nL: 1/4\nK: C\nC \"D\" D4 E ^F";
        String expected = "C \"D\" D4 E ^F"; 
        String result = abcModifier.extract(abcNotation);

        assertEquals(expected, result, "The notation should be properly extracted.");
    }

    @Test
    void testParse() {
        String melody = "C4 \"D\" D/2 E-F Gz_D  ::" ;
        String[] expected = {"C", "D", "E", "F", "G" , "z" , "_D"};
        String[] result = abcModifier.parse(melody);

        assertArrayEquals(expected, result, "The notation should be properly parsed.");
    }

    @Test
    void testNoteToIntValid1() throws Exception {
        String note = "C,,";
        int expected = 12;
        int result = ABCModifier.noteToInt(note);

        assertEquals(expected, result, "The note should be correctly converted to integer.");
    }
    
    @Test
    void testNoteToIntValid2() throws Exception {
        String note = "_A,";
        int expected = 32;
        int result = ABCModifier.noteToInt(note);

        assertEquals(expected, result, "The note should be correctly converted to integer.");
    }
    
    @Test
    void testNoteToIntValid3() throws Exception {
        String note = "^f''";
        int expected = 78;
        int result = ABCModifier.noteToInt(note);

        assertEquals(expected, result, "The note should be correctly converted to integer.");
    }
    
    @Test
    void testNoteToIntValid4() throws Exception {
        String note = "z";
        int expected = 200;
        int result = ABCModifier.noteToInt(note);

        assertEquals(expected, result, "The note should be correctly converted to integer.");
    }
    
    @Test
    void testNoteToIntKindOfValid() throws Exception {
        String note = "z''";
        int expected = 200;
        int result = ABCModifier.noteToInt(note);

        assertEquals(expected, result, "The note should be correctly converted to integer.");
    }

    @Test
    void testNoteToIntInvalid1() {
        String note = "X"; 

        assertThrows(Exception.class, () -> ABCModifier.noteToInt(note), "an exception should be thrown.");
    }

    @Test
    void testNoteToIntInvalid2() {
        String note = "Z"; 

        assertThrows(Exception.class, () -> ABCModifier.noteToInt(note), "an exception should be thrown.");
    }

    @Test
    void testIntToNoteValid1() throws Exception {
        int value = 36;
        String expected = "C";
        String result = abcModifier.intToNote(value);

        assertEquals(expected, result, "The integer should be correctly converted to a note.");
    }
    
    @Test
    void testIntToNoteValid2() throws Exception {
        int value = 73;
        String expected1 = "^c''";
        String expected2 = "_d''";
        String result = abcModifier.intToNote(value);

        assertTrue(result.equals(expected1) || result.equals(expected2), "The integer should be correctly converted to a note.");
    }
    
    @Test
    void testIntToNoteValid3() throws Exception {
        int value = 200;
        String expected = "z";
        String result = abcModifier.intToNote(value);

        assertEquals(expected, result, "The integer should be correctly converted to a note.");
    }

    
    @Test
    void testIntToNoteValid4() throws Exception {
        int value = 19;
        String expected = "G,,";
        String result = abcModifier.intToNote(value);

        assertEquals(expected, result, "The integer should be correctly converted to a note.");
    }
    
    @Test
    void testIntToNoteInvalid1() {
        int value = -200; 

        assertThrows(Exception.class, () -> abcModifier.intToNote(value), "An exception should be thrown for an invalid value.");
    }
    
    void testIntToNoteInvalid2() {
        int value = 182; 

        assertThrows(Exception.class, () -> abcModifier.intToNote(value), "An exception should be thrown for an invalid value.");
    }
}
