package hcicourse.hciproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import hcicourse.hciproject.data_structures.Experiment;
import hcicourse.hciproject.data_structures.User;

/**
 * Created by Steffy on 2015-11-02.
 */
public class UserTableHelper {

    public static void create_table(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(DatabaseContract.UserEntry.SQL_CREATE_ENTRIES);
    }

    public static void initialize_table_data(Context context) {
        initialize_helper(context, 1, Experiment.InterfaceEnum.PIN, Experiment.InterfaceEnum.PATTERN, Experiment.InterfaceEnum.SPIN);
        initialize_helper(context, 8, Experiment.InterfaceEnum.PATTERN, Experiment.InterfaceEnum.SPIN, Experiment.InterfaceEnum.PIN);
        initialize_helper(context, 15, Experiment.InterfaceEnum.SPIN, Experiment.InterfaceEnum.PIN, Experiment.InterfaceEnum.PATTERN);
    }

    private static void initialize_helper(Context context, int starting_id, Experiment.InterfaceEnum int1, Experiment.InterfaceEnum int2, Experiment.InterfaceEnum int3) {
        for(int i = starting_id; i < starting_id + 3; i++){
            insert_user(context,i,false,int1.ordinal(),int2.ordinal(),int3.ordinal());
        }
        for(int i = starting_id + 3; i < starting_id + 7; i++) {
            insert_user(context,i,false,int1.ordinal(),int3.ordinal(),int2.ordinal());
        }
    }

    public static void delete_table(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(DatabaseContract.UserEntry.SQL_DELETE_ENTRIES);
    }

    private static long insert_user(Context context, int userid, boolean complete, int interface_1, int interface_2, int interface_3) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        //create the values
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.UserEntry.COLUMN_USER_ID,userid);
        values.put(DatabaseContract.UserEntry.COLUMN_COMPLETE,complete ? 1 : 0);
        values.put(DatabaseContract.UserEntry.COLUMN_INTERFACE_1,interface_1);
        values.put(DatabaseContract.UserEntry.COLUMN_INTERFACE_2,interface_2);
        values.put(DatabaseContract.UserEntry.COLUMN_INTERFACE_3,interface_3);

        long newRowId;
        newRowId = db.insert(
                DatabaseContract.UserEntry.TABLE_NAME,
                null,
                values);

        return newRowId;
    }
    /*

    public static long delete(Context context, User user) {
        return -1;
    }

    public static long update(Context context, User user) {

        return -1;
    }*/

    public static User extract_user(Context context, long userID)
    throws CorruptedDatabaseException, MissingEntriesException {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                DatabaseContract.UserEntry._ID,
                DatabaseContract.UserEntry.COLUMN_USER_ID,
                DatabaseContract.UserEntry.COLUMN_COMPLETE,
                DatabaseContract.UserEntry.COLUMN_INTERFACE_1,
                DatabaseContract.UserEntry.COLUMN_INTERFACE_2,
                DatabaseContract.UserEntry.COLUMN_INTERFACE_3
        };

        // Define
        String whereClause = DatabaseContract.UserEntry.COLUMN_USER_ID + " =?";
        String[] whereArgs = new String[]{String.valueOf(userID)};

        // How you want the results sorted in the resulting Cursor
        //String sortOrder =
         //       PropertyDatabaseContract.PropertyPointEntry.COLUMN_POINT_ID + " ASC";

        Cursor c = db.query(
                DatabaseContract.UserEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                whereClause,                                // The columns for the WHERE clause
                whereArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        if(c.getCount() > 1) {
            throw new CorruptedDatabaseException("User Table has non-unique user ids");
        } else if (c.getCount() == 0) {
            throw new MissingEntriesException("User Table does not contain user with id");
        } else {
            c.moveToFirst();
            User user = new User(
                    c.getInt(c.getColumnIndex(DatabaseContract.UserEntry._ID)),
                    c.getInt(c.getColumnIndex(DatabaseContract.UserEntry.COLUMN_USER_ID)),
                    c.getInt(c.getColumnIndex(DatabaseContract.UserEntry.COLUMN_COMPLETE)) != 0,
                    Experiment.InterfaceEnum.values()[c.getInt(c.getColumnIndex(DatabaseContract.UserEntry.COLUMN_INTERFACE_1))],
                    Experiment.InterfaceEnum.values()[c.getInt(c.getColumnIndex(DatabaseContract.UserEntry.COLUMN_INTERFACE_2))],
                    Experiment.InterfaceEnum.values()[c.getInt(c.getColumnIndex(DatabaseContract.UserEntry.COLUMN_INTERFACE_3))]);
            c.close();
            return user;
        }
    }

    public static ArrayList<User> extract_all_users(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                DatabaseContract.UserEntry._ID,
                DatabaseContract.UserEntry.COLUMN_USER_ID,
                DatabaseContract.UserEntry.COLUMN_COMPLETE,
                DatabaseContract.UserEntry.COLUMN_INTERFACE_1,
                DatabaseContract.UserEntry.COLUMN_INTERFACE_2,
                DatabaseContract.UserEntry.COLUMN_INTERFACE_3
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                DatabaseContract.UserEntry.COLUMN_USER_ID + " ASC";

        Cursor c = db.query(
                DatabaseContract.UserEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        ArrayList<User> result = new ArrayList<User>();
        try{
            if (c.moveToFirst()){
                do{
                    User user = new User(
                            c.getInt(c.getColumnIndex(DatabaseContract.UserEntry._ID)),
                            c.getInt(c.getColumnIndex(DatabaseContract.UserEntry.COLUMN_USER_ID)),
                            c.getInt(c.getColumnIndex(DatabaseContract.UserEntry.COLUMN_COMPLETE)) != 0,
                            Experiment.InterfaceEnum.values()[c.getInt(c.getColumnIndex(DatabaseContract.UserEntry.COLUMN_INTERFACE_1))],
                            Experiment.InterfaceEnum.values()[c.getInt(c.getColumnIndex(DatabaseContract.UserEntry.COLUMN_INTERFACE_2))],
                            Experiment.InterfaceEnum.values()[c.getInt(c.getColumnIndex(DatabaseContract.UserEntry.COLUMN_INTERFACE_3))]);
                    result.add(user);
                }while(c.moveToNext());
            }
        }finally{
            c.close();
        }
        return result;
    }
    //TODO: write the extraction/update/delete methods...
}

