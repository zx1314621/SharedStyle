package scu.edu.sharedstyle.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import scu.edu.sharedstyle.R;
import scu.edu.sharedstyle.model.GlideApp;
import scu.edu.sharedstyle.model.Item;

public class purchased_details extends AppCompatActivity
        implements EventListener<DocumentSnapshot> {

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private TextView productName;
    private TextView productBrand;
    private TextView productDescription;
    private TextView productPrice;
    private ImageView backBtn;
    private String itemName;
    private String item_brand;
    private double item_price;
    private String itemPath;

    //For firestore test
    private FirebaseFirestore firestore;
    private DocumentReference productRef;
    private ListenerRegistration productRegistration;


    private ArrayList<String> url = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchased_details);

        getSupportActionBar().hide();


        Bundle bundle =getIntent().getExtras();
        itemPath=bundle.getString("itemPath");
        url=bundle.getStringArrayList("imgURLs");

        firestore=FirebaseFirestore.getInstance();
        productRef=firestore.document(itemPath);


        viewPager2 = findViewById(R.id.pager1);
        tabLayout=findViewById(R.id.tab_layout1);
        viewPager2.setAdapter(new ScreenSlidePagerAdapter(url));
        //Indicator
        new TabLayoutMediator(tabLayout, viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                    }
                }
        ).attach();



        productName=findViewById(R.id.product_name1);
        productBrand=findViewById(R.id.product_brand1);
        productDescription=findViewById(R.id.product_description1);
        productPrice=findViewById(R.id.product_price1);
        backBtn=findViewById(R.id.ButtonBack1);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



    }


    @Override
    protected void onStart() {
        super.onStart();

        productRegistration=productRef.addSnapshotListener(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(productRegistration!=null){
            productRegistration.remove();
            productRegistration=null;
        }
    }

    @Override
    public void onBackPressed() {

        this.finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void buy(View view) {
        Intent intent = new Intent(this, Purchase.class);
        intent.putExtra("name", itemName);
        intent.putExtra("price", item_price);
        intent.putExtra("itemPath",itemPath);
        startActivity(intent);
    }



    @Override
    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
        if (error != null) {
            return;
        }
        onProductLoaded(snapshot.toObject(Item.class));
    }

    private void onProductLoaded(Item item){
        itemName=item.getItemName();
        productName.setText(item.getItemName());
        productBrand.setText(item.getBrand());
        productDescription.setText(item.getItemDesc());
        NumberFormat nm=NumberFormat.getInstance();
        item_price=item.getPrice();
        productPrice.setText(nm.format(item.getPrice()));
//        url=item.getImgURLs();



    }


    private static class ScreenSlidePagerAdapter extends RecyclerView.Adapter<ScreenSlidePagerAdapter.CardViewHolder> {
        private Context context;
        private StorageReference mstorageReference;

        private ArrayList<String> imageList;
        public ScreenSlidePagerAdapter(ArrayList<String> imageList){
            this.imageList =imageList;
        }


        @NonNull
        @Override
        public ScreenSlidePagerAdapter.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            context = parent.getContext();

            return new CardViewHolder(LayoutInflater.from(context).inflate(R.layout.image_scrolling, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ScreenSlidePagerAdapter.CardViewHolder holder, int position) {
            holder.setImageView(imageList.get(position));
        }

        @Override
        public int getItemCount() {
            return imageList.size();
        }

        public class CardViewHolder extends RecyclerView.ViewHolder {

            private ImageView imageView;

            public CardViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.product_image);
            }

            public void setImageView(String url){
                mstorageReference= FirebaseStorage.getInstance().getReferenceFromUrl(url);
                GlideApp.with(context)
                        .load(mstorageReference)
                        .into(imageView);

            }

        }
    }


}