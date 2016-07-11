package lucasconti.auto;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Lucas on 7/1/2016.
 */
public class TnmtListFrag extends Fragment implements MainActivity.FabListener,
        AddTnmtDialogFrag.AddTnmtDialogListener {
    private ArrayList<Tnmt> tnmtsList;
    private ListView tnmtsListView;
    private ArrayAdapter<Tnmt> tnmtsListAdapter;
    private FragmentManager childFm;
    private ChallongeManager mManager;
    private FloatingActionButton mFab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_tnmt_list, container, false);
        tnmtsList = new ArrayList<>();
        tnmtsListView = (ListView) v.findViewById(R.id.listview_tnmts);
        tnmtsListAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, tnmtsList);
        tnmtsListView.setAdapter(tnmtsListAdapter);
        childFm = getChildFragmentManager();
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
        tnmtsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RegistrationFrag frag = new RegistrationFrag();
                Bundle b = new Bundle();
                b.putString(RegistrationFrag.TAG_TNMT_URL, tnmtsList.get(position).getId() + "");
                frag.setArguments(b);
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().addToBackStack(null).replace(R.id.content, frag).commit();
            }
        });
        tnmtsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity()).setItems(
                        new String[] {"Delete"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mManager.deleteTnmt(tnmtsList.get(position));
                                tnmtsList.remove(position);
                                tnmtsListAdapter.notifyDataSetChanged();
                            }
                        }).create();
                dialog.show();
                return true;
            }
        });
        mFab = (FloatingActionButton) v.findViewById(R.id.fab_tnmt_list);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFabClick();
            }
        });
        return v;
    }

    @Override
    public void onFabClick() {
        //  Create a new tournament
        AddTnmtDialogFrag frag = new AddTnmtDialogFrag();
        frag.show(childFm, "AddTnmtDialogFrag");
    }

    @Override
    public void onPositiveClick(String name) {
        Tnmt tnmt = new Tnmt(name, (int)(Math.random() * 1000000), false);
        tnmtsList.add(tnmt);
        mManager.addTnmt(tnmt);
        tnmtsListAdapter.notifyDataSetChanged();
    }
}
