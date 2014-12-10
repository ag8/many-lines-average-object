package com.adg.MIN3;

import java.util.ArrayList;
import java.util.Comparator;

import org.apache.commons.collections15.Transformer;

//Ladies and Gnetlemen lo and behold the Person class *Correction : "Klass!!!!!!!"
public class Person implements Transformer<Person, String>, Comparator<Person> {
    private int ID;
    private ArrayList<Person> friends = new ArrayList<Person>();// A komu vobshe prigoditsya takoj masiv? (podskzka: ne nam)
    private Boolean isSick = false;
    private Boolean isImmune = false;
    private Boolean isHub = false;
    private Boolean isTeenager = false;
    private int daysSick = 0;
    private boolean curfewed;
    private int curfewedDays;
    private boolean immuneToCurfews;
    int friendCapacity = 0;

    //The constructor
    public Person(int ID) {
        this.ID = ID;
    }


    //Get Values
    public int getID() {
        return ID;
    }

    public int getCurfewedDays() {
        return this.curfewedDays;
    }

    public void setCurfewedDays(int days) {
        this.curfewedDays = days;
    }

    public void incrementCurfewedDays() {
        this.curfewedDays++;
    }

    public void setCurfewed(boolean curfewed) {
        this.curfewed = curfewed;
    }

    public void curfew() { //We don't actually need this method since we have setCurfewed but it looks nice. As in Grisha.curfew(), not Grisha.setCurfewed(true).
        this.curfewed = true;
    }

    public boolean isImmuneToCurfews() {
        return this.immuneToCurfews;
    }

    public void setImmuneToCurfews(boolean immuneToCurfews) {
        this.immuneToCurfews = immuneToCurfews;
    }

    public boolean isCurfewed() {
        return this.curfewed;
    }

    public ArrayList<Person> getFriends() {// helpful advice "Legko skazat' no trudno sdelat'..."
        return friends;
    }

    public int getNumFriends() {
        return friends.size();
    }

    public Boolean isSick() {
        return isSick;
    }

    public Boolean isImmune() {
        return isImmune;
    }

    public Boolean isFriend(Person person) {
        return friends.contains(person);
    }

    public Boolean isHub() {
        return isHub;
    }

    public int getCapacity() {
        return friendCapacity;
    }

    public boolean capacityFull() {
        if (friends.size() >= friendCapacity) {
            return true;
        } else {
            return false;
        }
    }

    public int getDaysSick() {
        return daysSick;
    }

    public boolean isTeenager() {
        return isTeenager;
    }

    public void setIsTeenager(boolean isTeenager) {
        this.isTeenager = isTeenager;
    }

    public void setDaysSick(int daysSick) {
        this.daysSick = daysSick;
    }

    public void incrementDaysSick() {
        this.daysSick++;
    }

    public int numberFriends() {
        return friends.size();
    }

    //Set Values
    public void setID(int newID) {
        this.ID = newID;
    }

    public void addFriend(Person newFriend) {
        this.friends.add(newFriend);
    }

    public void setIsSick(Boolean value) {
        this.isSick = value;
    }

    public void setIsImmune(Boolean value) {
        this.isImmune = value;
    }

    public void setIsHub(Boolean newValue) {
        this.isHub = newValue;
    }

    public void setCapacity(int newValue) {
        this.friendCapacity = newValue;
    }

    //Custom Transformer and Comparator
    public static final Comparator orderByID = new Comparator<Person>() {
        public int compare(Person person1, Person person2) {
            if (person1.getID() - person2.getID() == 0) {
                return 0;
            }
            if (person1.getID() - person2.getID() > 0) {
                return 1;
            }
            if (person1.getID() - person2.getID() < 0) {
                return -1;
            }
            return 0;// Program will never get to here
        }
    };
    public static Transformer labelByID = new Transformer<Person, String>() {
        @Override
        public String transform(Person person) {
            return Integer.toString(person.getID());
        }
    };

    @Override
    public String transform(Person i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int compare(Person o1, Person o2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    public void getWell() {
        this.daysSick = 0;
        this.isImmune = true;
        this.isSick = false;
    }
}