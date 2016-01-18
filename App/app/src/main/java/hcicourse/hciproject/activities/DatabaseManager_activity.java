package hcicourse.hciproject.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import hcicourse.hciproject.R;
import hcicourse.hciproject.data_structures.User;
import hcicourse.hciproject.database.AttemptTableHelper;
import hcicourse.hciproject.database.CorruptedDatabaseException;
import hcicourse.hciproject.database.MissingEntriesException;
import hcicourse.hciproject.database.PwdTableHelper;
import hcicourse.hciproject.database.TrialTableHelper;
import hcicourse.hciproject.database.UserPwdOrderTableHelper;
import hcicourse.hciproject.database.UserTableHelper;


public class DatabaseManager_activity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_manager);

        //user buttons
        Button user_table_delete_button = (Button) findViewById(R.id.button_user_delete_table);
        user_table_delete_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                UserTableHelper.delete_table(DatabaseManager_activity.this);
            }
        });

        Button user_table_initialize_button = (Button) findViewById(R.id.button_user_initialize_table);
        user_table_initialize_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                UserTableHelper.create_table(DatabaseManager_activity.this);
                UserTableHelper.initialize_table_data(DatabaseManager_activity.this);
            }
        });

        //pwd buttons
        Button pwd_table_initialize_button = (Button) findViewById(R.id.button_database_management_activity_pwd_initialize);
        pwd_table_initialize_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                PwdTableHelper.create_table(DatabaseManager_activity.this);
                //try {
                    PwdTableHelper.initialize_table(DatabaseManager_activity.this);
                //}catch(CorruptedDatabaseException e) {
                //    Toast.makeText(DatabaseManager_activity.this,"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
                //}catch(MissingEntriesException e) {
                //    Toast.makeText(DatabaseManager_activity.this,"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
                //}
            }
        });

        Button pwd_table_delete_button = (Button) findViewById(R.id.button_database_manager_activity_pwd_delete);
        pwd_table_delete_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                PwdTableHelper.delete_table(DatabaseManager_activity.this);
            }
        });

        //Pwd order buttons
        Button pwdorder_table_initialize_button = (Button) findViewById(R.id.button_database_management_activity_pwd_order_initialize);
        pwdorder_table_initialize_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                UserPwdOrderTableHelper.create_table(DatabaseManager_activity.this);
                try {
                    UserPwdOrderTableHelper.initialize_table_data(DatabaseManager_activity.this);
                }catch(CorruptedDatabaseException e) {
                    Toast.makeText(DatabaseManager_activity.this,"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
                }catch(MissingEntriesException e) {
                    Toast.makeText(DatabaseManager_activity.this,"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

        Button pwdorder_table_delete_button = (Button) findViewById(R.id.button_database_manager_activity_pwdorder_delete);
        pwdorder_table_delete_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                UserPwdOrderTableHelper.delete_table(DatabaseManager_activity.this);
            }
        });

        //trial buttons
        Button trial_table_delete_button = (Button) findViewById(R.id.button_trial_delete_table);
        trial_table_delete_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                TrialTableHelper.delete_table(DatabaseManager_activity.this);
            }
        });

        //attempt buttons
        Button attempt_table_delete_button = (Button) findViewById(R.id.button_attempt_delete_table);
        attempt_table_delete_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                AttemptTableHelper.delete_table(DatabaseManager_activity.this);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_database_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
