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
    private String apiKey = "4FL28vfZR3xRZSJToJnXUDUgD4iXDQDiUziLVSEl";
    private static final String BASE_URL = "https://api.challonge.com/v1/";
    private RequestQueue queue;
    private static ChallongeManager mManager;
    private Response.ErrorListener mErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            NetworkResponse response = error.networkResponse;
            if (response != null && response.data != null) {
                switch (response.statusCode) {
                    case 422:
                        String json = new String(response.data);
                        Log.e("t", json);
                        break;
                    default:
                        error.printStackTrace();
                }
            }
        }
    };

        private ChallongeManager(Context context) {
            queue = Volley.newRequestQueue(context);
        }

        public static ChallongeManager get(Context context) {
            if (mManager != null) {
                return mManager;
            }
            return new ChallongeManager(context);
        }

        public void getTnmts(Response.Listener<JSONArray> listener) {
            String url = BASE_URL + "tournaments.json?api_key=" + apiKey;
            JsonArrayRequest request = new JsonArrayRequest(url, listener, mErrorListener);
            queue.add(request);
        }

        public void addTnmt(final Tnmt tnmt, Response.Listener<String> listener) {
            String url = BASE_URL + "tournaments.json?api_key=" + apiKey;
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    listener, mErrorListener) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("tournament[name]", tnmt.getName());
                    params.put("tournament[url]", "" + tnmt.getUrl());
                    return params;
                }
            };
            queue.add(request);
        }

        public void deleteTnmt(Tnmt tnmt, Response.Listener<String> listener) {
            String url = BASE_URL + "tournaments/" + tnmt.getUrl() + ".json?api_key=" + apiKey;
            StringRequest request = new StringRequest(Request.Method.DELETE, url,
                    listener, mErrorListener);
            queue.add(request);
        }

        public void getPtcps(String tnmtUrl, Response.Listener<JSONArray> listener) {
            String url = BASE_URL + "tournaments/" + tnmtUrl + "/participants.json?api_key=" + apiKey;
            JsonArrayRequest request = new JsonArrayRequest(url, listener, mErrorListener);
            queue.add(request);
        }

        public void addPtcp(String tnmtUrl, final Ptcp ptcp, Response.Listener<String> listener) {
            String url = BASE_URL + "tournaments/" + tnmtUrl + "/participants.json?api_key=" + apiKey;
            StringRequest request = new StringRequest(Request.Method.POST, url, listener,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("participant[name]", ptcp.getName());
                    return params;
                }
            };
            queue.add(request);
        }

        public void deletePtcp(String tnmtUrl, final Ptcp ptcp, Response.Listener<String> listener) {
            String url = BASE_URL + "tournaments/" + tnmtUrl + "/participants/" + ptcp.getId()
                    + ".json?api_key=" + apiKey;
            StringRequest request = new StringRequest(Request.Method.DELETE, url, listener,
                    mErrorListener) {
            };
            queue.add(request);
        }

        public void updatePtcp(String tnmtUrl, String ptcpId, final String name, final String phoneNumber,
                               Response.Listener<String> listener) {
            String url = BASE_URL + "tournaments/" + tnmtUrl + "/participants/" + ptcpId
                    + ".json?api_key=" + apiKey;
            StringRequest request = new StringRequest(Request.Method.PUT, url, listener,
                    mErrorListener) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("participant[name]", name);

                    return params;
                }
            };
            queue.add(request);
        }

    }

