package no.ntnu.idatt1003.hikeregistry;

import no.ntnu.idatt1003.hikeregistry.ui.UserInterface;


public class App {
    public static void main(String[] args) {
        UserInterface ui = new UserInterface();

        ui.init();
        ui.start();
    }
}
