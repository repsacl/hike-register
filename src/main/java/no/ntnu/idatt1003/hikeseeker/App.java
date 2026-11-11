package no.ntnu.idatt1003.hikeseeker;

import no.ntnu.idatt1003.hikeseeker.ui.UserInterface;

public class App {
  public static void main(String[] args) {
    UserInterface ui = new UserInterface();

    ui.init();
    ui.start();
  }
}
