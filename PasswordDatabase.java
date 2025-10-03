import java.io.File;
import java.io.IOException;

/**
 * Given hashed passwords, store and retrieve them to a local file.
 * 
 * @version 1.0
 * @author Isaac Hager
 */
public class PasswordDatabase {
    private final File dbFile;
    
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

}
