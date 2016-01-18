package hcicourse.hciproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import hcicourse.hciproject.data_structures.Experiment;
import hcicourse.hciproject.data_structures.Password_for_db;
import hcicourse.hciproject.data_structures.Password;

/**
 * Created by Steffy on 2015-11-07.
 */
public class PwdTableHelper {

    public static void create_table(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(DatabaseContract.PasswordEntry.SQL_CREATE_ENTRIES);
        db.close();
    }

    public static void initialize_table(Context context) {
        insert_pwds_in_pw_array(context,1,                  Experiment.PwLevel.Easy,      Experiment.InterfaceEnum.PIN,     easy_pin_pwds);
        insert_pwds_in_pw_array(context,1+7,                Experiment.PwLevel.Medium,    Experiment.InterfaceEnum.PIN,     medium_pin_pwds);
        insert_pwds_in_pw_array(context,1+7+7,              Experiment.PwLevel.Hard,      Experiment.InterfaceEnum.PIN,     hard_pin_pwds);
        insert_pwds_in_pw_array(context,1+7+7+7,            Experiment.PwLevel.Easy,      Experiment.InterfaceEnum.PATTERN, easy_pattern_pwds);
        insert_pwds_in_pw_array(context,1+7+7+7+7,          Experiment.PwLevel.Medium,    Experiment.InterfaceEnum.PATTERN, medium_pattern_pwds);
        insert_pwds_in_pw_array(context,1+7+7+7+7+7,        Experiment.PwLevel.Hard,      Experiment.InterfaceEnum.PATTERN, hard_pattern_pwds);
        insert_pwds_in_pw_array(context,1+7+7+7+7+7+7,      Experiment.PwLevel.Easy,      Experiment.InterfaceEnum.SPIN,    easy_spin_pwds);
        insert_pwds_in_pw_array(context,1+7+7+7+7+7+7+7,    Experiment.PwLevel.Medium,    Experiment.InterfaceEnum.SPIN,    medium_spin_pwds);
        insert_pwds_in_pw_array(context,1+7+7+7+7+7+7+7+7,  Experiment.PwLevel.Hard,      Experiment.InterfaceEnum.SPIN,    hard_spin_pwds);
    }

    private static void insert_pwds_in_pw_array(Context context,int starting_pwd_id,Experiment.PwLevel pwd_category,Experiment.InterfaceEnum interface_type, int[][] pwd_array) {
        for(int i = 0; i < pwd_array.length; i++) {
            insert_pwd(context, starting_pwd_id + i, pwd_category.ordinal(), interface_type.ordinal(),i+1,convert_pw_to_string(pwd_array[i]));
        }
    }

    public static String convert_pw_to_string(ArrayList<Integer> int_array) {
        String output = "";
        for(int elem: int_array) {
            output += String.valueOf(elem) + ",";
        }
        if(!output.isEmpty()){
            output = output.substring(0, output.length() - 1);
        }
        return output;
    }
    public static String convert_pw_to_string_no_commas(ArrayList<Integer> int_array) {
        String output = "";
        for(int elem: int_array) {
            output += String.valueOf(elem);
        }
        return output;
    }

    private static String convert_pw_to_string(int[] int_array) {
        String output = "";
        for(int elem: int_array) {
            output += String.valueOf(elem) + ",";
        }
        if(!output.isEmpty()){
            output = output.substring(0, output.length() - 1);
        }
        return output;
    }

    public static ArrayList<Integer> convert_pwstring_to_pwd_array(String str){
        ArrayList<Integer> ret = new ArrayList<>();
        if(!str.isEmpty()) {
            String[] retstr = str.split(",");
            for (String s : retstr) {
                ret.add(Integer.parseInt(s));
            }
        }
        return ret;
    }

    static int[][] easy_pin_pwds = new int[][]{
            {1,2,6,8},
            {3,2,4,8},
            {7,5,6,8},
            {9,8,4,2},
            {6,9,5,3},
            {7,4,2,6},
            {2,4,5,9}
    };

    static int[][] medium_pin_pwds = new int[][]{
            {1,3,6,5,8},
            {2,1,7,8,0},
            {5,2,3,9,8},
            {4,6,9,8,5},
            {7,4,1,3,6},
            {0,5,4,7,8},
            {3,2,1,7,8}
    };

    static int[][] hard_pin_pwds = new int[][]{
            {1,9,6,8,5,3},
            {7,0,8,9,1,5},
            {4,2,6,3,7,8},
            {9,0,7,3,2,5},
            {1,4,2,5,3,7},
            {2,1,9,6,8,4},
            {6,3,5,9,1,4}
    };

    static int[][] easy_pattern_pwds = new int[][]{
            {0,3,4,7},
            {8,7,4,5},
            {2,1,4,3},
            {4,3,0,1},
            {5,4,3,0},
            {2,5,8,7},
            {6,3,4,5}
    };

    static int[][] medium_pattern_pwds = new int[][]{
            {0,3,6,7,4},
            {2,1,4,7,6},
            {8,7,4,1,2},
            {2,1,4,3,6},
            {2,5,4,7,8},
            {6,7,4,3,0},
            {0,3,4,7,8}
    };

    static int[][] hard_pattern_pwds = new int[][]{
            {2,4,8,7,6,3},
            {0,3,6,4,7,5},
            {6,4,2,1,3,0},
            {6,7,3,1,4,8},
            {3,1,4,5,7,8},
            {4,0,3,7,8,5},
            {2,5,4,7,3,1}
    };

    static int[][] easy_spin_pwds = new int[][]{
            {7,5,7,6},
            {1,2,9,0},
            {3,5,3,4},
            {5,7,6,8},
            {4,1,2,1},
            {2,4,3,5},
            {0,1,0,3}
    };

