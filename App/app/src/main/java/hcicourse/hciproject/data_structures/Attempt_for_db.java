package hcicourse.hciproject.data_structures;

/**
 * Created by Steffy on 2015-11-10.
 */
public class Attempt_for_db {
    private long _id;
    private long _trial_id;
    private double time;
    private boolean success;
    private int attempt_no;

    public long get_id() {
        return _id;
    }

    public long get_trial_id() {
        return _trial_id;
    }

    public double getTime() {
        return time;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getAttempt_no() {
        return attempt_no;
    }

    public Attempt_for_db(long _id, long _trial_id, double time, boolean success, int attempt_no) {
        this._id = _id;
        this._trial_id = _trial_id;
        this.time = time;
        this.success = success;
        this.attempt_no = attempt_no;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public void set_trial_id(long _trial_id) {
        this._trial_id = _trial_id;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setAttempt_no(int attempt_no) {
        this.attempt_no = attempt_no;
    }

}
