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

public class ProductDetailActivity extends AppCompatActivity
        implements EventListener<DocumentSnapshot> {

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private TextView productName;
    private TextView productBrand;
    private TextView productDescription;
    private TextView productPrice;
    private Button buyNow;
    private String itemName;
    private String item_brand;
    private double item_price;
    private String itemPath;

    //For firestore test
    private FirebaseFirestore firestore;
    private DocumentReference productRef;
    private ListenerRegistration productRegistration;



    private ArrayList<Integer> url=new ArrayList<>();
//    private ArrayList<String> url = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);

        ActionBar actionBar=getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        Bundle bundle =getIntent().getExtras();

        itemName = bundle.getString("itemName");
        item_brand = bundle.getString("item_brand");
        item_price = bundle.getDouble("item_price");
        itemPath=bundle.getString("itemPath");
        int img_url = bundle.getInt("img_url");
        String img_desc = bundle.getString("img_desc");

        firestore=FirebaseFirestore.getInstance();
        productRef=firestore.document(itemPath);


        Toast.makeText(this, "itemName :" + itemName, Toast.LENGTH_SHORT).show();

        //For image load test
//        url.add("https://s3.ax1x.com/2020/11/11/BvRJZF.jpg");
//        url.add("https://images.macrumors.com/t/GWt5RjGgFZHXv4jPG-W9BHTKqSU=/1960x/https://images.macrumors.com/article-new/2020/11/new-m1-chip.jpg");
//        url.add("https://www.longchamp.com/dw/image/v2/BCVX_PRD/on/demandware.static/-/Sites-LC-master-catalog/default/dw48cfd7a3/images/DIS/L1899089556_0.png?sw=500&sh=500&sm=fit");
        url.add(img_url);

        viewPager2 = findViewById(R.id.pager);
        viewPager2.setAdapter(new ScreenSlidePagerAdapter(url));

        tabLayout=findViewById(R.id.tab_layout);


        //Indicator
        new TabLayoutMediator(tabLayout, viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                    }
                }
        ).attach();

        productName=findViewById(R.id.product_name);
        productBrand=findViewById(R.id.product_brand);
        productDescription=findViewById(R.id.product_description);
        productPrice=findViewById(R.id.product_price);



    }

    @Override
    protected void onStart() {
        super.onStart();


        productRegistration=productRef.addSnapshotListener(this);
    }

    @Override
    public void onBackPressed() {

        finish();
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
        startActivity(intent);
    }



    @Override
    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
        if (error != null) {
            Log.w("ProductDetailActivity", "restaurant:onEvent", error);
            return;
        }
        System.out.println(snapshot.getId());
        onProductLoaded(snapshot.toObject(Item.class));
    }

    private void onProductLoaded(Item item){
        productName.setText(item.getItemName());
        productBrand.setText(item.getBrand());
        productDescription.setText(item.getItemDesc());
        NumberFormat nm=NumberFormat.getInstance();
        productPrice.setText(nm.format(item.getPrice()));
    }


    private static class ScreenSlidePagerAdapter extends RecyclerView.Adapter<ScreenSlidePagerAdapter.CardViewHolder> {
        private Context context;
        private List<Integer> imageList;
        private StorageReference mstorageReference;

//        private List<String> imageList;
//        public ScreenSlidePagerAdapter(List<String> imageList){
//            this.imageList =imageList;
//        }

        public ScreenSlidePagerAdapter(List<Integer> imageList){this.imageList=imageList;}


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

            public void setImageView(int url){
                mstorageReference= FirebaseStorage.getInstance().getReferenceFromUrl("gs://bionic-run-191808.appspot.com/Item/c902964b-e2f9-42f9-b664-f4daaf77bcca.jpeg");
                GlideApp.with(context)
                        .load(mstorageReference)
                        .into(imageView);

            }

        }
    }


}