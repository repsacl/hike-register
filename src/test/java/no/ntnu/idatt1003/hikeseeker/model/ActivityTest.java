package no.ntnu.idatt1003.hikeseeker.model;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// import no.ntnu.idatt1003.hikeseeker.model.Activity;
// import no.ntnu.idatt1003.hikeseeker.model.Group;
// import no.ntnu.idatt1003.hikeseeker.model.Person;

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
  private Person brotherman;
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
    brotherman = new Person("Brotherman", "Testern");
    time = LocalDateTime.of(2026, 10, 1, 12, 0);
  }

  /**
   * Positive test: verifies that an activity created with valid input returns
   * the correct values.
   * Returns title
   * Returns description
   * Returns distance
   * Returns rating
   * Returns time registrered
   * Returns logger
   * Returns group
   * Returns shared
   * Returns hasGroup
   * Returns canSeeActivity
   */
  @Test
  void validateReturnALlTrue() {
    group.addMember(brotherman);

    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, brotherman.getHikeGroup());

    assertEquals("Fjelltur", activity.getTitle());
    assertEquals("Fin tur", activity.getDescription());
    assertEquals(10, activity.getDistanceKm());
    assertEquals(7, activity.getRating());
    assertEquals(time, activity.getTimeRegistered());
    assertEquals(brotherman, activity.getLogger());
    assertEquals(group, activity.getGroup());
    assertFalse(activity.getShared());

    assertTrue(activity.hasGroup());
    assertTrue(activity.canSeeActivity(brotherman));
  }

  /**
   * Positive test: verifies that an activity without a group and person without a
   * group returns null from
   * getGroup and false from hasGroup, for both constructors.
   */
  @Test
  void returnTrueNoGroup() {
    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, brotherman.getHikeGroup());
    Activity activity2 = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman);

    assertNull(activity.getGroup());
    assertFalse(activity.hasGroup());

    assertNull(activity2.getGroup());
    assertFalse(activity2.hasGroup());
  }

  /**
   * Positive test: verifies that an activitys {@code shared} is set to false by
   * standard.
   */
  @Test
  void returnFlaseSharedSetToFalseStandard() {
    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, brotherman.getHikeGroup());

    assertFalse(activity.getShared());
  }

  /**
   * Positive test: verifies that an activitys {@code shared} is set to true by
   * using {@code setShared(true)} method when activity is associated wit a group.
   */
  @Test
  void returnTrueSharedSetToTrue() {
    group.addMember(brotherman);
    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, brotherman.getHikeGroup());
    activity.setShared(true);

    assertTrue(activity.getShared());
  }

  /**
   * Negative test: verifies that a person who wants shares his activty, but the
   * activtys is not associated with a group, throws an error.
   */
  @Test
  void throwWhenSetSharedTrueNoGroup() {
    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time,
        brotherman, brotherman.getHikeGroup());

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> activity.setShared(true));

    assertEquals("Can't share activity, no group is associated with it!",
        exception.getMessage());
  }

  /**
   * Positive test: verifies that people added with {@code addCanSeeActivity()}
   * can see the activity added to.
   */
  @Test
  void returnTrueCanSeeActictyAddedNoGroup() {
    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, brotherman.getHikeGroup());
    Person bob = new Person("Bob", "Bobsen");

    // brotherman shared activity with bob
    activity.addCanSeeActivity(bob);
    assertTrue(activity.canSeeActivity(bob));
  }

  /**
   * Positive test: verifies that people with different groups added with
   * {@code addCanSeeActivity()}, and shared is false,
   * can still see the activty added to.
   */
  @Test
  void returnTrueCanSeeActictyAddedDifferentGroupAndSharedFalse() {
    group.addMember(brotherman);
    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, brotherman.getHikeGroup());

    Person bob = new Person("Bob", "Bobsen", group2);

    activity.addCanSeeActivity(bob);
    assertTrue(activity.canSeeActivity(bob));
  }

  /**
   * Positive test: verifies that people with different groups added with
   * {@code addCanSeeActivity()}, and shared is true,
   * can still see the activty added to.
   */
  @Test
  void returnTrueCanSeeActictyAddedDifferentGroupAndSharedTrue() {
    group.addMember(brotherman);
    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, brotherman.getHikeGroup());
    activity.setShared(true);

    Person bob = new Person("Bob", "Bobsen", group2);

    activity.addCanSeeActivity(bob);
    assertTrue(activity.canSeeActivity(bob));
  }

  /**
   * Positive test: verifies that people who are not the logger, not added, or no
   * group
   * cannot see an activity that is associated without their group.
   */
  @Test
  void returnFalseCanSeeActictyNotSharedNotAdded() {
    group.addMember(brotherman);

    Person bob = new Person("Bob", "Bobsen", group);
    Person peter = new Person("Peter", "Oppland");

    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, brotherman.getHikeGroup());

    assertFalse(activity.canSeeActivity(bob));
    assertFalse(activity.canSeeActivity(peter));
  }

  /**
   * Positive test: verifies that a member looses a shared activity when shared is
   * set to false.
   */
  @Test
  void returnFalseCanSeeActivitySharedSetToFalseAfterTrue() {
    group.addMember(brotherman);

    Person bob = new Person("Bob", "Bobsen", group);

    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, brotherman.getHikeGroup());

    activity.setShared(true);
    assertTrue(activity.canSeeActivity(bob));

    activity.setShared(false);
    assertFalse(activity.canSeeActivity(bob));
  }

  /**
   * Negative test: verifies that a IllegalArgumentException is thrown
   * when a person added with {@code addCanSeeActivity()} is null.
   */
  @Test
  void throwWhenAddCanSeeActivityNull() {
    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time,
        brotherman, brotherman.getHikeGroup());

    NullPointerException exception = assertThrows(NullPointerException.class,
        () -> activity.addCanSeeActivity(null));

    assertEquals("Person added cannot be null!", exception.getMessage());
  }

  /**
   * Positive test: verifies that all group members can see the activity,
   * including members added later.
   */
  @Test
  void returnTrueCanSeeActictySharedAndLaterMembers() {
    group.addMember(brotherman);
    Person bob = new Person("Bob", "Bobsen", group);

    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, brotherman.getHikeGroup());
    activity.setShared(true);

    Person peter = new Person("Peter", "Oppland", group);

    assertTrue(activity.canSeeActivity(bob));
    assertTrue(activity.canSeeActivity(peter));
  }

  /**
   * Positive test: verifies that a member who was in the group, could se
   * shared activtys, but after switching, the member looses them.
   */
  @Test
  void returnFalseCanSeeActivityAfterSwitchGroup() {
    group.addMember(brotherman);

    Person bob = new Person("Bob", "Bobsen", group);

    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, brotherman.getHikeGroup());
    activity.setShared(true);

    assertTrue(activity.canSeeActivity(bob));

    group2.addMember(bob);

    assertFalse(activity.canSeeActivity(bob));
  }

  /**
   * Positive test: verifies that a member of a group, can se still see
   * shared activities, even if on of the activities logger switched group.
   * Logger can also see it's activitys even if them are associated with another
   * group.
   */
  @Test
  void returnTrueCanSeeActivityShareAfterLoggerSwitchOrLeave() {
    group.addMember(brotherman);
    Person bob = new Person("Bob", "Bobsen", group);

    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, brotherman.getHikeGroup());
    activity.setShared(true);

    assertTrue(activity.canSeeActivity(bob));
    assertTrue(activity.canSeeActivity(brotherman));

    group2.addMember(brotherman);
    assertTrue(activity.canSeeActivity(bob));
    assertTrue(activity.canSeeActivity(brotherman));
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when the
   * title is empty or blank.
   */
  @Test
  void throwWhenTitleEmpty() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Activity("", "Fin tur", 10, 10, time, brotherman));

    IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class,
        () -> new Activity("  ", "Fin tur", 10, 10, time, brotherman));

    assertEquals("Activity must have a title!", exception.getMessage());
    assertEquals("Activity must have a title!", exception2.getMessage());
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when the
   * title is null.
   */
  @Test
  void throwWhenTitleNull() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Activity(null, "Fin tur", 10, 10, time, brotherman));

    assertEquals("Activity must have a title!", exception.getMessage());
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when the
   * description is empty or blank.
   */
  @Test
  void throwWhenDescriptionEmpty() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Activity("Fjelltur", "", 10, 10, time, brotherman));

    IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class,
        () -> new Activity("Fjelltur", "  ", 10, 10, time, brotherman));

    assertEquals("Activity must have a description!", exception.getMessage());
    assertEquals("Activity must have a description!", exception2.getMessage());
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when the
   * description is null.
   */
  @Test
  void throwWhenDescriptionNull() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Activity("Fjelltur", null, 10, 10, time, brotherman));

    assertEquals("Activity must have a description!", exception.getMessage());
  }

  /**
   * Positive test: verifies that a small distance is allowed as long it's
   * positive
   */
  @Test
  void DistanceSmallisAllowed() {
    Activity activity = new Activity("Fjelltur", "Fin tur", .1, 7, time, brotherman, brotherman.getHikeGroup());

    assertEquals(.1, activity.getDistanceKm());
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when the
   * distance is zero and/or negative.
   */
  @Test
  void throwWhenDistanceZeroAndNegative() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Activity("Fjelltur", "Fin tur", 0, 10, time, brotherman));
    assertEquals("Activity must have a valid distance!", exception.getMessage());

    IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class,
        () -> new Activity("Fjelltur", "Fin tur", -10, 10, time, brotherman));
    assertEquals("Activity must have a valid distance!", exception2.getMessage());
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when the
   * distance is NaN.
   */
  @Test
  void throwWhenDistanceNaN() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Activity("Fjelltur", "Fin tur", Double.NaN, 10, time, brotherman));

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
        () -> new Activity("Fjelltur", "Fin tur", distance, 10, time, brotherman));

    assertEquals("Activity must have a valid distance!", exception.getMessage());
  }

  /**
   * Positive test: verifies that 1 as a rating is allowed as long it's
   * positive
   */
  @Test
  void RatingOneAllowed() {
    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 1, time, brotherman, brotherman.getHikeGroup());

    assertEquals(1, activity.getRating());
  }

  /**
   * Positive test: verifies that 10 as a rating is allowed.
   */
  @Test
  void RatingTenAllowed() {
    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 10, time, brotherman, brotherman.getHikeGroup());

    assertEquals(10, activity.getRating());
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when the
   * rating is under 1.
   */
  @Test
  void throwWhenRatingIsUnderOne() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Activity("Fjelltur", "Fin tur", 10, 0, time, brotherman));

    IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class,
        () -> new Activity("Fjelltur", "Fin tur", 10, -1, time, brotherman));

    assertEquals("Rating must be between 1 and 10!", exception.getMessage());
    assertEquals("Rating must be between 1 and 10!", exception2.getMessage());
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when the
   * rating is over 10.
   */
  @Test
  void throwWhenRatingIsOver() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Activity("Fjelltur", "Fin tur", 10, 11, time, brotherman));

    assertEquals("Rating must be between 1 and 10!", exception.getMessage());
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when the
   * timeRegistrered is null.
   */
  @Test
  void throwWhenTimeRegistreredNull() {
    NullPointerException exception = assertThrows(NullPointerException.class,
        () -> new Activity("Fjelltur", "Fin tur", 10, 7, null, brotherman));

    assertEquals("Activity must have a time registered!", exception.getMessage());
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when the
   * logger is null.
   */
  @Test
  void throwWhenLoggerNull() {
    NullPointerException exception = assertThrows(NullPointerException.class,
        () -> new Activity("Fjelltur", "Fin tur", 10, 7, time, null));

    assertEquals("Activity must have a logger!", exception.getMessage());
  }

}
