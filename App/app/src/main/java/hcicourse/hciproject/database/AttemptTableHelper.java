package hcicourse.hciproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import hcicourse.hciproject.data_structures.Attempt;
import hcicourse.hciproject.data_structures.Attempt_for_db;
import hcicourse.hciproject.data_structures.Experiment;
import hcicourse.hciproject.data_structures.Password_for_db;

/**
 * Created by Steffy on 2015-11-03.
 */
public class AttemptTableHelper {
    public static void create_table(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(DatabaseContract.AttemptEntry.SQL_CREATE_ENTRIES);
    }

    public static void delete_table(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(DatabaseContract.AttemptEntry.SQL_DELETE_ENTRIES);
    }

    private static long insert_attempt(Context context, int trial_id, double time, boolean success, int attempt_no) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        //create the values
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.AttemptEntry.COLUMN_FK_TRIAL_ID,trial_id);
        values.put(DatabaseContract.AttemptEntry.COLUMN_TIME,time);
        values.put(DatabaseContract.AttemptEntry.COLUMN_SUCCESS,success ? 1 : 0);
        values.put(DatabaseContract.AttemptEntry.COLUMN_ATT_NO,attempt_no);

        long newRowId;
        newRowId = db.insert(
                DatabaseContract.AttemptEntry.TABLE_NAME,
                null,
                values);

        return newRowId;
    }

    public static long[] insert_attempts_for_trial(Context context, int trial_id, ArrayList<Attempt> attempts) {

        long[] rowids = new long[attempts.size()];

        for (int i = 0; i < attempts.size(); i++) {
            Attempt a = attempts.get(i);
            long rowid = insert_attempt(context,trial_id,a.get_time(),a.get_success_status(),i+1);
            rowids[i] = rowid;
        }

        return rowids;
    }

    public static ArrayList<Attempt_for_db> extract_all_attempts(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                DatabaseContract.AttemptEntry._ID,
                DatabaseContract.AttemptEntry.COLUMN_FK_TRIAL_ID,
                DatabaseContract.AttemptEntry.COLUMN_TIME,
                DatabaseContract.AttemptEntry.COLUMN_SUCCESS,
                DatabaseContract.AttemptEntry.COLUMN_ATT_NO
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                DatabaseContract.AttemptEntry._ID + " ASC";

        Cursor c = db.query(
                DatabaseContract.AttemptEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        ArrayList<Attempt_for_db> result = new ArrayList<>();
        try{
            if (c.moveToFirst()){
                do{
                    Attempt_for_db attempt = new Attempt_for_db(
                            c.getLong(c.getColumnIndex(DatabaseContract.AttemptEntry._ID)),
                            c.getLong(c.getColumnIndex(DatabaseContract.AttemptEntry.COLUMN_FK_TRIAL_ID)),
                            c.getDouble(c.getColumnIndex(DatabaseContract.AttemptEntry.COLUMN_TIME)),
                            c.getInt(c.getColumnIndex(DatabaseContract.AttemptEntry.COLUMN_SUCCESS)) == 1 ? true : false,
                            c.getInt(c.getColumnIndex(DatabaseContract.AttemptEntry.COLUMN_ATT_NO)));
                    result.add(attempt);
                }while(c.moveToNext());
            }
        }finally{
            c.close();
        }
        db.close();
        return result;
    }
}
