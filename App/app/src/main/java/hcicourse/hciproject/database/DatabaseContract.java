package hcicourse.hciproject.database;

import android.provider.BaseColumns;

/**
 * Created by Steffy on 2015-11-02.
 */
public class DatabaseContract {

    //empty constructor - can't accidentally instantiate it
    public DatabaseContract() {}

    //properties for developing
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String INTEGER_PRIMARY_KEY_TYPE = " INTEGER PRIMARY KEY";
    private static final String STRING_TYPE = " STRING";
    private static final String DATETIME_TYPE = " DATETIME DEFAULT CURRENT_TIMESTAMP";

    private static final String COMMA_SEP = ",";
    private static final String CREATE_TABLE = "CREATE TABLE ";
    private static final String CREATE_TABLE_IF_NOT_EXISTS = "CREATE TABLE IF NOT EXISTS ";
    private static final String OPEN_BRACKET = " (";
    private static final String CLOSE_BRACKET = " )";
    private static final String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS ";

    //Abstract table
    public static abstract class AbstractTable implements BaseColumns {

    }

    //Table: User
    public static abstract class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "User";
        public static final String COLUMN_USER_ID = "userid";
        public static final String COLUMN_COMPLETE = "complete";
        public static final String COLUMN_INTERFACE_1 = "interface_1";
        public static final String COLUMN_INTERFACE_2 = "interface_2";
        public static final String COLUMN_INTERFACE_3 = "interface_3";
        //TODO: add more columns?

        public static final String SQL_CREATE_ENTRIES =
                CREATE_TABLE_IF_NOT_EXISTS + UserEntry.TABLE_NAME + OPEN_BRACKET +
                        _ID +                       INTEGER_PRIMARY_KEY_TYPE +  COMMA_SEP +
                        COLUMN_USER_ID          +   INTEGER_TYPE +              COMMA_SEP +
                        COLUMN_COMPLETE         +   INTEGER_TYPE +              COMMA_SEP +
                        COLUMN_INTERFACE_1      +   INTEGER_TYPE +              COMMA_SEP +
                        COLUMN_INTERFACE_2      +   INTEGER_TYPE +              COMMA_SEP +
                        COLUMN_INTERFACE_3      +   INTEGER_TYPE +
                        CLOSE_BRACKET;

