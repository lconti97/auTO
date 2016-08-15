package lucasconti.auto;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
        setupMatchQueue(v);
        setupToolbar(v);
        return v;
    }

    private void setupToolbar(View v) {
        setHasOptionsMenu(true);
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setTitle(mTnmtName  + " Match Queue");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
    }

    private void setupMatchQueue(View v) {
        mMatchListViews[0] = ((ListView) v.findViewById(R.id.listview_match_queue_ready));
        mMatchListViews[1] = ((ListView) v.findViewById(R.id.listview_match_queue_in_progress));
        mMatchListViews[2] = ((ListView) v.findViewById(R.id.listview_match_queue_pending));
        mMatchListViews[3] = ((ListView) v.findViewById(R.id.listview_match_queue_completed));
        for (int i = 0; i < mMatchLists.length; i++) {
            mMatchLists[i] = new ArrayList<>();
        }
        updateMatchesList();
        for (int j = 0; j < 4; j++) {
            mMatchListAdapters[j] = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                    mMatchLists[j]);
            mMatchListViews[j].setAdapter(mMatchListAdapters[j]);
        }
        setMatchListListeners();
    }

    private void setMatchListListeners() {
        mMatchListViews[0].setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Match match = (Match) parent.getItemAtPosition(position);
                showStartMatchDialog(match);
            }
        });
        mMatchListViews[0].setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Match match = (Match) parent.getItemAtPosition(position);
                showUpdateScoreDialog(match);
                return true;
            }
        });
        mMatchListViews[1].setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Match match = (Match) parent.getItemAtPosition(position);
                showUpdateScoreDialog(match);
            }
        });
    }

    private void updateMatchesList() {
        for (int i = 0; i < mMatchLists.length; i++) {
            mMatchLists[i].clear();
        }
        mManager.getMatches(mTnmtUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject matchJSON = ((JSONObject) response.get(i)).getJSONObject("match");
                        String matchId = matchJSON.getString("id");
                        String state = matchJSON.getString("state");
                        int player1id = matchJSON.optInt("player1_id");
                        int player2id = matchJSON.optInt("player2_id");
                        final Match match = new Match(state, matchId, player1id, player2id);
                        mManager.getPtcpNames(mTnmtUrl, match, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                addMatchToQueue(match);
                            }
                        });
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

    private void addMatchToQueue(Match match) {
        String state = match.getState();
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

    private void showStartMatchDialog(final Match match)  {
        new AlertDialog.Builder(getContext()).setTitle(match.toString())
                .setPositiveButton("Start", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (match.isReady()) {
                            startMatch(match);
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    private void startMatch(Match match) {
        mMatchLists[0].remove(match);
        mMatchLists[1].add(match);
        mMatchListAdapters[0].notifyDataSetChanged();
        mMatchListAdapters[1].notifyDataSetChanged();
        match.setState(Match.STATE_IN_PROGRESS);
    }

    private void endMatch(final Match match, String scoreString) {
        int player1Score = Integer.parseInt(scoreString.substring(0,1));
        int player2Score = Integer.parseInt(scoreString.substring(2));
        int winnerId = player1Score > player2Score ? match.getPlayer1Id() : match.getPlayer2Id();
        mManager.updateMatch(mTnmtUrl, match.getId(), scoreString, winnerId + "",
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mMatchLists[0].remove(match);
                mMatchLists[1].remove(match);
                mMatchLists[3].add(match);
                mMatchListAdapters[0].notifyDataSetChanged();
                mMatchListAdapters[1].notifyDataSetChanged();
                mMatchListAdapters[3].notifyDataSetChanged();
                match.setState(Match.STATE_COMPLETED);
            }
        });
    }

    private void showUpdateScoreDialog(final Match match) {
        View view = getLayoutInflater(null).inflate(R.layout.dialog_update_match_score, null);
        final EditText player1ScoreEditText = (EditText) view.findViewById(
                R.id.Dialog_submit_match_score_Player_1_score_Edit_text);
        final EditText player2ScoreEditText = (EditText) view.findViewById(
                R.id.Dialog_submit_match_score_Player_2_score_Edit_text);
        new AlertDialog.Builder(getContext()).setTitle(match.toString())
                .setView(view)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String player1Score = player1ScoreEditText.getText().toString();
                        String player2Score = player2ScoreEditText.getText().toString();
                        String scoreString = player1Score + "-" + player2Score;
                        endMatch(match, scoreString);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }
}
