package startup.cube;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class WebService {
    private static String baseURL = "http://192.168.42.74:3000/mobile/";

    public static void registerUser(JSONObject credObj, final Context ctx){
        RequestQueue queue = Volley.newRequestQueue(ctx);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, baseURL.concat("register"), credObj,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    String statusCode = null;
                    String message = null;
                    try {
                        statusCode = response.getString("statusCode");
                        message = response.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(statusCode.equals("200")){
                        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ctx, "Network Error", Toast.LENGTH_SHORT).show();
                    Toast.makeText(ctx, "Please try again", Toast.LENGTH_SHORT).show();
                }
            });
        queue.add(req);
    }
}
