package hcicourse.hciproject.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.regex.Pattern;

import hcicourse.hciproject.R;
import hcicourse.hciproject.data_structures.Experiment;
import hcicourse.hciproject.data_structures.Password;
import hcicourse.hciproject.interfaces.PasswordResultListener;
import hcicourse.hciproject.widgets.DialLock;
import hcicourse.hciproject.widgets.PINLock;
import hcicourse.hciproject.widgets.PatternDisplay;
import hcicourse.hciproject.widgets.PatternLock;

/**
 * Created by Steffy on 2015-11-09.
 */
public abstract class AbstractPwd_activity extends ActionBarActivity implements PasswordResultListener {

    protected Experiment experiment;
    protected Experiment.InterfaceEnum current_interface;
    protected DialLock diallock;
    protected PINLock pinlock;
    protected PatternLock patternlock;
    protected PatternDisplay patternDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_activity);

        //get experiment from intent
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        experiment = b.getParcelable("experiment");

        diallock = (DialLock) findViewById(R.id.dial_lock);
        pinlock = (PINLock) findViewById(R.id.PIN_lock);
        patternlock = (PatternLock) findViewById(R.id.pattern_lock);

        // get the display header
        patternDisplay = (PatternDisplay) findViewById(R.id.activity_practice_activity_header_password_display);

        //get the first pwd
        experiment.start_next_trial(AbstractPwd_activity.this);

        //set the current interface
        current_interface = experiment.get_current_interface();

        Password pwd = get_next_password();
        initialize_pw_header(pwd);
        initialize_pw_body(pwd);

    }

    //---code for updating the header---
    protected void initialize_pw_header(Password pwd) {

        //make the right pwd visible
        if(experiment.is_current_interface_pattern()) {
            set_numeric_pwd_header_visibility(View.INVISIBLE);
            set_pattern_pwd_header_visibility(View.VISIBLE);
        } else {
            set_numeric_pwd_header_visibility(View.VISIBLE);
            set_pattern_pwd_header_visibility(View.INVISIBLE);
        }

        //update the pwd
        change_pw_header_pwd(pwd);

    }

    protected void change_pw_header_pwd(Password pwd) {
        //now fill the right pwd
        if(experiment.is_current_interface_pattern()) {
            change_pattern_pwd_header_pwd(pwd);
            set_pwd_header_size(130);
        } else {
            change_numeric_pwd_header_pwd(pwd);
            set_pwd_header_size(110);
        }
    }

    private void set_numeric_pwd_header_visibility(int visiblity){
        TextView num_pwd_text = (TextView) findViewById(R.id.activity_practice_activity_header_number_pin);
        num_pwd_text.setVisibility(visiblity);
    }

    private void set_pattern_pwd_header_visibility(int visibility) {
        //TODO: update this with the right view
        //ImageView pattern_pwd_img = (ImageView) findViewById(R.id.activity_practice_activity_header_number_pattern);
        //pattern_pwd_img.setVisibility(visibility);

        patternDisplay.setVisibility(visibility);

    }

    private void change_numeric_pwd_header_pwd(Password pwd) {
        //String pwd_string = PwdTableHelper.convert_pw_to_string(experiment.getCurrent_pwd().getPwd_content());
        TextView num_pwd_text = (TextView) findViewById(R.id.activity_practice_activity_header_number_pin);
        num_pwd_text.setText(pwd.get_pwd_as_string_no_commas());
    }

    private void change_pattern_pwd_header_pwd(Password pwd) {
        //TODO
        patternDisplay.ShowPassword(pwd.get_pwd_as_string_no_commas());
    }

    //--pwd views
    protected void initialize_pw_body(Password pwd) {

        //set visibility
        switch(current_interface) {
            case PIN:
                set_pin_pwd_visibility(View.VISIBLE);
                set_pattern_pwd_visibility(View.GONE);
                set_spin_pwd_visibility(View.GONE);
                break;
            case PATTERN:
                set_pin_pwd_visibility(View.GONE);
                set_pattern_pwd_visibility(View.VISIBLE);
                set_spin_pwd_visibility(View.GONE);
                break;
            case SPIN:
                set_pin_pwd_visibility(View.GONE);
                set_pattern_pwd_visibility(View.GONE);
                set_spin_pwd_visibility(View.VISIBLE);
                break;
        }

        //add password to the appropriate pwd view
        switch(current_interface) {
            case PIN:
                initialize_pin_lock(pwd);
                break;
            case PATTERN:
                initialize_pattern_lock(pwd);
                break;
            case SPIN:
                initialize_spin_lock(pwd);
                break;
        }
    }

    private void set_pwd_header_size(double size) {
        RelativeLayout pwd_header = (RelativeLayout) findViewById(R.id.activity_practice_activity_header_layout);
        final double scale = getResources().getDisplayMetrics().density;
        //int dpWidthInPx  = (int) (200 * scale);
        //int dpHeightInPx = (int) (150 * scale);
        pwd_header.getLayoutParams().height = (int) (size*scale);
        //pwd_header.setMinimumHeight(size);
        pwd_header.invalidate();
    }

    protected void change_lock_pwd(Password pwd) {

        switch(current_interface) {
            case PIN:
                change_pin_lock_pwd(pwd);
                break;
            case PATTERN:
                change_pattern_lock_pwd(pwd);
                break;
            case SPIN:
                change_spin_lock_pwd(pwd);
                break;
        }
    }

    private void change_spin_lock_pwd(Password pwd) {
        diallock.ViewInit();
        diallock.InstallPassword(pwd.get_pwd_as_string_no_commas());
    }

    private void change_pin_lock_pwd(Password pwd) {
        pinlock.ViewInit();
        pinlock.InstallPassword(pwd.get_pwd_as_string_no_commas());
    }

    private void change_pattern_lock_pwd(Password pwd) {
        patternlock.ViewInit();
        patternlock.InstallPassword(pwd.get_pwd_as_string_no_commas());
    }

    private void initialize_spin_lock(Password pwd) {
        // Set up the lock view
        diallock.ViewInit();
        change_spin_lock_pwd(pwd);
        diallock.AddPasswordResultListener(this);
    }

    private void initialize_pin_lock(Password pwd) {
        pinlock.ViewInit();
        change_pin_lock_pwd(pwd);
        pinlock.AddPasswordResultListener(this);
    }

    private void initialize_pattern_lock(Password pwd) {
        patternlock.ViewInit();
        change_pattern_lock_pwd(pwd);
        patternlock.AddPasswordResultListener(this);
    }

    private void set_pin_pwd_visibility(int visibility){
        pinlock.setVisibility(visibility);
    }

    private void set_pattern_pwd_visibility(int visibility){
        patternlock.setVisibility(visibility);
    }

    private void set_spin_pwd_visibility(int visibility){
        diallock.setVisibility(visibility);
    }

    //---menu---
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_practice_activity, menu);
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

    //---notify pwd change---
    @Override
    public void NotifyPasswordResult(boolean result) {

        //TODO: get new pwd
        long time = System.nanoTime();
        //TODO: End timer
        //Password pwd = get_next_password();

        if(result) {
            on_correct_pwd_entry(time);
        } else {
            on_incorrect_pwd_entry(time);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public void NotifyInputBegin() {
        //// TODO: Start timer
        long time = System.nanoTime();
        on_pwd_entry_start(time);
        return;
    }

    abstract protected void on_correct_pwd_entry(long time);
    abstract protected void on_incorrect_pwd_entry(long time);
    abstract protected void on_pwd_entry_start(long time);

    abstract protected Password get_next_password();

    protected void set_visibility_of_practice_header_items(int visibility) {
        ((RelativeLayout) findViewById(R.id.activity_practice_header_items)).setVisibility(visibility);
        ((RelativeLayout) findViewById(R.id.activity_practice_activity_footer_pracitce_layout)).setVisibility(visibility);
    }

    protected void set_visibility_of_test_header_items(int visibility) {
        ((RelativeLayout) findViewById(R.id.activity_test_header_items)).setVisibility(visibility);
    }

    protected void set_test_header_unlock_no(int unlock_no) {
        //((TextView) findViewById(R.id.activity_test_activity_header_unlock_no)).setText(String.valueOf(unlock_no) + "/3");
        int counter = 3-unlock_no+1;
        TextView counterText = ((TextView) findViewById(R.id.activity_test_activity_header_unlock_no));
        counterText.setText("x" + String.valueOf(counter));

        if (counter == 1){
            counterText.setTextColor(Color.parseColor("#ff0066"));
        }
        else
        {
            counterText.setTextColor(getResources().getColor(android.R.color.darker_gray));
        }
    }

    protected void set_test_header_progress_bar(Experiment.PwLevel level, int pwd_no) {
        int progress = (int)(((double)((level.ordinal()*3) + pwd_no - 1))/21 * 100);
        ((ProgressBar) findViewById(R.id.progressBar)).setProgress(progress);
        //((TextView) findViewById(R.id.activity_test_activity_header_level_no)).setText(String.valueOf(pwd_no) + "/7");
    }

    //protected void set_test_header_pwd_category(Experiment.PwLevel level){
    //    ((TextView) findViewById(R.id.activity_test_activity_header_level)).setText(level.toString());
    //}
}
