package scu.edu.sharedstyle.activities;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import scu.edu.sharedstyle.R;

public class  FirstStartActivity extends Activity implements ViewPager.OnPageChangeListener{

    private ViewPager mviewPager;
    private Button button;
    private ArrayList<View>views = new ArrayList<>();
    private ImageView[]imageViews;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_start);

        initview();
        initpoint();
    }
    private void initview(){
        mviewPager = (ViewPager) findViewById(R.id.first_start_viewpager);
        View view4 = getLayoutInflater().inflate(R.layout.first_start_layout4,null);
        button = view4.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoMainActivity();
            }
        });
        views.add(getLayoutInflater().inflate(R.layout.first_start_layout1,null));
        views.add(getLayoutInflater().inflate(R.layout.first_start_layout2,null));
        views.add(getLayoutInflater().inflate(R.layout.first_start_layout3,null));
        views.add(view4);
        mviewPager.addOnPageChangeListener(this);
        mviewPager.setAdapter(new ViewPagerAdapter());

    }
    private void initpoint(){
        LinearLayout point_layout = (LinearLayout) findViewById(R.id.point_layout);
        imageViews = new ImageView[views.size()];
        for (int i = 0;i<imageViews.length;i++){
            imageViews[i] = (ImageView) point_layout.getChildAt(i);
        }
        index = 0;
        imageViews[index].setImageResource(R.drawable.point_foucs);
    }

    private void setPoint(int position){
        if (index<0||index == position||index>imageViews.length-1){
            return;
        }
        imageViews[index].setImageResource(R.drawable.point);
        imageViews[position].setImageResource(R.drawable.point_foucs);
        index = position;
    }
    public void GoMainActivity(){
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), Front_page.class);
        startActivity(intent);
        this.finish();

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setPoint(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
   
    class ViewPagerAdapter extends PagerAdapter {


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = views.get(position);
            container.addView(v);
            return v;
        }

        @Override
        public int getCount() {
            return views.size();
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }


        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return super.getPageTitle(position);
        }
    }
}