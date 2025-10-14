import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Given hashed passwords, store and retrieve them to a local file.
 * 
 * @version 1.0
 * @author Isaac Hager & Jacob Smith
 */
public class PasswordDatabase {
    private final File dbFile;
    private HashMap<String, String> passwordHashMap;
    
    /**
    * Constructor. Points to a file for storage)
    * @param filename the file to store users in
    */
    public PasswordDatabase(String filename) {
        this.dbFile = new File(filename);

        // Creates file if it doesn't exist
        try {
            if (!dbFile.exists()) {
                dbFile.createNewFile();
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create database file", e);
        }
    }

    /**
     * Adds a username - password hash pair to the database
     * @param username
     * @param passwordHash
     */
    public void add(String username, String passwordHash) {
        passwordHashMap.put(username, passwordHash);
    }

    /**
     * Removes an entry from the database given username 
     * @param username
     */
    public void remove(String username) {
        passwordHashMap.remove(username);
    }

    /**
     * Returns the hashed password associated with a given username
     * @param username
     * @return hashed password
     */
    public String get(String username) {
        return passwordHashMap.get(username);
    }

    /**
     * Writes new hashmap information to the database file
     */
    private void updateDatabase() {
        // TODO implement method
        throw new UnsupportedOperationException("Unimplemented method updateDatabase");
    }

}
