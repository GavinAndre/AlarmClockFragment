package com.example.alarmclockfragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/28.
 */

public class AlarmClockFragment extends Fragment {

    private static final String TAG = AlarmClockFragment.class.getSimpleName();
    private ViewPager mViewPager, mBanner;
    private int mPage;
    public static final String ARG_PAGE = "ARG_PAGE";
    private boolean fabOpened, isAnimationEnd = true;
    private View maskView;
    private FloatingActionButton fabAddClock, fabNormalClock, fabCustomClock;
    private CircleIndicator mCircleIndicator;
    private Handler handler = new Handler();

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
//        mPage = getArguments().getInt(ARG_PAGE,0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm_clock, container, false);

        initToolBar(view);

        fabAddClock = (FloatingActionButton) view.findViewById(R.id.fab_add_clock);
        fabAddClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAnimationEnd) {
                    if (!fabOpened) {
                        openMenu(view);
                    } else {
                        closeMenu(view);
                    }
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

        mCircleIndicator = (CircleIndicator) view.findViewById(R.id.pager_indicator);
        mCircleIndicator.setViewPager(mBanner);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);
        return view;
    }

    private void initToolBar(View view) {
        Toolbar mToolBar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolBar.setNavigationIcon(R.drawable.menu);
        mToolBar.inflateMenu(R.menu.menu1);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Navigation1", Toast.LENGTH_SHORT).show();
            }
        });
        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getContext(), "menu1", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void openMenu(View view) {
        isAnimationEnd = false;
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "rotation", 0, -155, -135);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.65f);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.65f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(500);
        set.playTogether(animator1, animator2, animator3);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimationEnd = true;
            }
        });
        maskView.setVisibility(View.VISIBLE);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 0.8f);
        alphaAnimation.setDuration(500);
        alphaAnimation.setFillAfter(true);
        maskView.startAnimation(alphaAnimation);

        float normalClockInitX = fabNormalClock.getTranslationX();
        float normalClockInitY = fabNormalClock.getTranslationY();
        float customClockInitX = fabCustomClock.getTranslationX();
        float customClockInitY = fabCustomClock.getTranslationY();

        PropertyValuesHolder pvh1 = PropertyValuesHolder.ofFloat("translationX", normalClockInitX, normalClockInitX - 150f);
        PropertyValuesHolder pvh2 = PropertyValuesHolder.ofFloat("translationY", normalClockInitY, normalClockInitY - 150f);
        PropertyValuesHolder pvh3 = PropertyValuesHolder.ofFloat("translationX", customClockInitX, customClockInitX + 150f);
        PropertyValuesHolder pvh4 = PropertyValuesHolder.ofFloat("translationY", customClockInitY, customClockInitY - 150f);
        ObjectAnimator.ofPropertyValuesHolder(fabNormalClock, pvh1, pvh2).setDuration(400).start();
        ObjectAnimator.ofPropertyValuesHolder(fabCustomClock, pvh3, pvh4).setDuration(400).start();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fabNormalClock.show();
                fabCustomClock.show();
            }
        }, 100);

        fabOpened = true;
    }

    private void closeMenu(View view) {
        isAnimationEnd = false;
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "rotation", -135, 20, 0);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "scaleX", 0.65f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(view, "scaleY", 0.65f, 1f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(500);
        set.playTogether(animator1, animator2, animator3);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimationEnd = true;
            }
        });
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.8f, 0);
        alphaAnimation.setDuration(500);
        maskView.startAnimation(alphaAnimation);
        maskView.setVisibility(View.GONE);

        float normalClockInitX = fabNormalClock.getTranslationX();
        float normalClockInitY = fabNormalClock.getTranslationY();
        float customClockInitX = fabCustomClock.getTranslationX();
        float customClockInitY = fabCustomClock.getTranslationY();

        PropertyValuesHolder pvh1 = PropertyValuesHolder.ofFloat("translationX", normalClockInitX, normalClockInitX + 150f);
        PropertyValuesHolder pvh2 = PropertyValuesHolder.ofFloat("translationY", normalClockInitY, normalClockInitY + 150f);
        PropertyValuesHolder pvh3 = PropertyValuesHolder.ofFloat("translationX", customClockInitX, customClockInitX - 150f);
        PropertyValuesHolder pvh4 = PropertyValuesHolder.ofFloat("translationY", customClockInitY, customClockInitY + 150f);
        ObjectAnimator.ofPropertyValuesHolder(fabNormalClock, pvh1, pvh2).setDuration(400).start();
        ObjectAnimator.ofPropertyValuesHolder(fabCustomClock, pvh3, pvh4).setDuration(400).start();


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fabNormalClock.hide();
                fabCustomClock.hide();
            }
        }, 100);

        fabOpened = false;
    }

    private void setupBanner(ViewPager mBanner) {
        //fragment嵌套fragment需要用getChildFragmentManager()来获取FragmentManager
        MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
        adapter.addFragment(CharmBannerFragment.newInstance(1), "1");
        adapter.addFragment(CharmBannerFragment.newInstance(2), "2");
        adapter.addFragment(CharmBannerFragment.newInstance(3), "3");
        mBanner.setAdapter(adapter);
    }

    private void setupViewPager(ViewPager mViewPager) {
        MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new AlarmClockListFragment(), "普通闹铃");
        adapter.addFragment(new AlarmClockListFragment(), "定制闹铃");
        mViewPager.setAdapter(adapter);
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
