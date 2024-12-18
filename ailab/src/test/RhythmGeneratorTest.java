package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;

import org.junit.jupiter.api.Test;

import main.ailab.RhythmGenerator;

public class RhythmGeneratorTest {

    private RhythmGenerator rhythmGenerator = new RhythmGenerator();

    @Test
    public void testAbcToIntRhythmDatabaseValid1() throws Exception {
        // creating a proper temporary file to use in testing with 3 abc notations
        String sampleData = 
            "X: 1\nT: Sample\nM: 4/4\nK: C\n|: C2 C2 C2 C2 :| C2 C2 C2 C2 :| C2 C2 C2 C2 :| C2 C2 C2 C2 :|\n$$$" +
            "X: 2\nT: Another Sample\nM: 3/4\nK: C\n|: D2 D2 D2 :| C4 C4 :| C2 C2 C2 :| C C4 C :|\n$$$" +
            "X: 3\nT: Third Sample\nM: 4/4\nK: C\n|: E2 E2 E2 E2 :| C2 C2 C2 C2 | C2 C2 C2 C2 |";
        Path tempFile = Files.createTempFile("abc_test", ".abc");
        Files.writeString(tempFile, sampleData);

        int[][] result = rhythmGenerator.abcToIntRhythmDatabase(tempFile.toString(), "4/4");

        // the output should contain only the patterns matching notations with the given time signature "4/4"
        int[][] expected = {
            {0,0,0,0}, 
            {0,0,0}  
        };
        assertTrue(Arrays.deepEquals(result, expected));
        Files.delete(tempFile);
    }
    
    @Test
    public void testAbcToIntRhythmDatabaseValid2() throws Exception {
        // creating a proper temporary file to use in testing with 3 abc notations
        String sampleData = 
            "X: 1\nT: Sample\nM: 3/4\nK: C\n|: C4 C4 C4 C4 :|\n$$$" +
            "X: 2\nT: Another Sample\nM: 3/4\nK: C\n|: D4 D4 D4 :|\n$$$" +
            "X: 3\nT: Third Sample\nM: 2/4\nK: C\n|: E4 E4 E4 E4 :|";
        Path tempFile = Files.createTempFile("abc_test", ".abc");
        Files.writeString(tempFile, sampleData);

        int[][] result = rhythmGenerator.abcToIntRhythmDatabase(tempFile.toString(), "4/4");

        // the output should be empty, since there are no notations with the given time signature
        int[][] expected = {
        };
        assertTrue(Arrays.deepEquals(result, expected));
        Files.delete(tempFile);
    }

    @Test
    public void testAbcToIntRhythmDatabaseInvalid() {
       // ensuring the exception is thrown for the invalid file path
        Exception exception = assertThrows(IOException.class, () -> { rhythmGenerator.abcToIntRhythmDatabase("invalid/horrible/awful/path/file.abc", "4/4"); });
        assertTrue(exception.getMessage().contains("could not read file"));
    }
    
    
    
    
    
    @Test
    public void testParseRhythm() throws Exception {
        String melody1 = "C D E F | G A B c";
        String expected1[] = {"1", "1", "1", "1", "|",  "1", "1", "1", "1"};
        assertArrayEquals(expected1, rhythmGenerator.parseRhythm(melody1));

        String melody2 = "C2 D/2 E F4 | G/A,/ B'_c2";
        String expected2[] = {"2", "1/2", "1", "4", "|",  "1/2", "1/2", "1", "2"};
        assertArrayEquals(expected2, rhythmGenerator.parseRhythm(melody2));
    }

    @Test
    public void testBarToLengthArrayValid() throws Exception {
        String[] barLengths = {"2", "2", "1/2", "1/4"};
        int noteLengthDenominator1 = 4;
        Integer[] expected1 = {8, 8, 2, 1};
        assertArrayEquals(expected1, rhythmGenerator.barToLengthArray(barLengths, noteLengthDenominator1));

        int noteLengthDenominator2 = 16;
        String[] barLengths2 = {"2", "4", "1", "1", "3"};
        Integer[] expected2 = {2, 4, 1, 1, 3};
        assertArrayEquals(expected2, rhythmGenerator.barToLengthArray(barLengths2, noteLengthDenominator2));

    }

