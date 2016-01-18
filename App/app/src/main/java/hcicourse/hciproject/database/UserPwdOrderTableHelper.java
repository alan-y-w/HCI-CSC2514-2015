package hcicourse.hciproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

import hcicourse.hciproject.data_structures.Experiment;
import hcicourse.hciproject.data_structures.User;

/**
 * Created by Steffy on 2015-11-06.
 */
public class UserPwdOrderTableHelper {

    public static void create_table(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(DatabaseContract.UserPwOrderEntry.SQL_CREATE_ENTRIES);
        db.close();
    }

    public static void initialize_table_data(Context context)
    throws CorruptedDatabaseException, MissingEntriesException
    {
        for(int i = 1 ; i <= 21 ; i++) {
            //get interface orders
            User user = UserTableHelper.extract_user(context, i);

            //get line from array
            insert_single_users_pwdorders(context, i, pwd_orders[i-1], user.getInterfaces_in_order());
        }

        Toast.makeText(context, "Finished inserting all users",Toast.LENGTH_LONG).show();
    }

//    private static void initialize_helper(Context context, User.InterfaceEnum int1, User.InterfaceEnum int2, User.InterfaceEnum int3) {
//        for(int i = 1; i < 4; i++){
//            insert_user(context,i,false,int1.ordinal(),int2.ordinal(),int3.ordinal());
//        }
//        for(int i = 4; i < 8; i++) {
//            insert_user(context,i,false,int1.ordinal(),int3.ordinal(),int2.ordinal());
//        }
//    }

    public static void delete_table(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(DatabaseContract.UserPwOrderEntry.SQL_DELETE_ENTRIES);
    }


