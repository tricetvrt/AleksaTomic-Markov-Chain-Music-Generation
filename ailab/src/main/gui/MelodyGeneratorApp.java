package main.gui;

import java.io.File;

import java.util.HashMap;
import java.util.Map;
import abc.midi.BasicMidiConverter;
import abc.notation.Tune;
import abc.parser.TuneParser;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import main.ailab.ABCModifier;
import main.ailab.Combine;
import main.ailab.MidiConversion;
import main.ailab.Node;
import main.ailab.RhythmGenerator;
import main.ailab.Trie;
import javax.sound.midi.*;

public class MelodyGeneratorApp extends Application {
	private Sequencer sequencer;
	MidiConversion midiconv = new MidiConversion();
    @Override
    public void start(Stage primaryStage) {
        // layouts
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20;");

        // components
        Label degreeLabel = new Label("choose generation degree:");
        ComboBox<Integer> degreeComboBox = new ComboBox<>();
        degreeComboBox.getItems().addAll(1, 2, 3, 4, 5); // example degrees

        Label startLabel = new Label("starting sequence:");
        TextField startSequenceField = new TextField();

        Label lengthLabel = new Label("melody length (number of notes):");
        TextField lengthField = new TextField();
        
        Label timeSignatureLabel = new Label("choose time signature:");
        ComboBox<String> timeSignatureComboBox = new ComboBox<>();
        timeSignatureComboBox.getItems().addAll("4/4",/* "3/4", "2/4", */"7/8"/*, "6/8", "12/8"*/); // example degrees

        Button generateButton = new Button("generate! :)");
        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        
        

        Button playButton = new Button("PLAY");
        Button stopButton = new Button("STOP");

        // add components to layout
        root.getChildren().addAll(
            degreeLabel, degreeComboBox,
            startLabel, startSequenceField,
            lengthLabel, lengthField, 
            timeSignatureLabel, timeSignatureComboBox,
            generateButton, outputArea,
            playButton, stopButton
        );

        generateButton.setOnAction(e -> {
            try {
            
            	
            	ABCModifier modifier = new ABCModifier();
            	RhythmGenerator rhythm = new RhythmGenerator();
                // Retrieve user inputs
                int degree = degreeComboBox.getValue();
                String startingSequence = startSequenceField.getText();
                int[] startingSequenceInteger = modifier.abcToInt(startingSequence);
                int length = Integer.parseInt(lengthField.getText());
                String timeSignature = timeSignatureComboBox.getValue();
                
                Node melodyRoot = new Node();
            	Trie melodyTrie = new Trie(melodyRoot, degree);
            	Node rhythmRoot = new Node();
            	Trie rhythmTrie = new Trie(rhythmRoot, degree);
            	
            	// transforming the training data into arrays of int arrays, which are to be added to the tries
            	int[][] data = rhythm.abcToIntRhythmDatabase("abc_data.txt", "4/4");
                int[][] data2 = modifier.abcToIntDatabase("abc_data.txt");
                rhythmTrie.trainTrieDataset(data);
                melodyTrie.trainTrieDataset(data2);
                
                // now
                Combine combine = new Combine();
                Map<Integer, Integer[]> dictionary = new HashMap<>();
                dictionary = rhythm.getPatternDictionary(timeSignature);
                int[] generatedMelody = melodyTrie.generateSequence(startingSequenceInteger, length);
                int[] generatedRhythm = rhythmTrie.generateSequenceRhythm(length, dictionary);
                String abcNotation = combine.returnAbcNotation(generatedMelody, generatedRhythm, timeSignature);
                
                // display output abc notation
                outputArea.setText(abcNotation);
                
             // parsing the abc notation 
                TuneParser parser = new TuneParser();
                Tune tune = parser.parse(abcNotation);

                // converting to MIDI
                BasicMidiConverter midiConverter = new BasicMidiConverter();
                Sequence sequence = midiConverter.toMidiSequence(tune);
                //midiconv.convertToMidiSequence(generatedMelody, generatedRhythm, dictionary);
                sequencer = MidiSystem.getSequencer();
                if(!sequencer.isOpen())
                	sequencer.open();
               // loadSoundbank(sequencer);
                sequencer.setSequence(sequence);
                
            } catch (Exception ex) {
                outputArea.setText("Error: " + ex.getMessage());
            }
        });
        
        
        playButton.setOnAction(e -> {
            if (sequencer != null && !sequencer.isRunning()) {
                sequencer.start();
            }
        });

        stopButton.setOnAction(e -> {
            if (sequencer != null && sequencer.isRunning()) {
                sequencer.stop();
            }
        });
        
        
        
        // set the scene
        Scene scene = new Scene(root, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Melody Generator");
        primaryStage.show();
    }


    public void loadSoundbank(Sequencer sequencer) throws Exception {
        Synthesizer synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();

        
        File soundbankFile = new File("C:\\Users\\PC\\Downloads\\super_mario.sf2");// load the custom soundbank
        Soundbank soundbank = MidiSystem.getSoundbank(soundbankFile);
        if (synthesizer.isSoundbankSupported(soundbank)) {
            synthesizer.loadAllInstruments(soundbank);
            System.out.println("soundbank loaded successfully");
        } else {
            System.err.println("soundbank not supported");
        }
        // link sequencer to synthesizer
        Transmitter transmitter = sequencer.getTransmitter();
        Receiver receiver = synthesizer.getReceiver();
        transmitter.setReceiver(receiver);

        
        MidiChannel[] channels = synthesizer.getChannels();
        channels[0].programChange(24); // program change to another output instrument
        System.out.println("Instrument changed to Electric Guitar.");
    }

    
    
    public static void main(String[] args) {
        launch(args);
    }
}





