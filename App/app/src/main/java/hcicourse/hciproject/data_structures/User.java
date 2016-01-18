package hcicourse.hciproject.data_structures;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Steffy on 2015-11-04.
 */
public class User implements Parcelable{
    private long _id;
    private long user_id;
    private boolean complete_status;
    private Experiment.InterfaceEnum interface_1;
    private Experiment.InterfaceEnum interface_2;
    private Experiment.InterfaceEnum interface_3;

    public User(User user){
        this._id = user._id;
        this.user_id = user.user_id;
        this.complete_status = user.complete_status;
        this.interface_1 = user.interface_1;
        this.interface_2 = user.interface_2;
        this.interface_3 = user.interface_3;
    }

    public User(long _id,
                long user_id,
                boolean complete_status,
                Experiment.InterfaceEnum interface_1,
                Experiment.InterfaceEnum interface_2,
                Experiment.InterfaceEnum interface_3) {

        this._id = _id;
        this.user_id = user_id;
        this.complete_status = complete_status;
        this.interface_1 = interface_1;
        this.interface_2 = interface_2;
        this.interface_3 = interface_3;

    }

    public boolean isComplete() {
        return this.complete_status;
    }

    public long get_id() {
        return _id;
    }

    public long getUser_id() {
        return user_id;
    }

    public boolean isComplete_status() {
        return complete_status;
    }

    public Experiment.InterfaceEnum getInterface_1() {
        return interface_1;
    }

    public Experiment.InterfaceEnum getInterface_2() {
        return interface_2;
    }

    public Experiment.InterfaceEnum getInterface_3() {
        return interface_3;
    }

    public ArrayList<Experiment.InterfaceEnum> getInterfaces_in_order() {
        ArrayList<Experiment.InterfaceEnum> retval = new ArrayList<>();
        retval.add(getInterface_1());
        retval.add(getInterface_2());
        retval.add(getInterface_3());
        return retval;
    }


    //---Parcelable-----------
    protected User(Parcel in) {
        _id = in.readLong();
        user_id = in.readLong();
        complete_status = in.readByte() != 0;
        interface_1 = (Experiment.InterfaceEnum)in.readSerializable();
        interface_2 = (Experiment.InterfaceEnum)in.readSerializable();
        interface_3 = (Experiment.InterfaceEnum)in.readSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_id);
        dest.writeLong(user_id);
        dest.writeByte((byte) (complete_status ? 1 : 0));
        dest.writeSerializable(interface_1);
        dest.writeSerializable(interface_2);
        dest.writeSerializable(interface_3);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
