package scu.edu.sharedstyle.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import com.next.easynavigation.view.EasyNavigationBar;

import java.util.ArrayList;
import java.util.List;


import scu.edu.sharedstyle.Fragment.MainPageFragment;
import scu.edu.sharedstyle.Fragment.ProfileFragment;
import scu.edu.sharedstyle.activities.Post_item;
import scu.edu.sharedstyle.R;

public class MainActivity extends AppCompatActivity {


    private EasyNavigationBar navigationBar;
    private String[] tabText = {"MainPage","", "Profile"};
    private int[] normalIcon = {R.drawable.ic_baseline_home_not_select, R.drawable.ic_baseline_add_24, R.drawable.ic_baseline_person_not_selected};
    private int[] selectIcon = {R.drawable.ic_baseline_home_24, R.drawable.ic_baseline_add_24, R.drawable.ic_baseline_person_24};

    private List<Fragment> fragments = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationBar = findViewById(R.id.navigationBar);

        fragments.add(new MainPageFragment());
        fragments.add(new ProfileFragment());

        navigationBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .fragmentList(fragments)
                .mode(EasyNavigationBar.MODE_ADD)
                .onTabClickListener(new EasyNavigationBar.OnTabClickListener() {
                    @Override
                    public boolean onTabClickEvent(View view, int position) {
                        if (position == 1) {
                            Intent intent = new Intent(MainActivity.this, Post_item.class);

                            startActivity(intent);
                        }
                        return false;
                    }

                })
                .fragmentManager(getSupportFragmentManager())
                .build();


        init();


    }

    private void init() {



    }

}