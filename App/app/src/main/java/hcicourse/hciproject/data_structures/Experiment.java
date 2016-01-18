package hcicourse.hciproject.data_structures;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import java.util.ArrayList;

import hcicourse.hciproject.database.CorruptedDatabaseException;
import hcicourse.hciproject.database.MissingEntriesException;
import hcicourse.hciproject.database.PwdTableHelper;
import hcicourse.hciproject.database.TrialTableHelper;
import hcicourse.hciproject.database.UserPwdOrderTableHelper;

/**
 * Created by Steffy on 2015-11-07.
 */
public class Experiment implements Parcelable {

    public enum InterfaceEnum {
        PIN,
        PATTERN,
        SPIN
    }
    public enum PwLevel {
        Easy,
        Medium,
        Hard
    }

    public int getCurrent_PwNo_within_level() {
        return current_PwNo_within_level;
    }

    public PwLevel getCurrent_PwLevel() {
        return current_PwLevel;
    }

    public User getUser() {
        return user;
    }

    public Experiment(Experiment experiment) {
        this.user  = new User(experiment.user);
        this.current_InterfaceNo = experiment.current_InterfaceNo;
        this.current_PwLevel = experiment.current_PwLevel;
        this.current_PwNo_within_level = experiment.current_PwNo_within_level;
        this.current_trial = new Trial(experiment.current_trial);
        this.current_trial_no = experiment.current_trial_no;
        this.current_pwd_orders = new ArrayList<>();
        for(int elem : experiment.current_pwd_orders) {
            this.current_pwd_orders.add(elem);
        }
        this.current_pwd = new Password(experiment.current_pwd);
    }

    public Experiment(Context context, User user) {
        this.user = user;
        this.current_InterfaceNo = 1;
        this.current_PwLevel = PwLevel.Easy;
        this.current_PwNo_within_level = 1;
        //this.current_trial = new Trial();
        this.current_trial_no = -1;
        this.current_pwd_orders = get_current_pwd_orders(context);
        //this.current_pwd = new Password();
    }

    private User user; //user

    private int current_InterfaceNo; //1,2,3
    //public InterfaceEnum current_Interface; //Pin, Pattern, Spin <- contained within the user
    private PwLevel current_PwLevel; //Easy, Med, Hard
    private int current_PwNo_within_level; //1-7
    private int current_trial_no; //(0) 1-3

    public Trial getCurrent_trial() {
        return current_trial;
    }

    private Trial current_trial = new Trial(); //trial(contains attempt info)

    public int getCurrent_trial_no() {
        return current_trial_no;
    }

    public Password current_pwd = new Password();

    private ArrayList<Integer> current_pwd_orders;

    //Getters...
    public int getCurrent_InterfaceNo(){
        return this.current_InterfaceNo;
    }

    public Password getCurrent_pwd() {
        return current_pwd;
    }

    public static enum NextChange {
        TRIAL,
        PWD,
        PWD_CATEGORY,
        INTERFACE,
        USER
    }



    public NextChange start_next_trial(final Context context) {
        NextChange retval;

        //add trial to db
        if(current_trial_no > 0) {
            //TrialTableHelper.insert_trial_with_attempts(context, current_trial_no, current_trial, (int) user.getUser_id(), (int) current_pwd.getPwd_id());
            Experiment clone = new Experiment(this);
            new AsyncTask<Experiment, Void, Void>() {
                protected Void doInBackground(Experiment... experiments) {
                    TrialTableHelper.insert_trial_with_attempts(context, experiments[0].current_trial_no, experiments[0].current_trial, (int) experiments[0].user.getUser_id(), (int) experiments[0].current_pwd.getPwd_id());
                    return null;
                }
            }.execute(clone);

        } else {
            //todo
            current_pwd = extract_current_pwd_and_send_message(context);
        }

        //increase trial no
        if(current_trial_no < 3) {
            current_trial_no += 1;
            retval = NextChange.TRIAL;
        }
        else {
            retval = start_next_password(context);
        }

        //start new trial
        current_trial = new Trial();

        return retval;
    }

    private NextChange start_next_password(Context context) {
        NextChange retval;

        //reset trial no
        current_trial_no = 1;

        //increase password no
        if(current_PwNo_within_level < 7 ) {
            current_PwNo_within_level += 1;

            //get next password
            current_pwd = extract_current_pwd_and_send_message(context);

            retval = NextChange.PWD;
        } else {
            retval = start_next_pw_level(context);
        }

        return retval;
    }

    private Password extract_current_pwd_and_send_message(Context context) {
        try {
            Password passwordM = PwdTableHelper.extract_pwd(context,get_current_interface().ordinal(),current_PwLevel.ordinal(),current_PwNo_within_level);
            return passwordM;
        } catch(CorruptedDatabaseException e) {
            Toast.makeText(context,"Error extracting pwd: Multiple passwords with the same keys",Toast.LENGTH_LONG).show();
        } catch(MissingEntriesException e) {
            Toast.makeText(context,"Error extracting pwd: No passwords with this key",Toast.LENGTH_LONG).show();
        }
        return null;
    }

