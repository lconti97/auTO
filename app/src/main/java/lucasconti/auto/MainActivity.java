package lucasconti.auto;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showTnmtListFrag(savedInstanceState);
    }

    private void showTnmtListFrag(Bundle savedInstanceState) {
        //  Stop the fragment from being created multiple times
        if (savedInstanceState == null) {
            TnmtListFrag frag = new TnmtListFrag();
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().add(R.id.content, frag).commit();
        }
    }
}
