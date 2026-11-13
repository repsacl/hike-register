package no.ntnu.idatt1003.hikeseeker.registry;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

import no.ntnu.idatt1003.hikeseeker.model.Activity;
import no.ntnu.idatt1003.hikeseeker.model.Person;

/**
 * Represents a registry that keeps track of all activitys in the hike registry.
 *
 * <p>
 * The registry can add and remove activitys, return all activitys that a person
 * can see, returns activitys by title,
 * returns activtys by date and interval between to dates.
 * </p>
 */
public class ActivityRegistry {
  private final ArrayList<Activity> activities = new ArrayList<>();

  /**
   * Adds a new activity tot the activty register.
   *
   * @param newActivity
   */
  public void addActivity(Activity newActivity) {
    newActivity.getLogger().increasHikeRegistered();
    if (newActivity.hasGroup()) {
      newActivity.getGroup().increasHikeRegistered();
    }
    activities.add(newActivity);
  }

  /**
   * Gets all activitys who a person can see, and returns them in an array.
   *
   * @param user
   * @return array with activitys
   */
  public Activity[] getAllActivities(Person user) {
    // includes only activtys that user can see
    return activities.stream()
        .filter(act -> act.canSeeActivity(user))
        .sorted(Comparator.comparing(Activity::getTimeRegistered))
        .toArray(Activity[]::new);
  }

  /**
   * Get all activitys made by {@code user} and returns them in an array.
   *
   * @param user
   * @return array with activitys
   */
  public Activity[] getOwnActivities(Person user) {
    return activities.stream()
        .filter(act -> act.getLogger() == user)
        .sorted(Comparator.comparing(Activity::getTimeRegistered))
        .toArray(Activity[]::new);
  }

  /**
   * Finds a activtys by title and returns it as an array if person can see it.
   *
   * @param title
   * @return array with activitys
   */
  public Activity[] getActivityTitle(String title, Person user) {
    return activities.stream()
        .filter(act -> act.getTitle().equalsIgnoreCase(title) && act.canSeeActivity(user))
        .sorted(Comparator.comparing(Activity::getTimeRegistered))
        .toArray(Activity[]::new);
  }

  /**
   * Get all activitys on a spesific date.
   *
   * @param date
   * @param user to check if it can see the activity
   * @return list of activitys on the given date
   */
  public Activity[] getActivityDate(LocalDate date, Person user) {
    return activities.stream()
        .filter(act -> act.getTimeRegistered().toLocalDate().equals(date) && act.canSeeActivity(user))
        .sorted(Comparator.comparing(Activity::getTimeRegistered))
        .toArray(Activity[]::new);
  }

  /**
   * Get all activitys innside an interval between a start-date and an end-date.
   *
   * @param startDate
   * @param endDate
   * @param user      to check if it can see the activity
   * @return list of activitys between th given dates
   */
  public Activity[] getActivityDateStartEnd(LocalDate startDate, LocalDate endDate, Person user) {
    if (startDate.isAfter(endDate)) {
      throw new IllegalArgumentException("Start-date cant be after end-date!");
    }

    return activities.stream()
        .filter(act -> !act.getTimeRegistered().toLocalDate().isBefore(startDate)
            && !act.getTimeRegistered().toLocalDate().isAfter(endDate) &&
            act.canSeeActivity(user))
        .sorted(Comparator.comparing(Activity::getTimeRegistered))
        .toArray(Activity[]::new);
  }

  /**
   * Delets a spesific activity, users can only delete their own hikes.
   *
   * @param activity
   * @param user
   * @throws IllegalArgumentException if user aint th logger
   */
  public void deleteActivity(Activity activity, Person user) {
    if (activity.getLogger() != user) {
      throw new IllegalArgumentException("Persons can not delete an others activity!");
    }

    activity.getLogger().decreasHikeRegistered();
    if (activity.hasGroup()) {
      activity.getGroup().decreasHikeRegistered();
    }

    activities.remove(activity);
  }
}
