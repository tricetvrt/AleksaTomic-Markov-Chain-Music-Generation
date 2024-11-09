# Weekly report 2

This week I have focused on researching the Markov chains much more in depth, studied abc libraries and started coding the foundation of this future project. Here are the brief points of my progress:

## Markov chains for music generation

As I've said, I have done even deeper research to better understand the concept of Markov chains and various ways to implement them, especially focusing on their application in music generation, which is the core of my project. I’ve read articles and watched numerous visualizations to gain a comprehensive understanding of the topic. After all this research and exploration, I finally felt confident enough to start the coding part.

## Implementing the data structures

First, I started with implementing the essential data structures for this project. Trie, also know as the prefix tree, is the core data structure I will use for realizing the music generation.

## Functions and algorithms

Having written the core functions for implementing the Trie, such as insertion and search algorithms, I moved on to more specialized functions. So far, I've coded a function that returns a 2D array of all possible next notes along with their frequencies, which I used to develop a function that returns all possible notes with their probabilities. This setup enabled me to write a function for generating the next note.

## abc notation research

This week, I also focused on understanding abc notation, a text-based music notation system that encodes musical notes and structures in a compact, readable format. I have gained insight into how this notation can be used and converted in a way that the program can process it. 

## Current problems and dilemmas

Although I did not stumble upon many problems this week, I still have one dilemma I maybe need help with. I have implemented the trie node with the "children" argument which is an array of nodes. I am not sure if it would be better to implement this argument as a List instead. 
## Next steps

Next week, I plan to finish the program foundation for the algorithm. Additionally, I aim to do a more in depth research and try to gather the abc notation data, as I did not manage to complete this task this week.
