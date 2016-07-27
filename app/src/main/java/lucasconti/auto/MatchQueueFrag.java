package lucasconti.auto;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Lucas on 7/18/2016.
 */
public class MatchQueueFrag extends Fragment {
    public static final String TAG_TNMT_URL = "tnmt_url";
    public static final String TAG_TNMT_NAME = "tnmt_name";

    private ArrayList<Match>[] mMatchLists = new ArrayList[4];
    private ListView[] mMatchListViews = new ListView[4];
    private ArrayAdapter<Match>[] mMatchListAdapters = new ArrayAdapter[4];
    private String mTnmtUrl;
    private String mTnmtName;
    private ChallongeManager mManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_match_queue, null);
        mTnmtName = getArguments().getString(TAG_TNMT_NAME);
        mTnmtUrl = getArguments().getString(TAG_TNMT_URL);
        mManager = ChallongeManager.get(getContext());
        ((MainActivity) getActivity()).setTitle(mTnmtName + " Match Queue");
        setupMatchQueue(v);
        return v;
    }

    private void setupMatchQueue(View v) {
        mMatchListViews[0] = ((ListView) v.findViewById(R.id.listview_match_queue_ready));
        mMatchListViews[1] = ((ListView) v.findViewById(R.id.listview_match_queue_in_progress));
        mMatchListViews[2] = ((ListView) v.findViewById(R.id.listview_match_queue_pending));
        mMatchListViews[3] = ((ListView) v.findViewById(R.id.listview_match_queue_completed));
        for (int i = 0; i < mMatchLists.length; i++) {
            mMatchLists[i] = new ArrayList<>();
        }
        sortMatches();
        for (int j = 0; j < 4; j++) {
            mMatchListAdapters[j] = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                    mMatchLists[j]);
            mMatchListViews[j].setAdapter(mMatchListAdapters[j]);
        }
    }

    private void sortMatches() {
        mManager.getMatches(mTnmtUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject matchJSON = ((JSONObject) response.get(i)).getJSONObject("match");
                        final String state = matchJSON.getString("state");
                        int player1id = matchJSON.optInt("player1_id");
                        int player2id = matchJSON.optInt("player2_id");
                        if (player1id != 0 && player2id != 0) {
                            createTwoPlayerMatch(player1id, player2id, state);
                        }
                        else if (player1id == 0 && player2id != 0) {

                        }
                    }
                    for (ArrayAdapter<Match> matchList : mMatchListAdapters) {
                        matchList.notifyDataSetChanged();
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void createTwoPlayerMatch(int player1id, int player2id, final String state) {
        final Match match = new Match();
        mManager.getPtcp(mTnmtUrl, player1id, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String player1name = response.getJSONObject("participant")
                            .getString("name");
                    match.setPlayer1Name(player1name);
                    if (match.getPlayer2Name() != null) {
                        addMatchToQueue(match, state);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        mManager.getPtcp(mTnmtUrl, player2id, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String player2name = response.getJSONObject("participant")
                            .getString("name");
                    match.setPlayer2Name(player2name);
                    if (match.getPlayer1Name() != null) {
                        addMatchToQueue(match, state);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addMatchToQueue(Match match, String state) {
        switch (state)  {
            case "open" :
                mMatchLists[0].add(match);
                mMatchListAdapters[0].notifyDataSetChanged();
                break;
            case "pending" :
                mMatchLists[2].add(match);
                mMatchListAdapters[2].notifyDataSetChanged();
                break;
            default:
                mMatchLists[3].add(match);
                mMatchListAdapters[3].notifyDataSetChanged();
                break;
        }
    }
}
