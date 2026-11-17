package no.ntnu.idatt1003.hikeseeker.registry;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import no.ntnu.idatt1003.hikeseeker.model.Activity;
import no.ntnu.idatt1003.hikeseeker.model.Person;

/**
 * Represents a registry that keeps track of all activities in the hike registry.
 *
 * <p>
 * The registry can add and remove activities, return all activities that a person can see, returns
 * activities by title, returns activtys by date and interval between to dates.
 */
public class ActivityRegistry {
  private final ArrayList<Activity> activities = new ArrayList<>();

  /**
   * Adds a new activity tot the activty register.
   *
   * @param newActivity - activty to add.
   */
  public void addActivity(Activity newActivity) {
    newActivity.getLogger().increasHikeRegistered();
    if (newActivity.hasGroup()) {
      newActivity.getGroup().increasHikeRegistered();
    }
    activities.add(newActivity);
  }

  /**
   * Gets all activities who a person can see, and returns them in an array.
   *
   * @param user - witch users activtys
   * @return array with activities.
   */
  public Activity[] getAllActivities(Person user) {
    // includes only activities that user can see.
    return activities.stream().filter(act -> act.canSeeActivity(user))
        .sorted(Comparator.comparing(Activity::getTimeRegistered))
        .toArray(Activity[]::new);
  }

  /**
   * Get all activities made by {@code user} and returns them in an array.
   *
   * @param user - users activities to return.
   * @return array with activities.
   */
  public Activity[] getOwnActivities(Person user) {
    return activities.stream().filter(act -> act.getLogger() == user)
        .sorted(Comparator.comparing(Activity::getTimeRegistered))
        .toArray(Activity[]::new);
  }

  /**
   * Finds activities by title and returns it as an array if {@code user} can see it.
   *
   * @param title - title to search for.
   * @return array with activities.
   */
  public Activity[] getActivityTitle(String title, Person user) {
    return activities.stream()
        .filter(act -> act.getTitle().equalsIgnoreCase(title) && act.canSeeActivity(user))
        .sorted(Comparator.comparing(Activity::getTimeRegistered))
        .toArray(Activity[]::new);
  }

  /**
   * Get all activities on a spesific date.
   *
   * @param date - date to check for activities.
   * @param user - user to check if it can see the activity.
   * @return array of activities on the given date.
   */
  public Activity[] getActivityDate(LocalDate date, Person user) {
    return activities.stream()
        .filter(
            act -> act.getTimeRegistered().toLocalDate().equals(date) && act.canSeeActivity(user))
        .sorted(Comparator.comparing(Activity::getTimeRegistered))
        .toArray(Activity[]::new);
  }

  /**
   * Get all activities innside an interval between a start-date and an end-date.
   *
   * @param startDate - first date to check.
   * @param endDate - last date to check.
   * @param user - user to check if it can see the activity.
   * @return list of activities between th given dates.
   */
  public Activity[] getActivityDateStartEnd(LocalDate startDate, LocalDate endDate, Person user) {
    if (startDate.isAfter(endDate)) {
      throw new IllegalArgumentException("Start date can't be after end date!");
    }

    return activities.stream()
        .filter(act -> !act.getTimeRegistered().toLocalDate().isBefore(startDate)
            && !act.getTimeRegistered().toLocalDate().isAfter(endDate) && act.canSeeActivity(user))
        .sorted(Comparator.comparing(Activity::getTimeRegistered))
        .toArray(Activity[]::new);
  }

  /**
   * Gets all activities to a spesific person if {@code user} can see them, and returns them in an
   * array.
   *
   * @param personSearch - activities to person to search.
   * @param user - the person who search.
   * @return array of activities.
   */
  public Activity[] getActivitiesToPerson(Person personSearch, Person user) {
    if (personSearch == null) {
      throw new NullPointerException("Person to search can't be null!");
    }

    return activities.stream()
        .filter(act -> act.getLogger() == personSearch && act.canSeeActivity(user))
        .sorted(Comparator.comparing(Activity::getTimeRegistered))
        .toArray(Activity[]::new);
  }

  /**
   * Delets a spesific activity, users can only delete their own hikes.
   *
   * @param activity - activity to delete.
   * @param user - users activity to delete.
   * @throws IllegalArgumentException if user aint th logger.
   */
  public void deleteActivity(Activity activity, Person user) {
    if (activity.getLogger() != user) {
      throw new IllegalArgumentException("Persons can not delete another person's activity!");
    }

    activity.getLogger().decreasHikeRegistered();
    if (activity.hasGroup()) {
      activity.getGroup().decreasHikeRegistered();
    }

    activities.remove(activity);
  }
}
