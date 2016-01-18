package hcicourse.hciproject.data_structures;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import hcicourse.hciproject.database.PwdTableHelper;

/**
 * Created by Steffy on 2015-11-07.
 */
public class Password implements Parcelable{
    private long pwd_id;
    private ArrayList<Integer> pwd_content = new ArrayList<Integer>();

    public Password() {}

    public Password(Password pwd) {
        this.pwd_id = pwd.pwd_id;
        this.pwd_content = new ArrayList<>();
        for(int elem: pwd.pwd_content) {
            this.pwd_content.add(elem);
        }
    }

    public Password(long pwd_id, ArrayList<Integer> pwd_content) {
        this.pwd_content = pwd_content;
        this.pwd_id = pwd_id;
    }

    public Password(String pwd_comma_delim) {
        this.pwd_content = PwdTableHelper.convert_pwstring_to_pwd_array(pwd_comma_delim);
        this.pwd_id = -1;
    }

    public String get_pwd_as_string_no_commas() {
        return PwdTableHelper.convert_pw_to_string_no_commas(this.pwd_content);
    }

    public long getPwd_id() {
        return pwd_id;
    }

    public ArrayList<Integer> getPwd_content() {
        return pwd_content;
    }

    //---Parcelable-----------
    protected Password(Parcel in) {
        pwd_id = in.readLong();
        pwd_content = new ArrayList<Integer> ();
        //in.createTypedArrayList(Integer.class.CREATOR)
        in.readList(pwd_content,Integer.class.getClassLoader()); //null);//Integer.class.getClassLoader());
        //pwd_content = in.readArrayList(Thread.currentThread().getContextClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(pwd_id);
        dest.writeList(pwd_content);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Password> CREATOR = new Parcelable.Creator<Password>() {
        @Override
        public Password createFromParcel(Parcel in) {
            return new Password(in);
        }

        @Override
        public Password[] newArray(int size) {
            return new Password[size];
        }
    };

}
