package no.ntnu.idatt1003.hikeregistry.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Person} class.
 *
 * <p>Note: The basic structure of this test class was suggested by an AI assistant (Claude)
 * and then reviewed and adapted by me.
 */
class PersonTest {

  /**
   * Positive test: verifies that a person created with valid names
   * returns the full name formatted as "lastName, firstName".
   */
  @Test
  void getFullName_validNames_returnsLastNameCommaFirstName() {
    Person person = new Person("Ola", "Nordmann", "fjellgjengen");

    assertEquals("Nordmann, Ola", person.getFullName());
  }
}