package lucasconti.auto;

/**
 * Created by Lucas on 7/20/2016.
 */
public class Match {
    public static final String STATE_READY = "open";
    public static final String STATE_IN_PROGRESS = "in_progress";
    public static final String STATE_PENDING = "pending";
    public static final String STATE_COMPLETED = "complete";

    private String mPlayer1Name;
    private String mPlayer2Name;

    private String mState;

    public Match(String state) {
        mState = state;
    }

    public boolean isReady() {
        return mState.equals(STATE_READY);
    }

    public void setPlayer1Name(String name) {
        mPlayer1Name = name;
    }

    public void setPlayer2Name(String name) {
        mPlayer2Name = name;
    }

    public String getPlayer1Name() {
        return mPlayer1Name;
    }

    public String getPlayer2Name() {
        return mPlayer2Name;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    @Override
    public String toString() {
        return mPlayer1Name + " v " + mPlayer2Name;
    }
}
