/**
 * Simple command line user interface. Allows users to
 * add a password to the password database as well as
 * verify a password against the database.
 * 
 * Usage: $ java UserLoginUI <add|login> <username> <password> [<debug_lvl>]
 * where  add - adds a user to the database
 *        login - verifies a user against the database
 *        username - [A-Za-z0-9_]{1,16}
 *        password - [A-Za-z0-9_!@#$%^&*()]{1,16}
 *        debug_lvl - currently unused, defaults to 0
 * 
 * @version 1.0
 * @author Isaac Hager
 */
public class UserLoginUI {
  private final String USAGE = "Usage: $ java UserLoginUI <add|login> <username> <password>";
  private final boolean DEBUG_SUPPORTED = false;
  private String mode;
  private String username;
  private String password;
  private int debugLvl;
  private PasswordManager pm;

  /**
   * Runs the UI for a user to login or add other users
   * @param args
   */
  public static void main(String[] args) {
    UserLoginUI ui = new UserLoginUI();
    if (!ui.parseArgs(args)) {
      return;
    }

    if (ui.mode.equals("add")) {
      ui.pm.addUser(ui.username, ui.password);
    } else if (ui.mode.equals("login")) {
      if (ui.pm.verifyPassword(ui.username, ui.password)) {
        System.out.println("Login successful"); // This is all that logging in currently does.
      } else {
        System.out.println("Login failed");
      }
    } else {
      System.out.println("Invalid mode. This should never happen.");
    }
  }

  /**
   * Basic constructor.
   */
  public UserLoginUI() {
    pm = new PasswordManager();
  }

  /**
   * Parses command line arguments into class variables.
   * Returns true if arguments are valid.
   * @param args
   * @return true if arguments are valid, else false
   */
  @SuppressWarnings("unused")
  private boolean parseArgs(String[] args) {
    boolean valid = true;
    /* Check Length */
    if (args.length < 3 || args.length > 4) {
      System.out.println(USAGE);
      valid = false;
    } else {
      /* First arg */
      if (args[0].equals("add") || args[0].equals("login")) {
        this.mode = args[0];
      } else {
        System.out.println("Invalid mode. Must be 'add' or 'login'.");
        System.out.println(USAGE);
        valid = false;
      }

      /* Second arg */
      if (args[1].matches("[A-Za-z0-9_]{1,16}")) {
        this.username = args[1];
      } else {
        System.out.println("Invalid username. Must be 1-16 characters of [A-Za-z0-9_].");
        System.out.println(USAGE);
        valid = false;
      }

      /* Third arg */
      if (args[2].matches("[A-Za-z0-9_!@#$%^&*()]{1,16}")) {
        this.password = args[2];
      } else {
        System.out.println("Invalid password. Must be 1-16 characters of [A-Za-z0-9_!@#$%^&*()].");
        System.out.println(USAGE);
        valid = false;
      }

      /* If debug is implemented, check fourth arg */
      if (DEBUG_SUPPORTED && args.length == 4) {
        try {
          this.debugLvl = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
          System.out.println("Debug level must be an integer.");
          System.out.println(USAGE);
          valid = false;
        }
      } else {
        this.debugLvl = 0;
      }
    }
    return valid;
  }
}
