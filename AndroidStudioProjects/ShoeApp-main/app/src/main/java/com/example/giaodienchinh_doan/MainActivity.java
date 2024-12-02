package com.example.giaodienchinh_doan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.giaodienchinh_doan.AdapterView.ViewPagerAdapter;
import com.example.giaodienchinh_doan.AnotherNav.CartActivity;
import com.example.giaodienchinh_doan.AnotherNav.SearchViewActivity;
import com.example.giaodienchinh_doan.R;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {
    ViewPager mViewPager;
    Toolbar toolbar;
    MeowBottomNavigation bottomNavigation;
    //    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = findViewById(R.id.viewpager);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home_foreground));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_shop_foreground));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_person_foreground));
        bottomNavigation.show(1,true);

        toolbar=findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextAppearance(this, R.style.TitleTextAppearance_Bold);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(viewPagerAdapter);
        bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                mViewPager.setCurrentItem(model.getId() - 1);
                return null;
            }
        });
        bottomNavigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId()){
                    case 1:
                        mViewPager.setCurrentItem(0);
                        break;
                    case 2:
                        mViewPager.setCurrentItem(1);
                        break;
                    case 3:
                        mViewPager.setCurrentItem(2);
                        break;
                }
                return null;
            }
        });
        mViewPager.setCurrentItem(0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.menu_my_cart){
            startActivity(new Intent(MainActivity.this, CartActivity.class));
        }else if(id==R.id.menu_search){
            startActivity(new Intent(MainActivity.this, SearchViewActivity.class));
        }
        return true;

    }
}
