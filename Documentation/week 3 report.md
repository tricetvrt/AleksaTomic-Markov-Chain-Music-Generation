# Weekly report 3

This week I have been finishing and perfecting the code for my project, while researching and getting familiar with abc notation. Here are the brief points of my progress:

## Perfecting last week's code

Although the code from last week was mostly well-written, I went back to it and included additional test cases to handle edge cases better. I have rewritten the code to generate the next note using a weighted random approach instead of selecting the trie child with the highest probability. 

## Implementing the rest of Markov chains

I think that this week I have finished the implementation of Markov chains, implementing a final function for generating a sequence. Still, along the way, I may need some adjustments to the code. Additionally, I have implemented a train function and one for reseting the trie which may be useful while working with multiple datasets.

## Handling abc notation

This week, I also wrote functions for modifying and adapting the ABC notation to the integer array needed for my trie. After facing many complications with existing Java libraries for handling ABC notation, and after consulting with the course assistant, I decided to implement the conversion functions myself. I first implemented functions for extracting and parsing an ABC notation code. Then, I developed a function that converts a note to an integer, taking modifiers into account. Finally, I implemented a function to convert an entire ABC code into an integer array.

##  More abc notation research

Although I have researched this notation last week, there is a lot to cover so this week I became much more familiar with its syntax.

## Current problems and dilemmas

I'm thinking if I should include pairs of musical notes to add to complexity of the generated melody, and treat them as one note. For an example FE. This would require some changes in the noteToInt function code.

## Next week 

Next week, I plan to finally decide on the data I want my model to be trained on, and what level of data diversity should I pick. I need to make sure that the training data is consistent and possible only in one key. I should also start working on ways to test my project. Additionaly, I will make changes to my code if needed. 
