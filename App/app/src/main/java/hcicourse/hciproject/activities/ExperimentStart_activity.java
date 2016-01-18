package hcicourse.hciproject.activities;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import hcicourse.hciproject.R;
import hcicourse.hciproject.data_structures.Experiment;
import hcicourse.hciproject.data_structures.User;

public class ExperimentStart_activity extends ActionBarActivity {

    User user;
    Experiment experiment;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_start);

        //get the user from the bundle
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        user = bundle.getParcelable("user");

        experiment = new Experiment(ExperimentStart_activity.this,user);

        final Button button_start_experiment = (Button) findViewById(R.id.activity_experiment_start_button_start);
        button_start_experiment.setOnClickListener(new View.OnClickListener() {
            public void onClick(android.view.View v) {
                //go to first interface tutorial
                Intent new_intent = new Intent(ExperimentStart_activity.this, Tutorial_activity.class);
                Bundle b = new Bundle();
                b.putParcelable("experiment",experiment);
                //b.putParcelable("pwd",experiment.current_pwd);

                //new_intent.putExtra("experiment",(Parcelable) experiment);
                //new_intent.putExtra("pwd",(Parcelable) experiment.current_pwd);
                //new_intent.putExtra("experiment",experiment);
                new_intent.putExtras(b);
                ExperimentStart_activity.this.finish();
                startActivity(new_intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_experiment_start, menu);
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
