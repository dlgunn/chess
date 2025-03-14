package dataaccess;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception {
    private final int statusCode;

    public DataAccessException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public DataAccessException(String message) {
        super(message);
        statusCode = -1;
    }

    public int statusCode() {
        return this.statusCode;
    }
}
