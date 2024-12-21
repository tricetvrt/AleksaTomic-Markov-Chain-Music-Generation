# User Guide

This document serves as a guide for using the Melody Generator application. 

---
## **Prerequisites:**

  1. **Java development kit (JDK)**:
   - Recommended version: JDK 23 is used in this project. However, you can also modify the version specified in `<release>23</release>` in the `pom.xml` file.
   - Ensure the environment variable `JAVA_HOME` points to the JDK 23 installation.
   - **Download**: [Oracle JDK](https://www.oracle.com/java/technologies/javase-jdk23-downloads.html) or [OpenJDK](https://jdk.java.net/23/).
   - To check if the kit was properly installed, type the following in the command prompt or terminal:
```bash
java -version
```

  2. **Apache Maven**:
   - Maven is required to handle dependencies and manage the building. Additionally, it handles JUnit tests. 
   - Recommended version: Maven 3.8.0 or later.
   - **Download**: [Apache Maven](https://maven.apache.org/download.cgi).
   - To check if the Maven was properly installed, type the following in the command prompt or terminal:
```bash
mvn -v
```

  3. **JavaFX SDK**:
   - **Version**: 20 (as specified in the JavaFX dependencies of the `pom.xml` file).
   - **Download**: [JavaFX SDK](https://openjfx.io/).
   - Ensure the JavaFX runtime and SDK are correctly installed. To ensure that they are in your classpath, open the terminal or command prompt and write:
```bash
echo $PATH  # for Linux/macOS (JavaFX)
echo %PATH%  # for Windows (JavaFX)
echo $JAVA_HOME  # for Linux/macOS (JDK)
echo %JAVA_HOME%  # for Windows (JDK)
```

  4. **JUnit**:
   - If properly installed, Maven will automatically download JUnit (version 5.10.0) during the build process.


## **Running the program:**
   - Compile the project using your preferred IDE (e.g., Eclipse) or a build tool (like Maven).
     ```
     mvn clean compile
     ```
   - Run the `MelodyGeneratorApp` class:
     ```
     java -cp target/classes main.gui.MelodyGeneratorApp
     ```
   - You can also execute the program directly from your IDE by setting the `MelodyGeneratorApp` class as the entry point.

---
## Understanding ABC notation:

- **Notes**: `C`, `D`, `E`, `F` `G`, `A`, `B`, and `z` for breaks.  `c`, `d`, `e`, `f` `g`, `a`, `b` are the base notes raised by an octave.
- **Flats**: We write flats by adding `_` before the note ( `_D` is D flat)
- **Sharps**: On the other hand, we write sharps by adding `^` before the note (`^F` for F sharp)  
- **Modifiers**: After the note add `'` for raising an octave (`c'` is C raised by 2 octaves), and `,` for lowering an octave ( `_D,` is D flat lowered by an octave).
- **Note lengths**: The number after the note multiplies the basic length by it. For an example, if the base length is 1/16, `C4` will be the length of 1/4.

## The use of different functionalities:

### 1. **Choose generation degree:**
   - Use the dropdown menu to select the degree of the Markov chain. 
   - To explain it shortly, if the degree is two, the patterns are being learned based on the two previous states. The higher the degree, the more context-aware and complex the melodies are. Higher degrees need more extensive datasets.

### 2. **Input Starting Sequence:**
   - Enter a sequence of notes (in ABC notation) to serve as the starting point for melody generation.
   - **Notice**: The starting sequence should have **the length equal to the degree chosen**.
   - You can write all of the possible notes, and use the modifiers in the forms of '() and ,
   -  Examples if the degree is 2:
     ```
     C C
     ```
     ```
     d c
     ```

Note: You do not have to use spaces in between, and are allowed to write multiple spaces, the parsing takes care of basically everything. Note lengths are irrelevant in this input.

### 3. **Specify Melody Length:**
   - Provide the desired number of notes for the generated melody. For example, entering `50` will generate a melody with 50 notes.

### 4. **Choosing the time signature:**
   - Pick a time signature you would like the melody to be generated in. If you are unfamiliar with time signatures, you can learn more about them [here](https://www.skoove.com/blog/time-signatures-explained/)

### 5. **Generate Melody:**
   - Finally, click the **"generate! :)"** button to generate the melody. The output in the text area below is the generated melody written in ABC notation.

### 6. **Play and Stop Buttons:**
   - Click **"PLAY"** to listen to the generated melody.
   - Click **"STOP"** to stop the playback.

---

## Training dataset:
 - The training dataset is the given text file [abc_data.txt](https://github.com/tricetvrt/AleksaTomic-Markov-Chain-Music-Generation/blob/main/ailab/abc_data.txt)
 - You are allowed, and incouraged to gather your own data, and choose your own genre you would like to explore generation with.
 - Ensure the training data (`abc_data.txt`) is in place and that the ABC notation melodies are separated  by `$$$`, which is used for handling the dataset in the program.

If you encounter any issues or errors, look at the implementation document for debugging possibilities or potential improvements. 
You can also contact me at [nzmaleksa@gmail.com](mailto:nzmaleksa@gmail.com) for clearing any possible misunderstandings.
Also, please send me your amazing creations to the given mail! Have fun :)
