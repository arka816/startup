package startup.cube;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import static startup.cube.WebService.authLogin;

public class LoginActivity extends AppCompatActivity {

    private static boolean isPasswordVisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.statusBarColor));

        final EditText loginPhone = findViewById(R.id.loginPhone);
        final EditText loginPassword = findViewById(R.id.loginPassword);
        Button loginButton = findViewById(R.id.loginButton);
        final ImageView loginPasswordEye = findViewById(R.id.loginPasswordEye);

        String username;
        String phone;
        final String password;

        Bundle cred = getIntent().getExtras();

        if((cred) != null){
            username = cred.getString("username");
            phone = cred.getString("phone");
            password = cred.getString("password");
            loginPhone.setText(phone);
            loginPassword.setText(password);
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject authObj = new JSONObject();
                try{
                    authObj.put("phone", loginPhone.getText().toString());
                    authObj.put("password", loginPassword.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                authLogin(authObj, getApplicationContext());
            }
        });

        loginPasswordEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPasswordVisible){
                    loginPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD );
                    loginPasswordEye.setImageResource(R.drawable.eyeiconslash);
                }
                else{
                    loginPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT );
                    loginPasswordEye.setImageResource(R.drawable.eyeicon);
                }
                loginPassword.setTypeface(loginPhone.getTypeface());
                isPasswordVisible = !isPasswordVisible;
                loginPassword.setSelection(loginPassword.getText().length());
            }
        });
    }
}
