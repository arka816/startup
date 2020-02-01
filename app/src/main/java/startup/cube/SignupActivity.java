package startup.cube;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static startup.cube.WebService.registerUser;

public class SignupActivity extends AppCompatActivity {
    boolean isPasswordVisible = false;
    boolean isCPasswordVisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.statusBarColor));

        final ImageView passwordEye = findViewById(R.id.passwordEye);
        final ImageView cpasswordEye = findViewById(R.id.cpasswordEye);
        final EditText signupName = findViewById(R.id.signupName);
        final EditText signupPhone = findViewById(R.id.signupPhone);
        final EditText signupPassword = findViewById(R.id.signupPassword);
        final EditText signupCPassword = findViewById(R.id.signupCPassword);
        final Button signup = findViewById(R.id.signupButton);

        passwordEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPasswordVisible){
                    signupPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD );
                    passwordEye.setImageResource(R.drawable.eyeiconslash);
                }
                else{
                    signupPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT );
                    passwordEye.setImageResource(R.drawable.eyeicon);
                }
                signupPassword.setTypeface(signupName.getTypeface());
                isPasswordVisible = !isPasswordVisible;
                signupPassword.setSelection(signupPassword.getText().length());
            }
        });

        cpasswordEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isCPasswordVisible){
                    signupCPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD );
                    cpasswordEye.setImageResource(R.drawable.eyeiconslash);
                }
                else{
                    signupCPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    cpasswordEye.setImageResource(R.drawable.eyeicon);
                }
                signupCPassword.setTypeface(signupName.getTypeface());
                isCPasswordVisible = !isCPasswordVisible;
                signupCPassword.setSelection(signupCPassword.getText().length());
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!signupPassword.getText().toString().equals(signupCPassword.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
                    return;
                }
                JSONObject credObj = new JSONObject();
                try {
                    credObj.put("username", signupName.getText().toString());
                    credObj.put("phone", signupPhone.getText().toString());
                    credObj.put("password", signupPassword.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                registerUser(credObj, getApplicationContext());
            }
        });
    }
}
