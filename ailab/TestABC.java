package ailab;


public class TestABC {

	public static void main(String[] args) throws Exception {
		String abcNotation = "X:1\nT:example\nM:4/4\nK:C\nC D E C,|G A B c|";
		//TuneParser parser = new TuneParser(); 
        //Tune tune = parser.parse(abcNotation);
        ABCModifier modifier = new ABCModifier();
        int[] m = modifier.abcToInt(abcNotation);
        for(int i=0;i<m.length;i++) {
        	System.out.println(m[i]);
        }
	}
}
