package lucasconti.auto;

/**
 * Created by Lucas on 7/6/2016.
 */
public class Tnmt {
    private String mName;
    private String mUrl;
    private boolean mStarted;

    public Tnmt(String name, String url, boolean started) {
        mName = name;
        mUrl = url;
        mStarted = started;
    }

    @Override
    public String toString() {
        return mName;
    }

    public String getName() {
        return mName;
    }

    public String getUrl() {
        return mUrl;
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
