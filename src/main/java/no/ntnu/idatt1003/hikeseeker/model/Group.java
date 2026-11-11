package no.ntnu.idatt1003.hikeseeker.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Represents a group in the hike registry that persons can join and register
 * activities to.
 *
 * <p>
 * A group has a name, a list of members, the time it was registered and a
 * count of how many hikes have been registered to it. A person can only be a
 * member of one group at a time; adding a person to a new group removes them
 * from their previous group.
 */
public class Group {
  private final String name;
  private final ArrayList<Person> members = new ArrayList<>();
  private final LocalDateTime registered;
  private int hikesRegistered = 0;

  /**
   * Creates a new group.
   *
   * @param name       - name of the group
   * @param registered - when the group is created
   * @throws IllegalArgumentException if the name is null or empty
   */
  public Group(String name) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Group must have a name!");
    }
    this.name = name;
    this.registered = LocalDateTime.now();
  }

  /**
   * Returns the name og the group.
   *
   * @return name of the group
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the how many hike this person has registered.
   *
   * @return how many hikes this person has registered
   */
  public int getHikesRegistered() {
    return hikesRegistered;
  }

  /**
   * Increases this persons hikes registered. Wont go below zero.
   */
  public void increasHikeRegistered() {
    hikesRegistered += 1;
  }

  /**
   * Decreases this persons hikes registered.
   */
  public void decreasHikeRegistered() {
    hikesRegistered -= 1;
    if (hikesRegistered < 0) {
      hikesRegistered = 0;
    }
  }

  /**
   * Adds a new member to this group.
   *
   * @throws IllegalArgumentException if the member is null or i already a member.
   *
   * @param newMember
   */
  public void addMember(Person newMember) {
    if (newMember == null) {
      throw new IllegalArgumentException("New member cant be null.");
    }
    if (checkIfMember(newMember.getFirstName(), newMember.getLastName())) {
      throw new IllegalArgumentException("Is already a member!");
    }

    if (newMember.hasGroup() && newMember.getHikeGroup() != this) {
      newMember.getHikeGroup().removeMember(newMember);
    }
    // adds new member to the members list
    members.add(newMember);
    // updates the newMember assosiated group
    newMember.updateHikeGroup(this);
  }

  /**
   * Retruns how many members the group have.
   *
   * @return count of members
   */
  public int getCountMembers() {
    return members.size();
  }

  /**
   * Returns all mebers of this group as an array.
   *
   * @return array of members
   */
  public Person[] getAllMembers() {
    Person[] allMembers = members.toArray(new Person[0]);

    return allMembers;
  }

  /**
   * Returns the time of when the group registered.
   *
   * @return Time registered
   */
  public LocalDateTime getTimeRegistered() {
    return registered;
  }

  /**
   * Removes member from the group.
   * Sets the members group to null
   *
   * @param member
   */
  public void removeMember(Person member) {
    if (member.getHikeGroup() == this) {
      member.updateHikeGroup(null);
      members.remove(member);
    }
  }

  /**
   * Returns true or false if a person is a member.
   *
   * @param firstName
   * @param lastName
   * @return true or false
   */
  public boolean checkIfMember(String firstName, String lastName) {
    boolean isMember = false;

    for (int i = 0; i < members.size(); i++) {
      if (firstName.equalsIgnoreCase(members.get(i).getFirstName())
          && lastName.equalsIgnoreCase(members.get(i).getLastName())) {
        isMember = true;
      }
    }

    return isMember;
  }
}
