package no.ntnu.idatt1003.hikeregistry.model;

import java.time.LocalDateTime;

/**
 * Represents a single hike/activity logged in the hike registry.
 *
 * <p>
 * An activity has a title, a description, a distance in kilometers, a rating
 * from 1 to 10,
 * the time it was registered, the person who logged it,
 * the group who performed it and a unique id.
 * All fields are
 * immutable once the activity is created.
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
  private final int id;

  /**
   * Creates a new activity with the given values.
   *
   * @param title          the title of the activity, cannot be null or empty
   * @param description    a description of how the activity went, cannot be null
   *                       or empty
   * @param distanceKm     the distance of the activity in kilometers, must be a
   *                       finite number
   *                       greater than 0
   * @param rating         the rating of the activity, must be between 1 and 10
   *                       (inclusive)
   * @param timeRegistered the date and time the activity was registered
   * @param logger         the person who logged the activity
   * @param group          the group that was on this activity
   * @param id             the unique id of the activity
   * @throws IllegalArgumentException if the title or description is null or
   *                                  empty, if the
   *                                  distance is not a positive finite number, or
   *                                  if the rating
   *                                  is not between 1 and 10
   */
  public Activity(String title, String description, double distanceKm, int rating, LocalDateTime timeRegistered,
      Person logger, Group group, int id) {
    if (title == null || title.equals("")) {
      throw new IllegalArgumentException("Activity must have a title!");
    }
    this.title = title;

    if (description == null || description.equals("")) {
      throw new IllegalArgumentException("Activity must have a description!");
    }
    this.description = description;

    if (Double.isNaN(distanceKm) || Double.isInfinite(distanceKm) || distanceKm <= 0) {
      throw new IllegalArgumentException("Activity must have a valid distance!");
    }
    this.distanceKm = distanceKm;

    if (rating < 1 || rating > 10) {
      throw new IllegalArgumentException("Rating must be between 1 and 10!");
    }
    this.rating = rating;

    this.timeRegistered = timeRegistered;
    this.logger = logger;
    this.group = group;
    this.id = id;
  }

  /**
   * Creates a new activity with the given values without a group connected to the
   * activity.
   * Makes is possible for users to log a activity without a group.
   *
   * @param title          the title of the activity, cannot be null or empty
   * @param description    a description of how the activity went, cannot be null
   *                       or empty
   * @param distanceKm     the distance of the activity in kilometers, must be a
   *                       finite number
   *                       greater than 0
   * @param rating         the rating of the activity, must be between 1 and 10
   *                       (inclusive)
   * @param timeRegistered the date and time the activity was registered
   * @param logger         the person who logged the activity
   * @param group          the group is set to null
   * @param id             the unique id of the activity
   * @throws IllegalArgumentException if the title or description is null or
   *                                  empty, if the
   *                                  distance is not a positive finite number, or
   *                                  if the rating
   *                                  is not between 1 and 10
   */
  public Activity(String title, String description, double distanceKm, int rating, LocalDateTime timeRegistered,
      Person logger, int id) {
    if (title == null || title.equals("")) {
      throw new IllegalArgumentException("Activity must have a title!");
    }
    this.title = title;

    if (description == null || description.equals("")) {
      throw new IllegalArgumentException("Activity must have a description!");
    }
    this.description = description;

    if (Double.isNaN(distanceKm) || Double.isInfinite(distanceKm) || distanceKm <= 0) {
      throw new IllegalArgumentException("Activity must have a valid distance!");
    }
    this.distanceKm = distanceKm;

    if (rating < 1 || rating > 10) {
      throw new IllegalArgumentException("Rating must be between 1 and 10!");
    }
    this.rating = rating;

    this.timeRegistered = timeRegistered;
    this.logger = logger;
    this.group = null;
    this.id = id;
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
   * Returns the group who performed this activty.
   *
   * @return the group who performed this activity
   */
  public Group getGroup() {
    return group;
  }

  public boolean hasGroup() {
    boolean hasGroup = false;
    if (group != null) {
      hasGroup = true;
    }

    return hasGroup;
  }

  /**
   * Returns the unique id of this activity.
   *
   * @return the id of the activity
   */
  public int getId() {
    return id;
  }
}
