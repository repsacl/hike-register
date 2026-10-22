package no.ntnu.idatt1003.hikeregistry.registry;

import java.time.LocalDateTime;
import java.util.ArrayList;

import no.ntnu.idatt1003.hikeregistry.model.Activity;

public class ActivityRegistry {
  // valgte dette for det har flere funksjoner til seg som gjøre det lettere å
  // håndtere data, eks legge til og fjerne
  private final ArrayList<Activity> activitys = new ArrayList<>();

  // legger tl en ny aktivitet til registeret
  public void addActivity(Activity newActivity) {
    activitys.add(newActivity);
  }

  // retunerer alle aktivitetene som et array, er mer kjent med array og syntes
  // det er lettere å håndtere et for å vise fram aktivitetene
  public Activity[] getAllActivitys() {
    Activity[] act = activitys.toArray(new Activity[0]);
    return act;
  }

  // find activty by title
  public Activity getActivityTitle(String title) {
    Activity act = null;
    for (int i = 0; i < activitys.size(); i++) {
      if (title.equalsIgnoreCase(activitys.get(i).getTitle())) {
        act = activitys.get(i);
      }
    }

    return act;
  }

  // finds activity by data
  public Activity getActivityDate(LocalDateTime date) {
    Activity act = null;
    for (int i = 0; i < activitys.size(); i++) {
      if (date.equals(activitys.get(i).getTimeRegistered())) {
        act = activitys.get(i);
      }
    }

    return act;
  }

  // deletes a activity
  public void deleteActivity(Activity activity) {
    activitys.remove(activity);
  }
}
