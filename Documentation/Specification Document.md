# Specification Document

## Programming languages

I plan to primarily use Java for this project, utilizing its object-oriented capabilities and efficiency in managing states and transitions in Markov chains. Java's support for structured data handling make it suitable for implementing chain algorithms and manipulating musical data.

## Algorithms and data structures to be implemented

The primary algorithm for this project will involve generating Markov chains of varying orders to model musical sequences. I will implement tries as the main data structure, allowing efficient storage and retrieval of note sequences and transitions between them. The trie will represent each sequence of notes as a path, where nodes correspond to individual notes, and branches indicate possible transitions. Additionally, I may use file handling techniques for managing abc notation files and storing generated sequences.

## What problem is solved?

This project aims to explore algorithmic music composition through the use of Markov chains, focusing on the production of musical melodies in abc notation. This project will contribute to the understanding of stochastic processes in creative fields and explore the philosophical significance of AI in creative work.

## Taken inputs and how are they used

The program will take sequences of notes represented in abc notation as input, learning from these to generate probable note sequences. These inputs will be processed by the Markov model to predict note transitions and produce new compositions with the desired level of complexity (degree of Markov chains).

## Desired time and space complexity

For efficient performance, I aim to achieve an optimal time complexity for sequence generation, ideally O(n) where n is the length of the generated sequence. Space complexity will depend on the number of states and transitions stored but will be minimized to ensure manageable memory usage, with tries being space efficient. Generating longer compositions in reasonable time frames is a goal.

---
<p align="right">
Bachelor of Science in Computer Science (CS) <br>
The documentation for this project is written in English.
</p>
