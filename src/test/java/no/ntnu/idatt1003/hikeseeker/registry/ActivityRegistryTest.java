package no.ntnu.idatt1003.hikeseeker.registry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import no.ntnu.idatt1003.hikeseeker.model.Activity;
import no.ntnu.idatt1003.hikeseeker.model.Group;
import no.ntnu.idatt1003.hikeseeker.model.Person;

/**
 * Unit tests for the {@link ActivityRegistry} class.
 *
 * <p>
 * Note: The basic structure of this test class was suggested by an AI assistant (Claude) and then
 * reviewed and adapted by me.
 * </p>
 */
public class ActivityRegistryTest {

  private ActivityRegistry activityRegistry;
  private Group group;
  private LocalDateTime time;

  /**
   * Creates a fresh, empty registry, a group and a fixed time before each test.
   */
  @BeforeEach
  void setUp() {
    activityRegistry = new ActivityRegistry();
    group = new Group("Group");
    time = LocalDateTime.of(2026, 10, 1, 12, 0);
  }

  /**
   * Positive test: verifies that adding an activity stores it in the registry and increases the
   * logger's hikes registered.
   */
  @Test
  void addActivityStoresActivityAndIncreasesLoggerCount() {
    Person brotherman = new Person("Brotherman", "Testern");

    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman);

    activityRegistry.addActivity(activity);

