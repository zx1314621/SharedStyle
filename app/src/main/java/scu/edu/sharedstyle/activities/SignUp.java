package scu.edu.sharedstyle.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
<<<<<<< Updated upstream
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
=======
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;
>>>>>>> Stashed changes

import scu.edu.sharedstyle.R;
import scu.edu.sharedstyle.model.User;

public class SignUp extends AppCompatActivity {
    private ImageView ivEye;
    private boolean isOpenEye = false;
    private ImageView ivEye_confirm;
    private boolean isOpenEye_confirm = false;
    EditText email;
    EditText password;
    EditText confirmpswd;
    private FirebaseAuth mAuth;
    String TAG = "SignUp";
    String mEmail;
    String mPassword;
    FirebaseFirestore mFirestroe;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the UI layout for this activity
        setContentView(R.layout.signup_page);
        getSupportActionBar().hide();
        email = findViewById(R.id.et_usernamesu);
        password = findViewById(R.id.et_passwordsu);
        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        confirmpswd = findViewById(R.id.et_confirmpswd);
        confirmpswd.setTransformationMethod(PasswordTransformationMethod.getInstance());
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

        ivEye_confirm = (ImageView) findViewById(R.id.iv_eye_confirm);

        ivEye_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOpenEye) {
                    ivEye_confirm.setSelected(true);
                    isOpenEye_confirm = true;
                    confirmpswd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    ivEye_confirm.setSelected(false);
                    isOpenEye_confirm = false;
                    confirmpswd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        mAuth = FirebaseAuth.getInstance();
        mFirestroe = FirebaseFirestore.getInstance();
    }


    public void signup(View view) {
        mEmail = email.getText().toString();
        mPassword = password.getText().toString();
        if(mEmail.matches("")||mPassword.matches("") || !mEmail.contains("@")){
            Toast.makeText(this, "Please enter valid email address and password", Toast.LENGTH_LONG).show();
            return;
        } else if(!mPassword.equals(confirmpswd.getText().toString())){
            Toast.makeText(this, "Please confirm passwords match", Toast.LENGTH_LONG).show();
            return;
        } else if(mPassword.length() < 8){
            Toast.makeText(this, "Password has to contain at least 8 characters or numbers", Toast.LENGTH_LONG).show();
            return;
        }

        else {
            mAuth.createUserWithEmailAndPassword(mEmail, mPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
<<<<<<< Updated upstream
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                addUser(user,mEmail);
                                goBrowse();
=======
                                String User_id = FirebaseAuth.getInstance().getUid();
                                Map<String, Object> map = new HashMap<>();
                                map.put("name", mEmail);
                                String token_id = MyFirebaseService.getToken(SignUp.this);
                                Log.d("token", token_id);
                                map.put("token_id", token_id);

                                mFirestroe.collection("Users").document(User_id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        goBrowse();
                                    }
                                });
>>>>>>> Stashed changes
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUp.this, "Authentication failed",
                                        Toast.LENGTH_SHORT).show(); // TODO: add task error message to the users
                            }
                        }
                    });
        }


    }

    private void goBrowse() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void addUser(FirebaseUser user,String email){
        DocumentReference userRef=
                FirebaseFirestore.getInstance().collection("users").document();
        User addedUser=new User(user.getUid(),email.substring(0,email.indexOf("@")));
        userRef.set(addedUser);
    }
}
