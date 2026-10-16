package no.ntnu.idatt1003.hikeregistry.model;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Activity} class.
 *
 * <p>Note: The basic structure of this test class was suggested by an AI assistant (Claude)
 * and then reviewed and adapted by me.
 */
class ActivityTest {

  /**
   * Positive test: verifies that an activity created with valid input
   * stores and returns the correct title and logger.
   *
   * <p>A fixed {@link LocalDateTime} is used instead of {@code now()} so the test
   * gives the same result every time it runs.
   */
  @Test
  void constructor_validInput_storesTitleAndLogger() {
    Person person = new Person("Ola", "Nordmann", "fjellgjengen");
    LocalDateTime time = LocalDateTime.of(2026, 10, 1, 12, 0);

    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, person, 1);

    assertEquals("Fjelltur", activity.getTitle());
    assertEquals(person, activity.getLogger());
  }

  @Test
  void validateRating() {
    Person person = new Person("Ola", "Nordmann", "fjellgjengen");
    LocalDateTime time = LocalDateTime.of(2026, 10, 1, 12, 0);
    
    IllegalArgumentException exception = assertThrows(
      IllegalArgumentException.class,
      () -> new Activity("Fjelltur", "Fin tur", 10, 11, time, person, 1));
      assertEquals("Rating must be between 1 and 10!", exception.getMessage());
    }

}
