# The Testing Document
With the implementation of JUnit5 tests for the classes and functions of this project, I was able to detect and later fix numerous logical or syntax mistakes. This is a brief document explaining how the project was tested.

## The coverage report of the JUnit tests
![coverage](https://github.com/user-attachments/assets/5d5fc30c-39ad-4241-b468-327267c23758)

The tests implemented cover most of the project's code. The test suite turned out to be particularly effective, and most of the lines not covered are either simple or using already covered methods. The system meets performance and accuracy expectations. Further testing is recommended with more extensive datasets to validate scalability.

## What has been tested and how?

For each main class, there is a corresponding test class which covers an extensive number of cases:

### TrieTest

 - A Trie object was created as a setup and used for unit testing.
 - The TrieTest class tests various functionalities of the Trie class used for sequence and rhythm generation. It verifies that sequences are correctly inserted, searched, and managed for transitions, probabilities, and generation.
 - For every generated sequence, it was tested if their subsequences exist in the given trie, while taking into account the error handling functionalities.
 - Both valid and invalid inputs in the formof array sequences were used, ensuring that the exceptions were handled well.

### ABCModifierTest

  - An ABCModifier object was created as a setup and used for unit testing.
  - The ABCModifierTest class tests various functionalities of the ABCModifier class, which handles ABC notation conversions and manipulations.
  - The tests verify correct handling of input and output for converting ABC notation to integer values and vice versa, parsing, extracting, and converting notes.
  - Some of the methods have multiple unit tests, given the number of possible edge cases which needed to be covered.
  - Both valid and invalid ABC notation (String) and integer inputs were used to additionaly ensure that the exceptions were handled well.
    
### RhythmGeneratorTest

- A RhythmGenerator object was created as a setup and used for unit testing.
- The RhythmGeneratorTest class tests various functionalities of the RhythmGenerator class, which handles rhythm generation, parsing, and additional ABC notation conversions in the context of note lengths.
- Multiple different inputs, including malformed ones were tested for the implemented methods, in order to cover all of the functionalities and as many edge cases as possible.
- The inputs for this class were also in the forms of ABC notation (String) and integer sequences.
  
### CombineTest

- A Combine object was created as a setup and used for unit testing.
- The CombineTest class tests various functionalities of the Combine class, which handles combining the generated melodies and rhythms and formatting them correctly.
- Both valid and invalid inputs are tested, ensuring that exceptions are handled correctly for edge cases, including malformed input sequences and incorrect note lengths.

## Running the tests

To run the implemented unit tests, follow the these steps:

  1. **Ensure Maven is Installed**: if not already installed, you will need to install Maven from [here](https://maven.apache.org/install.html)
  3. **Clone the Repository**: if you haven't cloned the project yet, you should run:
```bash
git clone https://github.com/tricetvrt/AleksaTomic-Markov-Chain-Music-Generation
```
  3. **Run Tests with Maven** : To run the JUnit tests, use the following command:
```bash
mvn test
```
  This will automatically compile the code, run the tests, and display the results in the terminal/command line.

After running `mvn test`, users can find the results in the `target/surefire-reports/` directory. These reports are available as text files and can be useful for a detailed look into the testing.


