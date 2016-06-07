package lucasconti.auto;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Lucas on 6/6/2016.
 */
public class ChallongeManager {
    private static final String TAG = "ChallongeManager";
    private Context context;
    private String username = "lconti97";
    private String apiKey = "4FL28vfZR3xRZSJToJnXUDUgD4iXDQDiUziLVSEl";
    RequestQueue queue;

    public ChallongeManager(Context context) {
        this.context = context;
    }

    public void test() {
        queue = Volley.newRequestQueue(context);
        String url = "https://api.challonge.com/v1/tournaments.json?api_key=" + apiKey;
        JsonArrayRequest testRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, "Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });


// Add the request to the RequestQueue.
        queue.add(testRequest);
    }
}
