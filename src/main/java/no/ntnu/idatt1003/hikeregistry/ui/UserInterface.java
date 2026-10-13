package no.ntnu.idatt1003.hikeregistry.ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import no.ntnu.idatt1003.hikeregistry.model.Activity;
import no.ntnu.idatt1003.hikeregistry.model.Person;

public class UserInterface {
    private Activity[] activitys;

    /**
     * Inizialize the user interface and makes the app ready to use
    */
    public void init(){
        LocalDateTime now = LocalDateTime.now();

        Person[] persons = {
            new Person("Brotherman", "Testern", "brother-gruppen"),
            new Person("Testern", "Brotherman", "testern-gruppen"),
        };

        activitys = new Activity[]{
            new Activity("En lang tur", "Turen var veldig bra og lang, ble velsig sliten", 10, 7, now, persons[0], 1),
            new Activity("En kort tur", "Turen var kort og kjedelig. Bare skog :(", 3, 3, now.minusDays(2).minusHours(5), persons[0], 2),
            new Activity("Fjelltur", "Jeg gikk Gaustadtoppen. Det var bratt, men det gikk tilslutt.", 30, 8, now.minusMonths(2).minusDays(5).plusHours(2).plusMinutes(10), persons[1], 3),
        };
    }

    /**
     * Starts the user interface of the app 
    
    */
    public void start(){
        for (int i = 0; i < activitys.length; i++) {
            showActivity(activitys[i]);
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
}
