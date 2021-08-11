package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

public class MainActivity extends AppCompatActivity {

    ViewPager graphViewPager;

    PagerAdapter pagerAdapter;

    SpringDotsIndicator dotsIndicator;

    ImageView settingButton;

    ImageView sliderDrawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        graphViewPager = findViewById(R.id.graphViewPager);

        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());

        graphViewPager.setAdapter(pagerAdapter);

        dotsIndicator = findViewById(R.id.dotIndicator);

        dotsIndicator.setViewPager(graphViewPager);

        settingButton = findViewById(R.id.settingButton);

        sliderDrawable = findViewById(R.id.sliderDrawable);


        settingButton.setOnClickListener(v -> {
            startActivity(new Intent(this, MenuActivity.class));
        });

        graphViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        sliderDrawable.setImageResource(R.drawable.double_slider);
                        break;

                    case 1:
                        sliderDrawable.setImageResource(R.drawable.single_slider);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private static class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Graph.SliderMode sliderMode;
            switch (position){
                case 0:
                    sliderMode = Graph.SliderMode.DOUBLE_SLIDE;
                break;
                case 1:
                    sliderMode = Graph.SliderMode.SINGLE_SLIDE;
                break;
                default:
                    throw new IllegalStateException("Unexpected value: " + position);
            }

            return new GraphFragment(sliderMode);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}