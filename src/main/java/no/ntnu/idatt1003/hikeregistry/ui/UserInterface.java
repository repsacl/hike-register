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
     * Inizialize the user interface and makes the app ready to use
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

    private void showScanner(){
        System.out.println("> ");
        String input = scanner.next().trim().toLowerCase();
        String[] parts = input.split("\\s+");

        if(parts.length > 2){
            switch (parts[0]) {
                case "find" -> {
                    clearPreviusLines();
                    findPerson(parts[1]);
                }
                case "help" -> {
                    clearPreviusLines();
                    showHelpOptions();
                }
                case "exit" -> {
                    clearPreviusLines();
                    System.out.println("Goodbye!");
                    running = false;
                }
                case "" -> {}
                default -> {
                    System.out.println("Unknown kommand: " + input + " try 'help' to se all commands");
                }
            }
        }
        else { //if (parts.length <= 1){
            switch (input) {
                case "register" -> {
                    clearPreviusLines();
                    System.out.println("register a hike...");
                }
                case "all" -> {
                    clearPreviusLines();
                    showAllActivites();
                }
                case "help" -> {
                    clearPreviusLines();
                    showHelpOptions();
                }
                case "exit" -> {
                    clearPreviusLines();
                    System.out.println("Goodbye!");
                    running = false;
                }
                case "" -> {}
                default -> {
                    System.out.println("Unknown kommand: " + input + " try 'help' to se all commands");
                }
            }
        }
    }

    private void registerActivity(){

    }

    private void findPerson(String firstName){
        Person person = personRegistry.getPerson(firstName);

        // make person to have count of made activities.
        System.out.println("Found " + person.getFullName() + "\n" + person.getFirstName() + " have made... activities");
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
        System.out.println("==== Help MENU ====");
        System.out.println("help");
    }

    private void clearPreviusLines(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
