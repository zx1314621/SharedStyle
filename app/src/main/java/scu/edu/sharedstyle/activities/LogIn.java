package scu.edu.sharedstyle.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import scu.edu.sharedstyle.R;
import scu.edu.sharedstyle.model.UserInfo;

public class LogIn extends AppCompatActivity {
    private ImageView ivEye;
    private boolean isOpenEye = false;

    public final static String TAG = "LogIn";
    private FirebaseAuth mAuth;
    Button signin;
    EditText email;
    EditText password;
    String mEmail;
    String mPassword;
    int RC_SIGN_IN = 0;
    private GoogleSignInClient mGoogleSignInClient;
    FirebaseFirestore firestore;
    Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "On Create");

        // set the UI layout for this activity
        setContentView(R.layout.login_page);
        getSupportActionBar().hide();

        // initialize UI elements
        email = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        signin = findViewById(R.id.bt_signin);
        ivEye = (ImageView) findViewById(R.id.iv_eye);

        ivEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOpenEye) {
                    ivEye.setSelected(true);
                    isOpenEye = true;
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    ivEye.setSelected(false);
                    isOpenEye = false;
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"click login");
                login();
            }
        });


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
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
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            goBrowse();
        }
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
            Log.d(TAG, "firebaseAuthWithGoogle: account exists" );
            Log.d(TAG, "firebaseAuthWithGoogle:" + account.getIdToken());
            firebaseAuthWithGoogle(account.getIdToken());
//            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d(TAG, "signInResult:failed code=" + e);
            updateUI(null);
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user!= null){

            mEmail = user.getEmail();
            addUser(user,mEmail);

            String User_id = FirebaseAuth.getInstance().getUid();

            Map<String, Object> map = new HashMap<>();
            map.put("name", mEmail);
            String token_id = MyFirebaseService.getToken(LogIn.this);
            String user_name = mEmail.substring(0, mEmail.indexOf("@"));
            Log.d("token", token_id);
            map.put("token_id", token_id);
            map.put("user_name", user_name);

            firestore.collection("Users").document(User_id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "createUserWithEmail:success");
                    goBrowse();
                }
            });

            goBrowse();
        } else{
            Toast.makeText(this, "Sign-in with Google account failed.", Toast.LENGTH_LONG).show();
        }

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
//                            mEmail = user.getEmail();
//                            addUser(user,mEmail);
//
//                            String User_id = FirebaseAuth.getInstance().getUid();
//
//                            Map<String, Object> map = new HashMap<>();
//                            map.put("name", mEmail);
//                            String token_id = MyFirebaseService.getToken(LogIn.this);
//                            Log.d("token", token_id);
//                            map.put("token_id", token_id);
//
//                            firestore.collection("Users").document(User_id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    Log.d(TAG, "createUserWithEmail:success");
//                                    goBrowse();
//                                }
//                            });

                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LogIn.this, "Authentication Failed.", Toast.LENGTH_LONG).show();
                            updateUI(null);
                        }

                    }
                });

    }

    public void login() {
        mEmail = email.getText().toString();
        mPassword = password.getText().toString();
        Log.i(TAG, "login");
        if (email.getText().toString().matches("") || password.getText().toString().matches("")) {
            Log.i(TAG, "email/password issue");
            Toast.makeText(this, "Please input valid E-mail or Password", Toast.LENGTH_LONG).show();
            return;
        } else {
            Log.i(TAG, "success");
            mAuth.signInWithEmailAndPassword(mEmail, mPassword)
                    .addOnCompleteListener(LogIn.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:success");
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.i(TAG, "signInWithEmail:success");
                                String token_id = MyFirebaseService.getToken(LogIn.this);
                                Log.d("token", token_id);
                                String User_id = mAuth.getCurrentUser().getUid();
                                Map<String, Object> map = new HashMap<>();
                                map.put("token_id", token_id);
                                firestore.collection("Users").document(User_id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        goBrowse();
                                    }
                                });

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.i(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LogIn.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void goBrowse() {
        Intent intent = new Intent(LogIn.this, MainActivity.class);
        startActivity(intent);
    }
    private void addUser(FirebaseUser user,String email){
        DocumentReference userRef=
                FirebaseFirestore.getInstance().collection("users").document();
        UserInfo addedUser=new UserInfo(user.getUid(),email.substring(0,email.indexOf("@")));
        userRef.set(addedUser);
    }

}

