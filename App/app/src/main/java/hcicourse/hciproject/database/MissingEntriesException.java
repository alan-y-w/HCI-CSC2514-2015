package hcicourse.hciproject.database;

/**
 * Created by Steffy on 2015-11-04.
 */
public class MissingEntriesException extends Exception {
    public MissingEntriesException() {
        super();
    }

    public MissingEntriesException(String s) {
        super(s);
    }
}

