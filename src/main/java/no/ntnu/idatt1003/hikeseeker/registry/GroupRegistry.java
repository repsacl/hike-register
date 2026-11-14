package no.ntnu.idatt1003.hikeseeker.registry;

import java.util.ArrayList;

import no.ntnu.idatt1003.hikeseeker.model.Group;

/**
 * Represents a registry that keeps track of all groups in the hike registry.
 *
 * <p>
 * The registry can add and remove groups, check if a group name already exists
 * and look up groups by name. Group names are compared without regard to case.
 * </p>
 */

public class GroupRegistry {

  private final ArrayList<Group> groups = new ArrayList<>();

  /**
   * Adds a new group to the group registry.
   *
   * @param newGroup
   * @throws NullPointerException     if the group is null.
   * @throws IllegalArgumentException if the group already exists.
   */
  public void addGroup(Group newGroup) {
    if (newGroup == null) {
      throw new NullPointerException("Group added to the registry cant be null!");
    }

    if (checkIfExists(newGroup.getName())) {
      throw new IllegalArgumentException("Group already exists in the registry!");
    }

    groups.add(newGroup);
  }

  /**
   * Checks if a group name already exist and returns true or false.
   *
   * @param name
   * @throws NullPointerException     if the name is null.
   * @throws IllegalArgumentException if the name is blank.
   */
  public boolean checkIfExists(String name) {
    if (name == null) {
      throw new NullPointerException("Group name to check cannot be null!");
    }
    if (name.isBlank()) {
      throw new IllegalArgumentException("Group name to check cannot be empty!");
    }

    return groups.stream().anyMatch(grp -> grp.getName().equalsIgnoreCase(name.trim()));
  }

  /**
   * Gets all groups and returns it as an array.
   *
   * @return an array with all groups
   */
  public Group[] getAllGroups() {
    return groups.toArray(new Group[0]);
  }

  /**
   * Removes a group from the group registry.
   *
   * @param group
   */
  public void removeGroup(Group group) {
    groups.remove(group);
  }

  /**
   * Gets a groups name and returns it as a string.
   *
   * @param name
   * @return group name as string
   */
  public Group getGroup(String name) {
    if (name == null || name.isBlank()) {
      return null;
    }
    String searchName = name.trim();

    return groups.stream()
        .filter(grp -> grp.getName().equalsIgnoreCase(searchName))
        .findFirst()
        .orElse(null);
  }

  // /**
  // * Gets a groups name and returns it as a string.
  // *
  // * @param name
  // * @return group name as string
  // */
  // public String getGroupName(String name) {
  // return groups.stream()
  // .filter(grp -> grp.getName().equalsIgnoreCase(name))
  // .findFirst()
  // .map(Group::getName)
  // .orElse(null);
  // }
}