    private ArrayList<Integer> extract_current_pwd_order_and_send_message(Context context) {
        try {
            ArrayList<Integer> password_orders = UserPwdOrderTableHelper.extract_pwd_orders_for_category(context,user.getUser_id(),get_current_interface().ordinal(),current_PwLevel.ordinal());
            return password_orders;
        } catch(CorruptedDatabaseException e) {
            Toast.makeText(context,"Error extracting pwd order: Multiple passwords with the same keys",Toast.LENGTH_LONG).show();
        } catch(MissingEntriesException e) {
            Toast.makeText(context,"Error extracting pwd order: No passwords with this key",Toast.LENGTH_LONG).show();
        }
        return null;
    }

    private NextChange start_next_pw_level(Context context) {
        NextChange retval;

        //reset password no
        current_PwNo_within_level = 1;

        //increase pw level
        if(current_PwLevel != PwLevel.Hard) {
            switch(current_PwLevel) {
                case Easy:
                    current_PwLevel = PwLevel.Medium;
                    break;
                case Medium:
                    current_PwLevel = PwLevel.Hard;
                    break;
            }

            //get next password set
            current_pwd_orders = extract_current_pwd_order_and_send_message(context);
            // get next password
            current_pwd = extract_current_pwd_and_send_message(context);

            retval = NextChange.PWD_CATEGORY;

        } else {
            retval = start_next_interface(context);
        }

        return retval;
    }
    public void update_current_pwd_and_send_message(Context context) {
        current_pwd = extract_current_pwd_and_send_message(context);
    }
    private NextChange start_next_interface(Context context) {
        NextChange retval;

        //reset pw level
        current_PwLevel = PwLevel.Easy;

        if(current_InterfaceNo < 3) {
            current_InterfaceNo += 1;

            //TODO hack!!! (since we need to go though practice again)
            current_trial_no = -1;

            //get next password set
            current_pwd_orders = extract_current_pwd_order_and_send_message(context);
            // get next password
            current_pwd = extract_current_pwd_and_send_message(context);

            retval = NextChange.INTERFACE;

        } else {
            user.isComplete();
            retval = NextChange.USER;
        }

        return retval;
    }

    public boolean is_current_interface_pattern() {
        return get_current_interface() == InterfaceEnum.PATTERN;
    }

    public InterfaceEnum get_current_interface() {
        return user.getInterfaces_in_order().get(current_InterfaceNo - 1);
    }

    private ArrayList<Integer> get_current_pwd_orders(Context context) {
        //get current pwd orders from the db
        ArrayList<Integer> orders = new ArrayList<Integer>();
        try {
            orders = UserPwdOrderTableHelper.extract_pwd_orders_for_category(context, user.getUser_id(), get_current_interface().ordinal(), current_PwLevel.ordinal());
        } catch(CorruptedDatabaseException e) {
            Toast.makeText(context, "Corrputed Database Exception: failed to create test",Toast.LENGTH_LONG).show();
        } catch(MissingEntriesException e) {
            Toast.makeText(context, "Missing Database Entry: failed to create test",Toast.LENGTH_LONG).show();
        }
        return orders;
    }

    //---Parcelable-----------
    protected Experiment(Parcel in) {
        user = in.readParcelable(User.class.getClassLoader());
        current_InterfaceNo = in.readInt();
        current_PwLevel = (PwLevel) in.readSerializable();
        current_PwNo_within_level = in.readInt();
        current_trial_no = in.readInt();
        current_trial = in.readParcelable(Trial.class.getClassLoader()); //Trial.class.getClassLoader());
        //current_pwd = in.readParcelable(Thread.currentThread().getContextClassLoader());
        //current_pwd = in.readParcelable(Password.class.getClassLoader());//Password.class.getClassLoader());

        current_pwd_orders = new ArrayList<Integer>();
        in.readList(current_pwd_orders,Integer.class.getClassLoader());// = in.readArrayList(null); //Integer.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(user,flags);
        dest.writeInt(current_InterfaceNo);
        dest.writeSerializable(current_PwLevel);
        dest.writeInt(current_PwNo_within_level);
        dest.writeInt(current_trial_no);
        dest.writeParcelable(current_trial, flags);
        //dest.writeParcelable(current_pwd, flags);
        dest.writeList(current_pwd_orders);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Experiment> CREATOR = new Parcelable.Creator<Experiment>() {
        @Override
        public Experiment createFromParcel(Parcel in) {
            return new Experiment(in);
        }

        @Override
        public Experiment[] newArray(int size) {
            return new Experiment[size];
        }
    };

}
