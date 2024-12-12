package ailab;

public class Node {
	Node[] children;
	int freq;
	boolean isEnd;
	
	
	public Node() {
		super();
		this.children= new Node[201];//pitaj dal mozda treba da se koristi lista umesto niza ili samo da vidim kolko note ce ubacim
		this.freq = 0;
		this.isEnd = false;
	
	}


	public Node(Node[] children, int freq, boolean isEnd) {
		super();
		this.children = children;
		this.freq = freq;
		this.isEnd = isEnd;
	}
	
	
}
