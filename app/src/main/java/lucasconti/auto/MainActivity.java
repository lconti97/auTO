package lucasconti.auto;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();
        showTnmtListFrag(savedInstanceState);
    }

    public void setTitle(String title) {
        mToolbar.setTitle(title);
    }

    private void setToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("Tournaments");
        setSupportActionBar(mToolbar);
    }

    private void showTnmtListFrag(Bundle savedInstanceState) {
        //  Stop the fragment from being created multiple times
        if (savedInstanceState == null) {
            TnmtListFrag frag = new TnmtListFrag();
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().add(R.id.content, frag).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_finish) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
