package hcicourse.hciproject.activities;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import hcicourse.hciproject.R;
//import hcicourse.hciproject.data_structures.Test;
import hcicourse.hciproject.data_structures.Experiment;
import hcicourse.hciproject.data_structures.Password;
import hcicourse.hciproject.database.DatabaseContract;
import hcicourse.hciproject.database.PwdTableHelper;
import hcicourse.hciproject.interfaces.PasswordResultListener;
import hcicourse.hciproject.util.PracticePasswordGenerator;
import hcicourse.hciproject.widgets.DialLock;

public class Practice_activity extends AbstractPwd_activity {

    CountDownTimer timer;
    CountDownTimer stopWatch;
    long stopWatchTime = 0;
    PracticePasswordGenerator passwordGenerator = new PracticePasswordGenerator();

    @Override
    protected Password get_next_password() {
        //random 4 number generator
//        int int1 = (int) (Math.random()*9);
//        int int2 = (int) (Math.random()*9);
//        int int3 = (int) (Math.random()*9);
//        int int4 = (int) (Math.random()*9);
//
//        Password pwd = new Password(String.valueOf(int1) + "," + String.valueOf(int2) + "," + String.valueOf(int3) + "," + String.valueOf(int4));
//        return pwd;
        Password password = new Password();
        switch(current_interface) {
            case PIN:
                password = passwordGenerator.GetRandomPasswordPin();
                break;
            case PATTERN:
                password = passwordGenerator.GetRandomPasswordPattern();
                break;
            case SPIN:
                password = passwordGenerator.GetRandomPasswordDial();
                break;
        }
        return password;
    }

    @Override
    protected void on_pwd_entry_start(long time) {
        //start the stopwatch
        stopWatch.onFinish();
        stopWatch.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        set_visibility_of_test_header_items(View.GONE);
        set_visibility_of_practice_header_items(View.VISIBLE);

        setTitle("Practice:    Interface " + String.valueOf(experiment.getCurrent_InterfaceNo())  + "/3");

        //Countdown
        final TextView timertext = (TextView) findViewById(R.id.activity_practice_activity_header_time);
        timer = new CountDownTimer(301000, 1000) {

            public void onTick(long millisUntilFinished) {
                double minutes = Math.floor(millisUntilFinished / 60000);
                double seconds = millisUntilFinished - minutes * 60000;

                //TODO: format the seconds properly!!!
                timertext.setText(String.valueOf((int) minutes) + ":" + String.valueOf((int) (seconds / 1000)));
            }

            public void onFinish() {
                start_first_test_activity();
            }
        }.start();

        //add the attempt stopwatch
        final TextView stopwatchtext = (TextView) findViewById(R.id.activity_practice_activity_header_stopwatch);
        stopWatch = new CountDownTimer(3000000, 1) {

            public void onTick(long millisUntilFinished) {
                stopWatchTime += 1;
                long seconds = (int)(stopWatchTime/100);
                long milliseconds = stopWatchTime - 100*seconds;

                //TODO: format the seconds properly!!!
                stopwatchtext.setText(String.format("%02d:%02d", seconds, milliseconds));
            }

            public void onFinish() {
                stopWatchTime = 0;
                stopwatchtext.setText("00:00");
                //stopWatch.start();
            }
        };

        //Begin test button
        final Button begin_test_button = (Button) findViewById(R.id.activity_practice_activity_header_next_activity_button);
        begin_test_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                start_first_test_activity();
            }
        });
    }

    //start next activity - test
    private void start_first_test_activity() {
        //stop the timer
        timer.cancel();

        //launch test
        Intent intent = new Intent(Practice_activity.this, Test_activity.class);
        Bundle b = new Bundle();
        b.putParcelable("experiment", experiment);
        intent.putExtras(b);
        Practice_activity.this.finish();
        startActivity(intent);
    }

    int pw_no = 1;

    @Override
    protected void on_correct_pwd_entry(long time) {
        //reset the stopwatch
        stopWatch.cancel();

        //practice - generate new pwd
        final Handler handler_dot = new Handler();

        // Add a little delay to allow user see the prompts from the password entry
        handler_dot.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Practice_activity.this.pw_no == 3) {
                    Password pwd = get_next_password();
                    change_pw_header_pwd(pwd);
                    change_lock_pwd(pwd);
                    //reset the pw no
                    Practice_activity.this.pw_no = 0;
                }
                Practice_activity.this.pw_no += 1;
                set_test_header_unlock_no(Practice_activity.this.pw_no);
            }
        }, 500);

    }

    @Override
    protected void on_incorrect_pwd_entry(long time) {
        //reset the stopwatch
        stopWatch.cancel();
    }
}
