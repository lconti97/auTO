package lucasconti.auto;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Lucas on 7/18/2016.
 */
public class MatchQueueFrag extends Fragment {
    public static final String TAG_TNMT_URL = "tnmt_url";
    public static final String TAG_TNMT_NAME = "tnmt_name";

    private ArrayList<String> mMatchList;
    private ListView mMatchListView;
    private ArrayAdapter<String> mMatchListAdapter;
    private String mTnmtUrl;
    private String mTnmtName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_match_queue, null);
        mTnmtName = getArguments().getString(TAG_TNMT_NAME);
        mTnmtUrl = getArguments().getString(TAG_TNMT_URL);
        ((MainActivity) getActivity()).setTitle(mTnmtName + " Match Queue");
        setupMatchQueue(v);
        return v;
    }

    private void setupMatchQueue(View v) {
        mMatchListView = ((ListView) v.findViewById(R.id.listview_match_queue));
        mMatchList = new ArrayList<>(Arrays.asList("Armada v Hbox", "Mang0 v Mew2King",
                "Plup v Westballz"));
        mMatchListAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                mMatchList);
        mMatchListView.setAdapter(mMatchListAdapter);
    }
}
