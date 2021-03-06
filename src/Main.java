package com.adg.MIN3;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] ags) throws SimulationException, TheDevelopersHaveFallenAsleepException {
        MoreMethods methods = new MoreMethods();

        // Initialize variables
        int numPeople = 0;
        int hubNumber = -1;
        int minFriends = -1;
        int maxFriends = -1;
        String layoutString = "";
        String networkSelectString = "";
        String graphString = "";
        boolean drawJung = true;

        boolean done = false; // Controls when while loop stops

        // Gets input from user before graph
        while (!done) {
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(9, 0));
            JTextField numPeopleAnswer = new JTextField("100", 10);
            JTextField minFriendsAnswer = new JTextField("2", 10);
            JTextField hubNumberAnswer = new JTextField("0", 10);
            JTextField maxFriendsAnswer = new JTextField("5", 10);
            String[] possibilities = {"Circle", "FR", "ISOM", "Spring"};
            JComboBox<String> layoutAnswer = new JComboBox<String>(possibilities);
            String[] possibilitiesNetwork = {"Small World", "Random", "Scale-Free"};
            JComboBox<String> comboBoxNetwork = new JComboBox<String>(possibilitiesNetwork);
            JCheckBox checkBoxGraph = new JCheckBox();

            panel.add(new JLabel("PEOPLE SETUP:"));
            panel.add(new JLabel("----------------------------------------------"));
            panel.add(new JLabel("How many people?"));
            panel.add(numPeopleAnswer);
            panel.add(new JLabel("What is the minimum amount of friends?"));
            panel.add(minFriendsAnswer);
            panel.add(new JLabel("What is the maximum amount of friends?"));
            panel.add(maxFriendsAnswer);
            panel.add(new JLabel("How many hubs (random) or crossover lines (small world)?   "));
            panel.add(hubNumberAnswer);
            panel.add(new JLabel("Which type of network?"));
            panel.add(comboBoxNetwork);
            panel.add(new JLabel("GRAPH SETUP:"));
            panel.add(new JLabel("----------------------------------------------"));
            panel.add(new JLabel("Draw the jung diagram?"));
            panel.add(checkBoxGraph);
            panel.add(new JLabel("Which layout?"));
            panel.add(layoutAnswer);

            int result = JOptionPane.showConfirmDialog(null, panel, "Before-Graph Configuration", JOptionPane.OK_CANCEL_OPTION);

            if (result != JOptionPane.OK_OPTION) {
                System.exit(0);
            }

            try {
                String numPeopleAnswerString = numPeopleAnswer.getText();
                numPeople = Integer.parseInt(numPeopleAnswerString);
                if (numPeople < 1) {
                    throw new NumberFormatException();
                }

                String hubNumberString = hubNumberAnswer.getText();
                hubNumber = Integer.parseInt(hubNumberString);
                if (hubNumber < 0 || hubNumber > numPeople) {
                    throw new NumberFormatException();
                }

                String minFriendsAnswerString = minFriendsAnswer.getText();
                minFriends = Integer.parseInt(minFriendsAnswerString);
                if (minFriends < 0 || minFriends > numPeople) {
                    throw new NumberFormatException();
                }

                String maxFriendsAnswerString = maxFriendsAnswer.getText();
                maxFriends = Integer.parseInt(maxFriendsAnswerString);
                if (minFriends > maxFriends || maxFriends > numPeople) {
                    throw new NumberFormatException();
                }

                layoutString = (String) layoutAnswer.getSelectedItem();
                networkSelectString = (String) comboBoxNetwork.getSelectedItem();
                if (!checkBoxGraph.isSelected()) {
                    drawJung = false;
                }

                done = true; // Only done when go through try without errors
            } catch (NumberFormatException e) {
                if (result == JOptionPane.OK_OPTION) {
                    JOptionPane.showMessageDialog(new JFrame(), "ERROR: Input is invalid.", "Input Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    System.exit(0);
                }
            }
        }
        // ARAYLIST OF PEOPLE THAT WE WILL BE WORKING WITH!!! IMPORTANT IMPORTANT IMPORTANT!!!!!
        ArrayList<Person> people = new ArrayList<Person>();
        for (int i = 1; i < numPeople + 1; i++) { //Start with 1 so we don't have a number 0 which is extra
            Person person = new Person(i);
            people.add(person);
        }
        if (networkSelectString.equals("Random")) {
            methods.befriendRandom(people, minFriends, maxFriends, new Random(), hubNumber);
        }
        if (networkSelectString.equals("Small World")) {
            methods.befriendSmallWorld(people, minFriends, maxFriends, new Random(), hubNumber);
        }
        if (networkSelectString.equals("Scale-Free")) {
            methods.befriendScaleFree(people, minFriends, maxFriends, new Random());
        }
        if (drawJung) {
            // Creating the graph, vertices and frame
            UndirectedSparseMultigraph<Person, String> graph = new UndirectedSparseMultigraph<Person, String>();
            JFrame frame = new JFrame("ManyLinesAverage");
            methods.drawVerticies(graph, people);

            // Create vv and layout
            int xDim = 1000;
            int yDim = 600;
            //LAYOUT STUFF THAT SHOULD WORK BUT PROBABLY WON"T
            Layout<Person, String> layout = methods.chooseLayout(graph, layoutString);
            layout.setSize(new Dimension(xDim, yDim));

            VisualizationViewer vv = new VisualizationViewer<Person, String>(layout);
            vv.setPreferredSize(new Dimension(xDim + 50, yDim + 50));
            vv.getRenderContext().setVertexLabelTransformer(Person.labelByID);//Makes Labels on Vertices
            vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);

            // Graph Mouse
            DefaultModalGraphMouse<Object, Object> mouse = new DefaultModalGraphMouse<Object, Object>();
            mouse.setMode(ModalGraphMouse.Mode.PICKING);
            vv.addKeyListener(mouse.getModeKeyListener());
            vv.setGraphMouse(mouse);

            //DrawJung
            methods.drawJung(graph, vv, people);

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(vv);
            frame.pack();
            frame.setVisible(true);
        }
        // Initialize variables
        int getWellDays = 0;
        int discovery = 0;
        int newGetWell = 0;
        ArrayList<Integer> infectedPeople = new ArrayList<Integer>();
        int percentSick = 0;
        int getVac = -1;
        int percentTeenagers = 0;
        ArrayList<Integer> vaccinatedPeople = new ArrayList<Integer>();
        String fileName = "simResults";
        int runTimes = 0;

        done = false; // Controls when while loop stops

        // Gets input from user after graph
        while (!done) {
            JPanel panel2 = new JPanel();
            panel2.setLayout(new GridLayout(18, 0));

            JTextField getWellDaysAnswer = new JTextField("10", 10);
            JTextField discoveryAnswer = new JTextField("10000", 10);
            JTextField newGetWellAnswer = new JTextField("5", 10);
            JTextField initiallySickAnswer = new JTextField("10", 10);
            JTextField vaccinatedPeopleAnswer = new JTextField("", 10);
            JTextField percentSickAnswer = new JTextField("10", 10);
            JTextField getVacAnswer = new JTextField("0", 10);
            JTextField fileAnswer = new JTextField("simResults", 10);
            JTextField runTimesAnswer = new JTextField("10", 10);
            JTextField percentTeenagersAnswer = new JTextField("10", 10);

            panel2.add(new JLabel("INITIAL CONDITIONS SETUP:"));
            panel2.add(new JLabel("----------------------------------------------"));
            panel2.add(new JLabel("What people will be initially sick? (Ex. 2,3,1,6)"));
            panel2.add(initiallySickAnswer);
            panel2.add(new JLabel("What people will be initially vaccinated or immune? (Ex. 8,9,20)"));
            panel2.add(vaccinatedPeopleAnswer);

            panel2.add(new JLabel("TRANSMISSION SETUP:"));
            panel2.add(new JLabel("----------------------------------------------"));
            panel2.add(new JLabel("Amount of get well days before the cure is invented?"));
            panel2.add(getWellDaysAnswer);
            panel2.add(new JLabel("What chance does each person have of catching the disease (___ %)?"));
            panel2.add(percentSickAnswer);
            panel2.add(new JLabel("What percent of teenagers should there be?"));
            panel2.add(percentTeenagersAnswer);

            panel2.add(new JLabel("RUNNING TO HOSPITAL SETUP:"));
            panel2.add(new JLabel("----------------------------------------------"));
            panel2.add(new JLabel("What percent of people run to get vaccinated when their friend gets sick?   "));
            panel2.add(getVacAnswer);

            panel2.add(new JLabel("CURE SETUP:"));
            panel2.add(new JLabel("----------------------------------------------"));
            panel2.add(new JLabel("How long will it take until a cure is discovered?"));
            panel2.add(discoveryAnswer);
            panel2.add(new JLabel("Amount of get well days after the cure is invented?"));
            panel2.add(newGetWellAnswer);

            panel2.add(new JLabel("RESULTS SETUP:"));
            panel2.add(new JLabel("----------------------------------------------"));
            panel2.add(new JLabel("How many times should the simulation run?"));
            panel2.add(runTimesAnswer);
            panel2.add(new JLabel("What should be the file-name of the output graph?"));
            panel2.add(fileAnswer);

            int result = JOptionPane.showConfirmDialog(null, panel2, "After-Graph Configuration", JOptionPane.OK_CANCEL_OPTION);

            if (result != JOptionPane.OK_OPTION) {
                System.exit(0);
            }

            try {
                String getWellDaysString = getWellDaysAnswer.getText();
                getWellDays = Integer.parseInt(getWellDaysString);
                if (getWellDays < 1) {
                    throw new NumberFormatException();
                }

                String percentTeenagersString = percentTeenagersAnswer.getText();
                percentTeenagers = Integer.parseInt(percentTeenagersString);
                if (getWellDays < 1) {
                    throw new NumberFormatException();
                }

                String discoveryString = discoveryAnswer.getText();
                discovery = Integer.parseInt(discoveryString);
                if (discovery < 1) {
                    throw new NumberFormatException();
                }

                String newGetWellString = newGetWellAnswer.getText();
                newGetWell = Integer.parseInt(newGetWellString);
                if (newGetWell < 1) {
                    throw new NumberFormatException();
                }

                String initiallySickString = initiallySickAnswer.getText();
                infectedPeople = methods.commaListToArrayList(initiallySickString);
                methods.removeDuplicate(infectedPeople);
                for (int i : infectedPeople) {
                    if (i > numPeople) {
                        throw new NumberFormatException();
                    }
                }

                String percentSickString = percentSickAnswer.getText();
                percentSick = Integer.parseInt(percentSickString);
                if (percentSick <= 0 || percentSick > 100) {
                    throw new NumberFormatException();
                }

                String getVacString = getVacAnswer.getText();
                getVac = Integer.parseInt(getVacString);
                if (getVac < 0 || getVac > 100) {
                    throw new NumberFormatException();
                }

                String vaccinatedPeopleString = vaccinatedPeopleAnswer.getText();
                vaccinatedPeople = methods.commaListToArrayList(vaccinatedPeopleString);
                methods.removeDuplicate(vaccinatedPeople);
                for (int i : vaccinatedPeople) {
                    if (i > numPeople) {
                        throw new NumberFormatException();
                    }
                }

                fileName = fileAnswer.getText();

                String runTimesString = runTimesAnswer.getText();
                runTimes = Integer.parseInt(runTimesString);
                if (runTimes <= 0) {
                    throw new NumberFormatException();
                }
                done = true; // Only done when go through try without errors
            } catch (NumberFormatException e) {
                if (result == JOptionPane.OK_OPTION) {
                    JOptionPane.showMessageDialog(new JFrame(), "ERROR: Input is invalid.", "Input Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    System.exit(0);
                }
            }
        }

        //And now, we do legit stuff.
        Integer[] results = new Integer[0]; //Meh I don't know how to do it better
        try {
            results = new MoreMethods().simulate(people, getWellDays, discovery, infectedPeople, percentSick, getVac, vaccinatedPeople, fileName, runTimes, percentTeenagers);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (results[1] == 1) {
            System.out.println("Success!");
        }

        System.out.println("The average number of days was " + results[0] + ".");

        System.exit(0);
    }
}
