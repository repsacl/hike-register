package no.ntnu.idatt1003.hikeregistry.ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import no.ntnu.idatt1003.hikeregistry.model.Activity;
import no.ntnu.idatt1003.hikeregistry.model.Person;
import no.ntnu.idatt1003.hikeregistry.registry.ActivityRegistry;
import no.ntnu.idatt1003.hikeregistry.registry.PersonRegistry;

public class UserInterface {
    private boolean running = true;

    private final ActivityRegistry activityRegistry = new ActivityRegistry();
    private final PersonRegistry personRegistry = new  PersonRegistry();

    private final Scanner scanner = new  Scanner(System.in);

    /**
     * Inizialize the user interface and makes the app ready to use.
     * Pre builds som activitys.
    */
    public void init(){
        LocalDateTime now = LocalDateTime.now();

        personRegistry.addPerson(new Person("Brotherman", "Testern", "brother-gruppen"));
        personRegistry.addPerson(new Person("Testern", "Brotherman", "testern-gruppen"));
        Person[] persons = personRegistry.getAllPersons();

        activityRegistry.addActivity(new Activity("En lang tur", "Turen var veldig bra og lang, ble velsig sliten", 10, 10, now, persons[0], 1));
        activityRegistry.addActivity(new Activity("En kort tur", "Turen var kort og kjedelig. Bare skog :(", 3, 3, now.minusDays(2).minusHours(5), persons[0], 2));
        activityRegistry.addActivity(new Activity("Fjelltur", "Jeg gikk Gaustadtoppen. Det var bratt, men det gikk tilslutt.", 30, 8, now.minusMonths(2).minusDays(5).plusHours(2).plusMinutes(10), persons[1], 3));
    }

    /**
     * Starts the user interface of the app
    */
    public void start(){
        showMenu();

        while (running){
            showScanner();
        }
    }

    private void showScanner(){
        System.out.print("-> ");
        String input = scanner.nextLine().trim();

        String[] parts = input.split("\\s+");
        String command = parts[0].toLowerCase();

        // System.out.println(parts[2]);

        // better to use switch because 
        switch (command) {
            case "find" -> {
                if (parts.length >= 3 && parts[1].equals("person")) {
                    clearPreviusLines();
                    findPerson(parts[2]);
                }
                else if (parts.length >= 3 && parts[1].equals("hike")) {
                    clearPreviusLines();
                    findActivity(parts[2]);
                }
            }
            case "register" -> {
                registerActivity();
            }
            case "all" -> {
                showAllActivites();
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
            case "" -> {}
            default -> {
                System.out.println("Unknown command: " + input + " try 'help' to se all commands");
            }
        }
    }

    private void showActivity(Activity activity){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        String outPrint = 
            "Title:            " + activity.getTitle() +
            "\nDescription:      " + activity.getDescription() + 
            "\nDistance:         " + activity.getDistanceKm() + "km" + 
            "\nRating:           " + activity.getRating() + "/10" + 
            "\nLogged by:        " + activity.getLogger().getFullName() +
            "\nTime registered:  " + formatter.format(activity.getTimeRegistered()) + "\n";
        System.out.println(outPrint);
    }

    private void showAllActivites(){
        Activity[] activitys = activityRegistry.getAllActivitys();

        for (int i = 0; i < activitys.length; i++) {
            showActivity(activitys[i]);
        }  
    }

    private void registerActivity(){
        System.out.print("=== REGISTER A HIKE ====\n");

        System.out.print("Title -> ");
        String title = scanner.nextLine().trim();

        System.out.print("Description -> ");
        String description = scanner.nextLine().trim();

        System.out.print("Distance in km -> ");
        String distance = scanner.nextLine().trim();

        System.out.print("Rating 1-10 -> ");
        String rating = scanner.nextLine().trim();

        try{
            double dist = Double.parseDouble(distance);
            int rate = Integer.parseInt(rating);
            LocalDateTime now = LocalDateTime.now();

            Activity activity = new Activity(title, description, dist, rate, now, personRegistry.getPerson("Testern"), 5);

            activityRegistry.addActivity(activity);

            System.out.println("\nHike activity registered!");
        }
        catch (NumberFormatException e){
            System.out.println("Rating or double must be a valid number");
        }
        catch (IllegalArgumentException e) {
            System.out.println("Could not register activity " + e.getMessage());
        }
    }

    private void findPerson(String firstName){
        Person person = personRegistry.getPerson(firstName);

        if (person == null) {
            System.out.println("No person found with firstname " + firstName);
            return;
        }

        // perhaps make person to have count of made activities.
        // or show all activities made by this person
        System.out.println("Found " + person.getFullName() + "\n" + person.getFirstName() + " have made... hikes");
    }

    private void findActivity(String title) {
        Activity activity = activityRegistry.getActivity(title);

        if (activity == null) {
            System.out.println("No hike found with title: " + title);
            return;
        }

        System.out.println("Found activity " + title);
        showActivity(activity);
    }

    private void showMenu(){
        System.out.println("==== WELCOME TO THE HIKE APP ====");
        System.out.println("register:   to register a hike");
        System.out.println("all:        to se all activities");
        System.out.println("help:       to see more options and how to use the appllication");
        System.out.println("exit:       to exit the application");
        System.out.println("\nchoose an option: ");                    
    }

    private void showHelpOptions(){
        // some options are not made yet
        System.out.println("==== Help MENU ====");
        System.out.println("all                         -> shows all public hike activitys");
        System.out.println("find person <firstname>     -> finds a person and shows their profile");
        System.out.println("find hike <title>           -> finds a specific hike");
        System.out.println("find group <name>           -> shows this groups activitys");
        System.out.println("shared                      -> shows shared activitys to this user");
        System.out.println("register                    -> to registe a new hike");
        System.out.println("profile                     -> login to your profile/create profile/show your profile/");
        System.out.println("help                        -> show all options");
        System.out.println("exit                        -> exits the application");
        System.out.println("\nHOW TO:");
        System.out.println("This application behave like a terminal and have different commands to do different tasks.");
        System.out.println("You use the app by writing the commands shown over.");
        // System.out.println("mine -> to show your hikes");
    }

    // clears previus prints so the terminal does not feel clauded
    private void clearPreviusLines(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