    static int[][] pwd_orders = new int[][]{
            {3,2,5,1,6,7,4,4,2,3,5,7,1,6,6,1,5,3,2,4,7,6,2,5,1,4,7,3,4,6,5,7,3,2,1,4,6,1,5,2,3,7,7,2,6,4,3,5,1,3,1,5,6,4,7,2,1,7,4,5,3,6,2},
            {5,4,6,3,2,1,7,2,5,7,4,3,6,1,5,3,7,2,6,4,1,4,7,6,2,3,1,5,5,3,4,2,6,1,7,4,5,7,3,1,2,6,6,3,5,2,4,1,7,1,7,6,3,4,5,2,7,3,4,5,6,2,1},
            {7,3,5,4,2,6,1,7,6,3,4,2,5,1,3,2,1,7,5,6,4,7,2,4,1,5,6,3,4,2,5,1,6,7,3,5,2,1,4,6,3,7,3,2,5,6,4,7,1,6,4,5,2,3,7,1,2,5,4,6,3,1,7},
            {1,4,5,6,2,7,3,4,2,3,6,5,1,7,7,5,2,4,3,6,1,5,3,4,6,2,1,7,5,2,1,4,3,6,7,2,5,3,4,6,1,7,3,5,7,6,2,4,1,7,4,3,5,2,6,1,6,4,2,3,7,5,1},
            {5,3,1,4,7,6,2,3,6,4,2,5,7,1,3,2,4,6,7,5,1,3,1,2,5,7,6,4,4,5,1,6,2,3,7,5,1,6,3,4,7,2,6,5,1,4,3,2,7,4,2,3,5,1,6,7,5,4,2,3,6,1,7},
            {1,5,3,4,7,6,2,1,2,5,3,6,4,7,5,2,6,4,1,7,3,5,7,3,6,2,4,1,6,5,2,3,7,1,4,3,2,7,6,1,4,5,2,3,5,6,1,7,4,3,2,6,1,5,4,7,1,3,6,4,2,5,7},
            {7,3,2,5,6,1,4,2,6,5,3,1,7,4,3,2,6,4,7,1,5,6,2,3,1,5,7,4,4,1,2,6,3,5,7,7,6,5,1,2,4,3,5,2,3,6,4,7,1,6,4,7,3,2,1,5,1,6,3,4,5,2,7},
            {3,4,5,1,6,7,2,6,2,4,5,3,7,1,7,4,5,1,2,6,3,4,6,2,5,1,3,7,3,6,4,5,2,1,7,2,6,4,3,1,7,5,7,6,5,1,3,2,4,2,5,1,6,4,7,3,6,4,2,7,3,1,5},
            {5,4,3,1,6,7,2,7,2,1,3,5,6,4,5,4,3,7,6,2,1,2,4,6,5,3,1,7,3,4,5,2,7,6,1,7,6,5,2,3,1,4,2,4,3,6,7,1,5,5,2,4,6,7,1,3,5,2,1,7,4,3,6},
            {6,3,4,2,5,7,1,4,1,3,5,6,2,7,3,7,4,5,1,6,2,5,6,2,3,1,7,4,5,4,2,6,3,7,1,7,3,5,6,2,4,1,5,6,3,1,4,2,7,3,5,2,6,1,4,7,3,7,5,1,4,2,6},
            {3,4,2,5,6,1,7,1,4,5,6,2,3,7,5,3,6,4,2,7,1,5,3,2,4,6,1,7,3,4,7,5,6,2,1,2,3,6,4,7,1,5,4,2,1,5,6,7,3,6,4,2,3,5,1,7,5,3,2,6,7,4,1},
            {5,6,2,3,1,4,7,1,3,4,6,7,5,2,3,6,2,7,4,1,5,2,6,1,5,4,7,3,6,1,7,4,3,2,5,4,3,2,6,5,1,7,2,3,6,7,5,1,4,6,3,5,2,4,7,1,1,5,3,7,4,6,2},
            {1,7,5,4,2,6,3,4,5,7,3,2,6,1,2,3,6,1,4,7,5,4,2,3,5,6,7,1,7,3,6,1,4,2,5,1,5,6,2,7,3,4,7,5,4,3,2,1,6,3,7,2,5,1,6,4,3,6,7,4,5,1,2},
            {6,2,1,3,5,7,4,4,6,2,3,7,5,1,7,4,3,5,2,6,1,2,3,7,5,1,4,6,4,3,5,6,2,7,1,2,3,1,5,6,7,4,1,7,3,5,6,4,2,6,5,4,3,7,2,1,6,2,5,7,1,3,4},
            {2,3,6,5,4,7,1,3,1,4,6,5,7,2,3,5,6,4,2,1,7,1,3,5,4,7,2,6,3,7,4,1,6,5,2,6,2,4,3,1,7,5,5,6,2,4,3,7,1,6,5,2,7,3,4,1,2,5,4,7,6,3,1},
            {3,4,2,7,5,1,6,3,4,2,7,1,6,5,7,3,2,4,5,1,6,7,3,4,6,5,1,2,3,1,7,4,5,2,6,2,5,4,6,3,1,7,6,3,5,2,4,1,7,4,1,6,5,2,3,7,6,4,5,1,3,7,2},
            {4,6,2,3,1,7,5,5,3,1,6,2,4,7,7,4,1,2,3,5,6,3,5,6,2,4,7,1,4,3,6,5,2,7,1,3,2,5,1,7,6,4,3,1,2,7,6,5,4,6,4,1,2,3,5,7,5,2,6,7,4,3,1},
            {7,2,4,1,3,5,6,7,2,4,3,5,1,6,2,4,6,1,7,3,5,3,2,4,6,5,7,1,4,1,3,6,7,2,5,6,2,5,7,1,3,4,3,4,6,5,1,7,2,2,6,5,3,4,1,7,6,2,3,7,4,5,1},
            {4,6,1,3,5,2,7,3,4,2,5,1,6,7,7,3,4,2,5,6,1,5,1,4,7,2,3,6,6,3,2,7,4,1,5,3,6,5,4,2,7,1,6,2,3,4,1,5,7,4,5,1,3,7,6,2,3,5,7,4,6,1,2},
            {5,4,2,3,7,1,6,2,1,3,5,4,7,6,5,3,4,2,6,1,7,5,6,2,1,4,3,7,3,5,7,2,6,4,1,5,1,4,3,6,2,7,3,5,4,6,2,1,7,5,6,7,2,4,1,3,6,1,4,5,7,2,3},
            {2,6,1,3,7,4,5,1,3,6,7,2,4,5,7,2,1,4,3,5,6,6,2,5,3,7,1,4,6,2,4,5,7,1,3,6,1,2,7,4,5,3,5,1,6,3,4,7,2,1,7,5,3,4,2,6,4,1,2,6,7,5,3}
    };

