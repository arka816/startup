package startup.cube;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static startup.cube.WebService.authOTP;

public class OTPActivity extends AppCompatActivity {
    String old2;
    String old3;
    String old4;
    public String baseURL = "http://192.168.42.74:3000/mobile/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        Bundle credBundle = getIntent().getExtras();
        String phone = credBundle.getString("phone");
        TextView otpTextView = findViewById(R.id.otpNumber);
        String otpText = "OTP has been sent to you on your mobile number +91 " + phone + ". please enter it below";
        otpTextView.setText(otpText);

        final JSONObject credObj = new JSONObject();
        try {
            credObj.put("phone", phone);
            credObj.put("username", credBundle.getString("username"));
            credObj.put("password", credBundle.getString("password"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.statusBarColor));

        final EditText otp1 = findViewById(R.id.otp1);
        final EditText otp2 = findViewById(R.id.otp2);
        final EditText otp3 = findViewById(R.id.otp3);
        final EditText otp4 = findViewById(R.id.otp4);
        final Button OTPButton = findViewById(R.id.otpButton);

        otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(otp1.getText().toString().length() > 0){
                    otp2.requestFocus();
                }
            }
        });

        otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(otp2.getText().toString().length() > 0){
                    otp3.requestFocus();
                    old2 = otp2.getText().toString();
                }
            }
        });
        otp2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL){
                    if(old2.isEmpty()){
                        otp1.requestFocus();
                    }
                }
                old2 = otp2.getText().toString();
                return false;
            }
        });

        otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(otp3.getText().toString().length() > 0){
                    otp4.requestFocus();
                    old3 = otp3.getText().toString();
                }
            }
        });
        otp3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL){
                    if(old3.isEmpty()){
                        otp2.requestFocus();
                    }
                }
                old3 = otp3.getText().toString();
                return false;
            }
        });

        otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(otp4.getText().toString().length() > 0){
                    old4 = otp4.getText().toString();
                }
            }
        });
        otp4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL){
                    if(old4.isEmpty()){
                        otp3.requestFocus();
                    }
                }
                old4 = otp4.getText().toString();
                return false;
            }
        });

        OTPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String otp = otp1.getText().toString() + otp2.getText().toString() + otp3.getText().toString() + otp4.getText().toString();
                try{
                    credObj.put("otp", otp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                authOTP(credObj, getApplicationContext());
            }
        });
    }
}
