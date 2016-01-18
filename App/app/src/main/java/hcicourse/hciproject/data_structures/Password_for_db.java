package hcicourse.hciproject.data_structures;

import java.util.ArrayList;

/**
 * Created by Steffy on 2015-11-08.
 */
public class Password_for_db {
    private long pwd_id;
    private ArrayList<Integer> pwd_content = new ArrayList<>();

    //for db extraction only
    private long _id;
    private Experiment.PwLevel pwd_category;
    private Experiment.InterfaceEnum interface_enum;
    private int within_category_id;

    //for db extraction only!!!
    public Password_for_db(long _id, long pwd_id, Experiment.PwLevel pwd_category, Experiment.InterfaceEnum interface_enum, int within_category_id, ArrayList<Integer> pwd_content) {
        this._id = _id;
        this.pwd_id = pwd_id;
        this.pwd_category = pwd_category;
        this.interface_enum = interface_enum;
        this.within_category_id = within_category_id;
        this.pwd_content = pwd_content;
    }

    public long get_id() {
        return _id;
    }

    public Experiment.PwLevel getPwd_category() {
        return pwd_category;
    }

    public Experiment.InterfaceEnum getInterface_enum() {
        return interface_enum;
    }

    public int getWithin_category_id() {
        return within_category_id;
    }

    public long getPwd_id() {
        return pwd_id;
    }

    public ArrayList<Integer> getPwd_content() {
        return pwd_content;
    }

}
