package lucasconti.auto;

import java.util.ArrayList;

/**
 * Created by Lucas on 6/24/2016.
 */
public class Ptcp {
    private String mName;
    private String mPhoneNumber;
    private String mId;

    public Ptcp(String name, String phoneNumber, String id) {
        mName = name;
        mPhoneNumber = phoneNumber;
        mId = id;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public String getName() {
        return mName;
    }

    public String getId() { return mId; }

    public void setName(String name) {
        mName = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public void setId(String id) { mId = id; }

    @Override
    public String toString() {
        //  Only show the Ptcp's name in the list
        return mName;
    }

}
