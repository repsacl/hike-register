package no.ntnu.idatt1003.hikeseeker.registry;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import no.ntnu.idatt1003.hikeseeker.model.Person;

/**
 * Unit tests for the {@link PersonRegistry} class.
 *
 * <p>
 * Note: The basic structure of this test class was suggested by an AI assistant (Claude) and then
 * reviewed and adapted by me.
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
   * Positive test: verifies that an empty registry returns an empty array, not null.
   */
  @Test
  void getAllPersonsOnEmptyRegistryReturnsEmptyArray() {
    assertArrayEquals(new Person[] {}, personRegistry.getAllPersons());
  }

  /**
   * Positive test: verifies that added persons are returned in the order they were added.
   */
  @Test
  void getAllPersonsReturnsAddedPersonsInOrder() {
    personRegistry.addPerson(brotherman);
    personRegistry.addPerson(bob);

    assertArrayEquals(new Person[] {brotherman, bob}, personRegistry.getAllPersons());
  }

  /**
   * Positive test: verifies that getAllPersons returns a copy, so changing the returned array does
   * not change the registry.
   */
  @Test
  void getAllPersonsReturnsCopy() {
    personRegistry.addPerson(brotherman);

    Person[] allPersons = personRegistry.getAllPersons();
    allPersons[0] = bob;

    assertArrayEquals(new Person[] {brotherman}, personRegistry.getAllPersons());
  }

  /**
   * Negative test: verifies that adding null throws an exception.
   */
  @Test
  void addPersonWithNullThrowsException() {
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> personRegistry.addPerson(null));

    assertEquals("Person added to the registry can't be null!", exception.getMessage());
  }

  /**
   * Negative test: verifies that the same person cannot be added twice.
   */
  @Test
  void addPersonTwiceDoesNotCreateDuplicate() {
    personRegistry.addPerson(brotherman);

    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> personRegistry.addPerson(brotherman));

    assertEquals("Person already exists in the registry!", exception.getMessage());

    assertEquals(1, personRegistry.getAllPersons().length);
  }

  /**
   * Negative test: verifies that the same person cannot be added twice with caps in the name.
   */
  @Test
  void addPersonTwiceDoesNotCreateDuplicateCaps() {
    personRegistry.addPerson(brotherman);
    Person brot = new Person("BrotherMAN", "TESTern");

    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> personRegistry.addPerson(brot));

    assertEquals("Person already exists in the registry!", exception.getMessage());

    assertEquals(1, personRegistry.getAllPersons().length);
  }

  /**
   * Positive test: verifies that persons with the same first name can be added to the registry.
   */
  @Test
  void addPersonSameFirstName() {
    personRegistry.addPerson(bob);
    personRegistry.addPerson(bobT);

    assertEquals(2, personRegistry.getAllPersons().length);
  }

  /**
   * Positive test: verifies that persons with the same last name can be added to the registry.
   */
  @Test
  void addPersonSameLastName() {
    personRegistry.addPerson(brotherman);
    personRegistry.addPerson(bobT);

    assertEquals(2, personRegistry.getAllPersons().length);
  }

  /**
   * Positive test: verifies that a person can be found by first name.
   */
  @Test
  void getPersonFindsByFirstName() {
    personRegistry.addPerson(brotherman);

    assertArrayEquals(new Person[] {brotherman}, personRegistry.getPerson("Brotherman"));
  }

  /**
   * Positive test: verifies that a person can be found with spacing around the first name.
   */
  @Test
  void getPersonFindsSpaceTrim() {
    personRegistry.addPerson(brotherman);

    assertArrayEquals(new Person[] {brotherman}, personRegistry.getPerson("   Brotherman  "));
  }

  /**
   * Positive test: verifies that a person can be found by last name.
   */
  @Test
  void getPersonFindsByLastName() {
    personRegistry.addPerson(brotherman);

    assertArrayEquals(new Person[] {brotherman}, personRegistry.getPerson("Testern"));
  }

  /**
   * Positive test: verifies that a person can be found with spacing around the last name.
   */
  @Test
  void getPersonFindsByLastNameSpaceTrim() {
    personRegistry.addPerson(brotherman);

    assertArrayEquals(new Person[] {brotherman}, personRegistry.getPerson(" Testern"));
  }

  /**
   * Positive test: verifies that a person can be found by full name.
   */
  @Test
  void getPersonFindsByFullName() {
    personRegistry.addPerson(new Person("Peter", "Oppland"));
    personRegistry.addPerson(brotherman);

    assertArrayEquals(new Person[] {brotherman}, personRegistry.getPerson("Brotherman Testern"));
  }

  /**
   * Positive test: verifies that a person can be found with spacing around and inside the full
   * name.
   */
  @Test
  void getPersonFindsByFullNameSpaceTrim() {
    personRegistry.addPerson(brotherman);

    assertArrayEquals(new Person[] {brotherman},
        personRegistry.getPerson(" Brotherman         Testern   "));
  }

  /**
   * Positive test: verifies that the search ignores upper and lower case.
   */
  @Test
  void getPersonIsCaseIgnore() {
    personRegistry.addPerson(new Person("Peter", "Oppland"));
    personRegistry.addPerson(brotherman);

    assertArrayEquals(new Person[] {brotherman}, personRegistry.getPerson("BroTherMan"));
    assertArrayEquals(new Person[] {brotherman}, personRegistry.getPerson("TestERN"));
    assertArrayEquals(new Person[] {brotherman}, personRegistry.getPerson("BroTherMan TestERN"));
    assertArrayEquals(new Person[] {brotherman}, personRegistry.getPerson("BROTHERMAN"));
    assertArrayEquals(new Person[] {brotherman}, personRegistry.getPerson("TESTERN"));
    assertArrayEquals(new Person[] {brotherman}, personRegistry.getPerson("BROTHERMAN TESTERN"));
  }

  /**
   * Positive test: verifies that all matching persons are returned when several persons share the
   * same first name.
   */
  @Test
  void getPersonReturnsAllMatchesWhenFirstNameSame() {
    personRegistry.addPerson(bobT);
    personRegistry.addPerson(bob);

    assertArrayEquals(new Person[] {bobT, bob}, personRegistry.getPerson("Bob"));
  }

  /**
   * Positive test: verifies that all matching persons are returned when several persons share the
   * same last name.
   */
  @Test
  void getPersonReturnsAllMatchesWhenLastNameSame() {
    personRegistry.addPerson(new Person("Peter", "Oppland"));
    personRegistry.addPerson(brotherman);
    personRegistry.addPerson(bobT);

    assertArrayEquals(new Person[] {brotherman, bobT}, personRegistry.getPerson("Testern"));
  }

  /**
   * Positive test: verifies that all matching persons are returned when several persons share the
   * same first name and the search is by full name.
   */
  @Test
  void getPersonReturnsAllMatchesWhenFullNameShareFirstName() {
    personRegistry.addPerson(new Person("Peter", "Oppland"));
    personRegistry.addPerson(bob);
    personRegistry.addPerson(bobT);

    assertArrayEquals(new Person[] {bob, bobT}, personRegistry.getPerson("Bob Bobsen"));
  }

  /**
   * Positive test: verifies that all matching persons are returned when several persons share the
   * same last name and the search is by full name.
   */
  @Test
  void getPersonReturnsAllMatchesWhenFullNameShareLastName() {
    personRegistry.addPerson(new Person("Peter", "Oppland"));
    personRegistry.addPerson(brotherman);
    personRegistry.addPerson(bobT);

    assertArrayEquals(new Person[] {brotherman, bobT},
        personRegistry.getPerson("Brotherman Testern"));
  }

  /**
   * Positive test: verifies that a person with the same first and last name is only returned once.
   */
  @Test
  void getPersonSameFirstLastNameReturnsOne() {
    Person bobbo = new Person("Bob", "Bob");
    personRegistry.addPerson(bobbo);

    assertArrayEquals(new Person[] {bobbo}, personRegistry.getPerson("bob"));
  }

  /**
   * Negative test: verifies that a search that only partly matches returns an empty array.
   */
  @Test
  void getPersonPartlyReturnsEmptyArray() {
    personRegistry.addPerson(brotherman);

    assertArrayEquals(new Person[] {}, personRegistry.getPerson("Bro"));
  }

  /**
   * Negative test: verifies that searching for an unknown name returns an empty array.
   */
  @Test
  void getPersonWithUnknownNameReturnsEmptyArray() {
    personRegistry.addPerson(bob);

    assertArrayEquals(new Person[] {}, personRegistry.getPerson("Brotherman"));
  }

  /**
   * Negative test: verifies that searching for an unknown full name returns an empty array.
   */
  @Test
  void getPersonWithUnknownFullNameReturnsEmptyArray() {
    personRegistry.addPerson(bob);

    assertArrayEquals(new Person[] {}, personRegistry.getPerson("ABC def"));
  }

  /**
   * Negative test: verifies that searching in an empty registry returns an empty array.
   */
  @Test
  void getPersonOnEmptyRegistryReturnsEmptyArray() {
    assertArrayEquals(new Person[] {}, personRegistry.getPerson("Bob"));
  }

  /**
   * Negative test: verifies that searching with null returns an empty array.
   */
  @Test
  void getPersonWithNullReturnsEmptyArray() {
    personRegistry.addPerson(bob);

    assertArrayEquals(new Person[] {}, personRegistry.getPerson(null));
  }

  /**
   * Negative test: verifies that searching with a blank string returns an empty array.
   */
  @Test
  void getPersonWithBlankReturnsEmptyArray() {
    personRegistry.addPerson(bob);

    assertArrayEquals(new Person[] {}, personRegistry.getPerson(" "));
    assertArrayEquals(new Person[] {}, personRegistry.getPerson(""));
  }
}
