/**
 * Created by User on 22.03.2017.
 */

import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;

public class BeatBoxStudio {

    JPanel mainPanel;
    ArrayList<JCheckBox> checkBoxList;
    Sequencer sequencer;
    Sequence sequence;
    Track track;
    JFrame theFrame;

    String[] instrumentNames = {"Bass Drum", "Closed Hi-Hat", "Open Hi-Hat",
                                "Acoustic Snare", "Crash Cynbal", "Hand Clap",
                                "High Tom", "Hi Bongo", "Maracas", "Whistle",
                                "Low Conga", "Cowbell", "Vibraslap", "Low-mid Tom",
                                "High Agogo", "Open Hi Conga"};

    int[] instruments = {35,42,46,38,49,39,50,60,70,72,64,56,58,47,67,63};

    public static void main (String[] args) {
        new BeatBoxStudio().buildGUI();
    }

    public void buildGUI () {
        theFrame = new JFrame("Cyber BeatBox");
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BorderLayout layout = new BorderLayout();
        JPanel background = new JPanel(layout);
        background.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        checkBoxList = new ArrayList<JCheckBox>();
        Box buttonBox = new Box(BoxLayout.Y_AXIS);

        JButton start = new JButton("Start");
        start.addActionListener(new MyStartListener());
        buttonBox.add(start);

        JButton stop = new JButton("Stop");
        stop.addActionListener(new MyStopListener());
        buttonBox.add(stop);

        JButton upTempo = new JButton("Up Tempo");
        upTempo.addActionListener(new MyUpTempoListener());
        buttonBox.add(upTempo);

        JButton downTempo = new JButton("Down Tempo");
        downTempo.addActionListener(new MyDownTempoListener());
        buttonBox.add(downTempo);

        JButton save = new JButton("Save Bit");
        save.addActionListener(new SaveMyBitListener());
        buttonBox.add(save);

        JButton load = new JButton("Load Bit");
        load.addActionListener(new LoadMyBitListener());
        buttonBox.add(load);

        JButton clear = new JButton("Clear All");
        clear.addActionListener(new ClearAllListener());
        buttonBox.add(clear);

        Box nameBox = new Box(BoxLayout.Y_AXIS);
        for (int i = 0; i < 16; i++){
            nameBox.add(new Label(instrumentNames[i]));
        }

        background.add(BorderLayout.EAST, buttonBox);
        background.add(BorderLayout.WEST, nameBox);

        theFrame.getContentPane().add(background);

        GridLayout grid = new GridLayout(16,16);
        grid.setVgap(1);
        grid.setHgap(2);
        mainPanel = new JPanel(grid);
        background.add(BorderLayout.CENTER, mainPanel);

        for (int i = 0; i < 256; i++) {
            JCheckBox c = new JCheckBox();
            c.setSelected(false);
            checkBoxList.add(c);
            mainPanel.add(c);
        }

        setUpMIDI();

        theFrame.setBounds(50,50,300,300);
        theFrame.pack();
        theFrame.setVisible(true);

    }

    public void setUpMIDI () {
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();;
            sequence = new Sequence(Sequence.PPQ,4);
            track = sequence.createTrack();
            sequencer.setTempoInBPM(120);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buildTrackAndStart () {
        int[] tracklist = null;

        sequence.deleteTrack(track);
        track = sequence.createTrack();

        for (int i = 0; i < 16; i++) {
            tracklist = new int[16];

            int key = instruments[i];

            for (int j = 0; j < 16; j++) {

                JCheckBox jc = (JCheckBox) checkBoxList.get(j + (16 * i));
                if (jc.isSelected()) {
                    tracklist[j] = key;
                } else {
                    tracklist[j] = 0;
                }
            }

            makeTracks(tracklist);
            track.add(makeEvent(176,1,127,0,16));
        }

        track.add(makeEvent(192,9,1,0,15));
        try {

            sequencer.setSequence(sequence);
            sequencer.setLoopCount(sequencer.LOOP_CONTINUOUSLY);
            sequencer.start();
            sequencer.setTempoInBPM(120);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyStartListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            buildTrackAndStart();
        }
    }

    public class MyStopListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            sequencer.stop();
        }
    }

    public class MyUpTempoListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            float tempoFactor = sequencer.getTempoFactor();
            sequencer.setTempoFactor((float) (tempoFactor * 1.03));
        }
    }

    public class MyDownTempoListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            float tempoFarcor = sequencer.getTempoFactor();
            sequencer.setTempoFactor((float) (tempoFarcor * .97));
        }
    }

    public class SaveMyBitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean[] checkBoxState = new boolean[256];

            for (int i = 0; i < 256; i++)
            {
                JCheckBox check = checkBoxList.get(i);
                if (check.isSelected()) {
                    checkBoxState[i] = true;
                }
            }

            try {

                FileOutputStream fileStream = new FileOutputStream(new File("CheckBox.ser"));
                ObjectOutputStream os = new ObjectOutputStream(fileStream);
                os.writeObject(checkBoxState);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public class LoadMyBitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean[] checkBoxState = null;

            try {

                FileInputStream fileStream = new FileInputStream(new File("CheckBox.ser"));
                ObjectInputStream is = new ObjectInputStream(fileStream);
                checkBoxState = (boolean[]) is.readObject();

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            for (int i = 0; i < 256; i++) {
                JCheckBox check = (JCheckBox) checkBoxList.get(i);
                 if (checkBoxState[i]) {
                     check.setSelected(true);
                 } else {
                     check.setSelected(false);
                 }
            }

            sequencer.stop();
            buildTrackAndStart();
        }
    }

    public class ClearAllListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JCheckBox check;
            sequencer.stop();


            for (int i = 0; i < 256; i ++) {
                check = (JCheckBox) checkBoxList.get(i);
                check.setSelected(false);
            }
        }
    }

    public void makeTracks (int[] list) {

        for (int i = 0; i < 16; i++) {
            int key = list[i];

            if (key != 0) {
                track.add(makeEvent(144,9,key,100,i));
                track.add(makeEvent(128,9,key,100,i+1));

            }
        }
    }

    public MidiEvent makeEvent(int comd, int chan, int one, int two, int ticks) {
        MidiEvent event = null;
        try {
            ShortMessage a = new ShortMessage();
            a.setMessage(comd, chan, one, two);
            event = new MidiEvent(a, ticks);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return event;
    }



} //Конец BeatBoxStudio
