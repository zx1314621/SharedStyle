package scu.edu.sharedstyle.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Table;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;


import scu.edu.sharedstyle.R;
public class SettingsActivity extends AppCompatActivity {

    private TableRow tr_logOut;

    private TableRow tr_address;
    private TableRow tr_resetPW;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle("Settings");

        tr_logOut=findViewById(R.id.tr_logOut);
        tr_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
        mAuth = FirebaseAuth.getInstance();

        tr_address=findViewById(R.id.tr_address);
        tr_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAddress();
            }
        });

        tr_resetPW=findViewById(R.id.tr_resetpw);
        tr_resetPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });


    }

    private void logOut(){
        mAuth.signOut();
        Intent intent = new Intent(this,Front_page.class);
        Toast.makeText(this, "Log out", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        FirebaseAuth.getInstance().signOut();
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
    }

    private void changeAddress(){
        Intent intent=new Intent(this,AddressActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
    }

    private void resetPassword(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please input your new password");

        final View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_changepassword,null);
        builder.setView(dialogView);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText newPSW=dialogView.findViewById(R.id.et_newpswd);
                EditText conPSW=dialogView.findViewById(R.id.et_confirmpswd);
                String newPassword=newPSW.getText().toString();
                String nextPassword=conPSW.getText().toString();
                if(newPassword.length()>7&&newPassword.equals(nextPassword)){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.updatePassword(newPassword)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        System.out.println("change success");
                                        return;
                                    }
                                }
                            });
                    Intent intent = new Intent(getApplicationContext(),Front_page.class);
                    startActivity(intent);
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(getApplicationContext(),"Please Sign in again",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else if(!newPassword.equals(nextPassword)){
                    Toast.makeText(getApplicationContext(),"Password does not match",Toast.LENGTH_SHORT).show();
                }
                else if(newPassword.length()<8){
                    Toast.makeText(getApplicationContext(),"Password has to be at least 8 characters or numbers",Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }
}