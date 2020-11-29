package scu.edu.sharedstyle.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;

import java.util.ArrayList;
import java.util.List;

import scu.edu.sharedstyle.R;

public class Front_page extends AppCompatActivity {
    private Button btn_SignIn;
    private Button btn_SignUp;
    private SharedPreferences sharedPreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the UI layout for this activity
        setContentView(R.layout.front_page);
        getSupportActionBar().hide();

        sharedPreferences = getSharedPreferences("count",MODE_PRIVATE);
        int count = sharedPreferences.getInt("count",0);
        Log.d("print", String.valueOf(count));

        if (count == 0){
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), FirstStartActivity.class);
            startActivity(intent);
            this.finish();
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("count",++count);
        editor.commit();





        // initialize UI elements
        btn_SignIn = findViewById(R.id.bt_signin_front);
        btn_SignUp = findViewById(R.id.bt_signup_front);

        btn_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signin();
            }
        });

        btn_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });


    }


    private void signup() {
        Intent intentsu = new Intent(this, SignUp.class);
        startActivity(intentsu);
    }


    private void signin() {
        Intent intentsi = new Intent(this, LogIn.class);
        startActivity(intentsi);

    }

    @Override
    public void onBackPressed() {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }

}