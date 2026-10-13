package no.ntnu.idatt1003.hikeregistry.model;

import java.time.LocalDateTime;

public class Activity {

    private final String title; // tittel på turen. som regel kort og presist
    private final String description; // beskrivelse av hvordan aktiviteten/turen var
    private final double distanceKm; // lengde på turen i kilometer
    private final int rating; // rangere turen fra 1-10
    private final LocalDateTime timeRegistered; // dato/tidspunkt på nå turen ble registert
    private final Person logger; // hvem som loggførte turen
    private final int id;

    // deklarerre variablene
    public Activity(String title, String description, double distanceKm, int rating, LocalDateTime timeRegistered, Person logger, int id){
        this.title = title;
        this.description = description;
        this.distanceKm = distanceKm;
        this.rating = rating;
        this.timeRegistered = timeRegistered;
        this.logger = logger;
        this.id = id;
    }

    // retunerer titel på turen, eks kan brukes for å søke etter titel
    public String getTitle(){
        return title;
    }

    // returnerer beskrivels av truen
    public String getDescription(){
        return description;
    }

    // returnerer distansen gått i turen
    public double getDistanceKm(){
        return distanceKm;
    }

    // retunerer rating av turen
    public int getRating(){
        return rating;
    }

    // retunere tidspunkt på når turen ble registrert
    public LocalDateTime getTimeRegistered(){
        return timeRegistered;
    }

    // returnerer hvem som logget aktiviteten
    public Person getLogger(){
        return logger;
    }

    // retunerer aktiviteten sin id
    public int getId(){
        return id;
    }
}
