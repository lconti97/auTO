package lucasconti.auto;

/**
 * Created by Lucas on 7/20/2016.
 */
public class Match {
    private String mPlayer1Name;
    private String mPlayer2Name;

    public Match() {

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

    @Override
    public String toString() {
        return mPlayer1Name + " v " + mPlayer2Name;
    }
}
