package no.ntnu.idatt1003.hikeregistry.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Group {
  private final String name;
  private final ArrayList<Person> members = new ArrayList<>();
  private final LocalDateTime registered;
  private int hikesRegistered = 0;

  public Group(String name) {
    if (name.equals("") || name == null) {
      throw new IllegalArgumentException("Group must have a name!");
    }
    this.name = name;
    this.registered = LocalDateTime.now();
  }

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
   * Increases this persons hikes registered.
   */
  public void increasHikeRegistered() {
    hikesRegistered += 1;
  }

  /**
   * Decreases this persons hikes registered.
   */
  public void decreasHikeRegistered() {
    hikesRegistered -= 1;
  }

  /**
   * Adds a new member to this group.
   *
   * @param newMember
   */
  public void addMember(Person newMember) {
    if (checkIfMember(newMember.getFirstName(), newMember.getLastName())) {
      throw new IllegalArgumentException("Person is already a member!");
    }
    // if (newMember == null) {
    // throw new IllegalArgumentException("Person cant be null");
    // }

    members.add(newMember);
    // newMember.updateHikeGroup(name);???????
  }

  public int getCountMembers() {
    return members.size();
  }

  public Person[] getAllMembers() {
    Person[] allMembers = members.toArray(new Person[0]);

    return allMembers;
  }

  // dont know if I keep this
  public LocalDateTime getTimeRegistered() {
    return registered;
  }

  public void removeMember(Person member) {
    members.remove(member);
  }

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
