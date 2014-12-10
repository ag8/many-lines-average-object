package com.adg.MIN3;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;

import java.awt.Color;
import java.awt.Paint;
import java.io.File;
import java.io.IOException;
import java.util.*;

import org.apache.commons.collections15.Transformer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;

public class MoreMethods {

    public static int randInt(int min, int max) {
        Random rand = new Random();

        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    // GENERAL METHODS
    public ArrayList<Integer> commaListToArrayList(String commaString) { // Given a string of integers separated by commas, returns an ArrayList with the integers
        ArrayList<Integer> arrayList = new ArrayList();
        if (commaString.equals("")) {
            return arrayList;
        }
        int i;
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(commaString.split(",")));
        for (String s : list) {
            i = Integer.parseInt(s);
            arrayList.add(i);
        }
        return arrayList;
    }

    public void removeDuplicate(ArrayList<?> arlList) {// Removes duplicates from given ArrayList
        ArrayList newList = new ArrayList();
        for (Object item : arlList) {
            if (newList.contains(item)) {
                //Do Nothing
            } else {
                newList.add(item);
            }
        }
        arlList.clear();
        arlList.addAll(newList);
    }

    //JUNG METHODS
    public void drawVerticies(UndirectedSparseMultigraph g, ArrayList<Person> people) {
        for (Person person : people) {
            g.addVertex(person);
        }
    }

    public void greenVertices(VisualizationViewer vv) { // Makes all vertices green
        Transformer<Person, Paint> vertexColorGreen = new Transformer<Person, Paint>() {
            @Override
            public Paint transform(Person i) {
                return Color.GREEN;
            }
        };
        vv.getRenderContext().setVertexFillPaintTransformer(vertexColorGreen);
    }
    // NETWORK NOT JUNG METHODS

    public void recolorHubs(UndirectedSparseMultigraph g, VisualizationViewer vv, final ArrayList<Person> people) {
        Transformer<Person, Paint> vertexColorOrange = new Transformer<Person, Paint>() {
            @Override
            public Paint transform(Person i) {
                ArrayList<Person> friends = i.getFriends();
                //System.out.println(friends);
                if (friends.size() >= (people.size() / 100) * 10) {
                    return Color.ORANGE;
                }
                return Color.GREEN;
            }
        };
        vv.getRenderContext().setVertexFillPaintTransformer(vertexColorOrange);
    }

    public void makeHubs(int hubNumber, ArrayList<Person> people, Random random) {// Returns an arrayList of PeopleDestined to become hubs
        int randomInt;
        Boolean done;
        for (int i = 0; i < hubNumber; i++) {
            done = false;
            while (!done) {
                randomInt = random.nextInt(people.size());
                for (Person person : people) {
                    if (people.indexOf(person) == randomInt && !(person.isHub())) {
                        person.setIsHub(true);
                        done = true;
                        break;
                    }
                }
            }
        }
    }

    public void assignCapicities(ArrayList<Person> people, int minFriends, int maxFriends, Random random) {
        int randomInt;
        for (Person person : people) {
            randomInt = random.nextInt((maxFriends - minFriends) + 1) + minFriends;
            person.setCapacity(randomInt);
        }
    }

