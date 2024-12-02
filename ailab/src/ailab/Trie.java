package ailab;

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
	private void privPrint(Node root) {//test samo od 1 do 9
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
		for(int i=0;i<degree;i++) {
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
		for(int j=0;j<transitions[1].length;j++) {
			sumFreq+=transitions[1][j];
			probabilities[0][j]=transitions[0][j];
		}
		//System.out.println(sumFreq);
		 if (sumFreq==0) {
		        throw new Exception("total frequency is zero, cannot calculate probabilities");
		    }
		for(int j=0;j<transitions[1].length;j++) {
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
	    if (probabilities.length==0 || probabilities[0].length == 0) {
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
	
	public void trainTrie(int[] melody) throws Exception {//myb better that the argument is a database
		for (int i = 0; i <= melody.length - (degree + 1); i++) {
            int[] subsequence = new int[degree + 1];
            System.arraycopy(melody, i, subsequence, 0, degree + 1);
            insert(subsequence);
        }
	}
	
	

	

	
}
