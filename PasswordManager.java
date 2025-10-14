import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
        String hashedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            hashedPassword = bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("No Such Algorithm");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            System.out.println("Unsupported Encoding Exception");
            e.printStackTrace();
        }
        return hashedPassword;
    }

    /**
     * Converts byte array to readable hex string
     * @param hash
     * @return String representation of hash
     */
    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
