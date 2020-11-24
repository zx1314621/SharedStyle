package scu.edu.sharedstyle.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import scu.edu.sharedstyle.R;

public class SignUp extends AppCompatActivity {
    public static String USERNAME = "testUser";
    public static String PASSWORD = "123456";
    EditText username;
    EditText password;
    EditText confirmpswd;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the UI layout for this activity
        setContentView(R.layout.signup_page);
        username = findViewById(R.id.et_usernamesu);
        password = findViewById(R.id.et_passwordsu);
        confirmpswd = findViewById(R.id.et_confirmpswd);

    }


    public void signup(View view) {
        if(username.getText().toString().matches("")||password.getText().toString().matches("")){
            Toast.makeText(this, "Please enter valid username and password", Toast.LENGTH_LONG).show();
            return;
        } else if(!password.getText().toString().equals(confirmpswd.getText().toString())){
            Toast.makeText(this, "Please confirm passwords match", Toast.LENGTH_LONG).show();
            return;
        }
        else {
            USERNAME = username.getText().toString();
            PASSWORD = password.getText().toString();
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("name", USERNAME);
        intent.putExtra("password", PASSWORD);
        startActivity(intent);

    }
}
