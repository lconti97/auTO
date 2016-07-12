package lucasconti.auto;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Lucas on 7/1/2016.
 */
public class TnmtListFrag extends Fragment implements AddTnmtDialogFrag.AddTnmtDialogListener {
    private ArrayList<Tnmt> mTnmtsList;
    private ListView mTnmtsListView;
    private ArrayAdapter<Tnmt> mTnmtsListAdapter;
    private FragmentManager mChildFm;
    private ChallongeManager mManager;
    private FloatingActionButton mFab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_tnmt_list, container, false);
        setupTnmtsList(v);
        getTnmts();
        setupFab(v);
        mChildFm = getChildFragmentManager();
        return v;
    }

    @Override
    public void onPositiveClick(String name) {
        addTnmt(name);
    }

    private void setupTnmtsList(View v) {
        mTnmtsList = new ArrayList<>();
        mTnmtsListView = (ListView) v.findViewById(R.id.listview_tnmts);
        mTnmtsListAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, mTnmtsList);
        mTnmtsListView.setAdapter(mTnmtsListAdapter);
        mTnmtsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                swapToRegistrationFrag(mTnmtsList.get(position));
            }
        });
        mTnmtsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {
                showDeleteTnmtDialog(position);
                //  Return true to prevent onItemClick() from being called as well
                return true;
            }
        });
    }

    private void swapToRegistrationFrag(Tnmt tnmt) {
        RegistrationFrag frag = new RegistrationFrag();
        Bundle b = new Bundle();
        b.putString(RegistrationFrag.TAG_TNMT_URL, tnmt.getUrl());
        frag.setArguments(b);
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().addToBackStack(null).replace(R.id.content, frag)
                .commit();
    }

    private void showDeleteTnmtDialog(final int position) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity()).setItems(
                new String[] {"Delete"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mManager.deleteTnmt(mTnmtsList.get(position), new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {
                                mTnmtsList.remove(position);
                                mTnmtsListAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }).create();
        dialog.show();
    }

    private void showAddTnmtDialog() {
        AddTnmtDialogFrag frag = new AddTnmtDialogFrag();
        frag.show(mChildFm, "AddTnmtDialogFrag");
    }

    private void getTnmts() {
        mManager = ChallongeManager.get(getActivity());
        mManager.getTnmts(new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //  Iterate backwards to put most recent tnmts on top
                for (int i = response.length() - 1; i >= 0; i--) {
                    parseTnmtJSON(response, i);
                }
                mTnmtsListAdapter.notifyDataSetChanged();
            }
        });
    }

    private void parseTnmtJSON(JSONArray jsonArray, int i) {
        try {
            //  The response is an array containing an object that contains our object
            JSONObject intermediateJSON = jsonArray.getJSONObject(i);
            JSONObject tnmtJSON = intermediateJSON.getJSONObject("tournament");
            String name = tnmtJSON.getString("name");
            String url = tnmtJSON.getString("url");
            String startedString = tnmtJSON.getString("started_at");
            //  If "started_at" = null, the tnmt hasn't started
            boolean started = !startedString.equals("null");
            mTnmtsList.add(new Tnmt(name, url, started));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setupFab(View v) {
        mFab = (FloatingActionButton) v.findViewById(R.id.fab_tnmt_list);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTnmtDialog();
            }
        });
    }

    private void addTnmt(String name) {
        String url = "t" + (int)(Math.random() * 1000000);
        final Tnmt tnmt = new Tnmt(name, url + "", false);
        mManager.addTnmt(tnmt, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mTnmtsList.add(tnmt);
                mTnmtsListAdapter.notifyDataSetChanged();
            }
        });
    }
}