    public void befriendRandom(ArrayList<Person> people, int minFriends, int maxFriends, Random random, int hubNumber) {//It will be possible to make this algorhyth even better suggest way!!1
        //Choose people for hubs
        makeHubs(hubNumber, people, random);
        assignCapicities(people, minFriends, maxFriends, random);
        // Make friends
        int randomPerson;
        int counterCapacityFull = 0;
        ArrayList<Person> peopleReference = new ArrayList();
        for (Person person : people) {
            //Hub Case
            if (person.isHub()) {
                person.setCapacity(people.size() + 1);
                for (Person hubFriend : people) {
                    if ((hubFriend != person) && !(person.getFriends().contains(hubFriend))) {
                        if ((random.nextInt(100) + 1 > 20) && !(hubFriend.capacityFull())) {
                            person.addFriend(hubFriend);
                            hubFriend.addFriend(person);
                        }
                    }
                }
            }
            // If not HUB
            else {
                Collections.shuffle(peopleReference);
                for (Person possibleFriend : peopleReference) {
                    if (possibleFriend.getID() <= person.getID()) {
                        //Do nothing
                    } else if (possibleFriend.capacityFull() || person.capacityFull()) {
                        //Do nothing
                    } else {
                        person.addFriend(people.get(possibleFriend.getID() - 1));
                        people.get(possibleFriend.getID() - 1).addFriend(person);
                    }
                }
//                ArrayList<Integer> peopleNumberKey = new ArrayList();
//                for(int i = person.getID(); i < people.size() + 1; i++){
//                    peopleNumberKey.add(i);
//                }
//                while(!person.capacityFull()){
//                    if(person.getCapacity()== (people.size() - person.getID())){
//                        System.out.println("BREAK 1");
//                        break;
//                    }
//                    if(person.getID() == people.size()){
//                        System.out.println("BREAK 2");
//                        break;
//                    }
//                    for(Person checkPerson : people){
//                        if(checkPerson.capacityFull()){
//                            counterCapacityFull++;
//                        }
//                    }
//                    if(counterCapacityFull > (people.size() - person.getID())){
//                        counterCapacityFull = 0;
//                        System.out.println("BREAK 3" + person.getID());
//                        break;
//                    }
//                    Collections.shuffle(peopleNumberKey);
//                    randomPerson = random.nextInt(peopleNumberKey.size());
//                    System.out.println(person.getID());
//                    System.out.println("Attempted:" + people.get(randomPerson).getID());
//                    System.out.println(people.get(randomPerson).capacityFull());
//                    for(Person personTest: people){
//                        System.out.println("FOR THE PERSON: " + personTest.getID());
//                        System.out.println(personTest.capacityFull());
//                        System.out.println(personTest.getCapacity());
//                        System.out.println(person.numberFriends());
//                    }
//                    for(Person friend : person.getFriends()){
//                        System.out.print(friend.getID() + " ");
//                    }
//                    if((people.get(randomPerson) != person) && !person.getFriends().contains(people.get(randomPerson)) && !people.get(randomPerson).capacityFull()){
//                        person.addFriend(people.get(randomPerson));
//                        people.get(randomPerson).addFriend(person);
//                        System.out.println("new friend ship");
//                    }
//                }
            }
        }
    }

    public void befriendScaleFree(ArrayList<Person> people, int minFriends, int maxFriends, Random random) {
        ArrayList<Person> relativeConnections = new ArrayList();
        ArrayList<Person> check = new ArrayList();
        int randomPerson;
        Person randomPersonPlaceholder;//Used to switch arrayLists
        assignCapicities(people, minFriends, maxFriends, random);
        int addConstant = 30;
        for (Person person : people) {
            relativeConnections.add(person);
            while (!person.capacityFull()) {//Loops until person fits nescessary Conditions
                if (relativeConnections.size() == 1) {
                    break;
                }
                check.clear();
                check.add(person);
                check.addAll(person.getFriends());
                if (check.containsAll(relativeConnections)) {
                    break;
                }
                randomPerson = random.nextInt(relativeConnections.size());
                randomPersonPlaceholder = relativeConnections.get(randomPerson);
                randomPerson = people.indexOf(randomPersonPlaceholder);
                if ((people.get(randomPerson) != person) && !person.getFriends().contains(people.get(randomPerson))) {
                    person.addFriend(people.get(randomPerson));
                    people.get(randomPerson).addFriend(person);
                    System.out.println(person.getID() + " and " + people.get(randomPerson).getID());
                    for (int addCount = 0; addCount < addConstant; addCount++) {
                        relativeConnections.add(person);
                        relativeConnections.add(people.get(randomPerson));
                    }
                }
                System.out.println(person.getID());
                System.out.println("Capacity: " + person.getCapacity());
            }
        }
    }

