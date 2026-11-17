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
   * Creates two fresh, empty groups before each test.
   */
  @BeforeEach
  void setUp() {
    group = new Group("Group");
    group2 = new Group("Group2");
  }

  /**
   * Positive test: verifies that a group created with a valid name returns the correct name, hikes
   * registered, count of members and whether a person is a member.
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
   * Positive test: verifies that all members are returned in an array, and that a member who leaves
   * is no longer returned.
   */
  @Test
  void getAllMembersTrue() {
    Person brotherman = new Person("Brotherman", "Testern", group);
    Person bob = new Person("Bob", "Bobsen", group);

    assertArrayEquals(new Person[] {brotherman, bob}, group.getAllMembers());

    bob.leaveGroup();

    assertArrayEquals(new Person[] {brotherman}, group.getAllMembers());
  }

  /**
   * Positive test: verifies that getAllMembers returns an empty array when there are no members in
   * the group.
   */
  @Test
  void getAllMembersEmpty() {
    assertArrayEquals(new Person[] {}, group.getAllMembers());
  }

  /**
   * Positive test: verifies that getDateRegistered returns the date of its creation.
   */
  @Test
  void getDateRegisteredTrue() {
    assertEquals(LocalDate.now(), group.getDateRegistered());
  }

  /**
   * Positive test: verifies that increasHikeRegistered and decreasHikeRegistered work as intended.
   */
  @Test
  void hikesRegisteredIncreaseDecrease() {
    assertEquals(0, group.getHikesRegistered());

    group.increasHikeRegistered();
    group.increasHikeRegistered();
    assertEquals(2, group.getHikesRegistered());

    group.decreasHikeRegistered();
    assertEquals(1, group.getHikesRegistered());
  }

  /**
   * Positive test: verifies that hikesRegistered won't go below zero.
   */
  @Test
  void hikesRegisteredNeverBelowZero() {
    group.decreasHikeRegistered();
    group.decreasHikeRegistered();

    assertEquals(0, group.getHikesRegistered());
  }

  /**
   * Positive test: verifies that adding a new member to a group works as intended, and that the
   * person's group is updated.
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
   * Positive test: verifies that a new member who is a member of another group is moved to the new
   * group and removed from the old one.
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
   * Positive test: verifies that checkIfMember returns false for a person who has never been in a
   * group.
   */
  @Test
  void checkIfMemberNoGroup() {
    Person brotherman = new Person("Brotherman", "Testern");

    assertFalse(group.checkIfMember(brotherman.getFirstName(), brotherman.getLastName()));
    assertFalse(group2.checkIfMember(brotherman.getFirstName(), brotherman.getLastName()));
  }

  /**
   * Positive test: verifies that checkIfMember ignores upper and lower case.
   */
  @Test
  void checkIfMemberCaps() {
    Person brotherman = new Person("Brotherman", "Testern", group);

    assertTrue(group.checkIfMember("broTHERMAN", "TeSTERN"));
  }

  /**
   * Positive test: verifies that removing a member from a group also updates the person's
   * associated group.
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
   * Positive test: verifies that removing a member from a group they are not in does nothing.
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
   * Negative test: verifies that an IllegalArgumentException is thrown when the name is null.
   */
  @Test
  void throwWhenNameNull() {
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> new Group(null));

    assertEquals("Group must have a name!", exception.getMessage());
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when the name is empty or
   * blank.
   */
  @Test
  void throwWhenNameEmpty() {
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> new Group(""));

    IllegalArgumentException exception2 =
        assertThrows(IllegalArgumentException.class, () -> new Group(" "));

    assertEquals("Group must have a name!", exception.getMessage());
    assertEquals("Group must have a name!", exception2.getMessage());
  }

  /**
   * Negative test: verifies that a NullPointerException is thrown when a new member added is null.
   */
  @Test
  void throwWhenAddMemberNull() {
    NullPointerException exception =
        assertThrows(NullPointerException.class, () -> group.addMember(null));

    assertEquals("New member can't be null!", exception.getMessage());
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when a new member with the
   * same name already exists in the group.
   */
  @Test
  void throwWhenAddMemberExists() {
    Person brotherman = new Person("Brotherman", "Testern", group);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> group.addMember(new Person("BroTherman", "TESTERN")));

    assertEquals("Is already a member!", exception.getMessage());
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when a new member with the
   * same name in another group is added.
   */
  @Test
  void throwWhenAddMemberSameNameInAnotherGroup() {
    Person brotherman = new Person("Brotherman", "Testern", group);
    Person brotherman2 = new Person("BrotherMan", "Testern", group2);

    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> group2.addMember(brotherman));

    IllegalArgumentException exception2 =
        assertThrows(IllegalArgumentException.class, () -> group.addMember(brotherman2));

    assertEquals("Is already a member!", exception.getMessage());
    assertEquals("Is already a member!", exception.getMessage());
  }

  /**
   * Negative test: verifies that a NullPointerException is thrown when the member to remove is
   * null.
   */
  @Test
  void throwWhenRemoveMemberNull() {
    Person person = new Person("Brotherman", "Testern", group);

    NullPointerException exception =
        assertThrows(NullPointerException.class, () -> group.removeMember(null));

    assertEquals("Person to remove from group cannot be null!", exception.getMessage());
  }

}
