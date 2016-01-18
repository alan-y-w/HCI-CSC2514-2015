package hcicourse.hciproject.database;

/**
 * Created by Steffy on 2015-11-04.
 */
public class CorruptedDatabaseException extends Exception {
    public CorruptedDatabaseException() {
        super();
    }

    public CorruptedDatabaseException(String s) {
        super(s);
    }
}
