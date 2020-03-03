package startup.cube;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class WebService {
    private static String baseURL = "http://192.168.42.74:3000/mobile/";

    public static void registerUser(final JSONObject credObj, final Context ctx){
        RequestQueue queue = Volley.newRequestQueue(ctx);
        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.POST, baseURL.concat("registration_otp"), credObj,
        new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String statusCode = "";
                String message="";
                try {
                    statusCode = response.getString("statusCode");
                    message = response.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(statusCode.equals("200")){
                    Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
                    Intent OTPIntent = new Intent(ctx, OTPActivity.class);
                    Bundle OTPBundle = new Bundle();
                    try {
                        OTPBundle.putString("username", credObj.get("username").toString());
                        OTPBundle.putString("phone", credObj.get("phone").toString());
                        OTPBundle.putString("password", credObj.get("password").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    OTPIntent.putExtras(OTPBundle);
                    ctx.startActivity(OTPIntent);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ctx, "Network Error!", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(sr);
    }

    public static void authOTP(final JSONObject credObj, final Context ctx){
        RequestQueue queue = Volley.newRequestQueue(ctx);
        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.POST, baseURL.concat("register"), credObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String statusCode = "";
                        String message="";
                        String UUID = "";
                        try {
                            statusCode = response.getString("statusCode");
                            message = response.getString("message");
                            UUID = response.getString("uuid");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(statusCode.equals("200")){
                            Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
                            Intent LoginIntent = new Intent(ctx, LoginActivity.class);
                            Bundle LoginBundle = new Bundle();
                            try {
                                LoginBundle.putString("username", credObj.get("username").toString());
                                LoginBundle.putString("phone", credObj.get("phone").toString());
                                LoginBundle.putString("password", credObj.get("password").toString());
                                LoginBundle.putString("uuid", UUID);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            LoginIntent.putExtras(LoginBundle);
                            ctx.startActivity(LoginIntent);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ctx, "Network Error!", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(sr);
    }

    public static void authLogin(final JSONObject authObj, final Context ctx){
        RequestQueue queue = Volley.newRequestQueue(ctx);
        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.POST, baseURL.concat("login"), authObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String statusCode = "";
                        String message="";
                        String UUID = "";
                        try {
                            statusCode = response.getString("statusCode");
                            message = response.getString("message");
                            if(statusCode.equals("200")) UUID = response.getString("uuid");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(statusCode.equals("200")){
                            Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
                            Intent HomePageIntent = new Intent(ctx, MainActivity.class);
                            Bundle HomePageBundle = new Bundle();
                            try {
                                HomePageBundle.putString("username", authObj.get("username").toString());
                                HomePageBundle.putString("phone", authObj.get("phone").toString());
                                HomePageBundle.putString("password", authObj.get("password").toString());
                                HomePageBundle.putString("uuid", UUID);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            HomePageIntent.putExtras(HomePageBundle);
                            ctx.startActivity(HomePageIntent);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ctx, "Network Error!", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(sr);
    }

    public static void updateDP(final Bitmap finalDP, final Context ctx, final String phone){
        RequestQueue queue = Volley.newRequestQueue(ctx);

        File dpFile = new File(ctx.getFilesDir() + "/" + phone + ".jpeg");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        finalDP.compress(Bitmap.CompressFormat.JPEG, 0, bos);
        byte[] dpBitmapData = bos.toByteArray();

        try {
            dpFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(dpFile);
            fos.write(dpBitmapData);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        SimpleMultiPartRequest dpReq = new SimpleMultiPartRequest(
                Request.Method.POST, baseURL.concat("updateDP"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String statusCode = "";
                        String message = "";
                        try{
                            JSONObject responseObj = new JSONObject(response);
                            statusCode = responseObj.getString("statusCode");
                            message = responseObj.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(statusCode.equals("200")){
                            Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ctx, "network error", Toast.LENGTH_LONG).show();
                    }
                }
        );
        dpReq.addFile("image", dpFile.getPath());
        dpReq.addStringParam("phone", phone);
        queue.add(dpReq);
    }
}