        public static final String SQL_DELETE_ENTRIES =
                DROP_TABLE_IF_EXISTS + TABLE_NAME;
    }

    //Table: Attempt
    public static abstract class AttemptEntry implements BaseColumns {
        public static final String TABLE_NAME = "Attempt";
        public static final String COLUMN_FK_TRIAL_ID = "trialid";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_SUCCESS = "success";
        public static final String COLUMN_ATT_NO = "attemptno"; //attempt number in the trial
        //TODO: add more columns?

        public static final String SQL_CREATE_ENTRIES =
                CREATE_TABLE_IF_NOT_EXISTS + AttemptEntry.TABLE_NAME + OPEN_BRACKET +
                        _ID +                       INTEGER_PRIMARY_KEY_TYPE +  COMMA_SEP +
                        COLUMN_FK_TRIAL_ID +        INTEGER_TYPE +              COMMA_SEP +
                        COLUMN_TIME +               REAL_TYPE +                 COMMA_SEP +
                        COLUMN_SUCCESS +            INTEGER_TYPE +              COMMA_SEP +
                        COLUMN_ATT_NO +             INTEGER_TYPE +
                        CLOSE_BRACKET;

        public static final String SQL_DELETE_ENTRIES =
                DROP_TABLE_IF_EXISTS + TABLE_NAME;
    }

    //Table: Trial
    public static abstract class TrialEntry implements BaseColumns {
        public static final String TABLE_NAME = "Trial";
        public static final String COLUMN_FK_USER_ID = "userid";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_FK_PWD_ID = "pwdid";
        public static final String COLUMN_TRIAL_NO = "trial_no"; //1-3
        public static final String COLUMN_TIMESTAMP = "timestamp";
        //TODO: ADD trial_no column!!! (1,2,3) NOT attempt NO...
        //TODO: add more columns?

        public static final String SQL_CREATE_ENTRIES =
                CREATE_TABLE_IF_NOT_EXISTS + TrialEntry.TABLE_NAME + OPEN_BRACKET +
                        _ID +                       INTEGER_PRIMARY_KEY_TYPE +  COMMA_SEP +
                        COLUMN_FK_USER_ID +         INTEGER_TYPE +              COMMA_SEP +
                        COLUMN_FK_PWD_ID +          INTEGER_TYPE +              COMMA_SEP +
                        COLUMN_TRIAL_NO +           INTEGER_TYPE +              COMMA_SEP +
                        COLUMN_TIMESTAMP +          DATETIME_TYPE +             COMMA_SEP +
                        COLUMN_TIME +               REAL_TYPE    +
                        CLOSE_BRACKET;

        public static final String SQL_DELETE_ENTRIES =
                DROP_TABLE_IF_EXISTS + TABLE_NAME;
    }

    //Table: Password
    public static abstract class PasswordEntry implements BaseColumns {
        public static final String TABLE_NAME = "Password";
        public static final String COLUMN_PWD_ID = "pwdid"; //id
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_INTERFACE_NO = "interfaceno";
        public static final String COLUMN_WITHIN_CATEGORY_ID = "withincategoryid"; //1-7
        public static final String COLUMN_PW_CONTENT = "pwdcontent";
        //TODO: add more columns?

        public static final String SQL_CREATE_ENTRIES =
                CREATE_TABLE_IF_NOT_EXISTS + PasswordEntry.TABLE_NAME + OPEN_BRACKET +
                        _ID +                       INTEGER_PRIMARY_KEY_TYPE +  COMMA_SEP +
                        COLUMN_PWD_ID +             INTEGER_TYPE +              COMMA_SEP +
                        COLUMN_CATEGORY +           INTEGER_TYPE +              COMMA_SEP +
                        COLUMN_INTERFACE_NO +       INTEGER_TYPE +              COMMA_SEP +
                        COLUMN_WITHIN_CATEGORY_ID + INTEGER_TYPE +             COMMA_SEP +
                        COLUMN_PW_CONTENT   +       STRING_TYPE +
                        CLOSE_BRACKET;

        public static final String SQL_DELETE_ENTRIES =
                DROP_TABLE_IF_EXISTS + TABLE_NAME;
    }

    //Table: User/PW Order
    public static abstract class UserPwOrderEntry implements BaseColumns {
        public static final String TABLE_NAME = "UserPwOrder";
        public static final String COLUMN_FK_USER_ID = "userid";
        public static final String COLUMN_PW_CATEGORY = "pwdcategory";
        public static final String COLUMN_PW_ORDER_IN_CATEGORY = "pworder"; //for identifying passwords (key)
        public static final String COLUMN_ORDER = "nomorder"; //order of appearance
        public static final String COLUMN_PW_INTERFACE = "pwdinterface";
        //TODO: add more columns?

        public static final String SQL_CREATE_ENTRIES =
                CREATE_TABLE_IF_NOT_EXISTS + UserPwOrderEntry.TABLE_NAME + OPEN_BRACKET +
                        _ID +                         INTEGER_PRIMARY_KEY_TYPE +  COMMA_SEP +
                        COLUMN_FK_USER_ID +           INTEGER_TYPE +              COMMA_SEP +
                        COLUMN_PW_CATEGORY +          INTEGER_TYPE +              COMMA_SEP +
                        COLUMN_PW_INTERFACE +         INTEGER_TYPE +              COMMA_SEP +
                        COLUMN_ORDER +                INTEGER_TYPE +              COMMA_SEP +
                        COLUMN_PW_ORDER_IN_CATEGORY + INTEGER_TYPE +
                        CLOSE_BRACKET;

        public static final String SQL_DELETE_ENTRIES =
                DROP_TABLE_IF_EXISTS + TABLE_NAME;
    }

    //create a new table for User\pw orders

}
