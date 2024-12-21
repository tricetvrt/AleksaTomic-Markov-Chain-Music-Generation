package test;

import org.junit.jupiter.api.BeforeEach;


import org.junit.jupiter.api.Test;

import main.ailab.Node;
import main.ailab.RhythmGenerator;
import main.ailab.Trie;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Map;

class TrieTest {
    
    private Trie trie;

    @BeforeEach
    void setUp() {
    	Node root = new Node();
        trie = new Trie(root, 2);}

    @Test
    void testInsertSearch() throws Exception {
        int[] sequence = {1, 2, 3};
        trie.insert(sequence);
        assertTrue(trie.search(sequence), "the sequence should be found after being inserted");   
        int[] nonExistentSequence = {1, 2, 4};
        assertFalse(trie.search(nonExistentSequence), "the sequence should not be found");
    }

    @Test
    void testInsert2() {
        int[] invalidSequence = {1};  // length is less then it should be (degree+1)
        Exception exception = assertThrows(Exception.class, () -> trie.insert(invalidSequence));
        assertEquals("inappropriate sequence(insert)", exception.getMessage());
    }

    
    @Test
    void testGetTransitions1() throws Exception {
        int[] sequence1 = {1, 2, 3};
        trie.insert(sequence1);
        int[] subsequence = {1, 2};
        
        int[][] transitions = trie.getTransitions(subsequence);
        assertNotNull(transitions, "transitions should not be null");
        assertEquals(1, transitions[0].length, "there should be one valid transition");
        assertEquals(3, transitions[0][0], "transition note should be 3");
        assertEquals(1, transitions[1][0], "transition frequency should be 1");
    }
    
    @Test
    void testGetTransitions2() throws Exception {
        int[] sequence1 = {1, 2, 1};
        int[] sequence2 = {1, 2, 3};
        trie.insert(sequence1);
        trie.insert(sequence2);
        int[] subsequence = {1, 2};
        
        int[][] transitions = trie.getTransitions(subsequence);
        assertNotNull(transitions, "transitions should not be null");
        assertEquals(2, transitions[0].length, "there should be two valid transitions");
        assertEquals(1, transitions[0][0], "first transition note should be 1");
        assertEquals(3, transitions[0][1], "second trainsition note should be 3");
        assertEquals(1, transitions[1][0], "first transition frequency should be 1");
        assertEquals(1, transitions[1][1], "second trainsition frequency should be 1");
    }
    
    @Test
    void testGetTransitions3() throws Exception {
        int[] sequence1 = {1, 2, 1};
        trie.insert(sequence1);
        trie.insert(sequence1);
        int[] subsequence = {1, 2};
        
        int[][] transitions = trie.getTransitions(subsequence);
        assertNotNull(transitions, "transitions should not be null");
        assertEquals(1, transitions[0].length, "there should be one valid transition");
        assertEquals(1, transitions[0][0], "transition note should be 1");
        assertEquals(2, transitions[1][0], "transition frequency should be 2");
    }

    @Test
    void testGetProbabilities() throws Exception {
        int[] sequence1 = {1, 2, 3};
        int[] sequence2 = {1, 2, 4};
        trie.insert(sequence1);
        trie.insert(sequence2);

        float[][] probabilities = trie.getProbabilites(new int[]{1, 2});
        
        assertNotNull(probabilities, "probabilities should not be null");
        assertEquals(2, probabilities[0].length, "there should be two possible transitions");
        assertEquals(0.5, probabilities[1][0], "first probability should be 0.5");
        assertEquals(0.5, probabilities[1][1], "second probability should be 0.5");
        
        trie.insert(sequence1);//as an additional check to see how probabilities manifest / if the sum is 1
        float sum = 0;
        for (float probability : probabilities[1]) {
            sum += probability;
        }
        assertTrue(Math.abs(sum - 1.0f) < 0.001, "probabilities should sum to 1");
    }

    @Test
    void testGenerateNext1() throws Exception {
        
        int[] sequence1 = {1, 2, 4};
        trie.insert(sequence1);
       
        int[] subsequence = {1, 2};
        int nextNote = trie.generateNext(subsequence);
        assertTrue(nextNote == 4, "generated note should be 4");
    }
    
    @Test
    void testGenerateNext2() throws Exception {
		int[] subsequence = {1, 2};
        int[] sequence1 = {1, 2, 4};
        int[] sequence2 = {1, 2, 3};
        int[] sequence3 = {1, 2, 4};
        int[] sequence4 = {1, 2, 1};
        
        trie.insert(sequence1);
        trie.insert(sequence2);
        trie.insert(sequence3);
        trie.insert(sequence4);
        
        int nextNote = trie.generateNext(subsequence);
        assertTrue(nextNote == 3 || nextNote == 4 || nextNote == 1, "generated note should be 1, 3 or 4");
    }

