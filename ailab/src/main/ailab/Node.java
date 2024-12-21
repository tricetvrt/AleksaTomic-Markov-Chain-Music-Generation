package main.ailab;

public class Node {
	Node[] children;
	int freq;
	boolean isEnd;
	
	
	public Node() {
		super();
		this.children= new Node[201]; // children[200] is reserved for breaks (z)
		this.freq = 0;
		this.isEnd = false;
	
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
