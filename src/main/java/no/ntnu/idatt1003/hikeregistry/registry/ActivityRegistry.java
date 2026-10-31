package no.ntnu.idatt1003.hikeregistry.registry;

import java.time.LocalDateTime;
import java.util.ArrayList;

import no.ntnu.idatt1003.hikeregistry.model.Activity;
import no.ntnu.idatt1003.hikeregistry.model.Person;

public class ActivityRegistry {
  // valgte dette for det har flere funksjoner til seg som gjøre det lettere å
  // håndtere data, eks legge til og fjerne
  private final ArrayList<Activity> activitys = new ArrayList<>();

  // legger tl en ny aktivitet til registeret
  public void addActivity(Activity newActivity) {
    newActivity.getLogger().increasHikeRegistered();
    if (newActivity.hasGroup()) {
      newActivity.getGroup().increasHikeRegistered();
    }
    activitys.add(newActivity);
  }

  // retunerer alle aktivitetene som et array, er mer kjent med array og syntes
  // det er lettere å håndtere et for å vise fram aktivitetene
  public Activity[] getAllActivitys(Person user) {
    ArrayList<Activity> returnActivitys = new ArrayList<>();

    // removes the activitys that person can not see.
    for (int i = 0; i < activitys.size(); i++) {
      if (activitys.get(i).canSeeActivity(user)) {
        returnActivitys.add(activitys.get(i));
      }
    }

    return returnActivitys.toArray(new Activity[0]);
  }

  /**
   * Finds a activty by title and returns it.
   *
   * @param title
   * @return
   */
  public Activity[] getActivityTitle(String title, Person user) {
    ArrayList<Activity> returnActivitys = new ArrayList<>();

    for (int i = 0; i < activitys.size(); i++) {
      if (title.equalsIgnoreCase(activitys.get(i).getTitle()) && activitys.get(i).canSeeActivity(user)) {
        returnActivitys.add(activitys.get(i));
      }
    }

    return returnActivitys.toArray(new Activity[0]);
  }

  /**
   *
   * @param date
   * @param user to check if it can see the activity
   * @return list of activitys on the given date
   */
  public Activity[] getActivityDate(LocalDateTime date, Person user) {
    ArrayList<Activity> returnActivitys = new ArrayList<>();

    for (int i = 0; i < activitys.size(); i++) {
      if (date.toLocalDate().equals(activitys.get(i).getTimeRegistered().toLocalDate())
          && activitys.get(i).canSeeActivity(user)) {
        returnActivitys.add(activitys.get(i));
      }
    }

    return returnActivitys.toArray(new Activity[0]);
  }

  /**
   * Delets an activity
   *
   * @param activity
   */
  public void deleteActivity(Activity activity) {
    activity.getLogger().decreasHikeRegistered();
    if (activity.hasGroup()) {
      activity.getGroup().decreasHikeRegistered();
    }

    activitys.remove(activity);
  }
}
