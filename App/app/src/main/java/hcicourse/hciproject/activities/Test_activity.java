package hcicourse.hciproject.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import hcicourse.hciproject.R;
import hcicourse.hciproject.data_structures.Experiment;
import hcicourse.hciproject.data_structures.Password;

public class Test_activity extends AbstractPwd_activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        set_visibility_of_test_header_items(View.VISIBLE);
        set_visibility_of_practice_header_items(View.GONE);

        setTitle("Test:    Interface " + String.valueOf(experiment.getCurrent_InterfaceNo())  + "/3");

        //get experiment from intent
        //increase the test

    }

    @Override
    protected Password get_next_password() {
        //TODO: hack!!!
        Password pwd = experiment.getCurrent_pwd();
        //Password pwd = new Password("3421");
        return pwd;
    }

    //TODO: on click? on event? -> Test OR restart to tutorial (depending on "test" complete status) OR to DONE page

    @Override
    protected void on_correct_pwd_entry(long time) {
        //next trial
        experiment.getCurrent_trial().end_attempt(time,true);
        experiment.getCurrent_trial().end_trial(time);

        Experiment.NextChange changed = experiment.start_next_trial(Test_activity.this);


        if(changed == Experiment.NextChange.TRIAL) {
            //update the unlock no in the header
            set_test_header_unlock_no_to_current_trial_no();

            //don't change the pwd
        } else if(changed == Experiment.NextChange.PWD) {
            //update the unlock no in the header
            set_test_header_unlock_no_to_current_trial_no();
            set_test_header_progress_bar_to_current_progress();
            //set_test_header_pwd_no_to_current_pwd_category_no();

            //update the pwd, with a delay
            // Add a little delay to allow user see the prompts from the password entry
            final Handler handler_dot = new Handler();
            handler_dot.postDelayed(new Runnable() {
                @Override
                public void run() {
                    get_new_pwd_and_update_view();
                }
            }, 500);


        } else if(changed == Experiment.NextChange.PWD_CATEGORY) {
            //launch a break - and a notification?
            create_break_dialog();

            //update the unlock no in the header
            set_test_header_unlock_no_to_current_trial_no();
            set_test_header_progress_bar_to_current_progress();
            //set_test_header_pwd_no_to_current_pwd_category_no();
            //set_test_header_pwd_category_to_current_pwd_category();

            //update the pwd, with a delay
            // Add a little delay to allow user see the prompts from the password entry
            final Handler handler_dot = new Handler();
            handler_dot.postDelayed(new Runnable() {
                @Override
                public void run() {
                    get_new_pwd_and_update_view();
                }
            }, 500);

        } else if(changed == Experiment.NextChange.INTERFACE) {
            //switch interface! how??? - and a notification?
            //go to next interface tutorial
            Intent new_intent = new Intent(Test_activity.this, Tutorial_activity.class);
            Bundle b = new Bundle();
            b.putParcelable("experiment",experiment);
            new_intent.putExtras(b);
            Test_activity.this.finish();
            startActivity(new_intent);

            //TODO: launch a break (should this be here
            //create_break_dialog();

            //update the unlock no in the header
            //set_test_header_unlock_no_to_current_trial_no();
            //set_test_header_pwd_no_to_current_pwd_category_no();
            //set_test_header_pwd_category_to_current_pwd_category();

            //update the pwd
            //get_new_pwd_and_update_view();

        } else if(changed == Experiment.NextChange.USER) {
            //done!!!
            //TODO: launch message?

            //go to survey screen - sending the user
            Intent intent = new Intent(Test_activity.this, Survey_activity.class);
            Bundle b = new Bundle();
            b.putParcelable("user",experiment.getUser());
            intent.putExtras(b);
            Test_activity.this.finish();
            startActivity(intent);

        } else {
            //throw an exception!
        }
        //increase the trial no -> go to next trial
        //ensure that the trial is input into the db
        //launch a break if necessary
        //launch an "easy"/"med"/"hard" notification if necessary
        //launch a tutorial if necessary (done interface)
    }

    private void get_new_pwd_and_update_view() {
        Password pwd = get_next_password();
        change_lock_pwd(pwd);
        change_pw_header_pwd(pwd);
    }

    private void set_test_header_unlock_no_to_current_trial_no() {
        set_test_header_unlock_no(experiment.getCurrent_trial_no());
    }

    private void set_test_header_progress_bar_to_current_progress() {
        set_test_header_progress_bar(experiment.getCurrent_PwLevel(), experiment.getCurrent_PwNo_within_level());
    }

    //private void set_test_header_pwd_no_to_current_pwd_category_no() {
     //   set_test_header_pwd_no(experiment.getCurrent_PwNo_within_level());
    //
    //}

    //private void set_test_header_pwd_category_to_current_pwd_category() {
    //    set_test_header_pwd_category(experiment.getCurrent_PwLevel());
    //}

    @Override
    protected void on_incorrect_pwd_entry(long time) {
        //todo
        experiment.getCurrent_trial().end_attempt(time,false);
        //increase the attempt no and add an attempt to the experiment
    }

    @Override
    protected void on_pwd_entry_start(long time) {
        experiment.getCurrent_trial().start_new_attempt(time);
    }

    static int result_from_break_activity = 1;
    private void create_break_dialog() {
        Intent intent = new Intent(Test_activity.this,Break_activity.class);
        startActivityForResult(intent,result_from_break_activity);

    }

}
