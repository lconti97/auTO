package lucasconti.auto;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String TAG = "MainActivityFragment" ;
    private static final int REQUEST_RECEIVE_SMS = 50;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        askPermission();
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    private void askPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionCheck = getContext().checkSelfPermission(
                    Manifest.permission.RECEIVE_SMS);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS},
                        REQUEST_RECEIVE_SMS);
            }
        }
    }
}
