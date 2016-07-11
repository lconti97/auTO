package lucasconti.auto;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fm;
    private static final String TAG = "MainActivity";
    private RegistrationFrag registrationFrag;
    private RunTnmtFrag runTnmtFrag;
    private TnmtListFrag tnmtListFrag;
    public interface FabListener {
        public void onFabClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fm = getSupportFragmentManager();
        //  Stop the fragment from being created multiple times
        if (savedInstanceState == null) {
            tnmtListFrag = new TnmtListFrag();
            fm.beginTransaction().add(R.id.content, tnmtListFrag).commit();
        }

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                tnmtListFrag.onFabClick();
//                regFrag.addParticipant();
//                fm.beginTransaction().replace(R.id.content, new RunTnmtFrag())
//                        .commit();
//                ChallongeManager manager = new ChallongeManager(getApplicationContext());
//                manager.test();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
