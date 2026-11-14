package no.ntnu.idatt1003.hikeseeker.model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Group} class.
 */
public class GroupTest {

  private Group group;
  private Group group2;

  /**
   * Creates a fresh, empty groups before each test.
   */
  @BeforeEach
  void setUp() {
    group = new Group("Group");
    group2 = new Group("Group2");
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
    Person brotherman = new Person("Brotherman", "Testern", group);

    assertEquals("Group", group.getName());
    assertEquals(0, group.getHikesRegistered());
    assertEquals(1, group.getCountMembers());
    assertTrue(group.checkIfMember(brotherman.getFirstName(), brotherman.getLastName()));
  }

  /**
   * Positive test: verifies that all members are returned as a copy of the
   * register in an array.
   */
  @Test
  void getAllMembersTrue() {
    Person brotherman = new Person("Brotherman", "Testern", group);
    Person bob = new Person("Bob", "Bobsen", group);

    assertArrayEquals(new Person[] { brotherman, bob }, group.getAllMembers());

    bob.leaveGroup();

    assertArrayEquals(new Person[] { brotherman }, group.getAllMembers());
  }

  /**
   * Positive test: verifies that getAllmembers retursn an empty array when there
   * are no members in the group.
   */
  @Test
  void getAllMembersEmpty() {
    assertArrayEquals(new Person[] {}, group.getAllMembers());
  }

  /**
   * Positive test: verifies getDateRegistered returns the date of it's
   * creation.
   */
  @Test
  void getDateRegisteredTrue() {
    assertEquals(LocalDate.now(), group.getDateRegistered());
  }

  /**
   * Positive test: verifies that the increasHikeRegistered and
   * decreasHikeRegistered works as intended.
   */
  @Test
  void hikesRegisteredIncreasDecreas() {
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
    Person brotherman = new Person("Brotherman", "Testern");

    group.addMember(brotherman);

    assertTrue(group.checkIfMember(brotherman.getFirstName(), brotherman.getLastName()));
    assertEquals(1, group.getCountMembers());

    assertEquals(group, brotherman.getHikeGroup());
  }

  /**
   * Positive test: verifies that adding a new member who is a member in another
   * group is updated.
   */
  @Test
  void addNewMemberWithGroupFollows() {
    Person brotherman = new Person("Brotherman", "Testern", group2);

    assertEquals(group2, brotherman.getHikeGroup());
    assertTrue(group2.checkIfMember(brotherman.getFirstName(), brotherman.getLastName()));
    assertEquals(1, group2.getCountMembers());

    group.addMember(brotherman);

    assertEquals(group, brotherman.getHikeGroup());
    assertEquals(1, group.getCountMembers());
    assertTrue(group.checkIfMember(brotherman.getFirstName(), brotherman.getLastName()));

    assertEquals(0, group2.getCountMembers());
    assertFalse(group2.checkIfMember(brotherman.getFirstName(), brotherman.getLastName()));
  }

  /**
   * Positive test: verifies that a member who have never been in an group before,
   * returns false.
   */
  @Test
  void checkIfMemberNoGroup() {
    Person brotherman = new Person("Brotherman", "Testern");

    assertFalse(group.checkIfMember(brotherman.getFirstName(), brotherman.getLastName()));
    assertFalse(group2.checkIfMember(brotherman.getFirstName(), brotherman.getLastName()));
  }

  /**
   * Positive test: verifies that checkIfMember ignores caps with string check.
   */
  @Test
  void checkIfMemberCaps() {
    Person brotherman = new Person("Brotherman", "Testern", group);

    assertTrue(group.checkIfMember("broTHERMAN", "TeSTERN"));
  }

  /**
   * Positive test: verifies that removeing a meber from a group, also updates the
   * persons associated group.
   */
  @Test
  void removeMemberFollowsGroupAndPerson() {
    Person brotherman = new Person("Brotherman", "Testern", group);

    assertEquals(group, brotherman.getHikeGroup());
    assertTrue(group.checkIfMember(brotherman.getFirstName(), brotherman.getLastName()));
    assertEquals(1, group.getCountMembers());

    group.removeMember(brotherman);

    assertNull(brotherman.getHikeGroup());
    assertFalse(group.checkIfMember(brotherman.getFirstName(), brotherman.getLastName()));
    assertEquals(0, group.getCountMembers());
  }

  /**
   * Positive test: verifies that removeing a meber from another group does
   * nothing.
   */
  @Test
  void removeMemberFromAnotherGroupNothing() {
    Person brotherman = new Person("Brotherman", "Testern", group);

    assertTrue(group.checkIfMember(brotherman.getFirstName(), brotherman.getLastName()));
    assertEquals(1, group.getCountMembers());

    group2.removeMember(brotherman);

    assertEquals(group, brotherman.getHikeGroup());
    assertTrue(group.checkIfMember(brotherman.getFirstName(), brotherman.getLastName()));
    assertEquals(1, group.getCountMembers());
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

    IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class,
        () -> new Group(" "));

    assertEquals("Group must have a name!", exception.getMessage());
    assertEquals("Group must have a name!", exception2.getMessage());
  }

  /**
   * Negative test: verifies that a NullPointerException is thrown when a new
   * member added is null.
   */
  @Test
  void throwWhenAddMemberNull() {
    NullPointerException exception = assertThrows(NullPointerException.class,
        () -> group.addMember(null));

    assertEquals("New member cant be null.", exception.getMessage());
  }

  /**
   * Negative test: verifies that a IllegalArgumentException is thrown when a new
   * member added already exist.
   */
  @Test
  void throwWhenAddMemberExists() {
    Person brotherman = new Person("Brotherman", "Testern", group);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> group.addMember(new Person("BroTherman", "TESTERN")));

    assertEquals("Is already a member!", exception.getMessage());
  }

  /**
   * Negative test: verifies that a IllegalArgumentException is thrown when a new
   * member added have the same name in another group.
   */
  @Test
  void throwWhenAddMemberSameNameInAnotherGroup() {
    Person brotherman = new Person("Brotherman", "Testern", group);
    Person brotherman2 = new Person("BrotherMan", "Testern", group2);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> group2.addMember(brotherman2));

    assertEquals("Is already a member!", exception.getMessage());
  }

  /**
   * Negative test: verifies that a NullPointerException is thrown when a member
   * to remvoe is null.
   */
  @Test
  void throwWhenRemoveMeberNull() {
    Person person = new Person("Brotherman", "Testern", group);

    NullPointerException exception = assertThrows(NullPointerException.class,
        () -> group.removeMember(null));

    assertEquals("Person to removve from group cannot be null!", exception.getMessage());
  }

}