    assertArrayEquals(new Activity[] {activity}, activityRegistry.getAllActivities(brotherman));
    assertEquals(1, brotherman.getHikesRegistered());
  }

  /**
   * Positive test: verifies that adding an activity with a group stores it in the registry and
   * increases the group's hikes registered.
   */
  @Test
  void addActivityStoresActivityAndIncreasesGroupCount() {
    Person brotherman = new Person("Brotherman", "Testern", group);

    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, group);

    activityRegistry.addActivity(activity);

    assertArrayEquals(new Activity[] {activity}, activityRegistry.getAllActivities(brotherman));
    assertEquals(1, group.getHikesRegistered());
  }

  /**
   * Positive test: verifies that a person sees the group's activities and their own private
   * activities, but not the private activities of other group members.
   */
  @Test
  void getAllActivitiesGroupMemberCanSeeAndPrivateActivity() {
    Person brotherman = new Person("Brotherman", "Testern", group);
    Person bob = new Person("Bob", "Bobsen", group);

    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, group);
    Activity activity2 = new Activity("Dårligtur", "Dårlig tur", 5, 2, time, brotherman, group);
    Activity activity3 = new Activity("En kort tur", "Turen var kort og kjedelig. Bare skog :(", 3,
        3, time.minusDays(2).minusHours(5), bob);

    activity.setShared(true);
    activity2.setShared(true);
    activity3.setShared(false);

    activityRegistry.addActivity(activity);
    activityRegistry.addActivity(activity2);
    activityRegistry.addActivity(activity3);

    assertArrayEquals(new Activity[] {activity3, activity, activity2},
        activityRegistry.getAllActivities(bob));
    assertArrayEquals(new Activity[] {activity, activity2},
        activityRegistry.getAllActivities(brotherman));
    assertEquals(2, group.getHikesRegistered());
  }

  /**
   * Positive test: verifies that an empty array is returned when no activities have been added.
   */
  @Test
  void getEmptyArrayIfNoActivitiesAdded() {
    Person bob = new Person("Bob", "Bobsen");

    assertArrayEquals(new Activity[] {}, activityRegistry.getAllActivities(bob));
  }

  /**
   * Positive test: verifies that a group member can find a group activity by title, and that the
   * search ignores upper and lower case.
   */
  @Test
  void getActivityTitleCanSeeGroupMember() {
    Person brotherman = new Person("Brotherman", "Testern", group);
    Person bob = new Person("Bob", "Bobsen", group);

    Activity activity = new Activity("Dårligtur", "Dårlig tur", 5, 2, time, brotherman, group);
    Activity activity2 = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, group);

    // adds two activities to search through
    activity2.setShared(true);
    activity.setShared(true);
    activityRegistry.addActivity(activity);
    activityRegistry.addActivity(activity2);

    assertArrayEquals(new Activity[] {activity2},
        activityRegistry.getActivityTitle("Fjelltur", bob));
    assertArrayEquals(new Activity[] {activity2},
        activityRegistry.getActivityTitle("FJELLTUR", brotherman));
  }

  /**
   * Positive test: verifies that a person outside the group gets an empty array when searching for
   * the title of a group activity.
   */
  @Test
  void getEmptyActivityTitleCantSeeGroup() {
    Person brotherman = new Person("Brotherman", "Testern", group);
    Person bob = new Person("Bob", "Bobsen");

    Activity activity = new Activity("Dårligtur", "Dårlig tur", 5, 2, time, brotherman, group);
    Activity activity2 = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, group);

    activityRegistry.addActivity(activity);
    activityRegistry.addActivity(activity2);

    assertArrayEquals(new Activity[] {}, activityRegistry.getActivityTitle("Fjelltur", bob));
    assertArrayEquals(new Activity[] {activity2},
        activityRegistry.getActivityTitle("Fjelltur", brotherman));
  }

  /**
   * Positive test: verifies that all activities with the same title are returned.
   */
  @Test
  void getActivityTitleSameTitle() {
    Person brotherman = new Person("Brotherman", "Testern", group);

    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman);
    Activity activity2 = new Activity("Fjelltur", "Dårlig tur", 6, 3, time, brotherman);

    activityRegistry.addActivity(activity);
    activityRegistry.addActivity(activity2);

    assertArrayEquals(new Activity[] {activity, activity2},
        activityRegistry.getActivityTitle("fjelltur", brotherman));
  }

  /**
   * Positive test: verifies that an empty array is returned when no activity has the given title.
   */
  @Test
  void getActivityTitleUnknownTitleEmpty() {
    Person brotherman = new Person("Brotherman", "Testern", group);

    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman);

    activityRegistry.addActivity(activity);

    assertArrayEquals(new Activity[] {},
        activityRegistry.getActivityTitle("Dårligtur", brotherman));
  }

  /**
   * Positive test: verifies that the logger can delete their own activity, and that the logger's
   * and the group's hikes registered are decreased.
   */
  @Test
  void deleteActivityUserIsLogger() {
    Person brotherman = new Person("Brotherman", "Testern", group);

    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, group);
    Activity activity2 = new Activity("Dårligtur", "Dårlig tur", 5, 2, time, brotherman, group);

    activityRegistry.addActivity(activity);
    activityRegistry.addActivity(activity2);

    assertArrayEquals(new Activity[] {activity, activity2},
        activityRegistry.getAllActivities(brotherman));
    assertEquals(2, brotherman.getHikesRegistered());

    activityRegistry.deleteActivity(activity2, brotherman);

    assertArrayEquals(new Activity[] {activity}, activityRegistry.getAllActivities(brotherman));
    assertEquals(1, brotherman.getHikesRegistered());
    assertEquals(1, group.getHikesRegistered());
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when a person who is not the
   * logger deletes an activity, and that the activity and the counts are unchanged.
   */
  @Test
  void deleteActivityThrowsWhenUserIsNotLogger() {
    Person brotherman = new Person("Brotherman", "Testern", group);
    Person bob = new Person("Bob", "Bobsen", group);

    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, group);
    activityRegistry.addActivity(activity);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> activityRegistry.deleteActivity(activity, bob));

    assertEquals("Persons can not delete another person's activity!", exception.getMessage());
    assertArrayEquals(new Activity[] {activity}, activityRegistry.getAllActivities(brotherman));
    assertEquals(1, brotherman.getHikesRegistered());
    assertEquals(1, group.getHikesRegistered());
  }

  /**
   * Positive test: verifies that getOwnActivities only returns activities the person has logged,
   * and not activities logged by other group members.
   */
  @Test
  void getOwnActivitiesToLoggerAndNotToGroup() {
    Person brotherman = new Person("Brotherman", "Testern", group);
    Person bob = new Person("Bob", "Bobsen", group);

    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, group);
    Activity activity2 = new Activity("Dårligtur", "Dårlig tur", 5, 2, time, bob, group);
    Activity activity3 = new Activity("Kort tur", "En kort tur", 2, 5, time, brotherman);

    activity.setShared(true);
    activity2.setShared(true);

    activityRegistry.addActivity(activity);
    activityRegistry.addActivity(activity2);
    activityRegistry.addActivity(activity3);

    assertArrayEquals(new Activity[] {activity, activity2, activity3},
        activityRegistry.getAllActivities(brotherman));
    assertArrayEquals(new Activity[] {activity, activity3},
        activityRegistry.getOwnActivities(brotherman));

    assertArrayEquals(new Activity[] {activity, activity2}, activityRegistry.getAllActivities(bob));
    assertArrayEquals(new Activity[] {activity2}, activityRegistry.getOwnActivities(bob));
  }

  /**
   * Positive test: verifies that an activity shared with a person is visible to them, but is not
   * returned by getOwnActivities.
   */
  @Test
  void getOwnActivitiesToLoggerAndNotSharedEmpty() {
    Person brotherman = new Person("Brotherman", "Testern", group);
    Person bob = new Person("Bob", "Bobsen");

    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, group);

    activityRegistry.addActivity(activity);

    activity.addCanSeeActivity(bob);

    assertArrayEquals(new Activity[] {activity}, activityRegistry.getAllActivities(bob));
    assertArrayEquals(new Activity[] {}, activityRegistry.getOwnActivities(bob));
  }

  /**
   * Positive test: verifies that only activities on the given date that the person can see are
   * returned.
   */
  @Test
  void getActivityDateSameDateCanSee() {
    Person brotherman = new Person("Brotherman", "Testern", group);
    Person bob = new Person("Bob", "Bobsen", group);

    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, group);
    Activity activity2 = new Activity("Dårligtur", "Dårlig tur", 5, 2,
        time.plusHours(5).plusMinutes(30), bob, group);
    Activity activity3 =
        new Activity("Kort tur", "En kort tur", 2, 5, time.minusHours(12), brotherman);
    Activity activity4 = new Activity("Lang tur", "En lang tur", 20, 6,
        time.minusHours(12).minusMinutes(1), brotherman);
    activity.setShared(true);
    activity2.setShared(true);

    activityRegistry.addActivity(activity);
    activityRegistry.addActivity(activity2);
    activityRegistry.addActivity(activity3);
    // activity4 won't be in the array since it is the day before
    activityRegistry.addActivity(activity4);

    assertArrayEquals(new Activity[] {activity3, activity, activity2},
        activityRegistry.getActivityDate(time.toLocalDate(), brotherman));
    assertArrayEquals(new Activity[] {activity, activity2},
        activityRegistry.getActivityDate(time.toLocalDate(), bob));
  }

  /**
   * Positive test: verifies that an empty array is returned when the person can not see the
   * activity on the given date.
   */
  @Test
  void getActivityDateCantSeeEmpty() {
    Person brotherman = new Person("Brotherman", "Testern", group);
    Person bob = new Person("Bob", "Bobsen");

    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, group);

    activityRegistry.addActivity(activity);

    assertArrayEquals(new Activity[] {}, activityRegistry.getActivityDate(time.toLocalDate(), bob));
  }

  /**
   * Positive test: verifies that only activities between the start date and end date that the
   * person can see are returned.
   */
  @Test
  void getActivityDateStartEndCanSee() {
    Person brotherman = new Person("Brotherman", "Testern", group);
    Person bob = new Person("Bob", "Bobsen", group);
    LocalDateTime time2 = LocalDateTime.of(2026, 10, 3, 12, 0);

    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, group);
    Activity activity2 =
        new Activity("Dårligtur", "Dårlig tur", 5, 2, time2.plusHours(12), bob, group);
    Activity activity3 =
        new Activity("Kort tur", "En kort tur", 2, 5, time2.minusHours(12), brotherman);
    Activity activity4 = new Activity("Lang tur", "En lang tur", 20, 6,
        time.minusDays(2).minusMinutes(1), brotherman);

    activityRegistry.addActivity(activity);
    // activity2 is after time2, so it won't be visible between time and time2
    activityRegistry.addActivity(activity2);
    activityRegistry.addActivity(activity3);
    activityRegistry.addActivity(activity4);

    assertArrayEquals(new Activity[] {activity, activity3}, activityRegistry
        .getActivityDateStartEnd(time.toLocalDate(), time2.toLocalDate(), brotherman));
  }

  /**
   * Positive test: verifies that a person only gets their own visible activities within the date
   * range, and an empty array when none of them are within it.
   */
  @Test
  void getActivityDateStartEndCantSeeEmpty() {
    Person brotherman = new Person("Brotherman", "Testern");
    Person bob = new Person("Bob", "Bobsen");
    LocalDateTime time2 = LocalDateTime.of(2026, 10, 3, 12, 0);

    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman);
    Activity activity2 = new Activity("Dårligtur", "Dårlig tur", 5, 2, time2.plusHours(12), bob);
    Activity activity3 =
        new Activity("Kort tur", "En kort tur", 2, 5, time2.minusHours(12), brotherman);

    activityRegistry.addActivity(activity);
    activityRegistry.addActivity(activity2);
    activityRegistry.addActivity(activity3);

    assertArrayEquals(new Activity[] {activity2}, activityRegistry
        .getActivityDateStartEnd(time.toLocalDate(), time2.plusHours(12).toLocalDate(), bob));
    assertArrayEquals(new Activity[] {},
        activityRegistry.getActivityDateStartEnd(time.toLocalDate(), time2.toLocalDate(), bob));
  }

  /**
   * Positive test: verifies that the same date can be used as both start date and end date, and
   * that all activities on that date that the person can see are returned, while activities on the
   * day before and the day after are not returned.
   */
  @Test
  void getActivityDateStartEndSameDate() {
    Person brotherman = new Person("Brotherman", "Testern");
    Person bob = new Person("Bob", "Bobsen");

    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman);
    Activity activity2 = new Activity("Dårligtur", "Dårlig tur", 5, 2, time, bob);

    Activity activity3 =
        new Activity("Dårligtur", "Dårlig tur", 5, 2, time.plusDays(1), brotherman);
    Activity activity4 =
        new Activity("Dårligtur", "Dårlig tur", 5, 2, time.minusDays(1), bob);

    activity.addCanSeeActivity(bob);
    activity2.addCanSeeActivity(brotherman);
    activity3.addCanSeeActivity(bob);
    activity4.addCanSeeActivity(brotherman);

    activityRegistry.addActivity(activity);
    activityRegistry.addActivity(activity2);
    // only activity and activity2 will be found
    activityRegistry.addActivity(activity3);
    activityRegistry.addActivity(activity4);

    assertArrayEquals(new Activity[] {activity, activity2},
        activityRegistry.getActivityDateStartEnd(time.toLocalDate(), time.toLocalDate(), bob));
    assertArrayEquals(new Activity[] {activity, activity2}, activityRegistry
        .getActivityDateStartEnd(time.toLocalDate(), time.toLocalDate(), brotherman));
  }

  /**
   * Negative test: verifies that an IllegalArgumentException is thrown when the start date is after
   * the end date.
   */
  @Test
  void getActivityDateStartAfterEnd() {
    Person brotherman = new Person("Brotherman", "Testern");

    LocalDateTime time2 = LocalDateTime.of(2026, 10, 3, 12, 0);

    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman);

    activityRegistry.addActivity(activity);

    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> activityRegistry
            .getActivityDateStartEnd(time2.toLocalDate(), time.toLocalDate(), brotherman));

    assertEquals("Start date can't be after end date!", exception.getMessage());
  }

  /**
   * Positive test: verifies that a group member only gets the shared group activities logged by the
   * person searched for, and not private activities or activities logged by other members.
   */
  @Test
  void getActivitiesToPersonGroupSeeOnly() {
    Person brotherman = new Person("Brotherman", "Testern", group);
    Person bob = new Person("Bob", "Bobsen", group);
    Person kari = new Person("Kari", "Karisen", group);

    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, group);
    Activity activity2 =
        new Activity("Kort tur", "En kort tur", 2, 5, time.plusHours(1), brotherman);
    Activity activity3 =
        new Activity("Dårligtur", "Dårlig tur", 5, 2, time.plusHours(2), kari, group);

    activity.setShared(true);
    activity3.setShared(true);

    activityRegistry.addActivity(activity);
    activityRegistry.addActivity(activity2);
    activityRegistry.addActivity(activity3);

    // bob can see activity3, but it is not logged by brotherman
    assertArrayEquals(new Activity[] {activity},
        activityRegistry.getActivitiesToPerson(brotherman, bob));
    // brotherman sees his own private activity as well
    assertArrayEquals(new Activity[] {activity, activity2},
        activityRegistry.getActivitiesToPerson(brotherman, brotherman));
  }

  /**
   * Positive test: verifies that a person searching for activities from a person who has shared
   * directly with them gets the shared activity.
   */
  @Test
  void getActivitiesToPersonAddedActivity() {
    Person brotherman = new Person("Brotherman", "Testern");
    Person bob = new Person("Bob", "Bobsen");

    Activity activity =
        new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, brotherman.getHikeGroup());

    activity.addCanSeeActivity(bob);

    activityRegistry.addActivity(activity);

    assertArrayEquals(new Activity[] {activity},
        activityRegistry.getActivitiesToPerson(brotherman, bob));
  }

  /**
   * Positive test: verifies that a person searching for activities from a person who has not shared
   * with them gets an empty array.
   */
  @Test
  void getActivitiesToPersonEmpty() {
    Person brotherman = new Person("Brotherman", "Testern", group);
    Person bob = new Person("Bob", "Bobsen", group);

    Activity activity = new Activity("Fjelltur", "Fin tur", 10, 7, time, brotherman, group);

    activityRegistry.addActivity(activity);

    assertArrayEquals(new Activity[] {}, activityRegistry.getActivitiesToPerson(brotherman, bob));
  }

  /**
   * Negative test: verifies that a NullPointerException is thrown when the person to search for is
   * null.
   */
  @Test
  void throwWhenGetActivitiesToPersonNull() {
    Person bob = new Person("Bob", "Bobsen");

    NullPointerException exception = assertThrows(NullPointerException.class,
        () -> activityRegistry.getActivitiesToPerson(null, bob));

    assertEquals("Person to search can't be null!", exception.getMessage());
  }

  /**
   * Positive test: verifies that searching by a word that only appears in an activity's title
   * returns that activity, regardless of upper or lower case.
   */
  @Test
  void getActivitiesByWordTitle() {
    Person brotherman = new Person("Brotherman", "Testern", group);
    Person bob = new Person("Bob", "Bobsen", group);

    Activity activity = new Activity("Fjelltur", "Fint", 10, 7, time, brotherman, group);
    Activity activity2 =
        new Activity("Kort tur", "En kort tur", 2, 5, time.plusDays(2), brotherman, group);

    activity.setShared(true);
    activity2.setShared(true);

    activityRegistry.addActivity(activity);
    activityRegistry.addActivity(activity2);

    assertArrayEquals(new Activity[] {activity},
        activityRegistry.getActivitiesByWord("fjelltur", bob));
  }

  /**
   * Positive test: verifies that searching by a word that only appears in an activity's description
   * returns that activity.
   */
  @Test
  void getActivitiesByWordDescription() {
    Person brotherman = new Person("Brotherman", "Testern", group);
    Person bob = new Person("Bob", "Bobsen", group);

    Activity activity = new Activity("Fjelltur", "Fint", 10, 7, time, brotherman, group);
    Activity activity2 =
        new Activity("Tur", "En kort tur", 2, 5, time.plusDays(2), brotherman, group);

    activity.setShared(true);
    activity2.setShared(true);
    activityRegistry.addActivity(activity);
    activityRegistry.addActivity(activity2);

    assertArrayEquals(new Activity[] {activity2},
        activityRegistry.getActivitiesByWord("kort", bob));
  }

  /**
   * Positive test: verifies that all matching activities are returned sorted by time, and that an
   * activity matching in both title and description is only returned once.
   */
  @Test
  void getActivitiesByWordTitleAndDescription() {
    Person brotherman = new Person("Brotherman", "Testern", group);
    Person bob = new Person("Bob", "Bobsen", group);

    Activity activity = new Activity("Fjelltur", "Fint", 10, 7, time, brotherman, group);
    Activity activity2 =
        new Activity("Kort tur", "En kort tur", 2, 5, time.plusDays(2), brotherman, group);

    activity.setShared(true);
    activity2.setShared(true);
    activityRegistry.addActivity(activity);
    activityRegistry.addActivity(activity2);

    assertArrayEquals(new Activity[] {activity, activity2},
        activityRegistry.getActivitiesByWord("tur", bob));
  }

  /**
   * Positive test: verifies that a group member does not get a matching activity that is not shared
   * with the group.
   */
  @Test
  void getActivitiesByWordCanNotSee() {
    Person brotherman = new Person("Brotherman", "Testern", group);
    Person bob = new Person("Bob", "Bobsen", group);

    Activity activity = new Activity("Fjelltur", "Fint", 10, 7, time, brotherman, group);

    activityRegistry.addActivity(activity);

    assertArrayEquals(new Activity[] {},
        activityRegistry.getActivitiesByWord("tur", bob));
  }

  /**
   * Positive test: verifies that a person gets a matching activity that has been shared directly
   * with them.
   */
  @Test
  void getActivitiesByWordShared() {
    Person brotherman = new Person("Brotherman", "Testern", group);
    Person bob = new Person("Bob", "Bobsen", group);

    Activity activity = new Activity("Fjelltur", "Fint", 10, 7, time, brotherman, group);

    activity.addCanSeeActivity(bob);
    activityRegistry.addActivity(activity);

    assertArrayEquals(new Activity[] {activity},
        activityRegistry.getActivitiesByWord("tur", bob));
  }

  /**
   * Positive test: verifies that an empty array is returned when no activity contains the search
   * word.
   */
  @Test
  void getActivitiesByWordNoMatch() {
    Person brotherman = new Person("Brotherman", "Testern", group);
    Person bob = new Person("Bob", "Bobsen", group);

    Activity activity = new Activity("Fjelltur", "Fint", 10, 7, time, brotherman, group);
    Activity activity2 =
        new Activity("Kort tur", "En kort tur", 2, 5, time.plusDays(2), brotherman, group);

    activity.setShared(true);
    activity2.setShared(true);
    activityRegistry.addActivity(activity);
    activityRegistry.addActivity(activity2);

    assertArrayEquals(new Activity[] {},
        activityRegistry.getActivitiesByWord("hike", bob));
  }

  /**
   * Negative test: verifies that IllegalArgumentException is thrown when search word is blank.
   */
  @Test
  void throwWhenGetActivitiesByWordBlank() {
    Person brotherman = new Person("Brotherman", "Testern", group);
    Person bob = new Person("Bob", "Bobsen", group);

    Activity activity = new Activity("Fjelltur", "Fint", 10, 7, time, brotherman, group);

    activity.addCanSeeActivity(bob);
    activityRegistry.addActivity(activity);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> activityRegistry.getActivitiesByWord(" ", bob));

    assertEquals("Word to search can't be blank!", exception.getMessage());
  }

}
