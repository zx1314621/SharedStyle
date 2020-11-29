package scu.edu.sharedstyle.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import scu.edu.sharedstyle.R;

public class Splash extends AppCompatActivity {

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser != null){
                    Intent i = new Intent(Splash.this, MainActivity.class);
                    startActivity(i);

                } else {
                    Intent i = new Intent(Splash.this, Front_page.class);
                    startActivity(i);
                }


                finish();
            }
        }, 3000);
    }

}