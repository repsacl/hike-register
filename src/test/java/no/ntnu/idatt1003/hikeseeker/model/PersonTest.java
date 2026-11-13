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
   * Positive test: verifies that a person created with valid names and hikegroup
   * returns the full name formatted as "firstName lastname".
   * returns firstname,
   * returns lastname,
   * returns hikegroup.
   * returns hikesRegistered
   * return true or fals if hasGroup
   * returns right amount of members in group, (person adds it self as a member if
   * it has a group on init)
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
   * Positive test: verifies that the increasHikeRegistered and
   * decreasHikeRegistered works as intended.
   */
  @Test
  void hikesRegisteredIncreasDecreas() {
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
   * Positive test: verifies that person names created with space is trimmed down.
   */
  @Test
  void personNameIsTrimmed() {
    Person brotherman = new Person("  Brotherman  ", "Testern  ");

    assertEquals("Brotherman", brotherman.getFirstName());
    assertEquals("Testern", brotherman.getLastName());
    assertEquals("Brotherman Testern", brotherman.getFullName());
  }

  /**
   * Positive test: verifies that a person with group null, returns rigth values
   * for group check.
   * Returns getHikegrup null and hasGroup false.
   */
  @Test
  void personWithoutGroupNull() {
    Person brotherman = new Person("Brotherman", "Testern", null);

    assertEquals("Brotherman Testern", brotherman.getFullName());
    assertNull(brotherman.getHikeGroup());
    assertFalse(brotherman.hasGroup());
  }

  /**
   * Positive test: verifies that creating a person with a group adds the person
   * as a member of that group.
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
   * Positive test: verifies that hikesRegistered wont go below zero, and goes up
   * after set to zero.
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
   * Positiv test: verifies that a person can leave a group, and persons group is
   * set to null.
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
   * Positive test: verifies that updateHikeGroup works as intended.
   * group.getCountMember will be 0 since the member
   * is not added, just associated with the group.
   * updateHikeGroup will never be used by it self, only group wil use it.
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
   * Negative test: verifies that an IllegalArgumentException throws when
   * firstname
   * is empty.
   */
  @Test
  void throwWhenFirstnameEmpty() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Person("", "Testern"));

    IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class,
        () -> new Person("  ", "Testern"));

    assertEquals("Person must have a firstname!", exception.getMessage());
    assertEquals("Person must have a firstname!", exception2.getMessage());
  }

  /**
   * Negative test: verifies that a IllegalArgumentException throws when firstname
   * is empty with a group.
   */
  @Test
  void throwWhenFirstnameEmptyGroup() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Person("", "Testern", group));

    IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class,
        () -> new Person("  ", "Testern", group));

    assertEquals("Person must have a firstname!", exception.getMessage());
    assertEquals("Person must have a firstname!", exception2.getMessage());
  }

  /**
   * Negative test: verifies that a IllegalArgumentException throws when firstname
   * is null.
   */
  @Test
  void throwWhenFirstnameNull() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Person(null, "Testern"));

    assertEquals("Person must have a firstname!", exception.getMessage());
  }

  /**
   * Negative test: verifies that a IllegalArgumentException throws when firstname
   * is null with a group.
   */
  @Test
  void throwWhenFirstnameNullGroup() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Person(null, "Testern", group));

    assertEquals("Person must have a firstname!", exception.getMessage());
  }

  /**
   * Negative test: verifies that a IllegalArgumentException throws when lastname
   * is empty.
   */
  @Test
  void throwWhenLastNameEmpty() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Person("Brotherman", ""));

    IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class,
        () -> new Person("Brotherman", "  "));

    assertEquals("Person must have a lastname!", exception.getMessage());
    assertEquals("Person must have a lastname!", exception2.getMessage());
  }

  /**
   * Negative test: verifies that a IllegalArgumentException throws when lastname
   * is empty with a group.
   */
  @Test
  void throwWhenLastNameEmptyGroup() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Person("Brotherman", "", group));

    IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class,
        () -> new Person("Brotherman", "  ", group));

    assertEquals("Person must have a lastname!", exception.getMessage());
    assertEquals("Person must have a lastname!", exception2.getMessage());
  }

  /**
   * Negative test: verifies that a IllegalArgumentException throws when lastname
   * is null.
   */
  @Test
  void throwWhenLastnameNull() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Person("Brotherman", null));

    assertEquals("Person must have a lastname!", exception.getMessage());
  }

  /**
   * Negative test: verifies that a IllegalArgumentException throws when lastname
   * is null.
   */
  @Test
  void throwWhenLastnameNullGroup() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Person("Brotherman", null, group));

    assertEquals("Person must have a lastname!", exception.getMessage());
  }

}