package no.ntnu.idatt1003.hikeseeker.model;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import no.ntnu.idatt1003.hikeseeker.model.Activity;
import no.ntnu.idatt1003.hikeseeker.model.Group;
import no.ntnu.idatt1003.hikeseeker.model.Person;

/**
 * Unit tests for the {@link Activity} class.
 *
 * <p>
 * Note: The basic structure of this test class was suggested by an AI assistant
 * (Claude) and then reviewed and adapted by me.
 * </p>
 */
public class ActivityTest {

  private Group group;
  private Group group2;
  private Person person;
  private LocalDateTime time;

  /**
   * Creates two groups, a person without a group and a fixed time before each
   * test.
   *
   * <p>
   * A fixed {@link LocalDateTime} is used instead of {@code now()} so the tests
   * give the same result every time they run.
   * </p>
   */
  @BeforeEach
  void setUp() {
    group = new Group("Group");
    group2 = new Group("Group2");
    person = new Person("Brotherman", "Testern");
    time = LocalDateTime.of(2026, 10, 1, 12, 0);
  }

  /**
   * Positive test: verifies that an activity created with valid input returns
   * the correct values.
   */
  @Test
  void validateReturnALlTrue() {
    group.addMember(person);

    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, person, person.getHikeGroup());

    assertEquals("Fjelltur", activity.getTitle());
    assertEquals("Fin tur", activity.getDescription());
    assertEquals(10, activity.getDistanceKm());
    assertEquals(7, activity.getRating());
    assertEquals(time, activity.getTimeRegistered());
    assertEquals(person, activity.getLogger());
    assertEquals(group, activity.getGroup());

