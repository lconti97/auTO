package lucasconti.auto;

/**
 * Created by Lucas on 7/6/2016.
 */
public class Tnmt {
    private String mName;
    private int mId;
    private boolean mStarted;

    public Tnmt(String name, int id, boolean started) {
        mName = name;
        mId = id;
        mStarted = started;
    }

    @Override
    public String toString() {
        return mName;
    }

    public String getName() {
        return mName;
    }

    public int getId() {
        return mId;
    }

    public boolean isStarted() {
        return mStarted;
    }

    public void setName(String name) {
        mName = name;
    }

    public void start() {
        mStarted = true;
    }
}
