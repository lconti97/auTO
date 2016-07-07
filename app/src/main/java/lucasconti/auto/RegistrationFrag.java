package lucasconti.auto;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class RegistrationFrag extends Fragment
        implements AddPtcpDialogFrag.AddPtcpDialogListener,
        EditPtcpDialogFrag.EditPtcpDialogListener {

    private static final String TAG = "RegistrationFrag" ;
    private static final int REQUEST_SMS = 50;
    public static final String TAG_NAME = "name";
    public static final String TAG_NUMBER = "number";

    private ChallongeManager mManager;
    private ListView ptcpsList;
    private ArrayAdapter<Ptcp> ptcpsAdapter;
    private FragmentManager fm;
    private ArrayList<Ptcp> mPtcps;
    private Ptcp mCurrPtcp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_registration, container, false);
        String[] permissions = {Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS};
        askPermissions(permissions);
        fm = getChildFragmentManager();
        mManager = ChallongeManager.get(getActivity());
        mPtcps = new ArrayList<>();
        mManager.getPtcps("v2e83dfv", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = response.length() - 1; i >= 0;  i--) {
                        String name = response.getJSONObject(i).getJSONObject("participant")
                                .getString("name");
                        mPtcps.add(new Ptcp(name, "0"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ptcpsAdapter.notifyDataSetChanged();
            }
        });
        ptcpsList = (ListView) v.findViewById(R.id.ptcps_list);
        ptcpsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrPtcp = (Ptcp)parent.getItemAtPosition(position);
                EditPtcpDialogFrag frag = new EditPtcpDialogFrag();
                Bundle b = new Bundle();
                b.putString(TAG_NAME, mCurrPtcp.getName());
                b.putString(TAG_NUMBER, mCurrPtcp.getPhoneNumber());
                frag.setArguments(b);
                frag.show(fm, "EditPtcpDialogFrag");
            }
        });
        ptcpsAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, mPtcps);
        ptcpsList.setAdapter(ptcpsAdapter);

        return v;
    }

    private void askPermissions(String[] permissions) {
        if (Build.VERSION.SDK_INT >= 23) {
            ArrayList<String> permissionsNeeded = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++) {
                int permissionCheck = getContext().checkSelfPermission(permissions[i]);
                if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                    permissionsNeeded.add(permissions[i]);
                }
            }
            if (permissionsNeeded.size() != 0) {
                String[] permissionsToAsk = Arrays.copyOf(permissionsNeeded.toArray(),
                        permissionsNeeded.size(), String[].class);
                requestPermissions(permissionsToAsk, REQUEST_SMS);
            }
        }
    }

    public void addParticipant() {
        AddPtcpDialogFrag dialog = new AddPtcpDialogFrag();
        dialog.show(fm, "AddPtcpDialogFrag");
    }

    @Override
    public void onAddPtcpDialogPositiveClick(String name, String phoneNumber) {
        mPtcps.add(new Ptcp(name, phoneNumber));
        ptcpsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEditPtcpDialogPositiveClick(String name, String phoneNumber) {
        mCurrPtcp.setName(name);
        mCurrPtcp.setPhoneNumber(phoneNumber);
        ptcpsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEditPtcpDialogNegativeClick() {
        mPtcps.remove(mCurrPtcp);
        ptcpsAdapter.notifyDataSetChanged();
    }

}
