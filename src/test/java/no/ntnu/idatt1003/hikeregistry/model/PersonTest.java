package no.ntnu.idatt1003.hikeregistry.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Person} class.
 *
 * <p>
 * Note: The basic structure of this test class was suggested by an AI assistant
 * (Claude)
 * and then reviewed and adapted by me.
 */
public class PersonTest {

  /**
   * Positive test: verifies that a person created with valid names and hikegroup
   * returns the full name formatted as "lastName, firstName".
   * returns firstname,
   * returns lastname,
   * returns hikegroup.
   */
  @Test
  void validateReturnAllTrue() {
    Group group = new Group("testern");
    Person person = new Person("Brotherman", "Testern", group);

    assertEquals("Brotherman Testern", person.getFullName());
    assertEquals("Brotherman", person.getFirstName());
    assertEquals("Testern", person.getLastName());
    assertEquals(group, person.getHikeGroup());
  }

  /**
   * Negative test: verifies that a IllegalArgumentException throws when firstname
   * is empty
   */
  @Test
  void throwWhenFirstnameEmpty() {
    Group group = new Group("testern");

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Person("", "Testern", group));

    assertEquals("Person must have a firstname!", exception.getMessage());
  }

  /**
   * Negative test: verifies that a IllegalArgumentException throws when firstname
   * is null
   */
  @Test
  void throwWhenFirstnameNull() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Person(null, "Testern"));

    assertEquals("Person must have a firstname!", exception.getMessage());
  }

  /**
   * Negative test: verifies that a IllegalArgumentException throws when lastname
   * is empty
   */
  @Test
  void throwWhenLastNameEmpty() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Person("Brotherman", ""));

    assertEquals("Person must have a lastname!", exception.getMessage());
  }

  /**
   * Negative test: verifies that a IllegalArgumentException throws when lastname
   * is null
   */
  @Test
  void throwWhenLastnameNull() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Person("Brotherman", null));

    assertEquals("Person must have a lastname!", exception.getMessage());
  }

  /**
   * Negative test: verifies that a IllegalArgumentException throws when hikegroup
   * is null
   */
  @Test
  void throwWhenHikegroupNull() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Person("Brotherman", "Testern", null));

    assertEquals("Person must have a valid hikegroup!", exception.getMessage());
  }

  /**
   * Negative test: verifies that a IllegalArgumentException throws when hikegroup
   * is empty
   */
  // @Test
  // void throwWhenHikegroupEmpty() {
  // IllegalArgumentException exception =
  // assertThrows(IllegalArgumentException.class,
  // () -> new Person("Brotherman", "Testern"));

  // assertEquals("Person must have a valid hikegroup!", exception.getMessage());
  // }
}