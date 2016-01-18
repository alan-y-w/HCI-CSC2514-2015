package hcicourse.hciproject.export;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import hcicourse.hciproject.data_structures.Attempt_for_db;
import hcicourse.hciproject.data_structures.Password_for_db;
import hcicourse.hciproject.data_structures.Trial_for_db;
import hcicourse.hciproject.data_structures.User;
import hcicourse.hciproject.database.AttemptTableHelper;
import hcicourse.hciproject.database.PwdTableHelper;
import hcicourse.hciproject.database.TrialTableHelper;
import hcicourse.hciproject.database.UserPwdOrderTableHelper;
import hcicourse.hciproject.database.UserTableHelper;

/**
 * Created by Steffy on 2015-11-05.
 */
public class CSV_Exporter {
    //http://stackoverflow.com/questions/9869507/how-to-create-an-xml-using-xmlserializer-in-android-app

    //private StringWriter writer = new StringWriter();
    File outputDir; // = getAc.getCacheDir();
    public File csvFile; // = File.createTempFile("temp", "kml", outputDir);
    FileOutputStream fileos;

    private void create_new_temp_file(Context context, String name) {
        try {
            outputDir = context.getCacheDir();
            Log.i("CacheDir:", outputDir.toString());
            csvFile = File.createTempFile(name, ".csv", outputDir);
            //xmlFile.createNewFile();
        } catch (IOException e) {
            Log.e("IOException", "Exception in create new File");
        }
    }

    private void get_file_output_stream() {
        try {
            fileos = new FileOutputStream(csvFile);
        } catch (FileNotFoundException e) {
            Log.e("FileNotFoundException", e.toString());
        }
    }

    public CSV_Exporter(Context context, String filename) {
        create_new_temp_file(context, filename);
        get_file_output_stream();
    }

    public static File Export_UsrPwdOrder_Table(Context context){
        CSV_Exporter csv_exporter = new CSV_Exporter(context,"pwdorder");

        //get the user data
        ArrayList<UserPwdOrderTableHelper.user_pwd_order_container> pwd_orders = UserPwdOrderTableHelper.extract_all_pw_orders(context);

        try {
            String s = "db_id,user_id,category,interface,in_category_id,order\n";
            csv_exporter.fileos.write(s.getBytes());
            //do stuff...
            for(UserPwdOrderTableHelper.user_pwd_order_container pwd_order: pwd_orders) {
                //get content for csv line
                ArrayList<String> pwd_array = new ArrayList<String>();
                pwd_array.add(String.valueOf(pwd_order.get_id()));
                pwd_array.add(String.valueOf(pwd_order.getUser_id()));
                pwd_array.add(pwd_order.getPw_level().name());
                pwd_array.add(pwd_order.getInterfaceEnum().name());
                pwd_array.add(String.valueOf(pwd_order.getIn_category_id()));
                pwd_array.add(String.valueOf(pwd_order.getOrder_of_appearance()));

                //now convert to csv
                String user_string_csv = to_csv(pwd_array);

                //now write to file
                csv_exporter.fileos.write((user_string_csv + "\n").getBytes());
            }

            csv_exporter.fileos.close();
        } catch (Exception e) {
            Log.e("Exception", "Exception occurred in writing");
        }

        return csv_exporter.csvFile;
    }


    public static File Export_Password_Table(Context context){
        CSV_Exporter csv_exporter = new CSV_Exporter(context,"pwd");

        //get the user data
        ArrayList<Password_for_db> passwords = PwdTableHelper.extract_all_passwords(context);

        try {
            String s = "db_id,pwd_id,category,interface,in_category_id,content\n";
            csv_exporter.fileos.write(s.getBytes());
            //do stuff...
            for(Password_for_db password : passwords) {
                //get content for csv line
                ArrayList<String> pwd_array = new ArrayList<String>();
                pwd_array.add(String.valueOf(password.get_id()));
                pwd_array.add(String.valueOf(password.getPwd_id()));
                pwd_array.add(password.getPwd_category().name());
                pwd_array.add(password.getInterface_enum().name());
                pwd_array.add(String.valueOf(password.getWithin_category_id()));
                pwd_array.add(PwdTableHelper.convert_pw_to_string(password.getPwd_content()));

                //now convert to csv
                String user_string_csv = to_csv(pwd_array);

                //now write to file
                csv_exporter.fileos.write((user_string_csv + "\n").getBytes());
            }

            csv_exporter.fileos.close();
        } catch (Exception e) {
            Log.e("Exception", "Exception occurred in writing");
        }

        return csv_exporter.csvFile;
    }

