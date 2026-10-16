package no.ntnu.idatt1003.hikeregistry.registry;

import java.util.ArrayList;

import no.ntnu.idatt1003.hikeregistry.model.Person;

public class PersonRegistry {
     // valgte dette for det har flere funksjoner til seg som gjøre det lettere å håndtere data
    private final ArrayList<Person> persons = new ArrayList<>();

    public void addPerson(Person newPerson){
        persons.add(newPerson);
    }

    // retunerer alle personenen som et array, er mer kjent med array og syntes det er lettere å håndtere for å vise fram personene
    // returns a copy of the ArrayList so i cant be manipulated
    public Person[] getAllPersons(){
        return persons.toArray(new Person[0]);
    }

    // finds person with firstname
    public Person getPerson(String firstname){
        Person who = null;
        for (int i = 0; i < persons.size(); i++) {
            if(firstname.equals(persons.get(i).getFirstName())){
                who = persons.get(i);
            }
        }

        return who;
    }
}
