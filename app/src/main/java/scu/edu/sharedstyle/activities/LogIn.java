package scu.edu.sharedstyle.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import scu.edu.sharedstyle.R;

public class LogIn extends AppCompatActivity {
    public final static String TAG = "MainActivity";
    public static String USERNAME = "testUser";
    public static String PASSWORD = "123456";

    EditText username;
    EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "On Create");

        // set the UI layout for this activity
        setContentView(R.layout.login_page);

        // initialize UI elements
        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
    }
    public void login(View view) {
        if(! username.getText().toString().equals(USERNAME) || !password.getText().toString().equals(PASSWORD)){
            Toast.makeText(this, "Incorrect Name or Password", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("name", USERNAME);
        startActivity(intent);
    }


    public void signup(View view) {
        if(username.getText().toString().matches("")||password.getText().toString().matches("")){
            Toast.makeText(this, "Please enter valid username and password", Toast.LENGTH_LONG).show();
            return;
        } else {
            USERNAME = username.getText().toString();
            PASSWORD = password.getText().toString();
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("name", USERNAME);
        intent.putExtra("password", PASSWORD);
        startActivity(intent);

    }
}