    static int[][] medium_spin_pwds = new int[][]{
            {0,2,9,1},
            {6,9,6,7},
            {5,1,3,2},
            {4,6,2,3},
            {3,2,7,6},
            {8,5,7,5},
            {9,6,7,4}
    };

    static int[][] hard_spin_pwds = new int[][]{
            {0,1,3,4},
            {5,3,9,7},
            {1,5,2,5},
            {8,4,6,2},
            {3,8,4,5},
            {4,1,6,4},
            {7,0,6,9}
    };

    public static void delete_table(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(DatabaseContract.PasswordEntry.SQL_DELETE_ENTRIES);
    }

    private static long insert_pwd(Context context,int pwd_id,int pwd_category,int interface_type, int pw_order_in_category,String pw_content) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        //create the values
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.PasswordEntry.COLUMN_PWD_ID,pwd_id);
        values.put(DatabaseContract.PasswordEntry.COLUMN_CATEGORY,pwd_category);
        values.put(DatabaseContract.PasswordEntry.COLUMN_INTERFACE_NO,interface_type);
        values.put(DatabaseContract.PasswordEntry.COLUMN_WITHIN_CATEGORY_ID,pw_order_in_category);
        values.put(DatabaseContract.PasswordEntry.COLUMN_PW_CONTENT,pw_content);

        long newRowId;
        newRowId = db.insert(
                DatabaseContract.PasswordEntry.TABLE_NAME,
                null,
                values);

        db.close();
        return newRowId;
    }

    public static Password extract_pwd(Context context, Experiment.InterfaceEnum interfaceEnum, Experiment.PwLevel pwd_category, int within_category_num)
    throws CorruptedDatabaseException, MissingEntriesException{
        return extract_pwd(context,interfaceEnum.ordinal(),pwd_category.ordinal(),within_category_num);
    }

    public static Password extract_pwd(Context context, int interface_id, int pwd_category, int within_category_num)
            throws CorruptedDatabaseException, MissingEntriesException {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                DatabaseContract.PasswordEntry._ID,
                DatabaseContract.PasswordEntry.COLUMN_PW_CONTENT,
                DatabaseContract.PasswordEntry.COLUMN_PWD_ID
        };

        // Define
        String whereClause = DatabaseContract.PasswordEntry.COLUMN_INTERFACE_NO + " =?" +
                " AND " + DatabaseContract.PasswordEntry.COLUMN_CATEGORY + " =?" +
                " AND " + DatabaseContract.PasswordEntry.COLUMN_WITHIN_CATEGORY_ID + " =?";
        String[] whereArgs = new String[]{String.valueOf(interface_id),
                String.valueOf(pwd_category),
                String.valueOf(within_category_num)};

        // How you want the results sorted in the resulting Cursor
        //String sortOrder =
        //       PropertyDatabaseContract.PropertyPointEntry.COLUMN_POINT_ID + " ASC";

        Cursor c = db.query(
                DatabaseContract.PasswordEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                whereClause,                                // The columns for the WHERE clause
                whereArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        if(c.getCount() > 1) {
            throw new CorruptedDatabaseException("Pwd Table has non-unique user/interface/category/order pairs");
        } else if (c.getCount() == 0) {
            throw new MissingEntriesException("Pwd Table does not contain entry with user/interface/category/order");
        } else {
            c.moveToFirst();
            String pw_order_in_cat = c.getString(c.getColumnIndex(DatabaseContract.PasswordEntry.COLUMN_PW_CONTENT));
            int pwd_id = c.getInt(c.getColumnIndex(DatabaseContract.PasswordEntry.COLUMN_PWD_ID));
            c.close();
            db.close();
            return new Password(pwd_id,convert_pwstring_to_pwd_array(pw_order_in_cat));
        }
    }

    public static ArrayList<Password_for_db> extract_all_passwords(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                DatabaseContract.PasswordEntry._ID,
                DatabaseContract.PasswordEntry.COLUMN_PWD_ID,
                DatabaseContract.PasswordEntry.COLUMN_CATEGORY,
                DatabaseContract.PasswordEntry.COLUMN_INTERFACE_NO,
                DatabaseContract.PasswordEntry.COLUMN_WITHIN_CATEGORY_ID,
                DatabaseContract.PasswordEntry.COLUMN_PW_CONTENT
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                DatabaseContract.PasswordEntry.COLUMN_PWD_ID + " ASC";

        Cursor c = db.query(
                DatabaseContract.PasswordEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        ArrayList<Password_for_db> result = new ArrayList<>();
        try{
            if (c.moveToFirst()){
                do{
                    Password_for_db password = new Password_for_db(
                            c.getLong(c.getColumnIndex(DatabaseContract.PasswordEntry._ID)),
                            c.getLong(c.getColumnIndex(DatabaseContract.PasswordEntry.COLUMN_PWD_ID)),
                            Experiment.PwLevel.values()[c.getInt(c.getColumnIndex(DatabaseContract.PasswordEntry.COLUMN_CATEGORY))],
                            Experiment.InterfaceEnum.values()[c.getInt(c.getColumnIndex(DatabaseContract.PasswordEntry.COLUMN_INTERFACE_NO))],
                            c.getInt(c.getColumnIndex(DatabaseContract.PasswordEntry.COLUMN_WITHIN_CATEGORY_ID)),
                            convert_pwstring_to_pwd_array(c.getString(c.getColumnIndex(DatabaseContract.PasswordEntry.COLUMN_PW_CONTENT))));
                    result.add(password);
                }while(c.moveToNext());
            }
        }finally{
            c.close();
        }
        db.close();
        return result;
    }
}
