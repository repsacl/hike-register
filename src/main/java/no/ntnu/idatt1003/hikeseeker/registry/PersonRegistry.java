package no.ntnu.idatt1003.hikeseeker.registry;

import java.util.ArrayList;

import no.ntnu.idatt1003.hikeseeker.model.Person;

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
    if (newPerson == null) {
      throw new IllegalArgumentException("Person added to the registry cant be null!");
    }

    if (persons.stream().anyMatch(prs -> prs.getFullName().equalsIgnoreCase(newPerson.getFullName()))) {
      throw new IllegalArgumentException("Person already exists in the registry!");
    }

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
   * @param name to search
   * @return person matching name
   */
  public Person[] getPerson(String name) {
    Person[] returnPersons = {};

    if (name == null || name.isBlank()) {
      return returnPersons;
    }
    String[] parts = name.trim().split("\\s+", 2);

    if (parts.length == 2) {
      return persons.stream()
          .filter(prs -> prs.getFullName().equalsIgnoreCase(parts[0] + " " + parts[1])
              || prs.getFirstName().equalsIgnoreCase(parts[0]) || prs.getLastName().equalsIgnoreCase(parts[1]))
          .toArray(Person[]::new);
    } else if (parts.length == 1) {
      return persons.stream()
          .filter(prs -> prs.getFirstName().equalsIgnoreCase(parts[0]) || prs.getFullName().equalsIgnoreCase(parts[0])
              || prs.getLastName().equalsIgnoreCase(parts[0]))
          .toArray(Person[]::new);
    }

    return returnPersons;
  }
}
