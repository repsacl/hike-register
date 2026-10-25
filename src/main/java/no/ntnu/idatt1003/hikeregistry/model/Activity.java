package no.ntnu.idatt1003.hikeregistry.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Represents a single hike/activity logged in the hike registry.
 *
 * <p>
 * An activity has a title, a description, a distance in kilometers, a rating
 * from 1 to 10, the time it was registered, the person who logged it and an
 * optional group. Only the logger, the group members and people added later
 * can see the activity.
 * </p>
 */
public class Activity {

  private final String title;
  private final String description;
  private final double distanceKm;
  private final int rating;
  private final LocalDateTime timeRegistered;
  private final Person logger;
  private final Group group;
  private final ArrayList<Person> seeActivity = new ArrayList<>();

  /**
   * Creates a new activity done by a group.
   *
   * @param title          the title, cannot be null or empty
   * @param description    how the activity went, cannot be null or empty
   * @param distanceKm     the distance in kilometers, must be greater than 0
   * @param rating         the rating, from 1 to 10
   * @param timeRegistered the date and time the activity was registered
   * @param logger         the person who logged the activity
   * @param group          the group that did the activity, can be null
   * @throws IllegalArgumentException if any of the values are invalid
   */
  public Activity(String title, String description, double distanceKm, int rating, LocalDateTime timeRegistered,
      Person logger, Group group) {
    if (title == null || title.equals("")) {
      throw new IllegalArgumentException("Activity must have a title!");
    }
    this.title = title;

    if (description == null || description.equals("")) {
      throw new IllegalArgumentException("Activity must have a description!");
    }
    this.description = description;

    if (Double.isNaN(distanceKm) || Double.isInfinite(distanceKm) || distanceKm <= 0) {
      throw new NumberFormatException("Activity must have a valid distance!");
    }
    this.distanceKm = distanceKm;

    if (rating < 1 || rating > 10) {
      throw new NumberFormatException("Rating must be between 1 and 10!");
    }
    this.rating = rating;

    this.timeRegistered = timeRegistered;
    this.logger = logger;
    seeActivity.add(logger);
    this.group = group;

    updateSeeActivity();
  }

  /**
   * Creates a new activity without a group.
   *
   * @param title          the title, cannot be null or empty
   * @param description    how the activity went, cannot be null or empty
   * @param distanceKm     the distance in kilometers, must be greater than 0
   * @param rating         the rating, from 1 to 10
   * @param timeRegistered the date and time the activity was registered
   * @param logger         the person who logged the activity
   * @throws IllegalArgumentException if any of the values are invalid
   */
  public Activity(String title, String description, double distanceKm, int rating, LocalDateTime timeRegistered,
      Person logger) {
    if (title == null || title.equals("")) {
      throw new IllegalArgumentException("Activity must have a title!");
    }
    this.title = title;

    if (description == null || description.equals("")) {
      throw new IllegalArgumentException("Activity must have a description!");
    }
    this.description = description;

    if (Double.isNaN(distanceKm) || Double.isInfinite(distanceKm) || distanceKm <= 0) {
      throw new NumberFormatException("Activity must have a valid distance!");
    }
    this.distanceKm = distanceKm;

    if (rating < 1 || rating > 10) {
      throw new NumberFormatException("Rating must be between 1 and 10!");
    }
    this.rating = rating;

    this.timeRegistered = timeRegistered;
    this.logger = logger;
    seeActivity.add(logger);
    this.group = null;

    updateSeeActivity();
  }

  /**
   * Returns the title of this activity.
   *
   * @return the title of the activity
   */
  public String getTitle() {
    return title;
  }

  /**
   * Returns the description of this activity.
   *
   * @return the description of the activity
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the distance of this activity in kilometers.
   *
   * @return the distance of the activity in kilometers
   */
  public double getDistanceKm() {
    return distanceKm;
  }

  /**
   * Returns the rating of this activity.
   *
   * @return the rating of the activity, between 1 and 10
   */
  public int getRating() {
    return rating;
  }

  /**
   * Returns the date and time this activity was registered.
   *
   * @return the date and time the activity was registered
   */
  public LocalDateTime getTimeRegistered() {
    return timeRegistered;
  }

  /**
   * Returns the person who logged this activity.
   *
   * @return the person who logged the activity
   */
  public Person getLogger() {
    return logger;
  }

  /**
   * Returns the group that did this activity.
   *
   * @return the group, or null if the activity has no group
   */
  public Group getGroup() {
    return group;
  }

  /**
   * Checks if this activity has a group.
   *
   * @return true if the activity has a group, otherwise false
   */
  public boolean hasGroup() {
    boolean hasGroup = false;
    if (group != null) {
      hasGroup = true;
    }

    return hasGroup;
  }

  /**
   * Checks if the given person can see this activity.
   *
   * <p>
   * The logger, the group members at the time the activity was created and
   * people added with {@link #addCanSeeActivity(Person)} can always see it.
   * Anyone who is currently in the group can also see it. A person who joins
   * the group later and then leaves loses access.
   * </p>
   *
   * @param person the person to check
   * @return true if the person can see the activity, otherwise false
   */
  public boolean canSeeActivity(Person person) {
    boolean canSee = false;

    if (isAdded(person) || person.getHikeGroup() == group && hasGroup()) {
      return canSee = true;
    }

    return canSee;
  }

  /**
   * Gives a person access to see this activity. Does nothing if the person
   * already has access.
   *
   * @param person the person to give access
   */
  public void addCanSeeActivity(Person person) {
    if (isAdded(person) == false) {
      seeActivity.add(person);
    }
  }

  /**
   * Checks if the given person has already been given access to this activity.
   *
   * @param person the person to check
   * @return true if the person has access, otherwise false
   */
  private boolean isAdded(Person person) {
    boolean added = false;

    for (int i = 0; i < seeActivity.size(); i++) {
      if (person == seeActivity.get(i)) {
        added = true;
      }
    }

    return added;
  }

  /**
   * Gives all current members of the group access to see this activity.
   * Does nothing if the activity has no group.
   */
  private void updateSeeActivity() {
    if (group == null) {
      return;
    }
    Person[] members = group.getAllMembers();
    for (int i = 0; i < members.length; i++) {
      addCanSeeActivity(members[i]);
    }
  }
}
