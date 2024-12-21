package main.ailab;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;

public class Trie {

	 Node root;
	int degree;

	public Trie(Node root) {
		super();
		this.root = root;
		degree = 2;
	} 
	
	
	public Trie(Node root, int degree) {
		super();
		this.root = root;
		this.degree = degree;
	}


	public void insert(int[] sequence) throws Exception {
		if(sequence==null || sequence.length!=degree+1) {
			throw new Exception("inappropriate sequence(insert)");
		}
		Node current = root;
		for(int i=0;i<sequence.length ; i++) {
			int note = sequence[i];
			if(current.children[note]==null)
				current.children[note]= new Node();
			current=current.children[note];
			current.freq++;
			
		}
		current.isEnd = true;
	}
	
	public Node getRoot() {
		return root;
	}


	public void setRoot(Node root) {
		this.root = root;
	}


	public boolean search(int[] sequence) throws Exception {
		if(sequence==null || sequence.length!=degree+1) {
			throw new Exception("inappropriate sequence(search)");
		}
		Node current = root;
		for(int i=0; i<sequence.length; i++) {
			int note = sequence[i];
			if(current.children[note]==null)
				return false;
			current=current.children[note];
		}
		return true;
	}
	private void privPrint(Node root) {//debugging function
		for(int i=0;i<9;i++) {
			if(root.children[i]!=null) {
				System.out.println(i+", " + root.children[i].freq);
				privPrint(root.children[i]);
				System.out.println();
			}
		}
	}
	public int[][] getTransitions(int[] notes) throws Exception{
		if(notes.length!=degree)
			throw new Exception("sequence length not equal to the degree");
		Node current = root;
		for(int i=0;i<degree;i++) { // going to the node of which we need to get transitions
			int note = notes[i];
			if(current.children[note]==null) {
				return null;
			}
			current = current.children[note];			
		}
		int br=0;
		for(int i=0; i<current.children.length;i++) {
			if(current.children[i]!=null) {
				br++;
			}}
		int[][] transitions = new int[2][br];
		int j=0;
		for(int i=0; i<current.children.length;i++) {
			if(current.children[i]!=null) {
				transitions[0][j]=i;
				transitions[1][j]=current.children[i].freq;
				j++;
			}
		}
		return transitions;
	}
	public float[][] getProbabilites(int[] sequence) throws Exception {// maybe double instead of float?
		int[][] transitions = getTransitions(sequence);
		if (transitions==null || transitions[0].length == 0) {
	        return new float[0][0]; 
	    }
		float[][] probabilities = new float[2][transitions[0].length];
		int sumFreq=0;
		for(int j= 0; j < transitions[1].length; j++) {
			sumFreq+=transitions[1][j];
			probabilities[0][j]=transitions[0][j];
		}
		//System.out.println(sumFreq);
		 if (sumFreq==0) {
		        throw new Exception("total frequency is zero, cannot calculate probabilities");
		    }
		for(int j=0; j<transitions[1].length ;j++) {
			//System.out.println(transitions[1][j]/sumFreq);
			probabilities[1][j] = (float)transitions[1][j]/sumFreq;
		}
		return probabilities;	
	}
	public void print() {
		System.out.println('.');
		privPrint(root);
		
	}
	
	public int generateNext(int[] sequence) throws Exception {
	    float[][] probabilities = getProbabilites(sequence);
	    if (probabilities.length == 0 || probabilities[0].length == 0) {
	        return sequence[sequence.length - 1]; // if no transitions are available go to the last note
	    }
	    float rand= (float)Math.random(); //0<=rand<1
	    float probabilities_sum= 0;
	    for (int i = 0; i<probabilities[1].length; i++) {//weighted random
	    	probabilities_sum+= probabilities[1][i];
	        if (rand <= probabilities_sum) {// the random fell into the range of this particular note
	            return (int) probabilities[0][i];
	        }
	    }
	    throw new Exception("no valid next note found");
	}
	
	public int[] generateSequence(int[] startingSeq, int length) throws Exception {
		int[] result = new int[length]; //length of the melody
		System.arraycopy(startingSeq, 0, result, 0, degree);
		for(int i=degree;i<length;i++) {
			int[] subseq= new int[degree];
			System.arraycopy( result, i-degree, subseq, 0, degree);
			result[i] = generateNext(subseq);
		}
		return result;
	}
	
	public void resetTrie() {
	    this.root = new Node(); // in case I need to work with multiple training datasets
	}
	
	public void trainTrie(int[] melody) throws Exception{//myb better that the argument is a database
		if(melody.length <= degree)
			return; //"the length of the melody used for training should be at least bigger than the degree" + melody.length);
		// not using an Exception since it is possible that the notation used has no prewritten rhythm patterns. Then the generation would be terminated.
		// by not using an Exception, the generation is uninterrupted by this edge case
		for (int i = 0; i <= melody.length - (degree + 1); i++) {
            int[] subsequence = new int[degree + 1];
            System.arraycopy(melody, i, subsequence, 0, degree + 1);
            insert(subsequence);
        }
	}
	
	public void trainTrieDataset(int[][] melodies) throws Exception {
		for(int i = 0; i< melodies.length ; i++) {
			trainTrie(melodies[i]);
		}
	}
	
	public int[] generateSequenceRhythm(int length, Map<Integer, Integer[]> dictionary) throws Exception {
		int[] result = new int[length]; //length of the melody
		int[] startingSeq = new int[degree];
		
		Random random = new Random();
		int randomIndex = 50;
		while(root.children[randomIndex]==null ||  root.children[randomIndex].freq==0)//freq part maybe redundant but just in case, checking if the pattern even exists in the training data
			randomIndex = random.nextInt(dictionary.size());
        // get a random key - they are the same as indexes
		Node current = root.children[randomIndex];
		startingSeq[0] = randomIndex;

		randomIndex=0;// setting the randomIndex to 0, to automatically generate the next patterns for the starting sequence
		int rhythmLength = dictionary.get(startingSeq[0]).length; // add the number of notes in the generated pattern
		for(int i=1 ; i< degree ;i++) {
			while(current.children[randomIndex]==null )
				randomIndex = random.nextInt(dictionary.size()); //searches for an existing child of the previous state (of patterns) in the sequence
			 startingSeq[i]= randomIndex;// starting sequence
			 current = current.children[randomIndex]; // the current is the last pattern in the sequence so far
			 rhythmLength += dictionary.get(startingSeq[i]).length; // adds the number of notes for every next generated pattern in the starting sequence
		}
		
		// we have the random starting sequence, now we regularly generate the rest
		System.arraycopy(startingSeq, 0, result, 0, degree);
		
		int i= degree;
		while(rhythmLength<length) { // the loop will stop when teh number of notes in the rhythm exceedes the number f notes in the generated melody
			int[] subseq= new int[degree];
			System.arraycopy( result, i-degree, subseq, 0, degree);
			result[i] = generateNext(subseq);
			rhythmLength += dictionary.get(result[i]).length; // adds the number of notes for every next generated pattern
			i++;
		}
		int[] result2 = Arrays.copyOfRange(result, 0, i);; // to trim all of the empty array spots, more memory efficiency maybe if done with an ArrayList
		return result2;
	}// possible to not return an array of patterns, but an array of note durations which would shorten the code
	// however, this way it is possible to later possibly use the generated sequences for training, if we think that it is appropriate
	
	

	

	
}