    public void befriendSmallWorld(ArrayList<Person> people, int minFriends, int maxFriends, Random random, int hubNumber) {
        int randomPerson;
        int randomPersonForHub;
        assignCapicities(people, minFriends, maxFriends, random);
        int respectiveFriend;
        int numPeople = people.size();
        makeHubs(hubNumber, people, random);
        boolean done = false;
        //This is where actual befriendsing starts! Otdel Znakmostv!!! Get Excited
        for (Person person : people) {
            while (!person.capacityFull()) {//Loops until person fits nescessary Conditions
                if (maxFriends % 2 == 0) {
                    respectiveFriend = random.nextInt(maxFriends) - maxFriends / 2;
                } else {
                    respectiveFriend = random.nextInt(maxFriends + 1) - (maxFriends + 1) / 2;
                }
//                System.out.println("Person: " + person.getID());
//                System.out.println("Respective Friend: " + respectiveFriend);
                randomPerson = (((respectiveFriend + person.getID())) % numPeople + numPeople) % numPeople;
                if (randomPerson == 0) {
                    randomPerson = numPeople - 1;
                }
                //System.out.println("Random Person: " + randomPerson);
                if ((people.get(randomPerson) != person) && !person.getFriends().contains(people.get(randomPerson))) {
                    //System.out.println("SUCCESFUL FRIENDSHIP!");
                    person.addFriend(people.get(randomPerson));
                    people.get(randomPerson).addFriend(person);
                }
            }
            done = false;
            if (person.isHub()) {
                while (!done) {
                    randomPersonForHub = random.nextInt(people.size());
                    if ((people.get(randomPersonForHub) != person) && !person.getFriends().contains(people.get(randomPersonForHub))) {
                        person.addFriend(people.get(randomPersonForHub));
                        people.get(randomPersonForHub).addFriend(person);
                        done = true;
                    }
                }
            }
        }
    }

    public void drawJung(UndirectedSparseMultigraph g, VisualizationViewer vv, ArrayList<Person> people) {
        ArrayList<Person> friends = new ArrayList();
        for (Person person : people) {
            friends = person.getFriends();
            for (Person friend : friends) {
                if (people.indexOf(friend) > people.indexOf(person)) {
                    g.addEdge(Integer.toString(person.getID()) + " to " + Integer.toString(friend.getID()), person, friend);
                }
            }
        }
        greenVertices(vv);
        recolorHubs(g, vv, people);
    }

    public Layout chooseLayout(UndirectedSparseMultigraph graph, String layoutString) {
        if (layoutString.equals("FR")) {
            FRLayout layout = new FRLayout<Person, String>(graph);
            return layout;
        } else if (layoutString.equals("ISOM")) {
            ISOMLayout layout = new ISOMLayout<Person, String>(graph);
            return layout;
        } else if (layoutString.equals("Spring")) {
            SpringLayout layout = new SpringLayout<Person, String>(graph);
            return layout;
        } else if (layoutString.equals("Circle")) {
            CircleLayout layout = new CircleLayout<Person, String>(graph);
            layout.setVertexOrder(Person.orderByID);
            return layout;
        } else {
            ISOMLayout layout = new ISOMLayout<Person, String>(graph); // Make it defalut (ISOM)
            return layout;
        }
    }

