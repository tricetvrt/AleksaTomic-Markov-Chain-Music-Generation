# Weekly report 4

This week, I have been perfecting the ABC notation modification code for my project, implementing rhythm generation, collecting some of my training data, and familiarizing myself with the necessary general knowledge in the field of music. Here are the brief points of my progress:

## Modifying the conversion method and other methods

After discussing possible conversion methods with the course assistant, I realized my approach did not account for flats and sharps. I extended all the related conversion methods and addressed numerous boundary cases encountered while exploring ABC notation, obtaining training data, and lots of example testing. These improvements ensure more accurate handling of complex musical notations and improve the overall stability of my implementation. I have also decided to split grouped notes and ignore the lengths of notes while generating a sequence, and rather generate the rhythm afterwards. 

## First generated sequences

After perfecting the foundation code, I have managed to generate fairly satisfying melodies using a very small sample of data. However, due to the simplicity of the sound, I realized that rhythm generation is essential, and using prewritten patterns alone would not be sufficient. It was also clear that a larger dataset is needed for the generated melody to be unique, but still, it was enough to encourage me.

## Implementing parts of rhythm generation

I have started implementing a method to generate rhythm using different patterns of note length combinations found in the dataset. This will also be implemented using tries and a Markov chain, while taking bars into consideration and maintaining a separate trie for each time signature. I have created another parsing function to detect note length patterns, which returns an array of note lengths relative to the base length defined in the ABC notation. Additionally, I wrote code to extract the default note length and time signature of an ABC melody. This information is used to translate the parsed array of strings within a bar into an integer array of note length duration, where 1 is equal to a 1/16 long note. These arrays can then be added to a hash map corresponding to the time signature. For every pattern not already in the hash map, it will be added, and every pattern will also be included in the Markov chain trie as a potential successor to its preceding pattern. Each pattern will be assigned a unique integer value.

## Obtaining an ABC dataset

This week, I have spent considerable time selecting my training data and exploring the factors that need to be considered. Currently, I plan to use a dataset of folklore melodies, mainly from Arab, Balkan, or Chinese folklore music. However, if I cannot find enough data to achieve the desired generation complexity, I will switch to a genre with a larger collection of ABC melodies. Additionally, I intend to experiment with output sounds in the interface, potentially incorporating electronic sounds to play folklore music, enhancing the experimental and artistic value while acknowledging and exploring the process behind these melodies.

##  Exploring general musical matter

In order to fully understand how to generate complex, stable melodies without affecting the overall structure, I needed to do more research on the fundamentals of music theory and melody composition. Understanding time signature was especially crucial for my rhythm implementation.

## Current problems and dilemmas

I am thinking of possible ways to store my data, and if it is convenient to only store all of the melodies of one genre in one document separated in a particular way. Also, I am thinking of using only melodies in one key, but am not sure if that is the way to go.

## Next week 

Next week I aim to complete my rhythm generation and complete obtanining a larger dataset. I am also planning to preprocess that data, and start working on proper unit tests.
