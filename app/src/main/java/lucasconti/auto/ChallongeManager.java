package lucasconti.auto;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lucas on 6/6/2016.
 */
public class ChallongeManager {
    private static final String TAG = "ChallongeManager";
    private String username = "lconti97";
    private String apiKey = "4FL28vfZR3xRZSJToJnXUDUgD4iXDQDiUziLVSEl";
    private static final String BASE_URL = "https://api.challonge.com/v1/";
    private RequestQueue queue;
    private static ChallongeManager mManager;

    private ChallongeManager(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public static ChallongeManager get(Context context) {
        if (mManager != null) {
            return mManager;
        }
        return new ChallongeManager(context);
    }

    public void test() {
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

    public void addTnmt(final String name) {
        String url = BASE_URL + "tournaments.json?api_key=" + apiKey;
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("t", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String json = null;
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            switch (response.statusCode) {
                                case 422:
                                    json = new String(response.data);
                                    Log.i("t", json);
                                    break;
                            }
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("tournament[name]", name);
                params.put("tournament[url]", "" +  (int)(Math.floor(Math.random() * 100000)));
                return params;
            }
        };
        queue.add(request);
    }

}

