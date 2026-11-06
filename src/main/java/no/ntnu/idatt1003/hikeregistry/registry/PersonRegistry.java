package no.ntnu.idatt1003.hikeregistry.registry;

import java.util.ArrayList;

import no.ntnu.idatt1003.hikeregistry.model.Person;

/**
 * Represents a registry that keeps track of all persons in the hike registry.
 *
 * <p>
 * The registry can add and remove, return persons by firstname, lastname and
 * both. returns all persons as an array.
 * </p>
 */
public class PersonRegistry {
  private final ArrayList<Person> persons = new ArrayList<>();

  /**
   * Adds a new person to person registry.
   *
   * @param newPerson
   */
  public void addPerson(Person newPerson) {
    persons.add(newPerson);
  }

  /**
   * Gets all persons in person registry.
   *
   * @return array with all persons
   */
  public Person[] getAllPersons() {
    return persons.toArray(new Person[0]);
  }

  /**
   * Finds person by name. The user can write firstname lastname and both.
   *
   * @param name
   * @return person
   */
  public Person getPerson(String name) {
    return persons.stream()
        .filter(prs -> prs.getFirstName().equalsIgnoreCase(name) || prs.getFullName().equalsIgnoreCase(name)
            || prs.getLastName().equalsIgnoreCase(name))
        .findFirst()
        .orElse(null);
  }
}
