package hcicourse.hciproject.activities;

import android.content.Intent;
import android.media.session.MediaController;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

import hcicourse.hciproject.R;
import hcicourse.hciproject.data_structures.Experiment;
import hcicourse.hciproject.data_structures.Password;

public class Tutorial_activity extends ActionBarActivity {

    Experiment experiment;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        //get test from intent
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        experiment = b.getParcelable("experiment");
        //regenerate the password
        //experiment.update_current_pwd_and_send_message(Tutorial_activity.this);
        //experiment.current_pwd = b.getParcelable("pwd");
        //Bundle b = intent.getExtras();
        //experiment = b.getParcelable("experiment");

        //TODO: load the visuals!!!
        setTitle("Tutorial:    Interface " + String.valueOf(experiment.getCurrent_InterfaceNo())  + "/3");

        ImageView mVideoView = (ImageView) findViewById(R.id.tutorial_videoview);
        final int video_resid;
        switch(experiment.get_current_interface()){
            case PIN:
                video_resid = R.raw.pin;
                break;
            case PATTERN:
                video_resid = R.raw.pattern;
                break;
            case SPIN:
                video_resid = R.raw.dial;
                break;
            default:
                throw new RuntimeException("bad interface");
        }


        final int result_from_tut_video_activity = 1;
        mVideoView.setOnClickListener(new View.OnClickListener() {
            public void onClick(android.view.View v) {
                Intent intent = new Intent(Tutorial_activity.this, TutorialVideo_activity.class);
                Bundle b = new Bundle();
                b.putInt("resid", video_resid);
                intent.putExtras(b);
                startActivityForResult(intent, result_from_tut_video_activity);
            }
        });
        //launch new activity

        //TODO: code for the replay button

        //TODO: code for the video

        //TODO: code for the proceed button
        final Button button_proceed = (Button) findViewById(R.id.tutorial_proceed_button);
        button_proceed.setOnClickListener(new View.OnClickListener() {
            public void onClick(android.view.View v) {
                //go to first interface practice
                Intent new_intent = new Intent(Tutorial_activity.this, Practice_activity.class);
                //Intent new_intent = new Intent(Tutorial_activity.this, Practice_activity.class);
                Bundle b = new Bundle();
                b.putParcelable("experiment",experiment);
                //b.putParcelable("pwd",experiment.current_pwd);
               // b.putParcelable("experiment",experiment);
                //b.putParcelable("pwd",experiment.current_pwd);
                new_intent.putExtras(b);
                Tutorial_activity.this.finish();
                startActivity(new_intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tutorial, menu);

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
