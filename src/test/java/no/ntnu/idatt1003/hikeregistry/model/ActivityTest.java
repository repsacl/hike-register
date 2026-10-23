package no.ntnu.idatt1003.hikeregistry.model;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Activity} class.
 *
 * <p>
 * Note: The basic structure of this test class was suggested by an AI assistant
 * (Claude)
 * and then reviewed and adapted by me.
 */
public class ActivityTest {

  /**
   * Positive test: verifies that an activity created with valid input
   * stores and returns the correct title and logger.
   *
   * <p>
   * A fixed {@link LocalDateTime} is used instead of {@code now()} so the test
   * gives the same result every time it runs.
   */
  @Test
  void validateReturnALlTrue() {
    Group group = new Group("testern");
    Person person = new Person("Brotherman", "Testern", group);
    LocalDateTime time = LocalDateTime.of(2026, 10, 1, 12, 0);

    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, person, person.getHikeGroup(), 1);

    assertEquals("Fjelltur", activity.getTitle());
    assertEquals("Fin tur", activity.getDescription());
    assertEquals(10, activity.getDistanceKm());
    assertEquals(7, activity.getRating());
    assertEquals(time, activity.getTimeRegistered());
    assertEquals(person, activity.getLogger());
    assertEquals(group, activity.getGroup());
  }

  /**
   * Negative test: verifies that a IllegalArgumentException throws when title is
   * empty
   */
  @Test
  void throwWhenTitleEmpty() {
    Person person = new Person("Brotherman", "Testern");
    LocalDateTime time = LocalDateTime.of(2026, 10, 1, 12, 0);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Activity("", "Fin tur", 10, 10, time, person, 0));

    assertEquals("Activity must have a title!", exception.getMessage());
  }

  /**
   * Negative test: verifies that a IllegalArgumentException throws when title is
   * null
   */
  @Test
  void throwWhenTitleNull() {
    Person person = new Person("Brotherman", "Testern");
    LocalDateTime time = LocalDateTime.of(2026, 10, 1, 12, 0);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Activity(null, "Fin tur", 10, 10, time, person, 0));

    assertEquals("Activity must have a title!", exception.getMessage());
  }

  /**
   * Negative test: verifies that a IllegalArgumentException throws when
   * description is empty
   */
  @Test
  void throwWhenDescriptionEmpty() {
    Person person = new Person("Brotherman", "Testern");
    LocalDateTime time = LocalDateTime.of(2026, 10, 1, 12, 0);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Activity("Fjelltur", "", 10, 10, time, person, 0));

    assertEquals("Activity must have a description!", exception.getMessage());
  }

  /**
   * Negative test: verifies that a IllegalArgumentException throws when
   * description is null
   */
  @Test
  void throwWhenDescriptionNull() {
    Person person = new Person("Brotherman", "Testern");
    LocalDateTime time = LocalDateTime.of(2026, 10, 1, 12, 0);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Activity("Fjelltur", null, 10, 10, time, person, 0));

    assertEquals("Activity must have a description!", exception.getMessage());
  }

  /**
   * Negative test: verifies that a IllegalArgumentException throws when distance
   * is negative or zero
   */
  @Test
  void throwWhenDistanceNegative() {
    Person person = new Person("Brotherman", "Testern");
    LocalDateTime time = LocalDateTime.of(2026, 10, 1, 12, 0);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Activity("Fjelltur", "Fin tur", 0, 10, time, person, 0));
    assertEquals("Activity must have a valid distance!", exception.getMessage());

    IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class,
        () -> new Activity("Fjelltur", "Fin tur", -10, 10, time, person, 0));
    assertEquals("Activity must have a valid distance!", exception2.getMessage());
  }

  /**
   * Negative test: verifies that a IllegalArgumentException throws when distance
   * is NaN
   */
  @Test
  void throwWhenDistanceNaN() {
    Person person = new Person("Brotherman", "Testern");
    LocalDateTime time = LocalDateTime.of(2026, 10, 1, 12, 0);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Activity("Fjelltur", "Fin tur", Double.NaN, 10, time, person, 0));

    assertEquals("Activity must have a valid distance!", exception.getMessage());
  }

  /**
   * Negative test: verifies that a IllegalArgumentException throws when rating is
   * under 1
   */
  @Test
  void throwWhenRatingIsUnder() {
    Person person = new Person("Brotherman", "Testern");
    LocalDateTime time = LocalDateTime.of(2026, 10, 1, 12, 0);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Activity("Fjelltur", "Fin tur", 10, 0, time, person, 0));

    assertEquals("Rating must be between 1 and 10!", exception.getMessage());
  }

  /**
   * Negative test: verifies that a IllegalArgumentException throws when rating is
   * over 10
   */
  @Test
  void throwWhenRatingIsOver() {
    Person person = new Person("Brotherman", "Testern");
    LocalDateTime time = LocalDateTime.of(2026, 10, 1, 12, 0);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Activity("Fjelltur", "Fin tur", 10, 11, time, person, 11));

    assertEquals("Rating must be between 1 and 10!", exception.getMessage());
  }

}