    assertTrue(activity.hasGroup());
    assertTrue(activity.canSeeActivity(person));
  }

  /**
   * Positive test: verifies that an activity without a group returns null from
   * getGroup and false from hasGroup, for both constructors.
   */
  @Test
  void returTrueNoGroup() {
    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, person, person.getHikeGroup());
    Activity activity2 = new Activity("Fjelltur", "Fin tur", 10, 7, time, person);

    assertNull(activity.getGroup());
    assertFalse(activity.hasGroup());

    assertNull(activity2.getGroup());
    assertFalse(activity2.hasGroup());
  }

  /**
   * Positive test: verifies that people added with addCanSeeActivity can see
   * the activity.
   */
  @Test
  void returnTrueSeeActicty() {
    Person brotherman = new Person("Brotherman", "Testern");

    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, brotherman.getHikeGroup());

    Person bob = new Person("Bob", "Bobsen");
    Person peter = new Person("Peter", "Oppland");

    activity.addCanSeeActivity(bob);
    activity.addCanSeeActivity(peter);

    assertTrue(activity.canSeeActivity(brotherman));
    assertTrue(activity.canSeeActivity(bob));
    assertTrue(activity.canSeeActivity(peter));
  }

  /**
   * Positive test: verifies that people who are not the logger and not added
   * cannot see an activity without a group.
   */
  @Test
  void returnFalseSeeActictyNotShared() {
    Person brotherman = new Person("Brotherman", "Testern");

    Person bob = new Person("Bob", "Bobsen", group);

    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, brotherman.getHikeGroup());

    Person peter = new Person("Peter", "Oppland");

    assertFalse(activity.canSeeActivity(bob));
    assertFalse(activity.canSeeActivity(peter));
  }

  /**
   * Positive test: verifies that all group members can see the activity,
   * including someone who joins the group after it was created.
   */
  @Test
  void returnTrueSeeActictyShared() {
    Person brotherman = new Person("Brotherman", "Testern", group);
    Person bob = new Person("Bob", "Bobsen", group);
    Person peter = new Person("Peter", "Oppland", group);

    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, brotherman.getHikeGroup());
    activity.setShared(true);

    Person cal = new Person("Cal", "Tango", group);

    assertTrue(activity.canSeeActivity(brotherman));
    assertTrue(activity.canSeeActivity(bob));
    assertTrue(activity.canSeeActivity(peter));
    assertTrue(activity.canSeeActivity(cal));
  }

  /**
   * Positive test: verifies that people in another group, or in no group,
   * cannot see the activity.
   */
  @Test
  void returnFalseSeeActictyNotAdded() {
    Person brotherman = new Person("Brotherman", "Testern", group);

    Person bob = new Person("Bob", "Bobsen", group2);
    Person peter = new Person("Peter", "Oppland");

    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, brotherman.getHikeGroup());

    assertFalse(activity.canSeeActivity(bob));
    assertFalse(activity.canSeeActivity(peter));
  }

  // /**
  // * Positive test: verifies that a member who was in the group when the
  // * activity was created can still see it after switching group.
  // */
  // @Test
  // void returnTrueSeeActivityAfterSwitchGroup() {
  // Person brotherman = new Person("Brotherman", "Testern", group);

  // Person bob = new Person("Bob", "Bobsen", group);
  // Person peter = new Person("Peter", "Oppland", group);

  // Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time,
  // brotherman, brotherman.getHikeGroup());
  // activity.setShared(true);

  // assertTrue(activity.canSeeActivity(bob));
  // assertTrue(activity.canSeeActivity(peter));

  // Person cal = new Person("Cal", "Tango");
  // group.addMember(cal);

  // group2.addMember(peter);

  // assertTrue(activity.canSeeActivity(cal));
  // assertTrue(activity.canSeeActivity(peter));
  // }

  // /**
  // * Positive test: verifies that a person who joins the group after the
  // * activity loses access when they leave the group.
  // */
  // @Test
  // void returnFalseSeeActivityAfterHikeAfterSwitchGroup() {
  // Person brotherman = new Person("Brotherman", "Testern", group);
  // Person bob = new Person("Bob", "Bobsen", group);

  // Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time,
  // brotherman, brotherman.getHikeGroup());
  // activity.setShared(true);

  // Person cal = new Person("Cal", "Tango");
  // group.addMember(cal);
  // group2.addMember(bob);

  // assertTrue(activity.canSeeActivity(cal));
  // assertTrue(activity.canSeeActivity(bob));

  // // cal was not a member when the hike was done, so he loses access
  // group2.addMember(cal);
  // assertFalse(activity.canSeeActivity(cal));
  // }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when the
   * title is empty.
   */
  @Test
  void throwWhenTitleEmpty() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Activity("", "Fin tur", 10, 10, time, person));

    assertEquals("Activity must have a title!", exception.getMessage());
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when the
   * title is null.
   */
  @Test
  void throwWhenTitleNull() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Activity(null, "Fin tur", 10, 10, time, person));

    assertEquals("Activity must have a title!", exception.getMessage());
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when the
   * description is empty.
   */
  @Test
  void throwWhenDescriptionEmpty() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Activity("Fjelltur", "", 10, 10, time, person));

    assertEquals("Activity must have a description!", exception.getMessage());
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when the
   * description is null.
   */
  @Test
  void throwWhenDescriptionNull() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Activity("Fjelltur", null, 10, 10, time, person));

    assertEquals("Activity must have a description!", exception.getMessage());
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when the
   * distance is zero or negative.
   */
  @Test
  void throwWhenDistanceNegative() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Activity("Fjelltur", "Fin tur", 0, 10, time, person));
    assertEquals("Activity must have a valid distance!", exception.getMessage());

    IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class,
        () -> new Activity("Fjelltur", "Fin tur", -10, 10, time, person));
    assertEquals("Activity must have a valid distance!", exception2.getMessage());
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when the
   * distance is NaN.
   */
  @Test
  void throwWhenDistanceNaN() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Activity("Fjelltur", "Fin tur", Double.NaN, 10, time, person));

    assertEquals("Activity must have a valid distance!", exception.getMessage());
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when the
   * distance is infinite.
   */
  @Test
  void throwWhenDistanceInf() {
    Double distance = (1.0 / 0.0);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Activity("Fjelltur", "Fin tur", distance, 10, time, person));

    assertEquals("Activity must have a valid distance!", exception.getMessage());
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when the
   * rating is under 1.
   */
  @Test
  void throwWhenRatingIsUnder() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Activity("Fjelltur", "Fin tur", 10, 0, time, person));

    assertEquals("Rating must be between 1 and 10!", exception.getMessage());
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when the
   * rating is over 10.
   */
  @Test
  void throwWhenRatingIsOver() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Activity("Fjelltur", "Fin tur", 10, 11, time, person));

    assertEquals("Rating must be between 1 and 10!", exception.getMessage());
  }

}
