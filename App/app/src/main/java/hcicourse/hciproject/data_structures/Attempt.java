package hcicourse.hciproject.data_structures;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Steffy on 2015-11-02.
 */
public class Attempt implements Parcelable{
    private long start_time;
    private long end_time;

    public void setSuccess_status(boolean success_status) {
        this.success_status = success_status;
    }

    private boolean success_status;

    public void setStart_time() {
        this.start_time = System.nanoTime();
    }

    public Attempt(Attempt attempt) {
        this.start_time = attempt.start_time;
        this.end_time = attempt.end_time;
        this.success_status = attempt.success_status;
    }

    public Attempt(long start_time) {
        this.start_time = start_time;
        this.success_status = false;
    }
    public void setStart_time(long time) {
        this.start_time = time;
    }

    public void setEnd_time(long time) {
        this.end_time = time;
    }

    public void setEnd_time() {
        this.end_time = System.nanoTime();
    }

    public double get_time() {
        long duration = this.end_time - this.start_time;
        return ((double)duration / 1000000000);
    }

    public boolean get_success_status() {
        return this.success_status;
    }


    //---Parcelable-----------
    protected Attempt(Parcel in) {
        start_time = in.readLong();
        end_time = in.readLong();
        success_status = (in.readInt()==1);//Attempt.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(start_time);
        dest.writeLong(end_time);
        dest.writeInt(success_status ? 1: 0);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Attempt> CREATOR = new Parcelable.Creator<Attempt>() {
        @Override
        public Attempt createFromParcel(Parcel in) {
            return new Attempt(in);
        }

        @Override
        public Attempt[] newArray(int size) {
            return new Attempt[size];
        }
    };
}
