import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

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
 * @author Isaac Hager and Tanner Klinge
 */

public class UserLoginUI extends JFrame implements ActionListener {
  private final String USAGE = "Usage: $ java UserLoginUI <add|login> <username> <password> [<debug_lvl>]";
  private String mode;
  private String username;
  private String password;
  private PasswordManager pm;
  private JLabel usernameLabel; 
  private JLabel passwordLabel;
  private JTextField usernameField;
  private JTextField passwordField;



  /**
   * Runs the UI for a user to login or add other users
   * @param args
   */
  public static void main(String[] args) {
    UserLoginUI ui = new UserLoginUI();
    ui.parseArgs(args);
    ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    ui.pack();
    ui.setVisible(true);

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
    // Used to specify GUI component layout
      GridBagConstraints layoutConst = null;

      //title
      setTitle("Vault");

      //new Labels
      usernameLabel = new JLabel("Username:");
      passwordLabel = new JLabel("Password:");

       // Set username
      usernameField = new JTextField(15);
      usernameField.setEditable(true);
      usernameField.setText("");
      usernameField.addActionListener(this);

      // set password
      passwordField = new JTextField(15);
      passwordField.setEditable(true);
      passwordField.setText("");
      passwordField.addActionListener(this);

      // enter button
      JButton submitButton = new JButton("Submit");
      submitButton.addActionListener(this);

      
      // Use a GridBagLayout
      setLayout(new GridBagLayout());
      layoutConst = new GridBagConstraints();

      // Specify component's grid location
      layoutConst.gridx = 0;
      layoutConst.gridy = 0;

      // 10 pixels of padding around component
      layoutConst.insets = new Insets(10, 10, 10, 10);

      // Add component using the specified constraints
      add(usernameLabel, layoutConst);

      layoutConst = new GridBagConstraints();
      layoutConst.gridx = 1;
      layoutConst.gridy = 0;
      layoutConst.insets = new Insets(10, 10, 10, 10);
      add(usernameField, layoutConst);

      layoutConst = new GridBagConstraints();
      layoutConst.gridx = 0;
      layoutConst.gridy = 1;
      layoutConst.insets = new Insets(10, 10, 10, 10);
      add(passwordLabel, layoutConst);

      layoutConst = new GridBagConstraints();
      layoutConst.gridx = 1;
      layoutConst.gridy = 1;
      layoutConst.insets = new Insets(10, 10, 10, 10);
      add(passwordField, layoutConst);

      layoutConst = new GridBagConstraints();
      layoutConst.gridx = 1;
      layoutConst.gridy = 2;
      layoutConst.insets = new Insets(10, 10, 10, 10);
      add(submitButton, layoutConst);

    // Make Enter key activate this button
getRootPane().setDefaultButton(submitButton);
  }

  /* Method is automatically called when an event 
    occurs (e.g, Enter key is pressed) */
   @Override
   public void actionPerformed(ActionEvent event) {

      // Get user's username input
      username = usernameField.getText();
      
      // Get user's password input
      password = passwordField.getText();

      if (mode == null) {
      // If they opened the GUI without CLI args, default to login
      mode = "login";
    }

      if (mode.equals("add")) {
        pm.addUser(username, password);
        JOptionPane.showMessageDialog(this, "User added successfully!");
    } 
      else if (mode.equals("login")) {
        if (pm.verifyPassword(username, password)) {
            JOptionPane.showMessageDialog(this, "Login successful!");
        } else {
            JOptionPane.showMessageDialog(this, "Login failed.");
        }
    }
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
    }
    return valid;
  }
}
