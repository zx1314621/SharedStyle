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

public class PurchasedItem extends AppCompatActivity {

    private FirestoreAdapter adapter;
    private RecyclerView purchasedView;

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
        purchasedView.addOnItemTouchListener(new RecyclerItemClickListener(this,purchasedView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                System.out.println("hello world");
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

        }));
        purchasedView.setAdapter(adapter);

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

        adapter=new FirestoreAdapter(options);

    }

    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private OnItemClickListener mListener;


        public interface OnItemClickListener {
            public void onItemClick(View view, int position);
            public void onLongItemClick(View view, int position);
        }

        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && mListener != null) {
                        mListener.onLongItemClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
                mListener.onLongItemClick(childView,view.getChildAdapterPosition(childView));
                return true;
            }
            return false;
        }

        @Override public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }

        @Override
        public void onRequestDisallowInterceptTouchEvent (boolean disallowIntercept){}
    }
}