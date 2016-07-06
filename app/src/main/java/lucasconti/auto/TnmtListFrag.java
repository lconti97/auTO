package lucasconti.auto;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Lucas on 7/1/2016.
 */
public class TnmtListFrag extends Fragment implements MainActivity.FabListener,
        AddTnmtDialogFrag.AddTnmtDialogListener {
    private ArrayList<String> tnmts;
    private ListView tnmtsList;
    private ArrayAdapter<String> tnmtsListAdapter;
    private FragmentManager fm;
    private ChallongeManager mManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_tnmt_list, container, false);
        tnmts = new ArrayList<>(Arrays.asList("CEO", "Apex", "EVO"));
        tnmtsList = (ListView) v.findViewById(R.id.listview_tnmts);
        tnmtsListAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, tnmts);
        tnmtsList.setAdapter(tnmtsListAdapter);
        fm = getChildFragmentManager();
        mManager = ChallongeManager.get(getActivity());
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
        tnmts.add(name);
        tnmtsListAdapter.notifyDataSetChanged();
        mManager.addTnmt(name);
    }
}
