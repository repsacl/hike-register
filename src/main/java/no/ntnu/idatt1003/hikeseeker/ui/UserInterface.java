package no.ntnu.idatt1003.hikeseeker.ui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Scanner;

import no.ntnu.idatt1003.hikeseeker.model.Activity;
import no.ntnu.idatt1003.hikeseeker.model.Group;
import no.ntnu.idatt1003.hikeseeker.model.Person;
import no.ntnu.idatt1003.hikeseeker.registry.ActivityRegistry;
import no.ntnu.idatt1003.hikeseeker.registry.GroupRegistry;
import no.ntnu.idatt1003.hikeseeker.registry.PersonRegistry;

public class UserInterface {
  private boolean running = true;

  private final ActivityRegistry activityRegistry = new ActivityRegistry();
  private final PersonRegistry personRegistry = new PersonRegistry();
  private final GroupRegistry groupRegistry = new GroupRegistry();

  private final Scanner scanner = new Scanner(System.in);
  private Person currentUser;

  /**
   * Inizialize the user interface and makes the app ready to use.
   * Starts with pre buildt activitys, persosns and groups.
   */
  public void init() {
    LocalDateTime now = LocalDateTime.now();
    groupRegistry.addGroup(new Group("GRUPPEN"));
    groupRegistry.addGroup(new Group("Mountain-group"));
    Group[] groups = groupRegistry.getAllGroups();

    personRegistry.addPerson(new Person("Brotherman", "Testern", groups[0]));
    personRegistry.addPerson(new Person("Bob", "Bobsen", groups[0]));
    personRegistry.addPerson(new Person("Peter", "Oppland", groups[1]));
    Person[] persons = personRegistry.getAllPersons();

    currentUser = persons[2];

    activityRegistry.addActivity(new Activity("En lang tur", "Turen var veldig bra og lang, ble velsig sliten", 10,
        10, now, persons[0]));
    activityRegistry.addActivity(new Activity("En kort tur", "Turen var kort og kjedelig. Bare skog :(", 3, 3,
        now.minusDays(2).minusHours(5), persons[1], persons[1].getHikeGroup()));
    activityRegistry
        .addActivity(new Activity("Fjelltur", "Jeg gikk til Gaustadtoppen. Det var bratt, men det gikk tilslutt.",
            30, 8, now.minusMonths(2).minusDays(5).plusHours(2).plusMinutes(10), persons[2],
            persons[2].getHikeGroup()));
  }

  /**
   * Starts the user interface of the app
   */
  public void start() {
    showMenu();

    while (running) {
      showScanner();
    }
  }

