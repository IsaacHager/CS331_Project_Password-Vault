/**
 * Helper class to store password hashes for constant-time lookup.
 * // TODO research and confirm validity of hash principles used
 * @author isaachager
 */
private class PasswordHashmap {
    private int tableSize;
    private String[] table;

    /**
     * Basic constructor.
     * Table size should be prime.
     * @param tableSize
     */
    protected PasswordHashmap(int tableSize) {
        this.tableSize = tableSize;
        table = new String[tableSize];
    }

    /**
     * Adds given password hash to the table.
     * @param hash
     */
    protected void add(String hash) {
        index = hashString(hash);
        table[index] = hash;
    }

    /**
     * Finds the index of a given string if it is in the table
     * @param hash
     * @return index of string or -1 if not found
     */
    protected int find(String hash) {
        int index stringToSum(hash);
        return doubleHashIndex(index, hash);
    }

    /**
     * Adds the ASCII values of a given hash
     * for the purpose of using the resulting
     * sum for index hashing.
     * @param hash
     * @return sum of characters
     */
    private int stringToSum(String hash) {
        int sum = 0;
        int index = 0;
        for (char c : hash) {
            sum += c;
        }
        return sum;
    }

    /**
     * Hashes a given string to a valid index
     * @param hash
     * @return valid index
     */
    private int hashString(String hash) {
        int sum = stringToSum(hash);
        int index = doubleHashIndex(sum);
        return index;
    }

    /**
     * Hashes the index to fit the table using double hashing for direct indexing
     * @param index
     * @return valid index
     */
    private int doubleHashIndex(int index) {
        int i = 0;
        int hashedIndex = (h1(index) + i * (h2(index))) % tableSize;
        while(table[hashedIndex] != null) {
            if (i > tableSize) {
                throw new TimeoutException("Table is full or hash skips values");
            }
            i++;
            hashedIndex = (h1(index) + i * (h2(index))) % tableSize;
        }
    }

    /**
     * Hashes the index to find a matching value in the table.
     * // TODO confirm that this will always find a value in the table
     * @param index
     * @param hash
     * @return index of given string or -1 if not found;
     */
    private int doubleHashIndex(int index, String hash) {
        int i = 0;
        int hashedIndex = (h1(index) + i * (h2(index))) % tableSize;
        while(!table[hashedIndex].equals(hash) || i > tableSize) {
            i++;
            hashedIndex = (h1(index) + i * (h2(index))) % tableSize;
        }
        return (table[hashedIndex].equals(hash)) ? hashedIndex : -1;
    }

    /**
     * Primary hash function for double hashing
     */
    private int h1(int index) {
        return index % tableSize
    }

    /**
     * Secondary hash function for double hashing
     */
    private int h2(int index) {
        // TODO generate second hash function
        throw new Exception("Unimplemented method h2");
    }
}