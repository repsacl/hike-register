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

  /**
   * Inizialize the user interface and makes the app ready to use.
   * Pre builds som activitys.
   */
  public void init() {
    LocalDateTime now = LocalDateTime.now();
    groupRegistry.addGroup(new Group("Testern-gruppen"));
    groupRegistry.addGroup(new Group("Mountain-group"));
    Group[] groups = groupRegistry.getAllGroups();

    personRegistry.addPerson(new Person("Brotherman", "Testern", groups[0]));
    personRegistry.addPerson(new Person("Bob", "Bobsen", groups[0]));
    personRegistry.addPerson(new Person("Per", "Ola", groups[1]));
    Person[] persons = personRegistry.getAllPersons();

    activityRegistry.addActivity(new Activity("En lang tur", "Turen var veldig bra og lang, ble velsig sliten", 10,
        10, now, persons[0], 1));
    activityRegistry.addActivity(new Activity("En kort tur", "Turen var kort og kjedelig. Bare skog :(", 3, 3,
        now.minusDays(2).minusHours(5), persons[1], persons[1].getHikeGroup(), 2));
    activityRegistry
        .addActivity(new Activity("Fjelltur", "Jeg gikk til Gaustadtoppen. Det var bratt, men det gikk tilslutt.",
            30, 8, now.minusMonths(2).minusDays(5).plusHours(2).plusMinutes(10), persons[2], persons[2].getHikeGroup(),
            3));
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

  private void showScanner() {
    System.out.print("-> ");
    String input = scanner.nextLine().trim();

    String[] parts = input.trim().split("\\s", 3);
    String command = parts[0].toLowerCase();

    System.out.println(Arrays.toString(parts));

    // better to use switch because
    switch (command) {
      case "find" -> {
        if (parts.length >= 3 && parts[1].equalsIgnoreCase("person")) {
          clearPreviusLines();
          findPerson(parts[2].replace("\"", ""));
        } else if (parts.length >= 3 && parts[1].equalsIgnoreCase("hike")) {
          clearPreviusLines();
          findActivity(parts[2].replace("\"", ""));
        } else if (parts.length == 2) {
          System.out
              .println("Unknown command: " + parts[1] + " try 'find hike <title>' or 'help' to se all commands");
        } else {
          System.out
              .println("Find what? Try 'find hike <title>', 'find person <firstname>' or 'help' to see all commands");
        }
      }
      case "register" -> {
        clearPreviusLines();
        registerActivity();
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
      case "exit" -> {
        clearPreviusLines();
        System.out.println("Goodbye!");
        running = false;
      }
      case "" -> {
      }
      default -> {
        System.out.println("Unknown command: " + command + " try 'help' to se all commands");
      }
    }
  }

  // a function to show the information in a activity
  private void showActivity(Activity activity) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    System.out.println("Title:                  " + activity.getTitle());
    System.out.println("Description:            " + activity.getDescription());
    System.out.println("Distance:               " + activity.getDistanceKm() + "km");
    System.out.println("Rating:                 " + activity.getRating() + "/10");
    System.out.println("Logged by:              " + activity.getLogger().getFullName());
    if (activity.hasGroup()) {
      System.out.println("Group:                  " + activity.getGroup().getName());
    }
    System.out.println("Time registered:        " + formatter.format(activity.getTimeRegistered()) + "\n");
  }

  // shows all activitys in the register
  private void showAllActivites() {
    Activity[] activitys = activityRegistry.getAllActivitys();

    for (int i = 0; i < activitys.length; i++) {
      showActivity(activitys[i]);
    }
  }

  // regsiter a activity
  private void registerActivity() {
    System.out.println("=== REGISTER A HIKE ====");
    System.out.println("Write 'cancel' to end the registration\n");

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

    try {
      double dist = Double.parseDouble(distance);
      int rate = Integer.parseInt(rating);
      LocalDateTime now = LocalDateTime.now();

      Activity activity = new Activity(title, description, dist, rate, now, personRegistry.getPerson("Testern"),
          5);

      activityRegistry.addActivity(activity);

      System.out.println("\nHike activity registered!");
    } catch (NumberFormatException e) {
      System.out.println("Rating or distance must be a number");
    } catch (IllegalArgumentException e) {
      System.out.println("Could not register activity " + e.getMessage());
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
      System.out.println("Regsistation cancelled");
      return null;
    }

    return input;
  }

  /**
   * Find a person by name. Uses a registry function thet returns the person
   *
   * @param firstName
   */
  private void findPerson(String firstName) {
    Person person = personRegistry.getPerson(firstName);

    if (person == null) {
      System.out.println("No person found with firstname " + firstName);
      return;
    }

    // perhaps make person to have count of made activities.
    // or show all activities made by this person
    System.out.println(" Found " + person.getFullName() + "\n" + person.getFirstName() + " have made... hikes");
  }

  /**
   * A function to find a secific activity/hike
   * If an activity dose not exist,
   * then returns a print to the user with that the activity does not exist
   */
  private void findActivity(String title) {
    Activity activity = activityRegistry.getActivityTitle(title);

    if (activity == null) {
      System.out.println("No hike found with title: " + title);
      return;
    }

    System.out.println("Found hike " + title);
    showActivity(activity);
  }

  /**
   * Deletes/removes a activity using a paramater containing the title of an
   * activity/hike.
   * First finds the activity by the getActivityTitle function.
   * Then deletes the activity from the activity register.
   */
  private void deleteActivityTitle(String title) {
    Activity activity = activityRegistry.getActivityTitle(title);

    if (activity == null) {
      System.out.println("No hike found with title: " + title);
      return;
    }
    activityRegistry.deleteActivity(activity);

    System.out.println("Hike: " + title + ", is deleted");
  }

  /**
   * Prints out the menu of the app.
   * It contains the most usefull commands to navigate the application
   */
  private void showMenu() {
    System.out.println("==== WELCOME TO THE HIKE APP ====");
    System.out.println("register:     to register a hike");
    System.out.println("all:          to se all activities");
    System.out.println("help:         to see more options and how to use the appllication");
    System.out.println("exit:         to exit the application");
    System.out.println("\nchoose an option: ");
  }

  /**
   * Shows all options on how to use the application.
   * It prints out lines of all the commands that is availabael.
   * Ends with a line on how to use the commands
   */
  private void showHelpOptions() {
    // some options are not made yet
    System.out.println("==== Help MENU ====");
    System.out.println("all                         -> shows all public hike activitys shared with you");
    System.out.println("find person 'firstname'     -> finds a person and shows their profile");
    System.out.println("find hike 'title'           -> finds a specific hike");
    System.out.println("find group 'name'           -> shows this groups activitys");
    System.out.println("delete hike 'title'         -> deletes a specific hike");
    System.out.println("shared                      -> shows shared activitys to this user");
    System.out.println("register                    -> to registe a new hike");
    System.out.println("profile                     -> login to your profile/create profile/show your profile/");
    System.out.println("help                        -> show all options");
    System.out.println("clear                       -> clears previus lines in the terminal");
    System.out.println("exit                        -> exits the application");
    System.out.println("\nHOW TO:");
    System.out
        .println("This application behave like a terminal and have different commands to do different tasks.");
    System.out.println("You use the app by using the commands shown over.");
    // System.out.println("mine -> to show your hikes");
  }

  /**
   * Clears previus prints so the terminal does not feel clauded, does not work
   * great in an IDE or editior. Works best in a normal terminal
   */
  private void clearPreviusLines() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }
}
