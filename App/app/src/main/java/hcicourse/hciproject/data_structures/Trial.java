package hcicourse.hciproject.data_structures;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Steffy on 2015-11-02.
 */

public class Trial implements Parcelable {
    private long start_time;
    private long end_time;

    public void start_new_attempt(long time) {
        //if the attempt list is empty, then this is also the start of the trial
        if(attempt_list.isEmpty()) {
            setStart_time(time);
        }

        Attempt a = new Attempt(time);
        attempt_list.add(a);
    }

    public void end_attempt(long time,boolean is_success) {
        attempt_list.get(attempt_list.size()-1).setEnd_time(time);
        attempt_list.get(attempt_list.size()-1).setSuccess_status(is_success);
    }

    public void end_trial(long time) {
        end_time = time;
    }

    //password class?
    public Trial() {}

    public Trial(Trial trial) {
        this.start_time = trial.start_time;
        this.end_time = trial.end_time;
        this.attempt_list = new ArrayList<>();
        for(Attempt elem: trial.attempt_list) {
            this.attempt_list.add(new Attempt(elem));
        }
    }

    private ArrayList<Attempt> attempt_list = new ArrayList<Attempt>();

    public void setStart_time() {
        this.start_time = System.nanoTime();
    }

    public void setStart_time(long time) {
        this.start_time = time;
    }
    public void setEnd_time() {
        this.end_time = System.nanoTime();
    }

    public void setEnd_time(long time) {
        this.end_time = time;
    }
    public double get_time() {
        long duration = this.end_time - this.start_time;
        return ((double)duration / 1000000000);
    }

    public Integer get_n_attempts() {
        return attempt_list.size();
    }

    public ArrayList<Attempt> getAttempts() {
        return attempt_list;
    }


    //---Parcelable-----------
    protected Trial(Parcel in) {
        start_time = in.readInt();
        end_time = in.readInt();
        attempt_list = new ArrayList<Attempt>();
        in.readTypedList(attempt_list,Attempt.CREATOR);//Attempt.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(start_time);
        dest.writeLong(end_time);
        dest.writeTypedList(attempt_list);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Trial> CREATOR = new Parcelable.Creator<Trial>() {
        @Override
        public Trial createFromParcel(Parcel in) {
            return new Trial(in);
        }

        @Override
        public Trial[] newArray(int size) {
            return new Trial[size];
        }
    };
}
