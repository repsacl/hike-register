package no.ntnu.idatt1003.hikeregistry.model;

import java.util.Random;

public class Person {

    private int id = 0;
    private final String firstName; // egen variabel for fornavn
    private final String lastName; // egen variabel for etternavn
    // private final String gender;
    private String hikeGroup; // hvis personen er i en tur-gruppe kan han/hun notere det her får å få det på aktivtetene sine. Gir også mulighet for å etterhvert sortere etter tur-grupper
    private final Random r = new Random();

    /** 
     * Declears varaibles
    */
    public Person(String firstName, String lastName, String hikeGroup){
        this.firstName = firstName;
        this.lastName = lastName;
        // this.gender = gender;
        this.hikeGroup = hikeGroup;
        this.id = r.nextInt(10,99);
    }

    // personer som skal loggføre må ikke være i en tur-gruppe
    /** 
     * If the user don't have a hike-group
    */
    public Person(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
        // this.gender = gender;
        this.id = r.nextInt(10,99);
    }

    /** 
     * returns the usersID.
     * Makes it esier to find an user if multiple user have the same name
    */
    public int getPersonId(){
        return id;
    }

    // retunerer fornavn når ropt på
    public String getFirstName(){
        return firstName;
    }

    // retunere etternavn
    public String getLastName(){
        return lastName;
    }

    public String getFullName(){
        return lastName + ", " + firstName;
    }

    // returns gender
    // public String getGender(){
    //     return gender;
    // }

    // returnere navnet på tur-gruppen
    public String getHikeGroup(){
        return hikeGroup;
    }

    // oppdaterer/legger personen til i en tur-gruppe
    public void updateHikeGroup(String name){
        hikeGroup = name;
    }
}
