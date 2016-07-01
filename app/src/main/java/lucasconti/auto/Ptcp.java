package lucasconti.auto;

import java.util.ArrayList;

/**
 * Created by Lucas on 6/24/2016.
 */
public class Ptcp {
    private String mName;
    private String mPhoneNumber;

    public Ptcp(String name, String phoneNumber) {
        mName = name;
        mPhoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        //  Only show the Ptcp's name in the list
        return mName;
    }

}
