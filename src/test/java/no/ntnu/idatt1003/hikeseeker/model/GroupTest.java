package no.ntnu.idatt1003.hikeseeker.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Group} class.
 */
public class GroupTest {

  private Group group;

  /**
   * Creates a fresh, empty group before each test.
   */
  @BeforeEach
  void setUp() {
    group = new Group("Group");
  }

  /**
   * Positive test: verifies that group created with a valid string
   * returns name
   * returns hikes registered
   * returns count of members
   * returns true or false if a person is amember
   */
  @Test
  void validateReturnAllTrue() {
    Person person = new Person("Brotherman", "Testern", group);

    assertEquals("Group", group.getName());
    assertEquals(0, group.getHikesRegistered());
    assertEquals(1, group.getCountMembers());
    assertTrue(group.checkIfMember(person.getFirstName(), person.getLastName()));
    // getTimeRegistered();
  }

  /**
   * Positive test: verifies that the increasHikeRegistered and
   * decreasHikeRegistered works as intended.
   */
  @Test
  void HikesRegisteredIncreasDecreas() {
    assertEquals(0, group.getHikesRegistered());

    group.increasHikeRegistered();
    group.increasHikeRegistered();
    assertEquals(2, group.getHikesRegistered());

    group.decreasHikeRegistered();
    assertEquals(1, group.getHikesRegistered());
  }

  /**
   * Positive test: verifies that hikesRegistered wont go below zero.
   */
  @Test
  void hikesRegisteredNeverBelowZero() {
    group.decreasHikeRegistered();
    group.decreasHikeRegistered();

    assertEquals(0, group.getHikesRegistered());
  }

  /**
   * Positive test: verifies that adding a new member to a group works as intenden
   * and the persons groups follows.
   */
  @Test
  void addNewMemberFollowsPerson() {
    Person person = new Person("Brotherman", "Testern");

    group.addMember(person);

    assertTrue(group.checkIfMember(person.getFirstName(), person.getLastName()));
    assertEquals(1, group.getCountMembers());

    assertEquals(group, person.getHikeGroup());
  }

  /**
   * Positive test: verifies that adding a new member who is a member in another
   * group is updated.
   */
  @Test
  void addNewMemberWithGroupFollows() {
    Group group2 = new Group("Group2");
    Person person = new Person("Brotherman", "Testern", group2);

    assertEquals(group2, person.getHikeGroup());
    assertTrue(group2.checkIfMember(person.getFirstName(), person.getLastName()));

    group.addMember(person);

    assertEquals(group, person.getHikeGroup());
    assertFalse(group2.checkIfMember(person.getFirstName(), person.getLastName()));
  }

  /**
   * Negative test: verifies that a IllegalArgumentException is throw when name
   * is null.
   */
  @Test
  void throwWhenNameNull() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Group(null));

    assertEquals("Group must have a name!", exception.getMessage());
  }

  /**
   * Negative test: verifies that a IllegalArgumentException is throw when name
   * is empty.
   */
  @Test
  void throwWhenNameEmpty() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Group(""));

    assertEquals("Group must have a name!", exception.getMessage());
  }

  /**
   * Negative test: verifies that a IllegalArgumentException is thrown when a new
   * member added is null.
   */
  @Test
  void throwWhenAddMemberNull() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> group.addMember(null));

    assertEquals("New member cant be null.", exception.getMessage());
  }

  /**
   * Negative test: verifies that a IllegalArgumentException is thrown when a new
   * member added already exist.
   */
  @Test
  void throwWhenAddMemberExists() {
    Person person = new Person("Brotherman", "Testern", group);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> group.addMember(person));

    assertEquals("Is already a member!", exception.getMessage());
  }

}
