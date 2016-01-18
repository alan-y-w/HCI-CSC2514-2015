package hcicourse.hciproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Timestamp;
import java.util.ArrayList;

import hcicourse.hciproject.data_structures.Attempt_for_db;
import hcicourse.hciproject.data_structures.Trial;
import hcicourse.hciproject.data_structures.Trial_for_db;

/**
 * Created by Steffy on 2015-11-03.
 */
public class TrialTableHelper {
    public static void create_table(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(DatabaseContract.TrialEntry.SQL_CREATE_ENTRIES);
    }

    public static void delete_table(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(DatabaseContract.TrialEntry.SQL_DELETE_ENTRIES);
    }

/*
    public static long delete(Context context, User user) {
        return -1;
    }

    public static long update(Context context, User user) {

        return -1;
    }*/

    //TODO: write the extraction/update/delete methods...

    public static void insert_trial_with_attempts(Context context,int trial_rep, Trial trial, int user_id, int pwd_id) {
        //insert trial first
        long trial_id = TrialTableHelper.insert_trial(context,trial_rep,trial.get_time(),user_id,pwd_id);
        //insert attempts now
        AttemptTableHelper.insert_attempts_for_trial(context,(int)trial_id,trial.getAttempts());
    }

    private static long insert_trial(Context context, int trial_rep, double time, int user_id, int pwd_id) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        //create the values
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.TrialEntry.COLUMN_TRIAL_NO,trial_rep);
        values.put(DatabaseContract.TrialEntry.COLUMN_FK_USER_ID,user_id);
        values.put(DatabaseContract.TrialEntry.COLUMN_TIME,time);
        values.put(DatabaseContract.TrialEntry.COLUMN_FK_PWD_ID,pwd_id);

        long newRowId;
        newRowId = db.insert(
                DatabaseContract.TrialEntry.TABLE_NAME,
                null,
                values);

        return newRowId;
    }

    public static ArrayList<Trial_for_db> extract_all_trials(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                DatabaseContract.TrialEntry._ID,
                DatabaseContract.TrialEntry.COLUMN_FK_USER_ID,
                DatabaseContract.TrialEntry.COLUMN_TIME,
                DatabaseContract.TrialEntry.COLUMN_FK_PWD_ID,
                DatabaseContract.TrialEntry.COLUMN_TRIAL_NO,
                DatabaseContract.TrialEntry.COLUMN_TIMESTAMP,
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                DatabaseContract.TrialEntry._ID + " ASC";

        Cursor c = db.query(
                DatabaseContract.TrialEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        ArrayList<Trial_for_db> result = new ArrayList<>();
        try {
            if (c.moveToFirst()) {
                do {
                    Trial_for_db trial = new Trial_for_db(
                            c.getLong(c.getColumnIndex(DatabaseContract.TrialEntry._ID)),
                            c.getLong(c.getColumnIndex(DatabaseContract.TrialEntry.COLUMN_FK_USER_ID)),
                            c.getDouble(c.getColumnIndex(DatabaseContract.TrialEntry.COLUMN_TIME)),
                            c.getLong(c.getColumnIndex(DatabaseContract.TrialEntry.COLUMN_FK_PWD_ID)),
                            c.getInt(c.getColumnIndex(DatabaseContract.TrialEntry.COLUMN_TRIAL_NO)),
                            Timestamp.valueOf(c.getString(c.getColumnIndex(DatabaseContract.TrialEntry.COLUMN_TIMESTAMP))));
                    result.add(trial);
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }
        db.close();
        return result;
    }
}
