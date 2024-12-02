package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import ailab.RhythmGenerator;

public class RhythmGeneratorTest {

    private RhythmGenerator rhythmGenerator = new RhythmGenerator();

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
        int[] expected1 = {8, 8, 2, 1};
        assertArrayEquals(expected1, rhythmGenerator.barToLengthArray(barLengths, noteLengthDenominator1));

        int noteLengthDenominator2 = 16;
        String[] barLengths2 = {"2", "4", "1", "1", "3"};
        int[] expected2 = {2, 4, 1, 1, 3};
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
        int expected2 = 8;
        assertEquals(expected2, rhythmGenerator.getNoteLength(abcNotation2));

        String abcNotationInvalid = "X:1\nC D E";
        Exception exception = assertThrows(Exception.class, () -> {
            rhythmGenerator.getNoteLength(abcNotationInvalid);
        });
        assertTrue(exception.getMessage().contains("no note length found"));
    }
}