  /**
   * A scanner function that takes input commands from users and performs them.
   */
  private void showScanner() {
    System.out.print("-> ");
    String input = scanner.nextLine().trim();

    String[] parts = input.trim().split("\\s", 3);
    String command = parts[0].toLowerCase();

    System.out.println(Arrays.toString(parts));

    // uses equalsignorecase to ignore capital letters, so the user dont have to
    // write with uppercase letters
    // better to use switch because
    switch (command) {
      case "me" -> {
        clearPreviusLines();
        showUser();
      }
      case "my" -> {
        if (parts.length >= 2 && parts[1].equalsIgnoreCase("hikes")) {
          clearPreviusLines();
          showUsersActivitys();
        } else {
          System.out.println("'My' what? Try 'my hikes' or 'help' to see all commands");
        }
      }
      case "shared" -> {
        clearPreviusLines();
        showAllShared();
      }
      case "users" -> {
        clearPreviusLines();
        showAllUsers();
      }
      case "groups" -> {
        clearPreviusLines();
        showAllGroups();
      }
      case "switch" -> {
        if (parts.length >= 2 && parts[1].equalsIgnoreCase("user")) {
          clearPreviusLines();
          switchUser();
        } else if (parts.length >= 2 && parts[1].equalsIgnoreCase("group")) {
          clearPreviusLines();
          switchGroup();
        } else {
          System.out.println("Unknown command, did you mean 'switch user'?");
        }
      }
      case "find" -> {
        if (parts.length >= 3 && parts[1].equalsIgnoreCase("person")) {
          clearPreviusLines();
          findPerson(parts[2].replace("\"", ""));
          System.out.println(Arrays.toString(parts));
        } else if (parts.length >= 3 && parts[1].equalsIgnoreCase("title")) {
          clearPreviusLines();
          findActivityTitle(parts[2].replace("\"", ""));

        } else if (parts.length >= 3 && parts[1].equalsIgnoreCase("date")) {
          clearPreviusLines();
          findActivityDate(parts[2]);
        } else if (parts.length == 2) {
          System.out
              .println("Unknown command: " + parts[1] + " try 'find title <title>' or 'help' to se all commands");
        } else {
          System.out
              .println("Find what? Try 'find title <title>', 'find person <name>' or 'help' to see all commands");
        }
      }
      case "register" -> {
        if (parts.length >= 2 && parts[1].equalsIgnoreCase("hike")) {
          clearPreviusLines();
          registerActivity();
        } else if (parts.length >= 2 && parts[1].equalsIgnoreCase("group")) {
          clearPreviusLines();
          registerGroup();
        } else if (parts.length >= 2 && parts[1].equalsIgnoreCase("user")) {
          clearPreviusLines();
          regsiterUser();
        } else {
          System.out
              .println("Register what? Try 'register hike', 'register group' or 'help' to see more commands");
        }
      }
      case "edit" -> {
        if (parts.length >= 2 && parts[1].equalsIgnoreCase("hike")) {
          clearPreviusLines();
          editHike();
        } else {
          System.out
              .println("edit what? Try 'edit hike' or 'help' to see all commands");
        }
      }
      case "share" -> {
        if (parts.length >= 2 && parts[1].equalsIgnoreCase("hike")) {
          clearPreviusLines();
          shareActivityPerson();
        } else {
          System.out
              .println("share what? Try 'share hike' or 'help' to see all commands");
        }
      }
      case "join" -> {
        if (parts.length >= 2 && parts[1].equalsIgnoreCase("group")) {
          clearPreviusLines();
          switchGroup();
        } else {
          System.out.println("Join what? did you mean 'join group'?");
        }
      }
      case "leave" -> {
        if (parts.length >= 2 && parts[1].equalsIgnoreCase("group")) {
          clearPreviusLines();
          try {
            currentUser.leaveGroup();
            System.out.println("\nGroup left!");
            showUser();
          } catch (IllegalArgumentException e) {
            System.out.println("Could not leave group!: " + e.getMessage());
          }
        } else {
          System.out.println("Leave what? did you mean 'leave group'?");
        }
      }
      case "all" -> {
        clearPreviusLines();
        showAllActivites();
      }
      case "delete" -> {
        if (parts.length >= 2 && parts[1].equalsIgnoreCase("hike")) {
          clearPreviusLines();
          deleteActivity();
        } else {
          System.out.println("Delete what? Try 'delete hike' or 'help' to se all commands");
        }
      }
      case "clear" -> {
        clearPreviusLines();
      }
      case "help" -> {
        clearPreviusLines();
        showHelpOptions();
      }
      case "menu" -> {
        clearPreviusLines();
        showMenu();
      }
      case "hello" -> {
        System.out.println("\nHello!\n");
      }
      case "exit" -> {
        System.out.println("\nGoodbye!");
        scanner.close();
        running = false;
      }
      case "" -> {
        System.out.println("");
      }
      default -> {
        System.out.println("Unknown command: " + command + " try 'help' to se all commands");
      }
    }

  }

  /**
   * Shows the current users stats
   */
  private void showUser() {
    System.out.println("\n==== CURRENT USER ====");
    showPerson(currentUser);
  }

  private void showUsersActivitys() {
    Activity[] activities = activityRegistry.getOwnActivities(currentUser);

    if (activities.length == 0 || activities == new Activity[] {}) {
      System.out.println("\nNo hikes found, no hikes to show!\n");
      return;
    }

    System.out.println("\n==== ALl YOUR ACTIVITIES ====\n");
    for (int i = 0; i < activities.length; i++) {
      showActivity(activities[i]);
    }

  }