    @Test
    void testGenerateSequence1() throws Exception {
        // train the trie and generate a sequence
        int[] melody = {1, 2, 3, 2, 3, 4};
        trie.trainTrie(melody);
        
        int[] startingSeq = {1, 2};
        int length = 7;
        int[] generatedSequence = trie.generateSequence(startingSeq, length);
        
        
        for( int i = 0 ; i < generatedSequence.length - 2 ; i++) {
        	//now I will check if the subsequences in the generated sequence exist in the trie
        	// in order to avoid errors, I have implemented repeating th elast note if there are no last notes available
        	// to skip these cases, since they would assert false, i added the following if statement
        	// while it is not perfect, this was the only way to test this without removing the note repeating functionality
        	if(generatedSequence[i+1] != generatedSequence[i+2]) {
        		assertTrue(trie.search(Arrays.copyOfRange(generatedSequence, i, i+3)));
        	}
        }
        assertNotNull(generatedSequence, "generated sequence should not be null");
        assertEquals(length, generatedSequence.length, "generated sequence should have the correct length");
        // if the generated sequence starts well
        assertArrayEquals(new int[]{1, 2}, new int[]{generatedSequence[0], generatedSequence[1]}, "starting sequence should match");
    }

    @Test
    void testGenerateSequence2() throws Exception {
        // train the trie and generate a sequence
        int[] melody = {1, 2, 3, 4, 5, 6};
        trie.trainTrie(melody);
        
        int[] startingSeq = {1, 2};
        int length = 6;
        int[] generatedSequence = trie.generateSequence(startingSeq, length);
        
        assertNotNull(generatedSequence, "generated sequence should not be null");
        assertEquals(length, generatedSequence.length, "generated sequence should have the correct length");
        //the generated sequence in this case should be the same as the "melody" array because of no patterns
        assertArrayEquals(generatedSequence, melody, "the generated sequence in this test should match the prompted one");
    }
    
    @Test
    void testGenerateRhythmSequence1() throws Exception {
        // Train the trie and generate a sequence
        int[] rhythm = {1, 2, 3, 2, 0, 1, 0};
        trie.trainTrie(rhythm);
        Map<Integer, Integer[]> dictionary = RhythmGenerator.patterns_4_4;
        int length = 6;
        int[] generatedSequence = trie.generateSequenceRhythm(length, dictionary);
        int durationsLength = 0;
        for(int i = 0; i< generatedSequence.length ; i++) {
        	assertTrue(dictionary.containsKey(generatedSequence[i]), "generated sequence contains invalid key: " + generatedSequence[i]);
        	durationsLength += dictionary.get(generatedSequence[i]).length; }
        	
        assertNotNull(generatedSequence, "generated sequence should not be null");
        assertTrue(durationsLength >= length, "generated sequence's durations all together should have the correct length");
        trie.resetTrie();
        int[] rhythm2 = {1, 2, 2}; 
        // helped me find the need to control the length of the melodies used for training,
        // and fix the starting sequence generation for this method
        int[] expected = {1, 2};
        trie.trainTrie(rhythm2);
        generatedSequence = trie.generateSequenceRhythm(length, dictionary);
        // the generated sequence should now be the same as expected, as that would be the only possible one
        assertArrayEquals(generatedSequence, expected, "the generated sequence in this test should match the prompted one");
    }
    
    @Test
    void testGenerateRhythmSequence2() throws Exception {
        // Train the trie and generate a sequence
        int[] rhythm = {1, 2, 3, 2, 0, 1, 0};
        trie.trainTrie(rhythm);
        Map<Integer, Integer[]> dictionary = RhythmGenerator.patterns_7_8;
        int length = 30;
        int[] generatedSequence = trie.generateSequenceRhythm(length, dictionary);
        for(int i = 0; i< generatedSequence.length - 2 ; i++) {
        	assertTrue(dictionary.containsKey(generatedSequence[i]), "generated sequence contains invalid key: " + generatedSequence[i]);
        	//now I will check if the subsequences in the generated sequence exist in the trie
        	// in order to avoid errors, I have implemented repeating th elast note if there are no last notes available
        	// to skip these cases, since they would assert false, i added the following if statement
        	// while it is not perfect, this was the only way to test this without removing the note repeating functionality
        	if(generatedSequence[i+1] != generatedSequence[i+2]) {
        		assertTrue(trie.search(Arrays.copyOfRange(generatedSequence, i, i+3)));
        	}
        }
        	
        assertNotNull(generatedSequence, "generated sequence should not be null");
        
      }
    
    @Test
    void testResetTrie() throws Exception {
        int[] sequence = {1, 2, 3};
        trie.insert(sequence);
        assertTrue(trie.search(sequence), "sequence should be found (reset)");
        trie.resetTrie();
        assertFalse(trie.search(sequence), "sequence should not be found (reset)");
    }

    @Test
    void testTrainTrie() throws Exception {
        int[] melody = {1, 2, 3, 4, 3, 2};
        trie.trainTrie(melody);
        int[] sequence = {1, 2, 3};
        assertTrue(trie.search(sequence), "sequence should be found (train)");
        int[] nonExistentSequence = {4, 5, 6};
        assertFalse(trie.search(nonExistentSequence), "sequence should not be found (train)");
    }
    
    @Test
    void testTrainTrieInvalid() throws Exception {
        int[] melody = {1, 2};
        trie.trainTrie(melody);
        assertTrue(trie.getRoot().getChildren()[1] == null); // skipping all invalid melodies for training
    }
}
