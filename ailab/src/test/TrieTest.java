package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ailab.Node;
import ailab.Trie;

import static org.junit.jupiter.api.Assertions.*;

class TrieTest {
    
    private Trie trie;

    @BeforeEach
    void setUp() {
        trie = new Trie(new Node(), 2);}

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
        // Train the trie and generate a sequence
        int[] melody = {1, 2, 3, 2, 3, 4};
        trie.trainTrie(melody);
        
        int[] startingSeq = {1, 2};
        int length = 7;
        int[] generatedSequence = trie.generateSequence(startingSeq, length);
        
        assertNotNull(generatedSequence, "generated sequence should not be null");
        assertEquals(length, generatedSequence.length, "generated sequence should have the correct length");
        // if the generated sequence starts well
        assertArrayEquals(new int[]{1, 2}, new int[]{generatedSequence[0], generatedSequence[1]}, "starting sequence should match");
    }

    @Test
    void testGenerateSequence2() throws Exception {
        // Train the trie and generate a sequence
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
}
