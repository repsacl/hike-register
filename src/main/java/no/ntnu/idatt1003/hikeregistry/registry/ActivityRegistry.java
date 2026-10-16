package no.ntnu.idatt1003.hikeregistry.registry;

import java.util.ArrayList;

import no.ntnu.idatt1003.hikeregistry.model.Activity;

public class ActivityRegistry {
    // valgte dette for det har flere funksjoner til seg som gjøre det lettere å håndtere data
    private final ArrayList<Activity> activitys = new ArrayList<>();

    // legger tl en ny aktivitet til registeret
    public void addActivity(Activity newActivity) {
        activitys.add(newActivity);
    }

    // retunerer alle aktivitetene som et array, er mer kjent med array og syntes det er lettere å håndtere et for å vise fram aktivitetene
    public Activity[] getAllActivitys(){
        return activitys.toArray(new Activity[0]);
    }

    public Activity getActivity(String title) {
        Activity act = null;
        for (int i = 0; i < activitys.size(); i++) {
            if(title.equals(activitys.get(i).getTitle())){
                act = activitys.get(i);
            }
        }

        return act;
    }
}
