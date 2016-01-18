package hcicourse.hciproject.data_structures;

import java.security.Timestamp;

/**
 * Created by Steffy on 2015-11-10.
 */
public class Trial_for_db {
    private long _id;
    private long user_id;
    private double time;
    private long pwd_id;
    private int trial_no;
    private java.sql.Timestamp timestamp;

    public Trial_for_db(long _id, long user_id, double time, long pwd_id, int trial_no, java.sql.Timestamp timestamp) {
        this._id = _id;
        this.user_id = user_id;
        this.time = time;
        this.pwd_id = pwd_id;
        this.trial_no = trial_no;
        this.timestamp = timestamp;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public long getPwd_id() {
        return pwd_id;
    }

    public void setPwd_id(long pwd_id) {
        this.pwd_id = pwd_id;
    }

    public int getTrial_no() {
        return trial_no;
    }

    public void setTrial_no(int trial_no) {
        this.trial_no = trial_no;
    }

    public java.sql.Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(java.sql.Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
