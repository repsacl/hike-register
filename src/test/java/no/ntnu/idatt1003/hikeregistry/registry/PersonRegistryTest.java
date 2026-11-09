package no.ntnu.idatt1003.hikeregistry.registry;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import no.ntnu.idatt1003.hikeregistry.model.Person;

/**
 * Unit tests for the {@link PersonRegistry} class.
 *
 * <p>
 * Note: The basic structure of this test class was suggested by an AI assistant
 * (Claude)
 * and then reviewed and adapted by me.
 * </p>
 */
public class PersonRegistryTest {

  private PersonRegistry personRegistry;

  private Person brotherman;
  private Person bob;
  private Person bobT;

  /**
   * Creates a fresh, empty registry before each test.
   */
  @BeforeEach
  void setUp() {
    personRegistry = new PersonRegistry();

    brotherman = new Person("Brotherman", "Testern");
    bob = new Person("Bob", "Bobsen");
    bobT = new Person("Bob", "Testern");
  }

  /**
   * Positive test: verifies that an empty registry returns an empty array, not
   * null.
   */
  @Test
  void getAllPersonsOnEmptyRegistryReturnsEmptyArray() {
    assertArrayEquals(new Person[] {}, personRegistry.getAllPersons());
  }

  /**
   * Positive test: verifies that added persons are returned in the order they
   * were added.
   */
  @Test
  void getAllPersonsReturnsAddedPersonsInOrder() {
    personRegistry.addPerson(brotherman);
    personRegistry.addPerson(bob);

    assertArrayEquals(new Person[] { brotherman, bob }, personRegistry.getAllPersons());
  }

  /**
   * Negative test: verifies that adding null throws an exception.
   */
  @Test
  void addPersonWithNullThrowsException() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> personRegistry.addPerson(null));

    assertEquals("Person added to the registry cant be null!", exception.getMessage());
  }

  /**
   * Negative test: verifies that the same person cannot be added twice.
   */
  @Test
  void addPersonTwiceDoesNotCreateDuplicate() {
    personRegistry.addPerson(brotherman);

    Person brot = new Person("BrothermMAN", "TESTern");

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> personRegistry.addPerson(brotherman));

    assertEquals("Person already exists in the registry!", exception.getMessage());

    IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class,
        () -> personRegistry.addPerson(brotherman));

    assertEquals("Person already exists in the registry!", exception2.getMessage());
  }

  /**
   * Positive test: verifies that a person can be found by first name.
   */
  @Test
  void getPersonFindsByFirstName() {
    personRegistry.addPerson(brotherman);

    assertEquals(brotherman, personRegistry.getPerson("Brotherman"));
  }

  /**
   * Positive test: verifies that a person can be found by last name.
   */
  @Test
  void getPersonFindsByLastName() {
    personRegistry.addPerson(brotherman);

    assertEquals(brotherman, personRegistry.getPerson("Testern"));
  }

  /**
   * Positive test: verifies that a person can be found by full name.
   */
  @Test
  void getPersonFindsByFullName() {
    personRegistry.addPerson(brotherman);

    assertEquals(brotherman, personRegistry.getPerson("Brotherman Testern"));
  }

  /**
   * Positive test: verifies that the search ignores upper and lower case.
   */
  @Test
  void getPersonIsCaseIgnore() {
    personRegistry.addPerson(brotherman);

    assertEquals(brotherman, personRegistry.getPerson("BROTHERMAN"));
    assertEquals(brotherman, personRegistry.getPerson("TESTERN"));
  }

  /**
   * Positive test: verifies that the first matching person is returned when
   * several persons share the same name.
   */
  @Test
  void getPersonReturnsFirstMatchWhenNamesAreDuplicated() {
    personRegistry.addPerson(bobT);
    personRegistry.addPerson(bob);

    assertEquals(bobT, personRegistry.getPerson("Bob"));
  }

  /**
   * Negative test: verifies that searching for an unknown name returns null.
   */
  @Test
  void getPersonWithUnknownNameReturnsNull() {
    personRegistry.addPerson(bob);

    assertNull(personRegistry.getPerson("Brotherman"));
  }

  /**
   * Negative test: verifies that searching in an empty registry returns null.
   */
  @Test
  void getPersonOnEmptyRegistryReturnsNull() {
    assertNull(personRegistry.getPerson("Bob"));
  }

  /**
   * Negative test: verifies the behaviour when searching with null.
   */
  @Test
  void getPersonWithNullReturnsNull() {
    personRegistry.addPerson(bob);

    assertNull(personRegistry.getPerson(null));
  }

  /**
   * Negative test: verifies the behaviour when searching with blank string.
   */
  @Test
  void getPersonWithBlankReturnsNull() {
    personRegistry.addPerson(bob);

    assertNull(personRegistry.getPerson(" "));
    assertNull(personRegistry.getPerson(""));
  }

  /**
   * Edge case: verifies the behaviour when the search has leading or trailing
   * whitespace.
   */
  @Test
  void getPersonWithSurroundingWhitespace() {
    personRegistry.addPerson(brotherman);

    assertEquals(brotherman, personRegistry.getPerson("      Brotherman Testern"));
    assertEquals(brotherman, personRegistry.getPerson("   Brotherman    "));
  }
}
