package no.ntnu.idatt1003.hikeseeker.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Person} class.
 */
public class PersonTest {

  private Group group;

  /**
   * Creates a fresh, empty group before each test.
   */
  @BeforeEach
  void setUp() {
    group = new Group("testern");
  }

  /**
   * Positive test: verifies that a person created with valid names, with and without a hike group,
   * returns the correct full name ("firstName lastName"), first name, last name, hike group, hikes
   * registered and hasGroup. Also verifies the count of members in the group, since a person adds
   * itself as a member when created with a group.
   */
  @Test
  void validateReturnAllTrue() {
    Person brotherman = new Person("Brotherman", "Testern", group);
    Person bob = new Person("Bob", "Bobsen");

    assertEquals("Brotherman Testern", brotherman.getFullName());
    assertEquals("Brotherman", brotherman.getFirstName());
    assertEquals("Testern", brotherman.getLastName());
    assertEquals(group, brotherman.getHikeGroup());
    assertEquals(0, brotherman.getHikesRegistered());
    assertTrue(brotherman.hasGroup());
    assertEquals(1, brotherman.getHikeGroup().getCountMembers());

    assertEquals("Bob", bob.getFirstName());
    assertEquals("Bobsen", bob.getLastName());
    assertEquals("Bob Bobsen", bob.getFullName());
    assertNull(bob.getHikeGroup());
    assertEquals(0, bob.getHikesRegistered());
    assertFalse(bob.hasGroup());
  }

  /**
   * Positive test: verifies that increasHikeRegistered and decreasHikeRegistered work as intended.
   */
  @Test
  void hikesRegisteredIncreaseDecrease() {
    Person brotherman = new Person("Brotherman", "Testern");
    brotherman.increasHikeRegistered();
    brotherman.increasHikeRegistered();
    brotherman.increasHikeRegistered();

    assertEquals(3, brotherman.getHikesRegistered());

    brotherman.decreasHikeRegistered();
    brotherman.decreasHikeRegistered();

    assertEquals(1, brotherman.getHikesRegistered());
  }

  /**
   * Positive test: verifies that spaces around the first and last name are trimmed.
   */
  @Test
  void personNameIsTrimmed() {
    Person brotherman = new Person("  Brotherman  ", "Testern  ");

    assertEquals("Brotherman", brotherman.getFirstName());
    assertEquals("Testern", brotherman.getLastName());
    assertEquals("Brotherman Testern", brotherman.getFullName());
  }

  /**
   * Positive test: verifies that a person created with group null returns the right values for the
   * group check. Returns null from getHikeGroup and false from hasGroup.
   */
  @Test
  void personWithoutGroupNull() {
    Person brotherman = new Person("Brotherman", "Testern", null);

    assertEquals("Brotherman Testern", brotherman.getFullName());
    assertNull(brotherman.getHikeGroup());
    assertFalse(brotherman.hasGroup());
  }

  /**
   * Positive test: verifies that creating a person with a group adds the person as a member of that
   * group.
   */
  @Test
  void constructorAddsPersonToGroup() {
    Person brotherman = new Person("Brotherman", "Testern", group);

    assertEquals(group, brotherman.getHikeGroup());
    assertEquals(1, group.getCountMembers());
    assertTrue(group.checkIfMember("Brotherman", "Testern"));
  }

  /**
   * Positive test: verifies that hasGroup follows changes to the person's group.
   */
  @Test
  void hasGroupGetHikeGroupFollowsUpdates() {
    Person brotherman = new Person("Brotherman", "Testern");
    Group group2 = new Group("Group2");
    assertFalse(brotherman.hasGroup());
    assertNull(brotherman.getHikeGroup());

    group.addMember(brotherman);
    assertEquals(1, group.getCountMembers());
    assertEquals(0, group2.getCountMembers());
    assertEquals(group, brotherman.getHikeGroup());
    assertTrue(brotherman.hasGroup());

    group2.addMember(brotherman);
    assertEquals(0, group.getCountMembers());
    assertEquals(1, group2.getCountMembers());
    assertEquals(group2, brotherman.getHikeGroup());
    assertTrue(brotherman.hasGroup());
  }

  /**
   * Positive test: verifies that hikesRegistered won't go below zero, and increases again after
   * reaching zero.
   */
  @Test
  void hikesRegisteredNeverBelowZero() {
    Person brotherman = new Person("Brotherman", "Testern");
    brotherman.decreasHikeRegistered();
    brotherman.decreasHikeRegistered();

    assertEquals(0, brotherman.getHikesRegistered());

    brotherman.increasHikeRegistered();
    assertEquals(1, brotherman.getHikesRegistered());
  }

