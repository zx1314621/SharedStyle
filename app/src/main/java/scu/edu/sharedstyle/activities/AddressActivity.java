package scu.edu.sharedstyle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import scu.edu.sharedstyle.R;
import scu.edu.sharedstyle.model.User;

public class AddressActivity extends AppCompatActivity {


    private Button btn_cancel;
    private Button btn_save;
    private EditText et_name;
    private EditText et_phone;
    private EditText et_street;
    private EditText et_state;
    private EditText et_zip;
    private DocumentReference currentRef;
    private FirebaseUser user;
    private CollectionReference userCollec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle("Address");

        btn_cancel=findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        btn_save=findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        et_name=findViewById(R.id.et_name);
        et_phone=findViewById(R.id.et_phone);
        et_street=findViewById(R.id.et_street);
        et_state=findViewById(R.id.et_state);
        et_zip=findViewById(R.id.et_zip);

        currentRef=FirebaseFirestore.getInstance().collection("users").document();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userCollec= FirebaseFirestore.getInstance().collection("users");

        userCollec.whereEqualTo("uid",user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                User user=document.toObject(User.class);
                                et_name.setText(user.getName());
                                et_phone.setText(user.getPhone());
                                et_street.setText(user.getStreet());
                                et_state.setText(user.getState());
                                et_zip.setText(user.getZIP());
                            }
                        }
                    }
                });



    }

    private void cancel(){
        this.finish();
    }

    private void save(){
        final String name = et_name.getText().toString();
        final String phone=et_phone.getText().toString();
        final String street=et_street.getText().toString();
        final String state=et_state.getText().toString();
        final String zip=et_zip.getText().toString();

        if(name.equals("")||phone.equals("")||street.equals("")||state.equals("")||zip.equals("")){
            Toast.makeText(this,"Please fill up all the filed",Toast.LENGTH_SHORT).show();
        }
        else{
            userCollec.whereEqualTo("uid",user.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    User user=document.toObject(User.class);
                                    user.setName(name);
                                    user.setPhone(phone);
                                    user.setStreet(street);
                                    user.setState(state);
                                    user.setZIP(zip);
                                    DocumentReference tempUserRef=document.getReference();
                                    tempUserRef.delete();
                                    currentRef.set(user);
                                }
                            }
                        }
                    });
        }
        Toast.makeText(this,"Change Address Successful",Toast.LENGTH_SHORT).show();
        this.finish();
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