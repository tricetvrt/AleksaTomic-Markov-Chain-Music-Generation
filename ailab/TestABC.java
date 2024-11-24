package ailab;


public class TestABC {

	public static void main(String[] args) throws Exception {
		String abcNotation = "X: 8\r\n"
				+ "T: Ajde Jano\r\n"
				+ "F: http://www.youtube.com/watch?v=gDqhElZLnU8 \r\n"
				+ "F: http://www.youtube.com/watch?v=ZQn2l-DGCQ4\r\n"
				+ "O: Serbia\r\n"
				+ "M: 7/8\r\n"
				+ "L: 1/8\r\n"
				+ "Q: 1/4=120\r\n"
				+ "K: Dm\r\n"
				+ "\"F#m\"F2E D3/2E/FG|\"Dm\"F'2E F1/2E/D/2|\"Dm\"F2E DEFG    |\"Dm\"F3-F4   |\r\n"
				
				+ "";
		//TuneParser parser = new TuneParser(); 
        //Tune tune = parser.parse(abcNotation);
        ABCModifier modifier = new ABCModifier();
  //      System.out.println(modifier.getTimeSignature(abcNotation));
//        Node start = new Node();
//        Trie tr = new Trie(start);
//        int[] m = modifier.abcToInt(abcNotation);
//        tr.trainTrie(m);
//        int[] starting = {4,2};
//        int[] gen = tr.generateSequence(starting, 24);

        String[] aa = modifier.parseRhythm(modifier.extract(abcNotation));
        int[] aaa = modifier.barToLengthArray(aa, 8);
         for(int i=0;i<aaa.length;i++) {        	System.out.println(aaa[i]);
         }
        //for(int i=0;i<m.length;i++) {
        	//System.out.println(m[i]);
        //}
	}
}
