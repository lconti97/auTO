package lucasconti.auto;

import android.content.Context;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

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
    private int mPlayer1Id;
    private int mPlayer2Id;
    private String mState;
    private String mId;

    public Match(String state, String id, int player1Id, int player2Id) {
        mState = state;
        mId = id;
        mPlayer1Id = player1Id;
        mPlayer2Id = player2Id;
    }

    public String getId() {
        return mId;
    }

    public int getPlayer1Id() {
        return mPlayer1Id;
    }

    public int getPlayer2Id() {
        return mPlayer2Id;
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
