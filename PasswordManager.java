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
     * @param db the PasswordDatabase to use for storage
     */
    public PasswordManager(PasswordDatabase db) {
        this.db = db;
    }
}
