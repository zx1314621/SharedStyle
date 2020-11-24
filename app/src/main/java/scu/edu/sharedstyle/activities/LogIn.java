package scu.edu.sharedstyle.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import scu.edu.sharedstyle.R;

public class LogIn extends AppCompatActivity {
    public final static String TAG = "MainActivity";
    public static String USERNAME = "testUser";
    public static String PASSWORD = "123456";

    EditText username;
    EditText password;
    int RC_SIGN_IN = 0;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "On Create");

        // set the UI layout for this activity
        setContentView(R.layout.login_page);

        // initialize UI elements
        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gsignIn();
            }
        });
    }

    private void gsignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account!= null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else{
            Toast.makeText(this, "Sign-in with Google account failed.", Toast.LENGTH_LONG).show();
        }

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
