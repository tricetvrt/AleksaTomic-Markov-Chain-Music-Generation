package main.ailab;


import java.util.Map;

import javax.sound.midi.*;

public class MidiConversion {
	
	
	
	
	public Sequence convertToMidiSequence(int[] notes, int[] rhythm,  Map<Integer, Integer[]> dictionary) throws Exception {
        // create a new MIDI sequence with a single track
        Sequence sequence = new Sequence(Sequence.PPQ, 480); // pulses per quarter note
        Track track = sequence.createTrack();

        int helper = 0; //indicates the position of the last durations element
        Integer[] durations = new Integer[notes.length];
        for(int i = 0; i< rhythm.length ; i++) {
        	int patternLength = dictionary.get(rhythm[i]).length;
        	if(helper>= notes.length)
        		break;
        	if( helper + patternLength >= notes.length)
        		patternLength = notes.length-helper;
        		
        	System.arraycopy( dictionary.get(rhythm[i]), 0, durations, helper, patternLength); // vhanges an array of pattern keys to array of note durations
        	helper += patternLength; // modifying the position of the last int in the durations array
        }
        
        //System.out.println("Durations: " + Arrays.toString(durations));
        
        // add a tempo meta message (e.g., 120 BPM)
        MetaMessage tempoMessage = new MetaMessage();
        int tempo = 500000; // Microseconds per quarter note (120 BPM)
        tempoMessage.setMessage(0x51, new byte[] {
            (byte)(tempo >> 16), 
            (byte)(tempo >> 8), 
            (byte)(tempo)
        }, 3);
        track.add(new MidiEvent(tempoMessage, 0));

        // add notes to the track
        int tick = 0; // starting tick
        for (int i = 0; i < notes.length; i++) {
            int note = notes[i] -12; // MIDI note number
            int duration = durations[i]; // duration in ticks

            
            System.out.println("Tick before note " + i + ": " + tick);
            System.out.println("Adding Note-on: " + note + " at tick " + tick);
            // add note-on event
            ShortMessage noteOn = new ShortMessage();
            noteOn.setMessage(ShortMessage.NOTE_ON, 0, note, 64); // channel 0, velocity 64
            track.add(new MidiEvent(noteOn, tick));

            // add note-off event
            ShortMessage noteOff = new ShortMessage();
            noteOff.setMessage(ShortMessage.NOTE_OFF, 0, note, 64);
            track.add(new MidiEvent(noteOff, tick + duration));

            tick += duration; // advance tick by duration
            
            System.out.println("Adding Note-off: " + note + " at tick " + (tick + duration));
        }

        return sequence;
    }
	
	


}
