package ailab;

import java.util.HashMap;
import java.util.Map;

public class RhythmGenerator {
	private static final Map<Integer, Integer[]> patterns_4_4 = new HashMap<>();
	private static final Map<Integer, Integer[]> patterns_3_4 = new HashMap<>();
	private static final Map<Integer, Integer[]> patterns_2_4 = new HashMap<>();
	private static final Map<Integer, Integer[]> patterns_7_8 = new HashMap<>();
	private static final Map<Integer, Integer[]> patterns_6_8 = new HashMap<>();
	private static final Map<Integer, Integer[]> patterns_12_8 = new HashMap<>();//maybe no need

    static {
    	patterns_4_4.put(0, new Integer[]{4,4,4,4});
    	patterns_4_4.put(1, new Integer[]{8,8});
    	patterns_4_4.put(2, new Integer[]{2,4,2,8});
    	patterns_4_4.put(2, new Integer[]{2,2,4,8});
    	patterns_4_4.put(2, new Integer[]{4,2,2,8});
    	patterns_4_4.put(3, new Integer[]{2,2,2,2,2,2,2,2});
    	patterns_4_4.put(3, new Integer[]{2,2,4,2,2,4});
    	patterns_4_4.put(3, new Integer[]{2,2,2,4,2,4});
    	patterns_4_4.put(4, new Integer[]{6,2,4,4});
    	patterns_4_4.put(5, new Integer[]{1,1,1,1});
    	
    	
    }
    
    
}