  /**
   * Positive test: verifies that a person can leave a group, and that the person's group is set to
   * null.
   */
  @Test
  void personLeaveGroup() {
    Person brotherman = new Person("Brotherman", "Testern");

    group.addMember(brotherman);
    assertEquals(1, group.getCountMembers());
    assertEquals(group, brotherman.getHikeGroup());
    assertTrue(group.checkIfMember(brotherman.getFirstName(), brotherman.getLastName()));

    brotherman.leaveGroup();

    assertEquals(0, group.getCountMembers());
    assertNull(brotherman.getHikeGroup());
    assertFalse(group.checkIfMember(brotherman.getFirstName(), brotherman.getLastName()));
  }

  /**
   * Positive test: verifies that updateHikeGroup works as intended. group.getCountMembers will be 0
   * since the person is not added as a member, just associated with the group. updateHikeGroup is
   * never used by itself, only by the group.
   */
  @Test
  void updateHikeGroupPerson() {
    Person brotherman = new Person("Brotherman", "Testern");

    brotherman.updateHikeGroup(group);
    assertEquals(group, brotherman.getHikeGroup());
    assertEquals(0, group.getCountMembers());

    brotherman.updateHikeGroup(null);
    assertNull(brotherman.getHikeGroup());
    assertEquals(0, group.getCountMembers());
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when firstname is empty.
   */
  @Test
  void throwWhenFirstnameEmpty() {
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> new Person("", "Testern"));

    IllegalArgumentException exception2 =
        assertThrows(IllegalArgumentException.class, () -> new Person("  ", "Testern"));

    assertEquals("Person must have a firstname!", exception.getMessage());
    assertEquals("Person must have a firstname!", exception2.getMessage());
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when firstname is empty with
   * a group.
   */
  @Test
  void throwWhenFirstnameEmptyGroup() {
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> new Person("", "Testern", group));

    IllegalArgumentException exception2 =
        assertThrows(IllegalArgumentException.class, () -> new Person("  ", "Testern", group));

    assertEquals("Person must have a firstname!", exception.getMessage());
    assertEquals("Person must have a firstname!", exception2.getMessage());
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when firstname is null.
   */
  @Test
  void throwWhenFirstnameNull() {
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> new Person(null, "Testern"));

    assertEquals("Person must have a firstname!", exception.getMessage());
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when firstname is null with
   * a group.
   */
  @Test
  void throwWhenFirstnameNullGroup() {
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> new Person(null, "Testern", group));

    assertEquals("Person must have a firstname!", exception.getMessage());
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when lastname is empty.
   */
  @Test
  void throwWhenLastNameEmpty() {
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> new Person("Brotherman", ""));

    IllegalArgumentException exception2 =
        assertThrows(IllegalArgumentException.class, () -> new Person("Brotherman", "  "));

    assertEquals("Person must have a lastname!", exception.getMessage());
    assertEquals("Person must have a lastname!", exception2.getMessage());
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when lastname is empty with
   * a group.
   */
  @Test
  void throwWhenLastNameEmptyGroup() {
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> new Person("Brotherman", "", group));

    IllegalArgumentException exception2 =
        assertThrows(IllegalArgumentException.class, () -> new Person("Brotherman", "  ", group));

    assertEquals("Person must have a lastname!", exception.getMessage());
    assertEquals("Person must have a lastname!", exception2.getMessage());
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when lastname is null.
   */
  @Test
  void throwWhenLastnameNull() {
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> new Person("Brotherman", null));

    assertEquals("Person must have a lastname!", exception.getMessage());
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when lastname is null with a
   * group.
   */
  @Test
  void throwWhenLastnameNullGroup() {
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> new Person("Brotherman", null, group));

    assertEquals("Person must have a lastname!", exception.getMessage());
  }

  /**
   * Negative test: verifies that a NullPointerException is thrown when a person wants to leave a
   * group, but has no group associated with them.
   */
  @Test
  void throwWhenPersonLeaveGroupNoGroup() {
    Person brotherman = new Person("Brotherman", "Testern");

    NullPointerException exception =
        assertThrows(NullPointerException.class, () -> brotherman.leaveGroup());

    assertEquals("You are not a member of a group!", exception.getMessage());
  }

}
