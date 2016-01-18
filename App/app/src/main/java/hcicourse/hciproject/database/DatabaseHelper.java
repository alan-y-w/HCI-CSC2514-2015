package hcicourse.hciproject.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Steffy on 2015-11-02.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //increment this when the schema changes!!!
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "HCI.db";

    public DatabaseHelper(Context context) {
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.UserEntry.SQL_CREATE_ENTRIES);
        db.execSQL(DatabaseContract.AttemptEntry.SQL_CREATE_ENTRIES);
        db.execSQL(DatabaseContract.TrialEntry.SQL_CREATE_ENTRIES);
        db.execSQL(DatabaseContract.PasswordEntry.SQL_CREATE_ENTRIES);
        db.execSQL(DatabaseContract.UserPwOrderEntry.SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseContract.UserEntry.SQL_DELETE_ENTRIES);
        db.execSQL(DatabaseContract.AttemptEntry.SQL_DELETE_ENTRIES);
        db.execSQL(DatabaseContract.TrialEntry.SQL_DELETE_ENTRIES);
        db.execSQL(DatabaseContract.PasswordEntry.SQL_DELETE_ENTRIES);
        db.execSQL(DatabaseContract.UserPwOrderEntry.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
