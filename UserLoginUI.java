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
  private final String USAGE = "Usage: $ java UserLoginUI <add|login> <username> <password> [<debug_lvl>]";
  private final boolean DEBUG_SUPPORTED = false;
  private String mode;
  private String username;
  private String password;
  private int debugLvl;

  /**
   * TODO javadoc
   * @param args
   */
  public static void main(String[] args) {
    UserLoginUI ui = new UserLoginUI();
    if (!ui.parseArgs(args)) {
      return;
    }

    if (ui.mode.equals("add")) {
      // TODO implement add user
      System.out.println("Add user not yet implemented.");
      /*
       * Use a password manager class to handle adding users and verifying passwords.
       * Use a database class to handle hashed password storage.
       */
    } else if (ui.mode.equals("login")) {
      // TODO implement login user
      System.out.println("Login user not yet implemented.");
    } else {
      System.out.println("Invalid mode. This should never happen.");
    }
  }

  /**
   * Basic constructor.
   */
  public UserLoginUI() {
    // TODO implement method
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
      System.out.println("Invalid number of arguments.");
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

  /**
   * Represents a user with a username and an encrypted password.
   * 
   * @version 1.0
   * @author Isaac Hager
   */
  private class User {
    private String username;
    private String passwordHash;

    /**
     * Basic constructor.
     * @param username
     * @param passwordHash
     */
    public User(String username, String passwordHash) {
      this.username = username;
      this.passwordHash = passwordHash;
    }

    /**
     * Returns username.
     * @return username
     */
    public String getUsername() {
      return username;
    }

    /**
     * Sets username.
     * @param username
     */
    public void changeUsername(String username) {
      this.username = username;
    }

    /**
     * Returns password hash.
     * @return password hash
     */
    public String getPasswordHash() {
      return passwordHash;
    }
  }

}
