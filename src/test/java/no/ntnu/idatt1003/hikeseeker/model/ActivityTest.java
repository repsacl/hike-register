package no.ntnu.idatt1003.hikeseeker.model;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Activity} class.
 *
 * <p>
 * Note: The basic structure of this test class was suggested by an AI assistant (Claude) and then
 * reviewed and adapted by me.
 * </p>
 */
public class ActivityTest {

  private Group group;
  private Group group2;
  private Person brotherman;
  private LocalDateTime time;

  /**
   * Creates two groups, a person without a group and a fixed time before each test.
   *
   * <p>
   * A fixed {@link LocalDateTime} is used instead of {@code now()} so the tests give the same
   * result every time they run.
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
   * Positive test: verifies that an activity created with valid input returns the correct title,
   * description, distance, rating, time registered, logger, group, shared, hasGroup and
   * canSeeActivity.
   */
  @Test
  void validateReturnAllTrue() {
    group.addMember(brotherman);

    Activity activity =
        new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, brotherman.getHikeGroup());

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
   * Positive test: verifies that an activity logged by a person without a group returns null from
   * getGroup and false from hasGroup, for both constructors.
   */
  @Test
  void returnTrueNoGroup() {
    Activity activity =
        new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, brotherman.getHikeGroup());
    Activity activity2 = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman);

    assertNull(activity.getGroup());
    assertFalse(activity.hasGroup());

    assertNull(activity2.getGroup());
    assertFalse(activity2.hasGroup());
  }

  /**
   * Positive test: verifies that an activity's {@code shared} is set to false by default.
   */
  @Test
  void sharedSetToFalseStandard() {
    Activity activity =
        new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, brotherman.getHikeGroup());

    assertFalse(activity.getShared());
  }

  /**
   * Positive test: verifies that an activity's {@code shared} is set to true by using
   * {@code setShared(true)} when the activity is associated with a group.
   */
  @Test
  void sharedSetToTrue() {
    group.addMember(brotherman);
    Activity activity =
        new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, brotherman.getHikeGroup());
    activity.setShared(true);

    assertTrue(activity.getShared());
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when a person tries to share
   * an activity that is not associated with a group.
   */
  @Test
  void throwWhenSetSharedTrueNoGroup() {
    Activity activity =
        new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, brotherman.getHikeGroup());

    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> activity.setShared(true));

    assertEquals("Can't share activity, no group is associated with it!", exception.getMessage());
  }

  /**
   * Positive test: verifies that a person added with {@code addCanSeeActivity()} can see the
   * activity.
   */
  @Test
  void canSeeActivityAddedNoGroup() {
    Activity activity =
        new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, brotherman.getHikeGroup());
    Person bob = new Person("Bob", "Bobsen");

    // brotherman shared activity with bob
    activity.addCanSeeActivity(bob);
    assertTrue(activity.canSeeActivity(bob));
  }

  /**
   * Positive test: verifies that a person in a different group, added with
   * {@code addCanSeeActivity()}, can see the activity when shared is false.
   */
  @Test
  void canSeeActivityAddedDifferentGroupAndSharedFalse() {
    group.addMember(brotherman);
    Activity activity =
        new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, brotherman.getHikeGroup());

    Person bob = new Person("Bob", "Bobsen", group2);

    activity.addCanSeeActivity(bob);
    assertTrue(activity.canSeeActivity(bob));
  }

  /**
   * Positive test: verifies that a person in a different group, added with
   * {@code addCanSeeActivity()}, can see the activity when shared is true.
   */
  @Test
  void canSeeActivityAddedDifferentGroupAndSharedTrue() {
    group.addMember(brotherman);
    Activity activity =
        new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, brotherman.getHikeGroup());
    activity.setShared(true);

    Person bob = new Person("Bob", "Bobsen", group2);

    activity.addCanSeeActivity(bob);
    assertTrue(activity.canSeeActivity(bob));
  }

  /**
   * Positive test: verifies that persons who are not the logger and have not been added can not see
   * an activity that is not shared, both with and without a group.
   */
  @Test
  void canSeeActivityNotSharedNotAdded() {
    group.addMember(brotherman);

    Person bob = new Person("Bob", "Bobsen", group);
    Person peter = new Person("Peter", "Oppland");

    Activity activity =
        new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, brotherman.getHikeGroup());

    assertFalse(activity.canSeeActivity(bob));
    assertFalse(activity.canSeeActivity(peter));
  }

  /**
   * Positive test: verifies that a member loses a shared activity when shared is set to false.
   */
  @Test
  void canSeeActivitySharedSetToFalseAfterTrue() {
    group.addMember(brotherman);

    Person bob = new Person("Bob", "Bobsen", group);

    Activity activity =
        new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, brotherman.getHikeGroup());

    activity.setShared(true);
    assertTrue(activity.canSeeActivity(bob));

    activity.setShared(false);
    assertFalse(activity.canSeeActivity(bob));
  }

  /**
   * Negative test: verifies that a NullPointerException is thrown when the person added with
   * {@code addCanSeeActivity()} is null.
   */
  @Test
  void throwWhenAddCanSeeActivityNull() {
    Activity activity =
        new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, brotherman.getHikeGroup());

    NullPointerException exception =
        assertThrows(NullPointerException.class, () -> activity.addCanSeeActivity(null));

    assertEquals("Person added cannot be null!", exception.getMessage());
  }

  /**
   * Positive test: verifies that all group members can see the activity, including members added
   * later.
   */
  @Test
  void canSeeActivitySharedAndLaterMembers() {
    group.addMember(brotherman);
    Person bob = new Person("Bob", "Bobsen", group);

    Activity activity =
        new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, brotherman.getHikeGroup());
    activity.setShared(true);

    Person peter = new Person("Peter", "Oppland", group);

    assertTrue(activity.canSeeActivity(bob));
    assertTrue(activity.canSeeActivity(peter));
  }

  /**
   * Positive test: verifies that a member who could see shared activities in the group loses them
   * after leaving or switching group.
   */
  @Test
  void canNotSeeActivityAfterSwitchOrLeaveGroup() {
    group.addMember(brotherman);

    Person bob = new Person("Bob", "Bobsen", group);

    Activity activity =
        new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, brotherman.getHikeGroup());
    activity.setShared(true);

    assertTrue(activity.canSeeActivity(bob));

    bob.leaveGroup();
    assertFalse(activity.canSeeActivity(bob));

    group2.addMember(bob);
    assertFalse(activity.canSeeActivity(bob));
  }

  /**
   * Positive test: verifies that group members can still see a shared activity after the logger
   * leaves or switches group. The logger can also still see their own activity, even if it is
   * associated with another group.
   */
  @Test
  void canSeeActivityShareAfterLoggerSwitchOrLeave() {
    group.addMember(brotherman);
    Person bob = new Person("Bob", "Bobsen", group);

    Activity activity =
        new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, brotherman.getHikeGroup());
    activity.setShared(true);

    assertTrue(activity.canSeeActivity(bob));
    assertTrue(activity.canSeeActivity(brotherman));

    brotherman.leaveGroup();
    assertTrue(activity.canSeeActivity(bob));
    assertTrue(activity.canSeeActivity(brotherman));

    group2.addMember(brotherman);
    assertTrue(activity.canSeeActivity(bob));
    assertTrue(activity.canSeeActivity(brotherman));
  }

  /**
   * Positive test: verifies that a person without a group can not see an activity that is shared
   * with a group, when the person is not the logger and has not been added.
   */
  @Test
  void canNotSeeActivitySharedNoGroup() {
    group.addMember(brotherman);

    Person bob = new Person("Bob", "Bobsen");

    Activity activity =
        new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, brotherman.getHikeGroup());
    activity.setShared(true);

    assertFalse(activity.canSeeActivity(bob));
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when the title is empty or
   * blank.
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
   * Negative test: verifies that an IllegalArgumentException is thrown when the title is null.
   */
  @Test
  void throwWhenTitleNull() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Activity(null, "Fin tur", 10, 10, time, brotherman));

    assertEquals("Activity must have a title!", exception.getMessage());
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when the description is
   * empty or blank.
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
   * Negative test: verifies that an IllegalArgumentException is thrown when the description is
   * null.
   */
  @Test
  void throwWhenDescriptionNull() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Activity("Fjelltur", null, 10, 10, time, brotherman));

    assertEquals("Activity must have a description!", exception.getMessage());
  }

  /**
   * Positive test: verifies that a small distance is allowed as long as it is positive.
   */
  @Test
  void distanceSmallIsAllowed() {
    Activity activity =
        new Activity("Fjelltur", "Fin tur", .1, 7, time, brotherman, brotherman.getHikeGroup());

    assertEquals(.1, activity.getDistanceKm());
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when the distance is zero or
   * negative.
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
   * Negative test: verifies that an IllegalArgumentException is thrown when the distance is NaN.
   */
  @Test
  void throwWhenDistanceNaN() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Activity("Fjelltur", "Fin tur", Double.NaN, 10, time, brotherman));

    assertEquals("Activity must have a valid distance!", exception.getMessage());
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when the distance is
   * infinite.
   */
  @Test
  void throwWhenDistanceInf() {
    Double distance = (1.0 / 0.0);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Activity("Fjelltur", "Fin tur", distance, 10, time, brotherman));

    assertEquals("Activity must have a valid distance!", exception.getMessage());
  }

  /**
   * Positive test: verifies that 1 is allowed as a rating.
   */
  @Test
  void ratingOneAllowed() {
    Activity activity =
        new Activity("Fjelltur", "Fin tur", 10, 1, time, brotherman, brotherman.getHikeGroup());

    assertEquals(1, activity.getRating());
  }

  /**
   * Positive test: verifies that 10 as a rating is allowed.
   */
  @Test
  void ratingTenAllowed() {
    Activity activity =
        new Activity("Fjelltur", "Fin tur", 10, 10, time, brotherman, brotherman.getHikeGroup());

    assertEquals(10, activity.getRating());
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when the rating is under 1.
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
   * Negative test: verifies that an IllegalArgumentException is thrown when the rating is over 10.
   */
  @Test
  void throwWhenRatingIsOver() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new Activity("Fjelltur", "Fin tur", 10, 11, time, brotherman));

    assertEquals("Rating must be between 1 and 10!", exception.getMessage());
  }

  /**
   * Negative test: verifies that a NullPointerException is thrown when timeRegistered is null.
   */
  @Test
  void throwWhenTimeRegisteredNull() {
    NullPointerException exception = assertThrows(NullPointerException.class,
        () -> new Activity("Fjelltur", "Fin tur", 10, 7, null, brotherman));

    assertEquals("Activity must have a time registered!", exception.getMessage());
  }

  /**
   * Negative test: verifies that a NullPointerException is thrown when the logger is null.
   */
  @Test
  void throwWhenLoggerNull() {
    NullPointerException exception = assertThrows(NullPointerException.class,
        () -> new Activity("Fjelltur", "Fin tur", 10, 7, time, null));

    assertEquals("Activity must have a logger!", exception.getMessage());
  }

}
