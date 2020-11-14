package scu.edu.sharedstyle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends FragmentActivity {

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private TextView productName;
    private TextView productBrand;
    private TextView productDescription;
    private Button buyNow;


    //For image load test
    private ArrayList<String> url = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);

        //For image load test
        url.add("https://s3.ax1x.com/2020/11/11/BvRJZF.jpg");
        url.add("https://images.macrumors.com/t/GWt5RjGgFZHXv4jPG-W9BHTKqSU=/1960x/https://images.macrumors.com/article-new/2020/11/new-m1-chip.jpg");
        url.add("https://www.longchamp.com/dw/image/v2/BCVX_PRD/on/demandware.static/-/Sites-LC-master-catalog/default/dw48cfd7a3/images/DIS/L1899089556_0.png?sw=500&sh=500&sm=fit");

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



    }
    @Override
    public void onBackPressed() {

        finish();
    }

    private static class ScreenSlidePagerAdapter extends RecyclerView.Adapter<ScreenSlidePagerAdapter.CardViewHolder> {
        private Context context;
        private List<String> imageList;
        public ScreenSlidePagerAdapter(List<String> imageList){
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
                Glide.with(context)
                        .load(url)
                        .into(imageView);
            }
        }
    }


}