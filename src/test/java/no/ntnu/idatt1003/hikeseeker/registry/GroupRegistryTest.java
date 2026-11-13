package no.ntnu.idatt1003.hikeseeker.registry;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import no.ntnu.idatt1003.hikeseeker.model.Group;

/**
 * Unit tests for the {@link GroupRegistry} class.
 *
 * <p>
 * Note: The basic structure of this test class was suggested by an AI assistant
 * (Claude)
 * and then reviewed and adapted by me.
 * </p>
 */
public class GroupRegistryTest {
  private GroupRegistry groupRegistry;
  private Group group;
  private Group group2;

  /**
   * Creates a fresh, empty registry, a group and a fixed time before each test.
   */
  @BeforeEach
  void setUp() {
    groupRegistry = new GroupRegistry();

    group = new Group("Testern");
    group2 = new Group("Group");
  }

  /**
   * Positive test: verifies that an empty registry returns an empty array, not
   * null.
   */
  @Test
  void getAllGroupsOnEmptyRegistryReturnsEmptyArray() {
    assertArrayEquals(new Group[] {}, groupRegistry.getAllGroups());
  }

  /**
   * Positive test: verifies that added groups are returned in the order they
   * were added.
   */
  @Test
  void getAllGroupsReturnsAddedPersonsInOrder() {
    groupRegistry.addGroup(group);
    groupRegistry.addGroup(group2);

    assertArrayEquals(new Group[] { group, group2 }, groupRegistry.getAllGroups());
  }

  /**
   * Negative test: verifies that adding null throws an exception.
   */
  @Test
  void addGroupsWithNullThrowsException() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> groupRegistry.addGroup(null));

    assertEquals("Group added to the registry cant be null!", exception.getMessage());
  }

  /**
   * Negative test: verifies that the same group cannot be added twice.
   */
  @Test
  void addGroupTwiceDoesNotCreateDuplicate() {
    groupRegistry.addGroup(group);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> groupRegistry.addGroup(group));

    assertEquals("Group already exists in the registry!", exception.getMessage());

    Group testGroup = new Group("TESTern");

    IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class,
        () -> groupRegistry.addGroup(testGroup));

    assertEquals("Group already exists in the registry!", exception2.getMessage());
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
   * Negative test: verifies the behaviour when searching with blank string.
   */
  @Test
  void getGroupWithBlankReturnsNull() {
    groupRegistry.addGroup(group);

    assertNull(groupRegistry.getGroup("  "));
    assertNull(groupRegistry.getGroup(""));
  }

  /**
   * Edge case: verifies the behaviour when the search has leading or trailing
   * whitespace.
   */
  @Test
  void getGroupWithSurroundingWhitespace() {
    groupRegistry.addGroup(group);

    assertEquals(group, groupRegistry.getGroup("       Testern"));
    assertEquals(group, groupRegistry.getGroup("       Testern       "));
  }
}
