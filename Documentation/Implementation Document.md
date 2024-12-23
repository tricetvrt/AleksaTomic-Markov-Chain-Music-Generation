# The Implementation Document

The music generation idea was realized with the use of Markov chains and tries as the main data structure. However, there was the need to implement lots of conversion and preprocessing methods, and finally a graphic user interface. 

## Program structure

These are the main components of this program's structure:

1. **JavaFX GUI :**
   - Built using JavaFX, this module enables the user to customize the melody that is to be generated through the change of Markov chain degree, melody length, time signature and by picking the starting sequence. The output field showcases the ABC notation of the generated melody, and it is possible to play the audio of the MIDI sequence which we got by converting the ABC notation.
   - Key classes: `MelodyGeneratorApp`.

2. **ABC parsing and processing:**
   - Handles conversion between ABC notation and internal integer representations, for the note sequence generation, but also for generating the rhythm of the melody. The goal was to enable working even with messy training data by covering numerous edge cases.
   - Key classes: `ABCModifier`, `RhythmGenerator`, `Combine`.

3. **Data structures:**
   - Implements tries for melody and rhythm sequence generation based on the training data. The program uses two separate tries for the mentioned, and the dataset used for training the rhythm generation trie is filtered based on the time signature.
   - Key classes: `Trie`, `Node`.

4. **Rhythm and Melody Generation:**
   - Enables generating the melody sequence and the rhythm sequence based on the data and the Markov chain process. Later the two generated sequences are combined in one more complex sounding melody. The model detects patterns in both note sequences and the sequences rhythm patterns of the training data.
   - Key classes: `RhythmGenerator`, `Trie`, 'Combine'.


## Time and Space Complexities
Although the time and space complexities were not the main theme of this project, it was still important to simplify the program and make them as lower as possible.

### Trie Data Structure:
- **Time Complexity:**
  - For training the complexity was O(N * M), where N is the number of sequences and M is the average sequence length.
  - For generation the complexity would be O(L), where L is the chosen length of the sequence we want to generate.
- **Space Complexity:**
  - O(D * L), where D is the degree of the trie, and L is the total number of unique sequences.

### ABC Parsing:
- **Time Complexity:** O(N), where N is the length of the input ABC data.
- **Space Complexity:** O(N), proportional to the parsed data.


## Performance and Big O Analysis Comparison
The focus of this project was not primarely on the space and time complexities, or achieving a particularly fast generation. It was more on the ways of generating somewhat complex melodies which sound decent. Still, I wanted to argue why I made some decisions regarding my project. 

### Why tries?
The trie structure ensured very efficient lookups, making generation much easier. Still, they have has higher memory usage when compared to hash maps.
Alternative approaches such as Markov chains with direct mappings would reduce memory used, but might be less useful and effective for generating complex melodies.

### Rhythm generation space usage:
Rhythm generation could possibly be done in ways which do not increase time complexities and memory usage as much.
Still, this was a great way to use Markov chains for something new and to keep the project in scope, while still creating good sounding music sequences.

## Potential improvements:
Gathering a larger dataset which covers all of the time signatures I wanted to implement in rhythm generation could improve the generation. I would also like to modify the GUI, enabling the user to choose different sound outputs and possibly visualizing the melodies played using `p4.js`. Creating a MIDI conversion from scratch would be helpful, as it would eliminate the need for external libraries used for the conversion. This would need me to gain more insight into the MIDI structure.


## Use of extensive Language Models
ChatGPT was used for assistance in finding code errors, and mostly for help with the environment setups, which is something that troubled me the most during the project implementation. It was also used for giving me more insight into music theory, which was needed to fully understand the ways of generating a proper melody.


## References:

   - [Learn ABC notation](https://abcnotation.com/learn)
   - [Markov Chains Explanation](https://builtin.com/machine-learning/markov-chain)
   - [Tries Explained](https://www.geeksforgeeks.org/introduction-to-trie-data-structure-and-algorithm-tutorials/)
   - [Understanding Time Signatures](https://www.skoove.com/blog/time-signatures-explained/)
   - [Henrick Norbeck's ABC Tunes](https://www.norbeck.nu/abc/index2.asp?cat=m)
     
