package no.ntnu.idatt1003.hikeregistry.registry;

import java.util.ArrayList;

import no.ntnu.idatt1003.hikeregistry.model.Group;

/**
 * Represents a registry that keeps track of all groups in the hike registry.
 *
 * <p>
 * The registry can add and remove groups, check if a group name already exists
 * and look up groups by name. Group names are compared without regard to case.
 * </p>
 */

public class GroupRegistry {

  private ArrayList<Group> groups = new ArrayList<>();

  /**
   * Adds a new group to the group registry.
   *
   * @param newGroup
   */
  public void addGroup(Group newGroup) {
    groups.add(newGroup);
  }

  /**
   * Checks if a group name already exist and returns true or false.
   *
   * @param name
   * @return true or false
   */
  public boolean checkIfExists(String name) {
    return groups.stream().anyMatch(grp -> grp.getName().equalsIgnoreCase(name));
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
  public String getGroupName(String name) {
    return groups.stream()
        .filter(grp -> grp.getName().equalsIgnoreCase(name))
        .findFirst()
        .map(Group::getName)
        .orElse(null);
  }
}
