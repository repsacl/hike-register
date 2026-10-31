package no.ntnu.idatt1003.hikeregistry.ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;

import no.ntnu.idatt1003.hikeregistry.model.Activity;
import no.ntnu.idatt1003.hikeregistry.model.Group;
import no.ntnu.idatt1003.hikeregistry.model.Person;
import no.ntnu.idatt1003.hikeregistry.registry.ActivityRegistry;
import no.ntnu.idatt1003.hikeregistry.registry.GroupRegistry;
import no.ntnu.idatt1003.hikeregistry.registry.PersonRegistry;

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
        } else if (parts.length >= 3 && parts[1].equalsIgnoreCase("hike")) {
          clearPreviusLines();
          findActivityTitle(parts[2].replace("\"", ""));
        } else if (parts.length == 2) {
          System.out
              .println("Unknown command: " + parts[1] + " try 'find hike <title>' or 'help' to se all commands");
        } else {
          System.out
              .println("Find what? Try 'find hike <title>', 'find person <firstname>' or 'help' to see all commands");
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
              .println("Register what? Try 'register hike', 'register group' or 'help' to see more register commands");
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
        if (parts.length >= 3 && parts[1].equalsIgnoreCase("hike")) {
          clearPreviusLines();
          deleteActivityTitle(parts[2].replace("\"", ""));
        } else if (parts.length == 2) {
          System.out
              .println("Unknown command: " + parts[1] + " try 'delete hike <title>' or 'help' to se all commands");
        } else {
          System.out.println("Delete what? Try 'delete hike <title>' or 'help' to se all commands");
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

  private void showUser() {
    System.out.println("\n==== CURRENT USER ====");
    showPerson(currentUser);
  }

  /**
   * Shows information about a specific activity.
   *
   * @param activity
   */
  private void showActivity(Activity activity) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    System.out.println("Title:                  " + activity.getTitle());
    System.out.println("Description:            " + activity.getDescription()); // + "\n");
    System.out.println("Distance:               " + activity.getDistanceKm() + "km");
    System.out.println("Rating:                 " + activity.getRating() + "/10");
    System.out.println("Logged by:              " + activity.getLogger().getFullName());
    if (activity.hasGroup()) {
      System.out.println("Group:                  " + activity.getGroup().getName());
    }
    System.out.println("Time registered:        " + formatter.format(activity.getTimeRegistered()) + "\n");
  }

  /**
   * Shows all activitys shared with the current user.
   */
  private void showAllActivites() {
    Activity[] activitys = activityRegistry.getAllActivitys(currentUser);
    System.out.println("==== ALL ACTIVITYS ====");
    System.out.println("shows all your private activitys and activtys shared with you\n");

    for (int i = 0; i < activitys.length; i++) {
      showActivity(activitys[i]);
    }
  }

  private void showActivityList(Activity[] activityList) {
    System.out.println();

    for (int i = 0; i < activityList.length; i++) {
      showActivity(activityList[i]);
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

    // String group = null;
    // if (currentUser.hasGroup()) {
    // System.out.println("\nRegister this activity in your group or private");
    // group = readInputOrCancel("private");
    // }

    try {
      double dist = Double.parseDouble(distance);
      int rate = Integer.parseInt(rating);
      LocalDateTime now = LocalDateTime.now();

      Activity activity = new Activity(title, description, dist, rate, now, currentUser,
          currentUser.getHikeGroup());

      activityRegistry.addActivity(activity);

      System.out.println("\nHike activity registered!");
      showActivity(activity);
    } catch (NumberFormatException e) {
      System.out.println("Could not register activity: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      System.out.println("Could not register activity: " + e.getMessage());
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

      newGroup.addMember(currentUser);
      groupRegistry.addGroup(newGroup);

      System.out.println("\nGroup registered!");
    } catch (IllegalArgumentException e) {
      System.out.println("Could not register group: " + e.getMessage());
    }
  }

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

      for (int i = 0; i < groups.length; i++) {
        if (choice - 1 == i) {
          groups[i].addMember(currentUser);
        }
      }
      System.out.println("\nGroup joind!");
      System.out.println("Current group -> " + currentUser.getHikeGroup().getName());

    } catch (IllegalArgumentException e) {
      System.out.println("Could not join a group: " + e.getMessage());
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
      System.out.println("Could not register user: " + e.getMessage());
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

      for (int i = 0; i < users.length; i++) {
        if (choice - 1 == i) {
          currentUser = users[i];
        }
      }
      System.out.println("\nUser switched!");
      System.out.println("Current user logged in -> " + currentUser.getFullName());

    } catch (IllegalArgumentException e) {
      System.out.println("Could not switch user: " + e.getMessage());
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
    Person person = personRegistry.getPerson(name);

    if (person == null) {
      System.out.println("No person found with name " + name);
      return;
    }

    // perhaps make person to have count of made activities.
    // or show all activities made by this person
    System.out.println("\n==== PERSON SEARCH ====");
    System.out.println(" Found " + person.getFullName() + "\n" + person.getFirstName() + " have registrered "
        + person.getHikesRegistered() + " hikes");
  }

  /**
   * A function to find a secific activity/hike
   * If an activity dose not exist,
   * then returns a print to the user with that the activity does not exist
   */
  private void findActivityTitle(String title) {
    Activity[] activityList = activityRegistry.getActivityTitle(title, currentUser);

    if (activityList.length == 0) {
      System.out.println("No hike found with title: " + title);
      return;
    } else if (activityList.length == 1) {
      System.out.println("Found hike " + title);
    } else if (activityList.length > 1) {
      System.out.println("Found multiple hikes with title: " + title);
    }

    showActivityList(activityList);
  }

  /**
   * Deletes/removes a activity using a paramater containing the title of an
   * activity/hike.
   * First finds the activity by the getActivityTitle function.
   * Then deletes the activity from the activity register.
   */
  private void deleteActivityTitle(String title) {
    Activity[] activityList = activityRegistry.getActivityTitle(title, currentUser);

    if (activityList.length == 0) {
      System.out.println("No hike found with title: " + title);
      return;
    } else if (activityList.length == 1) {
      activityRegistry.deleteActivity(activityList[0]);
      System.out.println("Hike: " + title + ", is deleted");
      return;
    } else if (activityList.length > 1) {
      System.out.println("Found multiple hikes with title: " + title);
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

      activityRegistry.deleteActivity(activityList[choice - 1]);
      System.out.println("Hike: " + title + " " + choice + ", is deleted");

    } catch (IllegalArgumentException e) {
      System.out.println("Could not delete activity: " + e.getMessage());
    }
  }

  public Person getCurrentUser() {
    return currentUser;
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
    System.out.println("all:                    -> see all hike activitys shared with you");
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
    System.out.println("all                         -> see all public hike activitys shared with you");
    System.out.println("switch user                 -> switch user to a selected acount or register a new user.");
    System.out.println("switch group                -> switch group to a new group");
    System.out.println("join group                  -> to join a hike group");
    System.out.println(
        "find person 'name'          -> finds a person by firstname/lastname/fullname and shows their profile");
    System.out.println("find hike 'title'           -> finds a specific hike");
    System.out.println("find group 'name'           -> shows this groups activitys");
    System.out.println("register hike               -> to registe a new hike");
    System.out.println("register group              -> to registe a new hike group");
    System.out.println("register user               -> to registe a new user");
    System.out.println("delete hike 'title'         -> deletes a specific hike");
    System.out.println("me                          -> shows your profile and hikes");
    System.out.println("help                        -> show all options");
    System.out.println("clear                       -> clears previus lines in the terminal");
    System.out.println("exit                        -> exits the application");
    System.out.println("\nHOW TO:");
    System.out
        .println("This application behave like a terminal and have different commands to do different tasks.");
    System.out.println("You use the app by using the commands shown over.");
    System.out.println("\nCurrent user logged in -> " + currentUser.getFullName());

    // System.out.println("my hikes -> shows your hikes");

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
