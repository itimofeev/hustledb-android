package ru.hustledb.hustledb;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContestsListFragment extends Fragment {
    @BindView(R.id.fclViewPager)
    ViewPager pager;
    @BindView(R.id.fclTabs)
    TabLayout tabLayout;
    PagerAdapter pagerAdapter;
    @Inject
    ContestsCache contestsCache;

    public static ContestsListFragment newInstance() {
        return new ContestsListFragment();
    }

    public ContestsListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contests_list, container, false);
        ButterKnife.bind(this, view);
        pagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager());
        pager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(pager);
//        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                Log.d("FCL_TAG", "onPageSelected, position = " + position);
//            }
//
//            @Override
//            public void onPageScrolled(int position, float positionOffset,
//                                       int positionOffsetPixels) {
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//            }
//        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private int lastItemsNum = 0;

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return contestsCache.getYearAt(contestsCache.getYearsNumber() - position - 1);
        }

        @Override
        public Fragment getItem(int position) {
            return ContestsSublistFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            if(contestsCache.getYearsNumber() != lastItemsNum) {
                lastItemsNum = contestsCache.getYearsNumber();
                notifyDataSetChanged();
            }
            return contestsCache.getYearsNumber();
        }

    }
}
