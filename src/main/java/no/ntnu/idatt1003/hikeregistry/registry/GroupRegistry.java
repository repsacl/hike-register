package no.ntnu.idatt1003.hikeregistry.registry;

import java.util.ArrayList;

import no.ntnu.idatt1003.hikeregistry.model.Group;

public class GroupRegistry {

  private ArrayList<Group> groups = new ArrayList<>();

  public void addGroup(Group newGroup) {
    if (checkIfExists(newGroup.getName())) {
      System.out.println("Group alrady exists");
      return;
    }

    groups.add(newGroup);
  }

  public boolean checkIfExists(String name) {
    boolean exists = false;

    for (int i = 0; i < groups.size(); i++) {
      if (name.equalsIgnoreCase(groups.get(i).getName())) {
        exists = true;
      }
    }

    return exists;
  }

  public Group[] getAllGroups() {
    Group[] allGroups = groups.toArray(new Group[0]);
    return allGroups;
  }

  // public String getGroupNameToMember(Group group) {
  // String groupName = null;

  // return groupName;
  // }

  public void removeGroup(Group group) {
    groups.remove(group);
  }

  public String getGroupName(String name) {
    String groupName = null;

    for (int i = 0; i < groups.size(); i++) {
      if (name.equalsIgnoreCase(groups.get(i).getName())) {
        groupName = groups.get(i).getName();
      }
    }

    return groupName;
  }
}
