package com.example.alarmclockfragment;

import android.animation.ObjectAnimator;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Administrator on 2016/10/28.
 */

public class AlarmClockFragment extends Fragment {

    private ViewPager mViewPager, mBanner;
    private int mPage;
    public static final String ARG_PAGE = "ARG_PAGE";
    private boolean fabOpened;
    private View maskView;
    private FloatingActionButton fabAddClock, fabNormalClock, fabCustomClock;

    public static AlarmClockFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        AlarmClockFragment fragment = new AlarmClockFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, null);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "toolbar", Toast.LENGTH_SHORT).show();
            }
        });


        fabAddClock = (FloatingActionButton) view.findViewById(R.id.fab_add_clock);
        fabAddClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fabOpened) {
                    openMenu(view);
                } else {
                    closeMenu(view);
                }
            }
        });
        fabNormalClock = (FloatingActionButton) view.findViewById(R.id.fab_normal_clock);
        fabCustomClock = (FloatingActionButton) view.findViewById(R.id.fab_custom_clock);

        maskView = view.findViewById(R.id.mask);
        maskView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeMenu(fabAddClock);
            }
        });

//        ImageView ivImage = (ImageView) view.findViewById(R.id.ivImage);
//        ivImage.setImageResource(R.mipmap.book3);

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mBanner = (ViewPager) view.findViewById(R.id.banner);
        setupViewPager(mViewPager);
        setupBanner(mBanner);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);
        return view;
    }

    private void closeMenu(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", -135, 20, 0);
        animator.setDuration(500);
        animator.start();
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.8f, 0);
        alphaAnimation.setDuration(500);
        maskView.startAnimation(alphaAnimation);
        maskView.setVisibility(View.GONE);

        fabNormalClock.hide();
        fabCustomClock.hide();

        fabOpened = false;
    }

    private void openMenu(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 0, -155, -135);
        animator.setDuration(500);
        animator.start();
        maskView.setVisibility(View.VISIBLE);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 0.8f);
        alphaAnimation.setDuration(500);
        alphaAnimation.setFillAfter(true);
        maskView.startAnimation(alphaAnimation);

        fabNormalClock.show();
        fabCustomClock.show();

        fabOpened = true;
    }

    private void setupBanner(ViewPager mBanner) {
        MyPagerAdapter adapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(PageFragment.newInstance(1), "1");
        adapter.addFragment(PageFragment.newInstance(2), "2");
        mBanner.setAdapter(adapter);
    }

    private void setupViewPager(ViewPager mViewPager) {
        MyPagerAdapter adapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(DetailFragment.newInstance(getAsset("book_content.txt")), "内容简介");
        adapter.addFragment(DetailFragment.newInstance(getAsset("book_author.txt")), "作者简介");
        mViewPager.setAdapter(adapter);
    }

    private String getAsset(String fileName) {
        AssetManager am = getResources().getAssets();
        InputStream is = null;
        try {
            is = am.open(fileName, AssetManager.ACCESS_BUFFER);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Scanner(is).useDelimiter("\\Z").next();
    }


    static class MyPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

}
