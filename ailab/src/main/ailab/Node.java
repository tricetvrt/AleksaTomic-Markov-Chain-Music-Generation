package main.ailab;

public class Node {
	Node[] children;
	int freq;
	boolean isEnd;
	
	
	public Node() { // structure of nodes
		super();
		this.children= new Node[201]; // children[200] is reserved for breaks (z)
		this.freq = 0; // frequency - the number of occurences of the particular state represented by the node
		this.isEnd = false; // not used, but could be useful for further development of the project
	
	}


	public Node[] getChildren() {
		return children;
	}


	public void setChildren(Node[] children) {
		this.children = children;
	}


	public Node(Node[] children, int freq, boolean isEnd) {
		super();
		this.children = children;
		this.freq = freq;
		this.isEnd = isEnd;
	}
	
	
}