    public Integer[] simulate(ArrayList<Person> people, int getWellDays, int discovery, ArrayList<Integer> infectedPeople, int percentSick, int getVac, ArrayList<Integer> vaccinatedPeople, String filename, int runTimes, int percentTeenagers) throws InterruptedException, IOException {
        boolean success = true;

        int day = 0;

        int numberOfSickPeople = 1;

        ArrayList<Person> teenagers = getAndSetTeenagers(people, percentTeenagers);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset(); //Create a new dataset to store all of the simulation data

        while (numberOfSickPeople > 0) { //While there are sick people (This while loop goes every day)
            day++;
            //Save the current results to a dataset
            addPoint(dataset, numberOfSickPeople, "Infected People", Integer.toString(day));

            //First of all, check the people that are already sick.
            for (Person person : people) {
                if (person.isSick()) {
                    person.incrementDaysSick();
                }
                if (person.getDaysSick() > getWellDays) {
                    person.getWell();
                }
            }

            //Now, curfew some random teenagers
            for (Person teenager : teenagers) {
                teenager.incrementCurfewedDays();
                if (!teenager.isImmuneToCurfews()) {
                    if (randInt(0, 10) == 10) { //10% chance
                        teenager.curfew();
                    }
                }
                if (teenager.getCurfewedDays() > 10) { //If he finished his curfew, reset him
                    teenager.setCurfewed(false);
                    teenager.setCurfewedDays(0);
                    teenager.setImmuneToCurfews(true);
                }
            }


            //For every person...
            for (Person person : people) {
//                    System.out.println("At person " + person.getID());
                ArrayList<Person> friends = person.getFriends();
                for (Person friend : friends) {
//                        System.out.println("At friend " + friend.getID());
                    if (!(friend.isSick() || friend.isImmune() || friend.isCurfewed())) { //If they have a vulnerable friend
                        if (randInt(0, 100 / percentSick) == 100 / percentSick) { //A random chance according to percentSick
//                                System.out.println("Make him sick!");
                            friend.setIsSick(true);
                        }
                    }
                }
            }

            //Update the number of sick people
            numberOfSickPeople = getNumberOfSickPeople(people);
        }

        System.out.println("Simulation complete after " + day + " days.");


        people.get(2).setIsSick(true); //For now, make number 2 sick from the very beginning
        people.get(2).setDaysSick(0);

        int successful = success == true ? 1 : 0; //Convert boolean success to an integer (0 if true, 1 is false)

        makeChart(dataset, filename);

        return new Integer[]{day, successful};
    }

    private int getNumberOfSickPeople(ArrayList<Person> people) {
        int totalSick = 0;
        for (Person person : people) {
            if (person.isSick()) {
                totalSick++;
            }
        }
        return totalSick;
    }

    private ArrayList<Person> getAndSetTeenagers(ArrayList<Person> people, int percentTeenagers) {
        Collections.sort(people, new ComparatorByFriendNumber()); //Sort the people by their friend number

//        for (Person person : people) {
//            System.out.println("Person " + person.getID() + " has " + person.getNumFriends() + " friends.");
//        }

        int teenagerNumber = (int) (percentTeenagers * 0.01 * people.size());

        System.out.println("teenagers: " + teenagerNumber);

        ArrayList<Person> teenagers = new ArrayList<Person>();

        List<Person> subList = people.subList(0, teenagerNumber);

        teenagers.addAll(subList);

        for (Person teenager : teenagers) {
            teenager.setIsTeenager(true);
        }

        return teenagers;
    }

    public static void makeChart(DefaultCategoryDataset dataset, String filename) throws IOException {
        JFreeChart lineChartObject = ChartFactory.createLineChart("Number of Sick People", "Days", "Infected People", dataset, PlotOrientation.VERTICAL, true, true, false);

        int width = 640;
        int height = 480;
        File lineChart = new File(filename + "m.png");
        ChartUtilities.saveChartAsPNG(lineChart, lineChartObject, width, height);
    }

    private static void addPoint(DefaultCategoryDataset dataset, int yValue, String lineLabel, String xValue) {
        dataset.addValue(yValue, lineLabel, xValue);
    }

    //Create a Person Comparator subclass
    public class ComparatorByFriendNumber implements Comparator<Person> {
        @Override
        public int compare(Person p1, Person p2) {
            return p2.getNumFriends() - p1.getNumFriends(); //Descending
        }
    }
}