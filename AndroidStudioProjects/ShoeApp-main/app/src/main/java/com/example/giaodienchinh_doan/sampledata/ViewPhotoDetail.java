package com.example.giaodienchinh_doan.sampledata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.giaodienchinh_doan.R;
import com.example.giaodienchinh_doan.Fragment.ShopFragment;


public class ViewPhotoDetail extends AppCompatActivity {
    ImageView imageView;
    TextView tv_detail_title, tv_detail_description, shop_now_btn;
    ViewPager mViewPager;
    NewsModel newsModelModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo_detail);

        final Object obj = getIntent().getSerializableExtra("photo_inf");
        if (obj instanceof NewsModel) {
            newsModelModel = (NewsModel) obj;
        }
        imageView=findViewById(R.id.iv_detail);
        tv_detail_title=findViewById(R.id.tv_detail_title);
        tv_detail_description=findViewById(R.id.tv_detail_description);
        shop_now_btn = findViewById(R.id.shop_now_btn);

        Glide.with(getApplicationContext()).load(newsModelModel.getImgSrc()).into(imageView);
        tv_detail_title.setText(newsModelModel.getPhotoTitle());
        tv_detail_description.setText(newsModelModel.getDescription());

        shop_now_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                ShopFragment shopFragment = new ShopFragment();
                fragmentTransaction.replace(R.id.viewphotodetail, shopFragment);
                fragmentTransaction.commit();
            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar_viewPhotoDetail);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}