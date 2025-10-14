/**
 * Manages user passwords, including encryption, storage, and verification.
 * 
 * @version 1.0
 * @author Isaac Hager
 */
public class PasswordManager {
    private final PasswordDatabase db;

    /**
     * Constructor
     */
    public PasswordManager() {
        this.db = new PasswordDatabase("stdDB.txt");
    }

    /**
     * Custom database constructor 
     * @param db the PasswordDatabase to use for storage
     */
    public PasswordManager(PasswordDatabase db) {
        this.db = db;
    }
    
    /**
     * Handles adding users to the database
     * @param username
     * @param password
     */
    public void addUser(String username, String password) {
        String hashedPassword = hashPassword(password);
        db.add(username, hashedPassword);
    }
    
    /**
     * Handles removing users from the database given the correct password
     * @param username
     * @param password
     * @return true if successful
     */
    public boolean removeUser(String username, String password) {
        boolean success = false;
        if (verifyPassword(username, password)) {
            db.remove(username);
            success = true;
        }
        return success;
    }

    /**
     * Confirms whether or not a given password is correct for a given user
     * @param username
     * @param password
     * @return true if matching
     */
    public boolean verifyPassword(String username, String password) {
        String userPasswordHash = db.get(username);
        boolean matching = false;
        if (userPasswordHash == null) {
            matching = false;
        } else {
            String hashedPassword = hashPassword(password);
            matching = hashedPassword.equals(userPasswordHash);
        }
        return matching;
    }

    /**
     * Returns a password has using SHA-256
     * @param password
     * @return hashed password
     */
    private String hashPassword(String password) {
        /* TODO IMPLEMENT SHA-256 HASHING */
        if (true) {
            throw new UnsupportedOperationException("SHA-256 hashing not yet implemented");
        } else {
            String hashedPassword = "/* SHA-256 hash of password */";
            return hashedPassword;
        }
    }
}