    @Test
    public void testBarToLengthArrayInvalid() throws Exception {
    	int noteLengthDenominator1 = 4;
        String[] barLengths = {"4", "x", "4"};
        Exception exception = assertThrows(Exception.class, () -> {
            rhythmGenerator.barToLengthArray(barLengths, noteLengthDenominator1);
        });
        assertTrue(exception.getMessage().contains("invalid note length format"));
    }
    
    
    @Test
    public void testGetTimeSignatureValid() throws Exception {
        String abcNotation1 = "M: 4/4\nC D E F G";
        String expected1 = "4/4";
        assertEquals(expected1, rhythmGenerator.getTimeSignature(abcNotation1));

        String abcNotation2 = "X: 554\r\n"
        		+ "T: Zensko Kresteno\r\n"
        		+ "O: Macedonia\r\n"
        		+ "F: http://www.youtube.com/watch?v=cuFVnR0w8Dc\r\n"
        		+ "M: 12/8\r\n"
        		+ "L: 1/8\r\n"
        		+ "Q: 1/4=200\r\n"
        		+ "K: Cm\r\n"
        		+ "%%MIDI beatstring fppmppmppmpp\r\n"
        		+ "|:B,2z BAG FEF EDD |B,2z BAG FEF EDD   |\r\n"
        		+ "  B,2z BAG FEF EDD |E2D EFD B,CB, C3   :|\r\n"
        		+ "|:B,DD DGF EFE DDz |B,DD DGF EFE DDz   |\r\n"
        		+ "  B,DD DGF EFE DDz |E2D EFD B,CB, C3   :|\r\n"
        		+ "|:=EFz AGF E_DC DEz| =EFz AGF E_DC DEz |\\\r\n"
        		+ "  =EFz AGF E_DC DEz|=EFz AGF E_DC- C2z :|";
        String expected2 = "12/8";
        assertEquals(expected2, rhythmGenerator.getTimeSignature(abcNotation2));
    }
    
    @Test
    public void testGetTimeSignatureInvalid() throws Exception {

        String abcNotation = "X:1\nK: C\nC D E";
        Exception exception = assertThrows(Exception.class, () -> {
            rhythmGenerator.getTimeSignature(abcNotation);
        });
        assertTrue(exception.getMessage().contains("no time signature found"));
    }

    @Test
    public void testGetNoteLength() throws Exception {

        String abcNotation1 = "L:1/4\nC D E F";
        int expected1 = 4;
        assertEquals(expected1, rhythmGenerator.getNoteLength(abcNotation1));

        String abcNotation2 = "X: 554\r\n"
        		+ "T: Zensko Kresteno\r\n"
        		+ "O: Macedonia\r\n"
        		+ "F: http://www.youtube.com/watch?v=cuFVnR0w8Dc\r\n"
        		+ "M: 12/8\r\n"
        		+ "L: 1/16\r\n"
        		+ "Q: 1/4=200\r\n"
        		+ "K: Cm\r\n"
        		+ "%%MIDI beatstring fppmppmppmpp\r\n"
        		+ "|:B,2z BAG FEF EDD |B,2z BAG FEF EDD   |\r\n"
        		+ "  B,2z BAG FEF EDD |E2D EFD B,CB, C3   :|\r\n"
        		+ "|:B,DD DGF EFE DDz |B,DD DGF EFE DDz   |\r\n"
        		+ "  B,DD DGF EFE DDz |E2D EFD B,CB, C3   :|\r\n"
        		+ "|:=EFz AGF E_DC DEz| =EFz AGF E_DC DEz |\\\r\n"
        		+ "  =EFz AGF E_DC DEz|=EFz AGF E_DC- C2z :|";
        int expected2 = 16;
        assertEquals(expected2, rhythmGenerator.getNoteLength(abcNotation2));

        String abcNotationInvalid = "X:1\nC D E";
        assertEquals(8, rhythmGenerator.getNoteLength(abcNotationInvalid));
    }
    
    
    @Test
    public void testSplitByBar() throws Exception {

        String[] notes = {"2", "2", "1", "|", "4", "4", "|", "8"};
        String[][] expected = {
            {"2", "2", "1"},   
            {"4", "4"},
            {"8"}
        };


        String[][] result = RhythmGenerator.splitByBar(notes);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testSplitByBarInvalid() throws Exception {
        String[] noteLengths = {"|", "|", "|"};

        assertThrows(Exception.class, () -> {
            RhythmGenerator.splitByBar(noteLengths);
        });
    }
    
    
    @Test
    public void testPatternReader1() {
        Integer[][] bars = {
            {4, 4, 4, 4},
            {8, 8},
            {2, 4, 2, 8}
        };

        int[] expected = {0, 1, 2};
        int[] result = RhythmGenerator.patternReader(bars, RhythmGenerator.patterns_4_4);

        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testPatternReader2() {
        Integer[][] bars = {
            {4, 4, 4, 4},
            {8, 8},
            {1, 1, 2, 2},
            {2, 4, 2, 8},
            {2}
        };

        int[] expected = {0, 1, 2};
        int[] result = RhythmGenerator.patternReader(bars, RhythmGenerator.patterns_4_4);

        assertArrayEquals(expected, result);
    }

    
    @Test
    public void testPatternReaderInvalid() {
        Integer[][] bars = {
            {4, 4, 4, 7},
            {8, 8, 8, 8},
            {1, 1,1, 1, 1, 2, 2},
            {2, 4, 2, 8, 9},
            {2}
        };

        int[] expected = {};
        int[] result = RhythmGenerator.patternReader(bars, RhythmGenerator.patterns_4_4);

        assertArrayEquals(expected, result);
    }
 
    
    @Test
    public void testGetPatternDictionaryWithParsing() throws Exception {


        Map<Integer, Integer[]> dictionary = RhythmGenerator.patterns_4_4;
        assertEquals( dictionary, rhythmGenerator.getPatternDictionary("4/4\r\n"));

       }
    @Test
    public void testGetPatternDictionaryInvalid() {

        assertThrows(Exception.class, () -> {  rhythmGenerator.getPatternDictionary("1/8");});

       }
    
    
}
