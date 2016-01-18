package hcicourse.hciproject.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import hcicourse.hciproject.R;
import hcicourse.hciproject.data_structures.User;
import hcicourse.hciproject.database.AttemptTableHelper;
import hcicourse.hciproject.database.CorruptedDatabaseException;
import hcicourse.hciproject.database.MissingEntriesException;
import hcicourse.hciproject.database.TrialTableHelper;
import hcicourse.hciproject.database.UserTableHelper;
import hcicourse.hciproject.export.CSV_Exporter;

public class ExperimentHome_activity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_home_activity);

        //create the DB tables!!!
        UserTableHelper.create_table(ExperimentHome_activity.this);
        AttemptTableHelper.create_table(ExperimentHome_activity.this);
        TrialTableHelper.create_table(ExperimentHome_activity.this);

        final EditText user_id_edit_text = (EditText) findViewById(R.id.input_user_id_experiment_start);

        final Button button_start_experiment = (Button) findViewById(R.id.button_start_experiment);
        button_start_experiment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                //extract the user specified by the number in the box
                String user_id_as_string = user_id_edit_text.getText().toString();
                if(user_id_as_string.isEmpty()) {
                    Toast.makeText(ExperimentHome_activity.this,"Error: Must specify a user id.",Toast.LENGTH_LONG).show();
                    return;
                }

                long user_id = Long.parseLong(user_id_edit_text.getText().toString());
                try {
                    User user = UserTableHelper.extract_user(ExperimentHome_activity.this, user_id);
                    if(user.isComplete()) {
                        Toast.makeText(ExperimentHome_activity.this,"Error: This user has already been added.",Toast.LENGTH_LONG).show();
                    } else {
                        //start experiment
                        Intent intent = new Intent(ExperimentHome_activity.this, ExperimentStart_activity.class);
                        Bundle b = new Bundle();
                        b.putParcelable("user",user);
                        //intent.putExtra("user",user);
                        intent.putExtras(b);
                        //kill the current activity
                        ExperimentHome_activity.this.finish();
                        startActivity(intent);
                    }
                } catch(CorruptedDatabaseException e) {
                    Toast.makeText(ExperimentHome_activity.this,"Error: Corrupted Database. More than one user with this id",Toast.LENGTH_LONG).show();
                } catch(MissingEntriesException e) {
                    Toast.makeText(ExperimentHome_activity.this,"Error: No user with this id",Toast.LENGTH_LONG).show();
                }

                //TODO: HUGE HACK: REMOVE
                //Intent intent = new Intent(ExperimentHome_activity.this, Break_activity.class);
                //startActivity(intent);

            }
        });

        final Button button_export_data = (Button) findViewById(R.id.button_export_data);
        button_export_data.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                //Export
                CSV_Exporter.Export_everything(ExperimentHome_activity.this);
            }
        });

        final Button button_goto_manage_database = (Button) findViewById(R.id.button_goto_manage_database);
        button_goto_manage_database.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ExperimentHome_activity.this, DatabaseManager_activity.class);
                ExperimentHome_activity.this.finish();
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_experiment_home_activity, menu);
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