    public static File Export_User_Table(Context context){
        CSV_Exporter csv_exporter = new CSV_Exporter(context,"user");

        //get the user data
        ArrayList<User> users = UserTableHelper.extract_all_users(context);

        try {
            String s = "db_id,user_id,complete,interface1,interface2,interface3\n";
            csv_exporter.fileos.write(s.getBytes());
            //do stuff...
            for(User user: users) {
                //get content for csv line
                ArrayList<String> user_array = new ArrayList<String>();
                user_array.add(String.valueOf(user.get_id()));
                user_array.add(String.valueOf(user.getUser_id()));
                user_array.add(String.valueOf(user.isComplete_status()));
                user_array.add(user.getInterface_1().name());
                user_array.add(user.getInterface_2().name());
                user_array.add(user.getInterface_3().name());

                //now convert to csv
                String user_string_csv = to_csv(user_array);

                //now write to file
                csv_exporter.fileos.write((user_string_csv + "\n").getBytes());
            }

            csv_exporter.fileos.close();
        } catch (Exception e) {
            Log.e("Exception", "Exception occurred in writing");
        }

        return csv_exporter.csvFile;
    }

    public static File Export_Attempt_Table(Context context){
        CSV_Exporter csv_exporter = new CSV_Exporter(context,"attempt");

        //get the user data
        ArrayList<Attempt_for_db> attempts = AttemptTableHelper.extract_all_attempts(context);

        try {
            String s = "attempt_id,trial_id,time,success,attempt_no\n";
            csv_exporter.fileos.write(s.getBytes());
            //do stuff...
            for(Attempt_for_db attempt: attempts) {
                //get content for csv line
                ArrayList<String> attempt_array = new ArrayList<String>();
                attempt_array.add(String.valueOf(attempt.get_id()));
                attempt_array.add(String.valueOf(attempt.get_trial_id()));
                attempt_array.add(String.valueOf(attempt.getTime()));
                attempt_array.add(String.valueOf(attempt.isSuccess()? 1:0));
                attempt_array.add(String.valueOf(attempt.getAttempt_no()));

                //now convert to csv
                String user_string_csv = to_csv(attempt_array);

                //now write to file
                csv_exporter.fileos.write((user_string_csv + "\n").getBytes());
            }

            csv_exporter.fileos.close();
        } catch (Exception e) {
            Log.e("Exception", "Exception occurred in writing");
        }

        return csv_exporter.csvFile;
    }

    public static File Export_Trial_Table(Context context){
        CSV_Exporter csv_exporter = new CSV_Exporter(context,"trial");

        //get the user data
        ArrayList<Trial_for_db> trials = TrialTableHelper.extract_all_trials(context);

        try {
            String s = "trial_id,user_id,time,pwd_id,trial_no,timestamp\n";
            csv_exporter.fileos.write(s.getBytes());
            //do stuff...
            for(Trial_for_db trial: trials) {
                //get content for csv line
                ArrayList<String> trial_array = new ArrayList<String>();
                trial_array.add(String.valueOf(trial.get_id()));
                trial_array.add(String.valueOf(trial.getUser_id()));
                trial_array.add(String.valueOf(trial.getTime()));
                trial_array.add(String.valueOf(trial.getPwd_id()));
                trial_array.add(String.valueOf(trial.getTrial_no()));
                trial_array.add(String.valueOf(trial.getTimestamp()));

                //now convert to csv
                String user_string_csv = to_csv(trial_array);

                //now write to file
                csv_exporter.fileos.write((user_string_csv + "\n").getBytes());
            }

            csv_exporter.fileos.close();
        } catch (Exception e) {
            Log.e("Exception", "Exception occurred in writing");
        }

        return csv_exporter.csvFile;
    }

    public static String to_csv(ArrayList<String> strings) {
        if(strings.isEmpty()) {
            return "";
        }
        String output = "";
        for(String s: strings) {
            output += s + ",";
        }
        output = output.substring(0, output.length()-1);
        return output;
    }

    public static void send_email(Context context, ArrayList<File> files) {
    // Sends an email to the specified addresses with the files attached

        ArrayList<Uri> uris = new ArrayList<Uri>();
        Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"s.e.raimondo@gmail.com","alan.wu.yusheng@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "App: Export");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "temp text");
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        for (File file: files) {

            if (!file.exists() || !file.canRead()) {

            } else {

                Uri uri = FileProvider.getUriForFile(context, "hcicourse.hciproject.fileprovider", file);
                uris.add(uri);

            }
        }
        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        //startActivityForResult(Intent.createChooser(emailIntent, "Pick an Email Provider"),);
        context.startActivity(Intent.createChooser(emailIntent, "Pick an Email Provider"));
    }

    public static void Export_everything(Context context) {
        ArrayList<File> files = new ArrayList<>();

        //export user data
        files.add(CSV_Exporter.Export_User_Table(context));
        //export pwd table data
        files.add(CSV_Exporter.Export_Password_Table(context));
        //export pwdorder table data
        files.add(CSV_Exporter.Export_UsrPwdOrder_Table(context));
        //export attempt table
        files.add(CSV_Exporter.Export_Attempt_Table(context));
        //export trial table
        files.add(CSV_Exporter.Export_Trial_Table(context));


        //TODO: export more things!!!

        //send the email
        CSV_Exporter.send_email(context,files);
    }

}
