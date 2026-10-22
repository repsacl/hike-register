package no.ntnu.idatt1003.hikeregistry.model;

/**
 * Represents a person who can log activities in the hike registry.
 *
 * <p>
 * A person has a first name, a last name and optionally a hike group. The first
 * and last
 * name are immutable once the person is created, while the hike group can be
 * updated later.
 * </p>
 */
public class Person {

  // private int id = 0;
  private final String firstName; // egen variabel for fornavn
  private final String lastName; // egen variabel for etternavn
  // private final String gender;
  private String hikeGroup; // hvis personen er i en tur-gruppe kan han/hun notere det her får å få det på
                            // aktivtetene sine. Gir også mulighet for å etterhvert sortere etter
                            // tur-grupper
  // private final Random r = new Random();

  /**
   * Creates a new person who is a member of a hike group.
   *
   * @param firstName the person's first name, cannot be null or empty
   * @param lastName  the person's last name, cannot be null or empty
   * @param hikeGroup the name of the hike group, cannot be null or empty
   * @throws IllegalArgumentException if the first name, last name or hike group
   *                                  is null or empty
   */
  public Person(String firstName, String lastName, String hikeGroup) {
    if (firstName == null || firstName.equals("")) {
      throw new IllegalArgumentException("Person must have a firstname!");
    }
    this.firstName = firstName;

    if (lastName == null || lastName.equals("")) {
      throw new IllegalArgumentException("Person must have a lastname!");
    }
    this.lastName = lastName;
    // this.gender = gender;

    if (hikeGroup == null || hikeGroup.equals("")) {
      throw new IllegalArgumentException("Person must have a valid hikegroup!");
    }
    this.hikeGroup = hikeGroup;
    // this.id = r.nextInt(10,99);
  }

  // personer som skal loggføre må ikke være i en tur-gruppe
  /**
   * Creates a new person who is not a member of a hike group.
   *
   * <p>
   * The hike group is left unset ({@code null}) and can be added later with
   * {@link #updateHikeGroup(String)}.
   * </p>
   *
   * @param firstName the person's first name, cannot be null
   * @param lastName  the person's last name, cannot be null
   * @throws IllegalArgumentException if the first name or last name is null
   */
  public Person(String firstName, String lastName) {
    if (firstName == null) {
      throw new IllegalArgumentException("Person must have a firstname!");
    }
    this.firstName = firstName;

    if (lastName == null) {
      throw new IllegalArgumentException("Person must have a lastname!");
    }
    this.lastName = lastName;
    // this.gender = gender;
    // this.id = r.nextInt(10,99);
  }

  /**
   * Returns the first name of this person.
   *
   * @return the first name of the person
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Returns the last name of this person.
   *
   * @return the last name of the person
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Returns the full name of this person, formatted as "lastName, firstName".
   *
   * @return the full name of the person
   */
  public String getFullName() {
    return lastName + ", " + firstName;
  }

  /**
   * Returns the name of the hike group this person is a member of.
   *
   * @return the name of the hike group, or {@code null} if the person is not in a
   *         hike group
   */
  public String getHikeGroup() {
    return hikeGroup;
  }

  /**
   * Adds this person to a hike group, or replaces the current one.
   *
   * @param name the name of the new hike group
   */
  public void updateHikeGroup(String name) {
    hikeGroup = name;
  }
}
