package no.ntnu.idatt1003.hikeregistry.model;

import java.util.ArrayList;

public class Group {
  private final String name;
  private final ArrayList<Person> members = new ArrayList<>();

  public Group(String name) {
    if (name.equals("") || name == null) {
      throw new IllegalArgumentException("Group must have a name!");
    }
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void addMember(Person newMember) {
    if (checkIfMember(newMember.getFirstName(), newMember.getLastName())) {
      System.out.println("Person is already a member!");
      return;
    }
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