    private static long[] insert_single_users_pwdorders(Context context, int user_id, int[] pw_orders, ArrayList<Experiment.InterfaceEnum> interface_order) {
        Experiment.PwLevel[] pwd_levels = Experiment.PwLevel.values();

        long[] retval = new long[63];

        for(int i = 0; i < interface_order.size(); i++) {
            Experiment.InterfaceEnum intface = interface_order.get(i);
            for(int j = 0; j < pwd_levels.length; j++) {
                for(int k = 0; k < 7; k++) {
                    int full = i * 21 + j * 7 + k;
                    int pwd_no = pw_orders[full];
                    retval[k] = insert_pwdorder(context, user_id, pwd_levels[j].ordinal(), intface.ordinal(), pwd_no,k+1);
                }
            }
        }

        Toast.makeText(context, "Finished inserting user: " + String.valueOf(user_id),Toast.LENGTH_LONG).show();
        return retval;
    }

    private static long insert_pwdorder(Context context,int user_id,int pwd_category,int interface_type, int pw_order_in_category,int order) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        //create the values
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.UserPwOrderEntry.COLUMN_FK_USER_ID,user_id);
        values.put(DatabaseContract.UserPwOrderEntry.COLUMN_PW_CATEGORY,pwd_category);
        values.put(DatabaseContract.UserPwOrderEntry.COLUMN_PW_INTERFACE,interface_type);
        values.put(DatabaseContract.UserPwOrderEntry.COLUMN_ORDER,order);
        values.put(DatabaseContract.UserPwOrderEntry.COLUMN_PW_ORDER_IN_CATEGORY,pw_order_in_category);

        long newRowId;
        newRowId = db.insert(
                DatabaseContract.UserPwOrderEntry.TABLE_NAME,
                null,
                values);

        db.close();
        return newRowId;
    }

    public static ArrayList<Integer> extract_pwd_orders_for_category(Context context, long userID, int interface_id, int pwd_category)
    throws CorruptedDatabaseException, MissingEntriesException {
        ArrayList<Integer> order_within_category = new ArrayList<>();
        for(int i = 1; i <= 7; i++) {
            order_within_category.add(extract_pwd_order(context,userID,interface_id,pwd_category,i));
        }
        return order_within_category;
    }


    public static int extract_pwd_order(Context context, long userID, int interface_id, int pwd_category, int order)
            throws CorruptedDatabaseException, MissingEntriesException {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                DatabaseContract.UserPwOrderEntry._ID,
                DatabaseContract.UserPwOrderEntry.COLUMN_FK_USER_ID,
                DatabaseContract.UserPwOrderEntry.COLUMN_PW_CATEGORY,
                DatabaseContract.UserPwOrderEntry.COLUMN_PW_ORDER_IN_CATEGORY,
                DatabaseContract.UserPwOrderEntry.COLUMN_ORDER,
                DatabaseContract.UserPwOrderEntry.COLUMN_PW_INTERFACE
        };

        // Define
        String whereClause = DatabaseContract.UserPwOrderEntry.COLUMN_FK_USER_ID + " =?" +
                   " AND " + DatabaseContract.UserPwOrderEntry.COLUMN_PW_INTERFACE + " =?" +
                   " AND " + DatabaseContract.UserPwOrderEntry.COLUMN_PW_CATEGORY + " =?" +
                   " AND " + DatabaseContract.UserPwOrderEntry.COLUMN_ORDER + " =?";
        String[] whereArgs = new String[]{String.valueOf(userID),
                                          String.valueOf(interface_id),
                                          String.valueOf(pwd_category),
                                          String.valueOf(order)};

        // How you want the results sorted in the resulting Cursor
        //String sortOrder =
        //       PropertyDatabaseContract.PropertyPointEntry.COLUMN_POINT_ID + " ASC";

        Cursor c = db.query(
                DatabaseContract.UserPwOrderEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                whereClause,                                // The columns for the WHERE clause
                whereArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        if(c.getCount() > 1) {
            throw new CorruptedDatabaseException("PwdOrder Table has non-unique user/interface/category/order pairs");
        } else if (c.getCount() == 0) {
            throw new MissingEntriesException("PwdOrder Table does not contain entry with user/interface/category/order");
        } else {
            c.moveToFirst();
            int pw_order_in_cat = c.getInt(c.getColumnIndex(DatabaseContract.UserPwOrderEntry.COLUMN_PW_ORDER_IN_CATEGORY));
            c.close();
            db.close();
            return pw_order_in_cat;
        }
    }

    public static ArrayList<user_pwd_order_container> extract_all_pw_orders(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                DatabaseContract.UserPwOrderEntry._ID,
                DatabaseContract.UserPwOrderEntry.COLUMN_FK_USER_ID,
                DatabaseContract.UserPwOrderEntry.COLUMN_PW_CATEGORY,
                DatabaseContract.UserPwOrderEntry.COLUMN_PW_INTERFACE,
                DatabaseContract.UserPwOrderEntry.COLUMN_PW_ORDER_IN_CATEGORY,
                DatabaseContract.UserPwOrderEntry.COLUMN_ORDER
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                DatabaseContract.UserPwOrderEntry._ID + " ASC";

        Cursor c = db.query(
                DatabaseContract.UserPwOrderEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        ArrayList<user_pwd_order_container> result = new ArrayList<>();
        try{
            if (c.moveToFirst()){
                do{
                    user_pwd_order_container user_pwd_order_container = new user_pwd_order_container(
                            c.getLong(c.getColumnIndex(DatabaseContract.UserPwOrderEntry._ID)),
                            c.getLong(c.getColumnIndex(DatabaseContract.UserPwOrderEntry.COLUMN_FK_USER_ID)),
                            Experiment.PwLevel.values()[c.getInt(c.getColumnIndex(DatabaseContract.UserPwOrderEntry.COLUMN_PW_CATEGORY))],
                            Experiment.InterfaceEnum.values()[c.getInt(c.getColumnIndex(DatabaseContract.UserPwOrderEntry.COLUMN_PW_INTERFACE))],
                            c.getInt(c.getColumnIndex(DatabaseContract.UserPwOrderEntry.COLUMN_PW_ORDER_IN_CATEGORY)),
                            c.getInt(c.getColumnIndex(DatabaseContract.UserPwOrderEntry.COLUMN_ORDER)));
                    result.add(user_pwd_order_container);
                }while(c.moveToNext());
            }
        }finally{
            c.close();
        }
        db.close();
        return result;
    }

    public static class user_pwd_order_container {
        long _id;
        long user_id;
        Experiment.PwLevel pw_level;
        Experiment.InterfaceEnum interfaceEnum;
        int in_category_id;
        int order_of_appearance;

        public long get_id() {
            return _id;
        }

        public long getUser_id() {
            return user_id;
        }

        public Experiment.PwLevel getPw_level() {
            return pw_level;
        }

        public Experiment.InterfaceEnum getInterfaceEnum() {
            return interfaceEnum;
        }

        public int getIn_category_id() {
            return in_category_id;
        }

        public int getOrder_of_appearance() {
            return order_of_appearance;
        }

        public user_pwd_order_container(long _id, long user_id, Experiment.PwLevel pw_level, Experiment.InterfaceEnum interfaceEnum, int in_category_id, int order_of_appearance) {
            this._id = _id;
            this.user_id = user_id;
            this.pw_level = pw_level;
            this.interfaceEnum = interfaceEnum;
            this.in_category_id = in_category_id;
            this.order_of_appearance = order_of_appearance;
        }
    }
    /*

    public static long delete(Context context, User user) {
        return -1;
    }

    public static long update(Context context, User user) {

        return -1;
    }*/

    //TODO: write the extraction/update/delete methods...
}


