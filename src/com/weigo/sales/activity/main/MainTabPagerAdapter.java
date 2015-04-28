package com.weigo.sales.activity.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.weigo.sales.activity.base.BaseFragment;
import com.weigo.sales.activity.goods.GoodsFragment;

public class MainTabPagerAdapter extends FragmentPagerAdapter {	
	
	private static final int MAX_FRAGMENT_COUNT = 1;
	BaseFragment[] mFragments;
	public MainTabPagerAdapter(FragmentManager fm) {
		super(fm);
		mFragments = new BaseFragment[MAX_FRAGMENT_COUNT];
	}

	@Override
	public Fragment getItem(int position) {
		BaseFragment fragment = mFragments[position];
		if (fragment == null) {
			fragment = createFragment(position);
			mFragments[position] = fragment;
		}
		return fragment;
	}
	
	public void onResume(int position)
	{
		if (position < 0)
			return;
		BaseFragment fragment = mFragments[position];
		if (fragment != null) {
			fragment.onPageResume();
		}
	}
	
	public void onPause(int position)
	{
		if (position < 0)
			return;
		BaseFragment fragment = mFragments[position];
		if (fragment != null) {
			fragment.onPagePause();
		}
	}

	@Override
	public int getCount() {
		return MAX_FRAGMENT_COUNT;
	}

	private BaseFragment createFragment(int position) {
		
		switch (position) {
		case 0:			
			return new GoodsFragment();

		default:
			break;
		}

		return null;
	}

}
