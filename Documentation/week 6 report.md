# Weekly report 6

This week, I have completed the rhythm training and generating. I have implemented JUnit tests for the new methods. Additionally, I completed gathering a decent amount of data.

## Rhythm generating

I have implemented the rest of methods to enable conversing abc notation to an array of rhythm patterns. After that, those pattern sequences will be trained on a trie, different from the melody generating one. That way, using Markov chains, it will be possible to generate a rhythm, and assign different lengths to notes, which adds a layer of sound complexity.

## Implemenitng JUnit tests

For the newly implemented methods, I have written additional JUnit teststo detect any possible errors and ensure that the code is robust.

## Combine class

Furthermore, I have written the code for most of the methods needed to merge the generated note sequence and the generated rhythm pattern sequence. This enabled me to finally generate the ABC notation for the genrerated melody. Covering border cases was particularly important, since through manual testing I have encountered some errors.

##  Gathering the dataset

So far, I think I have completed with gathering the ABC notation training dataset. Still, I will need to process it.

## Current problems and dilemmas

After updating my Eclipse environment, I have had many problems regarding my code, and have spent a lot of time this week on fixing those problems. I have fixed most of them, but still, running JUnit tests from Eclipse does not work as expected. Other than that, I do not seem to have any problems or dilemmas at the moment. If I do, I will reach out to the course assistant.

## Next week 

Although I am close to finishing my project, I have lots of things to do. Now that I have more time, I am going to commit more to the project. I will have to preprocess the data, complete the user interface and work on my documentation. I was thinking about visualizing the melody through creative coding(p5.js), but I am not sure if I will have the time before the presentation.
