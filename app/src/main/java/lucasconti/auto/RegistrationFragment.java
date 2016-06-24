package lucasconti.auto;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class RegistrationFragment extends Fragment
        implements AddPtcpDialogFrag.AddPtcpDialogListener {

    private static final String TAG = "RegistrationFragment" ;
    private static final int REQUEST_SMS = 50;
    private ChallongeManager manager;
    private ListView participantsList;
    private ArrayAdapter<Ptcp> participantsAdapter;
    private FragmentManager fm;
    private ArrayList<Ptcp> mPtcps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_registration, container, false);
        String[] permissions = {Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS};
        askPermissions(permissions);
        mPtcps = new ArrayList<>();
        mPtcps.add(new Ptcp("Kirodin", "17164720456"));
        mPtcps.add(new Ptcp("Zain", "17164728811"));
        mPtcps.add(new Ptcp("Hbox", "17164728005"));
        participantsList = (ListView) v.findViewById(R.id.participants_list);
        participantsAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, mPtcps);
        participantsList.setAdapter(participantsAdapter);

        fm = getChildFragmentManager();

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
        participantsAdapter.notifyDataSetChanged();
    }

}
