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
  public Activity[] getAllActivitys(Person person) {
    ArrayList<Activity> returnActivitys = new ArrayList<>();

    // removes the activitys that person can not see.
    for (int i = 0; i < activitys.size(); i++) {
      if (activitys.get(i).canSeeActivity(person)) {
        returnActivitys.add(activitys.get(i));
      }
    }

    return returnActivitys.toArray(new Activity[0]);
  }

  // find activty by title
  public Activity getActivityTitle(String title) {
    Activity activity = null;
    for (int i = 0; i < activitys.size(); i++) {
      if (title.equalsIgnoreCase(activitys.get(i).getTitle())) {
        activity = activitys.get(i);
      }
    }

    return activity;
  }

  // finds activity by data
  public Activity getActivityDate(LocalDateTime date) {
    Activity activity = null;

    for (int i = 0; i < activitys.size(); i++) {
      if (date.equals(activitys.get(i).getTimeRegistered())) {
        activity = activitys.get(i);
      }
    }

    return activity;
  }

  // deletes a activity
  public void deleteActivity(Activity activity) {
    activity.getLogger().decreasHikeRegistered();
    if (activity.hasGroup()) {
      activity.getGroup().decreasHikeRegistered();
    }

    activitys.remove(activity);
  }
}
