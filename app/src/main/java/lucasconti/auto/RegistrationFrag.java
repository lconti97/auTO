package lucasconti.auto;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import java.util.Arrays;

public class RegistrationFrag extends Fragment
        implements AddPtcpDialogFrag.AddPtcpDialogListener,
        EditPtcpDialogFrag.EditPtcpDialogListener {

    private static final int REQUEST_SMS = 50;
    public static final String TAG_TNMT_URL = "tnmtUrl";
    public static final String TAG_TNMT_NAME = "tnmtName";

    private ChallongeManager mManager;
    private ListView ptcpsList;
    private ArrayAdapter<Ptcp> ptcpsAdapter;
    private FragmentManager mChildFm;
    private ArrayList<Ptcp> mPtcps;
    private FloatingActionButton mFab;
    private Ptcp mCurrPtcp;
    private String mTnmtUrl;
    private String mTnmtName;
    private SharedPreferences mPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_registration, container, false);
        askPermissions();
        mChildFm = getChildFragmentManager();
        mManager = ChallongeManager.get(getActivity());
        mTnmtUrl = getArguments().getString(TAG_TNMT_URL);
        mTnmtName = getArguments().getString(TAG_TNMT_NAME);
        mPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        ((MainActivity) getActivity()).setTitle(mTnmtName);
        setupPtcpList(v);
        getPtcps();
        setupFab(v);
        return v;
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        ((MainActivity) getActivity()).setTitle("Tournaments");
//    }

    @Override
    public void onAddPtcpDialogPositiveClick(String name, String phoneNumber) {
        final Ptcp ptcp = new Ptcp(name, phoneNumber, null);
        mManager.addPtcp(mTnmtUrl, ptcp, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject intermediateJSON = new JSONObject(response);
                    JSONObject ptcpJSON = intermediateJSON.getJSONObject("participant");
                    ptcp.setId(ptcpJSON.getString("id"));
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                mPtcps.add(ptcp);
                ptcpsAdapter.notifyDataSetChanged();
                storePtcpPhoneNumber(ptcp.getId(), ptcp.getPhoneNumber());
            }
        });
    }

    @Override
    public void onEditPtcpDialogPositiveClick(final String name, final String phoneNumber) {
        mManager.updatePtcp(mTnmtUrl, mCurrPtcp.getId(), name, phoneNumber,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mCurrPtcp.setName(name);
                mCurrPtcp.setPhoneNumber(phoneNumber);
                ptcpsAdapter.notifyDataSetChanged();
            }
        });
    }

    private void deletePtcp(final Ptcp ptcp) {
        mManager.deletePtcp(mTnmtUrl, ptcp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mPtcps.remove(ptcp);
                ptcpsAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setupPtcpList(View v) {
        mPtcps = new ArrayList<>();
        ptcpsList = (ListView) v.findViewById(R.id.ptcps_list);
        ptcpsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrPtcp = (Ptcp)parent.getItemAtPosition(position);
                showEditPtcpDialog();
            }
        });
        ptcpsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrPtcp = (Ptcp)parent.getItemAtPosition(position);
                new AlertDialog.Builder(getActivity()).setItems(new String[] {"Delete"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deletePtcp(mCurrPtcp);
                            }
                        }).show();
                return true;
            }
        });
        ptcpsAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, mPtcps);
        ptcpsList.setAdapter(ptcpsAdapter);
    }

    private void askPermissions() {
        String[] permissions = {Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS};
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

    private void getPtcps() {
        mManager.getPtcps(mTnmtUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = response.length() - 1; i >= 0;  i--) {
                        JSONObject ptcpJSON = response.getJSONObject(i).getJSONObject("participant");
                        String name = ptcpJSON.getString("name");
                        String id = ptcpJSON.getString("id");
                        String phoneNumber = getStoredPhoneNumber(id);
                        mPtcps.add(new Ptcp(name, phoneNumber, id));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ptcpsAdapter.notifyDataSetChanged();
            }
        });
    }

    private void showEditPtcpDialog() {
        EditPtcpDialogFrag frag = new EditPtcpDialogFrag();
        Bundle b = new Bundle();
        b.putString(EditPtcpDialogFrag.TAG_NAME, mCurrPtcp.getName());
        b.putString(EditPtcpDialogFrag.TAG_NUMBER, mCurrPtcp.getPhoneNumber());
        frag.setArguments(b);
        frag.show(mChildFm, "EditPtcpDialogFrag");
    }

    private void setupFab(View v) {
        mFab = (FloatingActionButton) v.findViewById(R.id.fab_registration);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPtcpDialogFrag dialog = new AddPtcpDialogFrag();
                dialog.show(mChildFm, "AddPtcpDialogFrag");
            }
        });
    }

    private void storePtcpPhoneNumber(String ptcpId, String phoneNumber) {
        if (!mPreferences.contains(ptcpId)) {
            mPreferences.edit().putString(ptcpId, phoneNumber).apply();
        }
    }

    private String getStoredPhoneNumber(String ptcpId) {
        return mPreferences.getString(ptcpId, "");
    }

}
