import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Manages user passwords, including encryption, storage, and verification.
 *
 * Passwords are hashed with PBKDF2-HMAC-SHA256 using a random, per-user
 * salt (rather than a single salt shared by every user) and a high
 * iteration count, matching OWASP's password storage guidance and making
 * offline brute-force/rainbow-table attacks on stdDB.txt impractical.
 *
 * @version 2.0
 * @author Isaac Hager & Jacob Smith
 */
public class PasswordManager {
    private final PasswordDatabase db;
    private static final int SALT_BYTES = 16;
    private static final int PBKDF2_ITERATIONS = 150_000;
    private static final int KEY_LENGTH_BITS = 256;

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
     * Handles adding users to the database. Fails if the username is
     * already taken, instead of silently overwriting the existing
     * user's password (which would otherwise let anyone hijack an
     * account just by "adding" it again with a new password).
     * @param username
     * @param password
     * @return true if the user was created, false if the username is taken
     */
    public boolean addUser(String username, String password) {
        if (db.get(username) != null) {
            return false;
        }
        byte[] salt = generateSalt();
        String hashedPassword = hashPassword(password, salt);
        db.add(username, bytesToHex(salt) + ":" + hashedPassword);
        return true;
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
        String stored = db.get(username);
        if (stored == null) {
            return false;
        }
        String[] parts = stored.split(":", 2);
        if (parts.length != 2) {
            return false; // entry predates per-user salts; needs to be re-added
        }
        byte[] salt = hexToBytes(parts[0]);
        String storedHash = parts[1];
        String candidateHash = hashPassword(password, salt);
        return constantTimeEquals(candidateHash, storedHash);
    }

    /**
     * Hashes a password with the given salt using PBKDF2-HMAC-SHA256
     * @param password
     * @param salt
     * @return hex-encoded hash
     */
    private String hashPassword(String password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, PBKDF2_ITERATIONS, KEY_LENGTH_BITS);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Could not hash password", e);
        }
    }

    /**
     * Generates a cryptographically random salt
     * @return random salt bytes
     */
    private byte[] generateSalt() {
        byte[] salt = new byte[SALT_BYTES];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    /**
     * Compares two hex-encoded hashes in constant time to avoid leaking
     * information about where they first differ via timing.
     * @param a
     * @param b
     * @return true if equal
     */
    private boolean constantTimeEquals(String a, String b) {
        if (a.length() != b.length()) {
            return false;
        }
        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result |= a.charAt(i) ^ b.charAt(i);
        }
        return result == 0;
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

    /**
     * Converts a hex string back to a byte array
     * @param hex
     * @return byte array
     */
    private byte[] hexToBytes(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }
}
