package no.ntnu.idatt1003.hikeseeker.registry;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import no.ntnu.idatt1003.hikeseeker.model.Group;

/**
 * Unit tests for the {@link GroupRegistry} class.
 *
 * <p>
 * Note: The basic structure of this test class was suggested by an AI assistant (Claude) and then
 * reviewed and adapted by me.
 * </p>
 */
public class GroupRegistryTest {
  private GroupRegistry groupRegistry;
  private Group group;
  private Group group2;

  /**
   * Creates a fresh, empty registry and two groups before each test.
   */
  @BeforeEach
  void setUp() {
    groupRegistry = new GroupRegistry();

    group = new Group("Testern");
    group2 = new Group("Group");
  }

  /**
   * Positive test: verifies that an empty registry returns an empty array, not null.
   */
  @Test
  void getAllGroupsOnEmptyRegistryReturnsEmptyArray() {
    assertArrayEquals(new Group[] {}, groupRegistry.getAllGroups());
  }

  /**
   * Positive test: verifies that added groups are returned in the order they were added.
   */
  @Test
  void getAllGroupsReturnsAddedGroupsInOrder() {
    groupRegistry.addGroup(group);
    groupRegistry.addGroup(group2);

    assertArrayEquals(new Group[] {group, group2}, groupRegistry.getAllGroups());
  }

  /**
   * Positive test: verifies that getAllGroups returns a copy, so changing the returned array does
   * not change the registry.
   */
  @Test
  void getAllGroupsReturnsCopy() {
    groupRegistry.addGroup(group);

    Group[] allGroups = groupRegistry.getAllGroups();
    allGroups[0] = group2;

    assertArrayEquals(new Group[] {group}, groupRegistry.getAllGroups());
  }

  /**
   * Negative test: verifies that adding null throws an exception.
   */
  @Test
  void addGroupsWithNullThrowsException() {
    NullPointerException exception =
        assertThrows(NullPointerException.class, () -> groupRegistry.addGroup(null));

    assertEquals("Group added to the registry can't be null!", exception.getMessage());
    assertEquals(0, groupRegistry.getAllGroups().length);
  }

  /**
   * Negative test: verifies that the same group cannot be added twice.
   */
  @Test
  void addGroupTwiceDoesNotCreateDuplicate() {
    groupRegistry.addGroup(group);
    assertEquals(1, groupRegistry.getAllGroups().length);

    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> groupRegistry.addGroup(group));

    assertEquals("Group already exists in the registry!", exception.getMessage());
    assertEquals(1, groupRegistry.getAllGroups().length);

    Group testGroup = new Group("TESTern");

    IllegalArgumentException exception2 =
        assertThrows(IllegalArgumentException.class, () -> groupRegistry.addGroup(testGroup));

    assertEquals("Group already exists in the registry!", exception2.getMessage());
    assertEquals(1, groupRegistry.getAllGroups().length);
  }

  /**
   * Positive test: verifies that checkIfExists works as intended.
   */
  @Test
  void checkIfExistsName() {
    assertFalse(groupRegistry.checkIfExists("Testern"));
    groupRegistry.addGroup(group);
    assertFalse(groupRegistry.checkIfExists("Group"));

    assertTrue(groupRegistry.checkIfExists(group.getName()));
    assertTrue(groupRegistry.checkIfExists("  TesterN"));
  }

  /**
   * Negative test: verifies that checkIfExists throws a NullPointerException when the name is null.
   */
  @Test
  void throwWhenCheckIfExistsNull() {
    NullPointerException exception =
        assertThrows(NullPointerException.class, () -> groupRegistry.checkIfExists(null));

    assertEquals("Group name to check cannot be null!", exception.getMessage());
  }

  /**
   * Negative test: verifies that checkIfExists throws an IllegalArgumentException when the name is
   * empty or blank.
   */
  @Test
  void throwWhenCheckIfExistsEmpty() {
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> groupRegistry.checkIfExists(""));

    IllegalArgumentException exception2 =
        assertThrows(IllegalArgumentException.class, () -> groupRegistry.checkIfExists(" "));

    assertEquals("Group name to check cannot be empty!", exception.getMessage());
    assertEquals("Group name to check cannot be empty!", exception2.getMessage());
  }

  /**
   * Positive test: verifies that a group can be found by name.
   */
  @Test
  void getGroupByName() {
    groupRegistry.addGroup(group);

    assertEquals(group, groupRegistry.getGroup("Testern"));
  }

  /**
   * Positive test: verifies that the search ignores upper and lower case.
   */
  @Test
  void getGroupIsCaseIgnore() {
    groupRegistry.addGroup(group);

    assertEquals(group, groupRegistry.getGroup("teSTern"));
    assertEquals(group, groupRegistry.getGroup("TESTERN"));
  }

  /**
   * Negative test: verifies that searching for an unknown name returns null.
   */
  @Test
  void getGroupWithUnknownNameReturnsNull() {
    groupRegistry.addGroup(group);

    assertNull(groupRegistry.getGroup("Group"));
  }

  /**
   * Negative test: verifies that searching in an empty registry returns null.
   */
  @Test
  void getGroupOnEmptyRegistryReturnsNull() {
    assertNull(groupRegistry.getGroup("Testern"));
  }

  /**
   * Negative test: verifies that searching with a blank string returns null.
   */
  @Test
  void getGroupWithBlankReturnsNull() {
    groupRegistry.addGroup(group);

    assertNull(groupRegistry.getGroup("  "));
    assertNull(groupRegistry.getGroup(""));
  }

  /**
   * Negative test: verifies that searching with null returns null.
   */
  @Test
  void getGroupWithNullReturnsNull() {
    groupRegistry.addGroup(group);

    assertNull(groupRegistry.getGroup(null));
  }

  /**
   * Edge case: verifies that a group is found when the search has leading or trailing whitespace.
   */
  @Test
  void getGroupWithSpaceTrim() {
    groupRegistry.addGroup(group);

    assertEquals(group, groupRegistry.getGroup("       Testern"));
    assertEquals(group, groupRegistry.getGroup("       Testern       "));
  }
}
