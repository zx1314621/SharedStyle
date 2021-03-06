package scu.edu.sharedstyle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import scu.edu.sharedstyle.R;
import scu.edu.sharedstyle.model.Item;
import scu.edu.sharedstyle.recyclerview.FirestoreAdapter;
import scu.edu.sharedstyle.recyclerview.PurchaseAdapter;

public class PurchasedItem extends AppCompatActivity {

    private PurchaseAdapter adapter;
    private RecyclerView purchasedView;
    private TextView tv_nothing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchased_item);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle("Purchased");

        purchasedView=findViewById(R.id.rv_purchased);
        purchasedView.setLayoutManager(new LinearLayoutManager(this));
        getData();
        //adapter.isClickable=false;
        purchasedView.setAdapter(adapter);
        tv_nothing=findViewById(R.id.tv_purchased);

    }


    @Override
    public void onStart() {
        adapter.startListening();
        super.onStart();
    }

    @Override
    public void onStop() {
        adapter.stopListening();
        super.onStop();
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

    private void getData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference productCollect = firestore.collection("sold");
        DocumentReference productRef = productCollect.document();

        Query query = productCollect
                .whereEqualTo("userID",user.getUid())
                .orderBy("timestamp",Query.Direction.DESCENDING)
                .limit(50);


        FirestoreRecyclerOptions<Item> options = new FirestoreRecyclerOptions.Builder<Item>()
                .setQuery(query, Item.class)
                .build();

        adapter=new PurchaseAdapter(options){
            @Override
            public void onDataChanged() {
                if (getItemCount() == 0){
                    tv_nothing.setVisibility(View.VISIBLE);
                }
                else{
                    tv_nothing.setVisibility(View.GONE);
                }
            }
        };

    }

}