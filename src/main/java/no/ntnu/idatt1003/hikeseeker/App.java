package no.ntnu.idatt1003.hikeseeker;

import no.ntnu.idatt1003.hikeseeker.ui.UserInterface;

/**
 * Application of the hike-seeker.
 */
public class App {
  /**
   * Main loop.
   *
   * @param args - empty args in this application.
   */
  public static void main(String[] args) {
    UserInterface ui = new UserInterface();

    ui.init();
    ui.start();
  }
}