  /**
   * Shows information about a specific activity.
   *
   * @param activity
   */
  private void showActivity(Activity activity) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    System.out.println("Title:                  " + activity.getTitle());
    System.out.println("Description:            " + activity.getDescription()); // + "\n");
    System.out.println("Distance:               " + activity.getDistanceKm() + "km");
    System.out.println("Rating:                 " + activity.getRating() + "/10");
    System.out.println("Logged by:              " + activity.getLogger().getFullName());
    if (activity.hasGroup()) {
      System.out.println("Group:                  " + activity.getGroup().getName());
    }
    System.out.println("Shared:                 " + activity.getShared());
    System.out.println("Time registered:        " + formatter.format(activity.getTimeRegistered()) + "\n");
  }

  /**
   * Shows all activitys shared with the current user.
   */
  private void showAllActivites() {
    Activity[] activitys = activityRegistry.getAllActivities(currentUser);

    if (activitys.length == 0 || activitys == new Activity[] {}) {
      System.out.println("\nNo hikes found, no hikes to show!\n");
      return;
    }

    System.out.println("==== ALL ACTIVITYS ====");
    System.out.println("shows all your private activitys and activtys shared with you\n");

    for (int i = 0; i < activitys.length; i++) {
      showActivity(activitys[i]);
    }
  }

  /**
   * Takes a list of activtys and prints them out
   *
   * @param activityList - list of activities
   */
  private void showActivityList(Activity[] activityList) {
    System.out.println();

    for (int i = 0; i < activityList.length; i++) {
      showActivity(activityList[i]);
    }
  }

  /**
   * Takes a list of persons and prints them out
   *
   * @param personList - list of activities
   */
  private void showPersonList(Person[] personList) {
    System.out.println();

    for (int i = 0; i < personList.length; i++) {
      showPerson(personList[i]);
    }
  }

  /**
   * Shows all activities shared with the user
   */
  private void showAllShared() {
    Activity[] activities = activityRegistry.getAllActivities(currentUser);

    if (activities.length == 0 || activities == new Activity[] {}) {
      System.out.println("\nNo hikes found, no hikes to show!\n");
      return;
    }

    System.out.println("==== ALL SHARED ACTIVITYS ====");
    System.out.println("shows all activtys shared with you\n");

    for (int i = 0; i < activities.length; i++) {
      if (activities[i].getLogger() != currentUser) {
        showActivity(activities[i]);
      }
    }
  }

  /**
   * Registers an activity.
   */
  private void registerActivity() {
    System.out.println("\n==== REGISTER A HIKE ====");
    System.out.println("Write 'cancel' to end the registration at any time\n");

    String title = readInputOrCancel("Title");
    if (title == null) {
      return;
    }

    String description = readInputOrCancel("Description");
    if (description == null) {
      return;
    }

    String distance = readInputOrCancel("Distance in km");
    if (distance == null) {
      return;
    }

    String rating = readInputOrCancel("Rating 1-10");
    if (rating == null) {
      return;
    }

    String share = null;
    if (currentUser.hasGroup()) {
      share = readInputOrCancel("Share with group?: yes/no");
    }

    try {
      double dist = Double.parseDouble(distance);
      int rate = Integer.parseInt(rating);
      LocalDateTime now = LocalDateTime.now();

      Activity activity = new Activity(title, description, dist, rate, now, currentUser,
          currentUser.getHikeGroup());

      if (share != null && share.equalsIgnoreCase("yes")) {
        activity.setShared(true);
      } else if (share != null && share.equalsIgnoreCase("no")) {
        activity.setShared(false);
      }

      activityRegistry.addActivity(activity);
      System.out.println("\nHike activity registered!\n");
      showActivity(activity);
    } catch (IllegalArgumentException e) {
      System.out.println("\nCould not register activity: " + e.getMessage() + "\n");
    }
  }

  /**
   * Edits a spesific hike
   */
  private void editHike() {
    System.out.println("==== EDIT HIKE ====");
    System.out.println("Write 'cancel' to end the edit\n");
    System.out.println("choose one of the hikes to edit\n");

    Activity[] activities = activityRegistry.getOwnActivities(currentUser);

    // int canEdit = 0;
    for (int i = 0; i < activities.length; i++) {
      if (activities[i].hasGroup()) {
        System.out.println(i + 1 + ")");
        showActivity(activities[i]);
        // canEdit++;
      }
    }

    String number = readInputOrCancel("Select hike nr.");
    if (number == null) {
      return;
    }

    System.out.println("\nShare with group?");
    String input = readInputOrCancel("yes/no");
    if (input == null) {
      return;
    }

    try {
      // System.out.println(canEdit);
      // if (canEdit > 0) {
      // System.out.println("\nNo hikes that you can edit!\n");
      // continue;
      // }

      int choice = Integer.parseInt(number);
      if (choice > activities.length) {
        System.out.println("\nHike number cant be greter thean the ones shown!\n");
      }

      if (choice <= 0) {
        System.out.println("\nHike number cant be smaller than the nubers shown!\n");
      }

      if (input.equalsIgnoreCase("no")) {
        for (int i = 0; i < activities.length; i++) {
          if (choice - 1 == i) {
            activities[i].setShared(false);
            System.out.println("\nHike set to private!\n");
            showActivity(activities[i]);
          }
        }
      } else if (input.equalsIgnoreCase("yes")) {
        for (int i = 0; i < activities.length; i++) {
          if (choice - 1 == i) {
            activities[i].setShared(true);
            System.out.println("\nHike shared with group!\n");
            showActivity(activities[i]);
          }
        }
      }

    } catch (IllegalArgumentException e) {
      System.out.println("\nCould not edit hike! " + e.getMessage() + "\n");
    }
  }

  /**
   * Registers a group and adds the current user as a member.
   */
  private void registerGroup() {
    System.out.println("\n==== REGISTER A GROUP ====");
    System.out.println("Write 'cancel' to end the registration at any time\n");

    String name = readInputOrCancel("Name of the group");
    if (name == null) {
      return;
    }

    try {
      Group newGroup = new Group(name);

      groupRegistry.addGroup(newGroup);
      newGroup.addMember(currentUser);

      System.out.println("\nGroup registered!\n");
    } catch (IllegalArgumentException e) {
      System.out.println("\nCould not register group: " + e.getMessage() + "\n");
    }
  }

  /**
   * Shows all groups in the groupsregistry
   */
  private void showAllGroups() {
    System.out.println("\n==== ALL GROUPS ====");
    System.out.println("shows all active groups\n");

    Group[] groups = groupRegistry.getAllGroups();

    for (int i = 0; i < groups.length; i++) {
      showGroup(groups[i]);
    }

  }

  /**
   * Shows a spesific groups information.
   *
   * @param group
   */
  private void showGroup(Group group) {
    System.out.println("Name       " + group.getName());
    System.out.println("Members:   " + group.getCountMembers());
    System.out.println("Hikes:     " + group.getHikesRegistered() + "\n");
  }

  /**
   * User switches a group or joins a group if not a member
   */
  private void switchGroup() {
    System.out.println("\n==== JOIN GROUP ====");
    System.out.println("Write 'cancel' to end the registration at any time\n");
    System.out.println("Select one of the groups below\n");

    Group[] groups = groupRegistry.getAllGroups();
    for (int i = 0; i < groups.length; i++) {
      System.out.println(i + 1 + ")");
      showGroup(groups[i]);
    }

    System.out.println("\n0) if you dont want to be member of a group");

    System.out.println("\nor write 'register' to register a new group\n");

    String input = readInputOrCancel("Select a number");
    if (input == null) {
      return;
    }
    if (input.equalsIgnoreCase("register")) {
      registerGroup();
      return;
    }

    try {
      int choice = Integer.parseInt(input);

      if (choice == 0 && currentUser.hasGroup()) {
        currentUser.leaveGroup();
        System.out.println("\nGroup left!\n");
        showUser();
        return;
      } else if (choice < 0) {
        System.out.println("\nNumber cant be smaller than 1!\n");
        return;
      } else if (choice > groups.length) {
        System.out.println("\nNumber cant be greater than " + groups.length + "!\n");
        return;
      }

      for (int i = 0; i < groups.length; i++) {
        if (choice - 1 == i) {
          groups[i].addMember(currentUser);
        }
      }

      System.out.println("\nGroup joind!");
      System.out.println("Current group -> " + currentUser.getHikeGroup().getName() + "\n");

    } catch (IllegalArgumentException e) {
      System.out.println("\nCould not join a group: " + e.getMessage() + "\n");
    }
  }

  /**
   * Registers a new user and login to that new user.
   */
  private void regsiterUser() {
    System.out.println("\n==== REGISTER A NEW USER ====");
    System.out.println("Write 'cancel' to end the registration at any time\n");

    String firstName = readInputOrCancel("Firstname");
    if (firstName == null) {
      return;
    }

    String lastName = readInputOrCancel("Lastname");
    if (lastName == null) {
      return;
    }

    try {
      Person newUser = new Person(firstName, lastName);

      personRegistry.addPerson(newUser);
      currentUser = newUser;

      System.out.println("\nNew user registered!");
      System.out.println("Current user logged in -> " + currentUser.getFullName());
      System.out.println("\nSuggestion: join a group!\n");
    } catch (IllegalArgumentException e) {
      System.out.println("\nCould not register user: " + e.getMessage() + "\n");
    }
  }

  /**
   * Switches users with a number related to a profile.
   */
  private void switchUser() {
    System.out.println("\n==== SWITCH USER ====");
    System.out.println("Write 'cancel' to end the registration at any time\n");
    System.out.println("Select one of the users below\n");

    Person[] users = personRegistry.getAllPersons();

    for (int i = 0; i < users.length; i++) {
      if (users[i] == currentUser) {
        continue;
      }
      System.out.println(i + 1 + ")");
      showPerson(users[i]);
    }
    System.out.println("\nor write 'register' to register a new user\n");

    String input = readInputOrCancel("Select a number");
    if (input == null) {
      return;
    }
    if (input.equalsIgnoreCase("register")) {
      regsiterUser();
      return;
    }

    try {
      int choice = Integer.parseInt(input);

      if (choice < 0) {
        System.out.println("\nNumber cant be smaller than 1!\n");
        return;
      } else if (choice > users.length) {
        System.out.println("\nNumber cant be greater than " + users.length + "!\n");
        return;
      }

      for (int i = 0; i < users.length; i++) {
        if (choice - 1 == i) {
          currentUser = users[i];
        }
      }
      System.out.println("\nUser switched!");
      System.out.println("Current user logged in -> " + currentUser.getFullName() + "\n");

    } catch (IllegalArgumentException e) {
      System.out.println("\nCould not switch user: " + e.getMessage() + "\n");
    }
  }

  /**
   * A scanner method that allows user to cancel a registartion at any time
   *
   * @param promt
   * @return input
   */
  private String readInputOrCancel(String promt) {
    System.out.print(promt + " -> ");
    String input = scanner.nextLine().trim();

    if (input.equalsIgnoreCase("cancel")) {
      System.out.println("\nCancelled!\n");
      return null;
    }

    return input;
  }

  /**
   * Find a person by name. User can write firstname, lastname and both. Uses a
   * registry function thet returns the person
   *
   * @param name
   */
  private void findPerson(String name) {
    Person[] persons = personRegistry.getPerson(name);

    System.out.println("\n==== PERSON SEARCH ====");

    if (persons.length == 1) {
      System.out.println("Found one person with name " + name);
    } else if (persons.length >= 2) {
      System.out.println("Found multiple persons with name " + name);
    } else if (persons.length == 0) {
      System.out.println("No person found with name " + name);
    }

    showPersonList(persons);
  }

  /**
   * Finds a secific hike with a title-name.
   * If an activity dose not exist,
   * then returns a print to the user that the given title-name does not exist.
   *
   * @param title - title of activty to find
   */
  private void findActivityTitle(String title) {
    Activity[] activityList = activityRegistry.getActivityTitle(title, currentUser);

    if (activityList.length == 0) {
      System.out.println("\nNo hike found with title: " + title + "\n");
      return;
    } else if (activityList.length == 1) {
      System.out.println("\nFound hike " + title + "\n");
    } else if (activityList.length > 1) {
      System.out.println("\nFound multiple hikes with title: " + title + "\n");
    }

    showActivityList(activityList);
  }

  /**
   * Finds a secific activity/hike with a date.
   * If an activity dose not exist,
   * then returns a print to the user the no activity exist with the given date.
   *
   */
  private void findActivityDate(String dateString) {
    Activity[] activities = null;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    String[] parts = dateString.trim().split("\\s");

    try {
      if (parts.length == 1) {
        LocalDate date = LocalDate.parse(dateString, formatter);
        activities = activityRegistry.getActivityDate(date, currentUser);
      } else if (parts.length >= 2) {
        LocalDate startDate = LocalDate.parse(parts[0], formatter);
        LocalDate endDate = LocalDate.parse(parts[1], formatter);

        activities = activityRegistry.getActivityDateStartEnd(startDate, endDate, currentUser);
      }

      if (activities.length == 0 && parts.length == 1) {
        System.out.println("\nNo hike found with date: " + dateString + "\n");
        return;
      } else if (activities.length == 0 && parts.length >= 2) {
        System.out.println("\nNo hike found with dates: " + parts[0] + " and " + parts[1] + "\n");
        return;
      } else if (activities.length == 1 && parts.length == 1) {
        System.out.println("\nFound one hike with date: " + dateString + "\n");
      } else if (activities.length == 1 && parts.length >= 2) {
        System.out.println("\nFound one hike with dates: " + parts[0] + " and " + parts[1] + "\n");
      } else if (activities.length > 1 && parts.length >= 2) {
        System.out.println("\nFound multiple hikes with dates: " + parts[0] + " and " + parts[1] + "\n");
      } else if (activities.length > 1 && parts.length == 1) {
        System.out.println("\nFound multiple hikes with dates: " + dateString + "\n");
      }

      showActivityList(activities);
    } catch (DateTimeParseException e) {
      System.out.println("\nCould not find hike: " + dateString + " is not a valide date!\n");
    } catch (IllegalArgumentException e) {
      System.out.println("\nCould not find hike:" + e.getMessage() + "\n");
    }
  }

  /**
   * Shares one of the users activty with another person.
   */
  private void shareActivityPerson() {
    System.out.println("==== SHARE ACTIVITY ====");
    System.out.println("Write 'cancel' to end at any time\n");

    Activity[] activities = activityRegistry.getOwnActivities(currentUser);
    Person[] persons = personRegistry.getAllPersons();

    for (int i = 0; i < activities.length; i++) {
      System.out.println(i + 1 + ")");
      showActivity(activities[i]);
    }

    System.out.println("\nSelect a hike you want to share\n");

    String number = readInputOrCancel("Hike nr.");
    if (number == null) {
      return;
    }

    for (int i = 0; i < persons.length; i++) {
      System.out.println(i + 1 + ")");
      showPerson(persons[i]);
    }

    System.out.println("\nSelect the person you want to share the hike with\n");

    String personNr = readInputOrCancel("Person nr.");
    if (personNr == null) {
      return;
    }

    try {
      Person person = null;

      int choiceAct = Integer.parseInt(number);
      int choicePer = Integer.parseInt(personNr);

      if (choiceAct < 0) {
        System.out.println("\nHike number cant be less than 1!\n");
        return;
      } else if (choiceAct > activities.length) {
        System.out.println("\nHike number cant be greater than " + activities.length + "!\n");
        return;
      }

      if (choicePer < 0) {
        System.out.println("\nPerson number cant be less than 1!\n");
        return;
      } else if (choicePer > persons.length) {
        System.out.println("\nPerson number cant be greater than " + persons.length + "!\n");
        return;
      }

      for (int i = 0; i < persons.length; i++) {
        if (choicePer - 1 == i) {
          person = persons[i];
        }
      }

      for (int i = 0; i < activities.length; i++) {
        if (choiceAct - 1 == i) {
          activities[i].addCanSeeActivity(person);
        }
      }

      System.out.println("Hike shared with " + person.getFullName() + "!\n");

    } catch (IllegalArgumentException e) {
      System.out.println("\nCould not share hike with " + personNr + " " + e.getMessage() + "\n");
    }
  }

  /**
   * Deletes/removes an activity. Shows all activitys made by currentuser with a
   * number related to it. User picks number with activity it want to delete.
   * Then deletes the activity from the activity register.
   */
  private void deleteActivity() {
    Activity[] activityList = activityRegistry.getOwnActivities(currentUser);

    if (activityList.length == 0) {
      System.out.println("\nNo hikes found, no hikes to delete!\n");
      return;
    }

    System.out.println("\n==== DELETE ACTIVITY ====");
    System.out.println("Write 'cancel' to end at any time");
    System.out.println("Select one of the activities to delete below\n");

    for (int i = 0; i < activityList.length; i++) {
      System.out.println(i + 1 + ")");
      showActivity(activityList[i]);
    }

    String input = readInputOrCancel("Select a number");
    if (input == null) {
      return;
    }

    try {
      int choice = Integer.parseInt(input);
      if (choice > activityList.length || choice <= 0) {
        System.out.println("\nCould not delete activity:");
        System.out.println(choice + " is not related to an activity!\n");
        return;
      }

      activityRegistry.deleteActivity(activityList[choice - 1], currentUser);
      System.out.println("\nHike: " + activityList[choice - 1].getTitle() + ", is deleted\n");

    } catch (IllegalArgumentException e) {
      System.out.println("\nCould not delete activity: " + e.getMessage());
    }
  }

  /**
   * shows a Persons info.
   *
   * @param person
   */
  private void showPerson(Person person) {
    System.out.println("Name:     " + person.getFullName());
    if (person.hasGroup()) {
      System.out.println("Group:    " + person.getHikeGroup().getName());
    } else {
      System.out.println("Group:    no group");
    }
    System.out.println("Hikes:    " + person.getHikesRegistered() + "\n");
  }

  /**
   * shows all users/persons
   */
  private void showAllUsers() {
    Person[] users = personRegistry.getAllPersons();
    System.out.println("==== USERS ====\n");
    System.out.println("shows all users");
    for (int i = 0; i < users.length; i++) {
      if (users[i] == currentUser) {
        System.out.println("== YOU ==");
      }
      showPerson(users[i]);
    }
  }

  /**
   * Prints out the menu of the app.
   * It contains the most usefull commands to navigate the application
   */
  private void showMenu() {
    System.out.println("\n==== WELCOME TO THE HIKE APP ====");
    System.out.println("register hike:          -> to register a hike");
    System.out.println("all:                    -> see all your hike activitys and activitys shared with you");
    System.out.println("my hikes                -> shows all hikes logged by you");
    System.out.println("switch user             -> switch user to a selected profile");
    System.out.println("help:                   -> shows more options and how to use the appllication");
    System.out.println("exit:                   -> exit the application");
    System.out.println("choose a command:\n");
    System.out.println("Current user logged in -> " + currentUser.getFullName());

  }

  /**
   * Shows all options on how to use the application.
   * It prints out lines of all the commands that is available.
   * One line is a description on how to use the commands
   */
  private void showHelpOptions() {
    // some options are not made yet
    System.out.println("\n==== Help MENU ====");
    System.out.println("all                         -> see all your hike activitys and hikes shared with you");
    System.out.println("my hikes                    -> shows all hikes logged by you");
    System.out.println("shared                      -> shows all hikes shared with you");
    System.out.println("switch user                 -> switch user to a selected acount or register a new user.");
    System.out.println("users                       -> shows all persons/users in the hike registry");
    System.out.println("groups                      -> shwos all groups in the hike registry");
    System.out.println("switch group                -> switch group to a new group");
    System.out.println("join group                  -> to join a new hike group");
    System.out.println(
        "find person 'name'          -> finds a person by firstname/lastname/fullname and shows their profile");
    System.out.println("find title 'title'           -> finds a specific hike that user can see");
    System.out.println("find date 'date'            -> find hikes on a spesific date");
    System.out.println("find hike 'start' 'end'    -> find hikes between to hikes");
    System.out.println("find group 'name'           -> shows this groups activitys");
    System.out.println("register hike               -> to registe a new hike");
    System.out.println("register group              -> to registe a new hike group");
    System.out.println("register user               -> to registe a new user");
    System.out.println("edit hike                   -> to change a hikes sharing status");
    System.out.println("delete hike                 -> menu of all your hikes to deletes a specific activity");
    System.out.println("me                          -> shows your profile");
    System.out.println("help                        -> show all options");
    System.out.println("clear                       -> clears previus lines in the terminal");
    System.out.println("exit                        -> exits the application");
    System.out.println("\nHOW TO:");
    System.out
        .println("This application behave like a terminal and have different commands to do different tasks.");
    System.out.println("You use the app by using the commands shown over.");
    System.out.println("\nCurrent user logged in -> " + currentUser.getFullName());

    // System.out.println("my group -> shows information about your group and shows
    // all hikes connected to your group");
  }

  /**
   * Clears previus prints so the terminal does not feel clauded, does not work
   * great in an IDE or editior. Works best in a normal terminal
   */
  private static void clearPreviusLines() {
    System.out.print("\033[H\033[2J\033[3J");
    System.out.flush();
  }
}
