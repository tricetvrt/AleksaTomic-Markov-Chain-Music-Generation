package main.ailab;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.sound.midi.Sequence;

public class TestABC { //manual testing, you can ignoree :)

	public static void main(String[] args) throws Exception {
		String abcNotation = "\r\n"
				+ "X:3\r\n"
				+ "T:Cicek Dagi\r\n"
				+ "O:Turkey\r\n"
				+ "M:4/4\r\n"
				+ "K:Bb\r\n"
				+ "D2CED2z2|D2CED2z2|DE^FEGFED|D2CED2z2|DE^FEGFED|D2CED2z2||\r\n"
				+ "C2B,DC2E2|D2CED2z>D|C2B,DC2E2|D2CED2zG,|\r\n"
				+ "B,CDB,C2E2|D2CED2G,A,|B,CDB,C2E2|D2CED2z2|\r\n"
				+ "|:D2G2G2G2|^FF/2A/2GFGFED|DE^FEGFED|DECED2z2:|\r\n"
				+ "C2B,DC2E2|D2CED2z>D|C2B,DC2E2|D2CED2zG,|\r\n"
				+ "B,CDB,C2E2|D2CED2G,A,|B,CDB,C2E2|D2CED2z2|\r\n"
				+ "|:DEFEFEFE|F>EDFE2z2|DzE2DzE2|E>DCED2z2:|\r\n"
				+ "|:D2G2G2G2|^FF/2A/2GFGFED|DE^FEGFED|DECED2z2:|\r\n"
				+ "C2B,DC2E2|D2CED2z>D|C2B,DC2E2|D2CED2zG,|\r\n"
				+ "B,CDB,C2E2|D2CED2G,A,|B,CDB,C2E2|D2CED2z2|\r\n"
				+ "^FGA2A2G2|G2B2A2z2|^FGA2A2G2|G2B2A2z2|^FGA2A2G2|\r\n"
				+ "c2BA/2B/2A2G2|^FGAGA2G2|^FF/2A/2GFE2D2|DE^FEGFED|DECED2z2|\r\n"
				+ "C2B,DC2E2|D2CED2z>D|C2B,DC2E2|D2CED2zG,|\r\n"
				+ "B,CDB,C2E2|D2CED2G,A,|B,CDB,C2E2|D2CED2z2|\r\n"
				+ "|:d>cded2c2|B8|ABcBcdB2|A8:||\r\n"
				+ "|B>AGBA2z2|G^FEGF2z2|E>DCED^FGA|\r\n"
				+ "B>AGBA2z2|G^FEGF2z2|E>DCED2z2|\r\n"
				+ "C2B,DC2E2|D2CED2z>D|C2B,DC2E2|D2CED2zG,|\r\n"
				+ "B,CDB,C2E2|D2CED2G,A,|B,CDB,C2E2|D2CED2z2||";


        

         RhythmGenerator r = new RhythmGenerator();
         ABCModifier abc = new ABCModifier();
//         int[] x = r.abcToIntRhythm(abcNotation, "4/4");
//         for(int i=0;i<x.length;i++) {
//        	 System.out.println(x[i]);
//         }
         
//         int[][] data = r.abcToIntRhythmDatabase("C:\\Users\\PC\\OneDrive\\Documents\\faks\\ai lab projekat\\ailab\\abc_data.txt", "4/4");
//         int[][] data2 = abc.abcToIntDatabase("C:\\Users\\PC\\OneDrive\\Documents\\faks\\ai lab projekat\\ailab\\abc_data.txt");
//// 
//         for(int i=0;i<data2.length;i++) {
//        	 for(int j=0;j<data2[i].length;j++) {
//        		 System.out.print(data2[i][j] + " ");
//        	 }
//        	 System.out.println("NEXT");
//         }
        Node koren = new Node();
        Node koren2 = new Node();
 		Trie drvo2 = new Trie(koren2,2);
 		Trie drvo3 = new Trie(koren,2);
 		drvo3.resetTrie();
 // 		drvo3.trainTrieDataset(data);
// 		drvo2.trainTrieDataset(data2);
// 		int[] x = drvo3.generateSequenceRhythm(10, RhythmGenerator.patterns_4_4 );
// 		for(int i = 0 ; i<x.length; i++)
// 			System.out.println(x[i]);
// 	    int[] y = drvo2.generateSequence(new int[]{43, 45}, 10);
// 		
// 		Combine c = new Combine();
//
// 		//System.out.println(c.returnAbcNotation(y, x, RhythmGenerator.patterns_4_4));
// 		
// 		MidiConversion mid = new MidiConversion();
// 		Sequence s = mid.convertToMidiSequence(y, x, RhythmGenerator.patterns_4_4);
// 		
 		

 		//System.out.println(drvo3.generateNext(new int[] {1}));
        
 
        
        
 		String sampleData = 
 	            "X: 1\nT: Sample\nM: 4/4\nK: C\n|: C2 C2 C2 C2 :| C2 C2 C2 C2 :| C2 C2 C2 C2 :| C2 C2 C2 C2 :|\n$$$" +
 	            "X: 2\nT: Another Sample\nM: 3/4\nK: C\n|: D2 D2 D2 :| C4 C4 :| C2 C2 C2 :| C C4 C :|\n$$$" +
 	            "X: 3\nT: Third Sample\nM: 4/4\nK: C\n|: E2 E2 E2 E2 :| C2 C2 C2 C2 | C2 C2 C2 C2 |";
 	   
            Path tempFile = Files.createTempFile("abc_test", ".txt");
            Files.writeString(tempFile, sampleData);

            
            String content = Files.readString(Paths.get(tempFile.toString()));
            String[] notations = content.split("\\$\\$\\$");
            
            List<int[]> result = new ArrayList<>();
    		for(int i=0; i<notations.length; i++) {
    			int[] saf = r.abcToIntRhythm( notations[i], "4/4");
    			for( int j = 0 ; j< saf.length ; j++)
    			System.out.println(saf[j]);
    			if(r.getTimeSignature(notations[i]).equals("4/4"))// filtering the dataset based on the given time signature
    				result.add(r.abcToIntRhythm(notations[i], "4/4"));// adding the pattern sequences to the list
    		}
    		int[][] result2 = new int[result.size()][]; // converting to int matrix so that training is possible
    		for( int i = 0 ; i< result2.length ; i++) {
    			result2[i] = result.get(i);
    		}
            int[][] resulttt = r.abcToIntRhythmDatabase(tempFile.toString(), "4/4");
            for(int i = 0; i< resulttt.length; i++) {
            	for(int j = 0 ; j< resulttt[i].length ; j++)
            		System.out.println(resulttt[i][j]);
            }

            Files.delete(tempFile);
 	//	Random random = new Random();
       // int randomIndex = random.nextInt(RhythmGenerator.patterns_4_4.size());
        //System.out.println(randomIndex);
// 		 drvo2.resetTrie();
// 		 drvo2.trainTrieDataset(data);
// 		 int[] melodija = drvo2.generateSequence(new int[]{36}, 120);
// 		 for(int i = 0; i<melodija.length; i++) {
// 			 System.out.print(a.intToNote(melodija[i]));
// 			 if((i+1)%4 ==0)
// 				 System.out.print(" | ");}
 		 
 		 
 		 
 //       
  //       int[] res = r.abcToIntRhythm(abcNotation2);
//         for(int i=0;i<res.length;i++)
//        	 System.out.println(res[i]);
     //    String[] x = a.parse(a.extract(abcNotation2));
//         for(int i=0;i<x.length;i++)
//        	 System.out.println(x[i]);
   //      System.out.println(c.returnAbcNotation(x, res, RhythmGenerator.patterns_4_4));

         
        
  //      System.out.println(modifier.getTimeSignature(abcNotation));
//        Node start = new Node();
//        Trie tr = new Trie(start);
//        int[] m = modifier.abcToInt(abcNotation);
//        tr.trainTrie(m);
//        int[] starting = {4,2};
//        int[] gen = tr.generateSequence(starting, 24);

//        String[] aa = modifier.parseRhythm(modifier.extract(abcNotation));
//        int[] aaa = modifier.barToLengthArray(aa, 8);
//         for(int i=0;i<aa.length;i++) {        	System.out.println(aa[i]);
//         }
        //for(int i=0;i<m.length;i++) {
        	//System.out.println(m[i]);
        //}
	}
}
