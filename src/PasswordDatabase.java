import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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
        this.passwordHashMap = new HashMap<String, String>();
        this.dbFile = new File(filename);

        // Creates file if it doesn't exist
        try {
            if (!dbFile.exists()) {
                dbFile.createNewFile();
            } else {
                Scanner lineScan = new Scanner(dbFile);
                Scanner splitter;
                while (lineScan.hasNextLine()) {
                    splitter = new Scanner(lineScan.nextLine());
                    String username = splitter.next();
                    String passwordHash = splitter.next();
                    this.passwordHashMap.put(username, passwordHash);
                    splitter.close();
                }
                lineScan.close();
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
        updateDatabase();   // In the future, this should only be called once the session ends
    }

    /**
     * Removes an entry from the database given username 
     * @param username
     */
    public void remove(String username) {
        passwordHashMap.remove(username);
        updateDatabase();   // In the future, this should only be called once the session ends
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
        FileOutputStream outputStream;
        PrintWriter pw;
        try {
            outputStream = new FileOutputStream(dbFile, false);
            pw = new PrintWriter(outputStream);
            for (Map.Entry<String, String> e : passwordHashMap.entrySet()) {
                pw.print(e.getKey());
                pw.print(" ");
                pw.print(e.getValue());
                pw.println();
            }
            pw.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find database file: " + dbFile.getName(), e);
        } catch (IOException e) {
            throw new RuntimeException("Error while writing to database file", e);
        }
    }

}
