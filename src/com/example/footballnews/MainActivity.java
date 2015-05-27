package com.example.footballnews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.viewpagerindicator.TabPageIndicator;
import com.xuchiya.util.Constant;

public class MainActivity extends FragmentActivity {
	public static final String[] TITLES = new String[] { "Ó¢³¬", "Î÷¼×", "µÂ¼×",
			"Òâ¼×", "Å·¹Ú" };

	private TabPageIndicator mIndicator;
	private ViewPager mViewPager;
//	private FragmentPagerAdapter mAdapter;
	private CacheData cache_activity;

	// private Fragment fragment_england = null;
	// private Fragment fragment_spain = null;
	// private Fragment fragment_german = null;
	// private Fragment fragment_italy = null;
	// private Fragment fragment_Europ = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mIndicator = (TabPageIndicator) findViewById(R.id.id_indicator);
		mViewPager = (ViewPager) findViewById(R.id.id_pager);
		// mAdapter
		mViewPager.setOffscreenPageLimit(0);
		mViewPager.setAdapter(new myViewPagerAdapter(
				getSupportFragmentManager()));
		mIndicator.setViewPager(mViewPager, 0);
		cache_activity = ((MyApplication) getApplication()).getCacheData();
	}

	public CacheData getCache_activity() {

		return cache_activity;
	}

	class myViewPagerAdapter extends FragmentPagerAdapter {

		public myViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			System.out.println("arg0=" + arg0);
			Fragment fragment = null;
			switch (arg0) {

			case Constant.NEWS_TYPE_ENGLAND:
				// if (fragment_england == null)
				fragment = new MainFragment(arg0);
				// else
				// fragment = fragment_england;
				break;
			case Constant.NEWS_TYPE_SPNAN:
				// if (fragment_spain == null)
				fragment = new MainFragment(arg0);
				// else
				// fragment = fragment_spain;
				break;
			case Constant.NEWS_TYPE_GERMAN:
				// if (fragment_german == null)
				fragment = new MainFragment(arg0);
				// else
				// fragment = fragment_german;
				break;
			case Constant.NEWS_TYPE_ITALY:
				// if (fragment_italy == null)
				fragment = new MainFragment(arg0);
				// else
				// fragment = fragment_italy;
				break;
			case Constant.NEWS_TYPE_EUORP:
				// if (fragment_Europ == null)
				fragment = new MainFragment(arg0);
				// else
				// fragment = fragment_Europ;
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return TITLES.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return TITLES[position % TITLES.length];
		}

	}

}
