package no.ntnu.idatt1003.hikeseeker.model;

/**
 * Represents a person who can log activities in the hike registry.
 *
 * <p>
 * A person has a first name, a last name and optionally a hike group. The first and last name are
 * immutable once the person is created, while the hike group can be updated later.
 */
public class Person {

  private final String firstName;
  private final String lastName;
  private Group group;
  private int hikesRegistered = 0;

  /**
   * Creates a new person who is a member of a hike group.
   *
   * @param firstName the person's first name.
   * @param lastName the person's last name.
   * @param group the name of the hike group.
   * @throws IllegalArgumentException if the first name, last name is null or empty.
   */
  public Person(String firstName, String lastName, Group group) {
    if (firstName == null || firstName.isBlank()) {
      throw new IllegalArgumentException("Person must have a firstname!");
    }
    this.firstName = firstName.trim();

    if (lastName == null || lastName.isBlank()) {
      throw new IllegalArgumentException("Person must have a lastname!");
    }
    this.lastName = lastName.trim();
    this.group = group;

    if (group != null) {
      group.addMember(this);
    }
  }

  /**
   * Creates a new person who is not a member of a hike group.
   *
   * <p>
   * The hike group is left unset ({@code null}) and can be added later with
   * {@link #updateHikeGroup(Group)}.
   *
   * @param firstName the person's first name.
   * @param lastName the person's last name.
   * @throws IllegalArgumentException if the first name, last name is null or empty.
   */
  public Person(String firstName, String lastName) {
    this(firstName, lastName, null);
  }

  /**
   * Returns the firstname of this person.
   *
   * @return Firsname.
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Returns the last name of this person.
   *
   * @return Lastname.
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Returns the fullname of this person, formatted as "firstname lastname".
   *
   * @return Fullname.
   */
  public String getFullName() {
    return firstName + " " + lastName;
  }

  /**
   * Returns the group of the hike group this person is a member of.
   *
   * @return the group of the hike group, or {@code null} if the person is not in a hike group.
   */
  public Group getHikeGroup() {
    return group;
  }

  /**
   * Returns tue or fals if person is a member in a group.
   *
   * @return true or false.
   */
  public boolean hasGroup() {
    boolean isGroup = false;
    if (group != null) {
      isGroup = true;
    }
    return isGroup;
  }

  /**
   * Returns the how many hike this person has registered.
   *
   * @return how many hikes this person has registered.
   */
  public int getHikesRegistered() {
    return hikesRegistered;
  }

  /**
   * Increases this persons hikes registered.
   */
  public void increasHikeRegistered() {
    hikesRegistered += 1;
  }

  /**
   * Decreases this persons hikes registered. Wont go below zero.
   */
  public void decreasHikeRegistered() {
    hikesRegistered -= 1;
    if (hikesRegistered < 0) {
      hikesRegistered = 0;
    }
  }

  /**
   * Adds a new a hike group to this persons group, or replaces the current one.
   *
   * @param newGroup - new group to add.
   */
  public void updateHikeGroup(Group newGroup) {
    group = newGroup;
  }

  /**
   * Removes this person from it's current group. Group is set to null.
   *
   * @throws NullPointerException if person want to leave a group, but no groups are associated to
   *         them.
   */
  public void leaveGroup() {
    if (group == null) {
      throw new NullPointerException("You are not a member of a group!");
    }
    group.removeMember(this);
  }
}
