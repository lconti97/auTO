package lucasconti.auto;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
 * Created by Lucas on 7/1/2016.
 */
public class TnmtListFrag extends Fragment implements MainActivity.FabListener,
        AddTnmtDialogFrag.AddTnmtDialogListener {
    private ArrayList<Tnmt> tnmtsList;
    private ListView tnmtsListView;
    private ArrayAdapter<Tnmt> tnmtsListAdapter;
    private FragmentManager fm;
    private ChallongeManager mManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_tnmt_list, container, false);
        tnmtsList = new ArrayList<>();
        tnmtsListView = (ListView) v.findViewById(R.id.listview_tnmts);
        tnmtsListAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, tnmtsList);
        tnmtsListView.setAdapter(tnmtsListAdapter);
        fm = getChildFragmentManager();
        mManager = ChallongeManager.get(getActivity());
        mManager.getTnmts(new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = response.length() - 1; i >= 0; i--) {
                    try {
                        //  The response is an array containing an object that contains our object
                        JSONObject intermediateJSON = (JSONObject) response.get(i);
                        JSONObject tnmtJSON = (JSONObject) intermediateJSON.get("tournament");
                        String name = tnmtJSON.getString("name");
                        int id = Integer.parseInt(tnmtJSON.getString("id"));
                        String startedString = tnmtJSON.getString("started_at");
                        boolean started = !startedString.equals("null");
                        tnmtsList.add(new Tnmt(name, id, started));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                tnmtsListAdapter.notifyDataSetChanged();
            }
        });
        return v;
    }

    @Override
    public void onFabClick() {
        //  Create a new tournament
        AddTnmtDialogFrag frag = new AddTnmtDialogFrag();
        frag.show(fm, "AddTnmtDialogFrag");
    }

    @Override
    public void onPositiveClick(String name) {
//        tnmtsList.add(new Tnmt(name, ));
//        tnmtsListAdapter.notifyDataSetChanged();
//        mManager.addTnmt(name);
    }
}
