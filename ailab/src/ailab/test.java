package ailab;

public class test {

	public static void main(String[] args) throws Exception {
		Node koren = new Node();
		Trie drvo = new Trie(koren,2);
		int[] a= {1,3,2},b= {1,3,1},c= {1,3,2},d= {1,3,3};
		drvo.insert(a);
		drvo.insert(b);
		drvo.insert(c);
		drvo.insert(d);
		drvo.print();
		int[] seq = {1,3};
		float[][] matra = drvo.getProbabilites(seq);
		for(int i=0;i<2;i++) {
			for(int j=0;j<matra[0].length;j++)
				System.out.print(matra[i][j]+" ");
			System.out.println();
		}
		
	}
}
