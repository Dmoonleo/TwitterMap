package cse.ust.twittermap.listeners;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import cse.ust.twittermap.R;
import cse.ust.twittermap.TwitterFactory;
import cse.ust.twittermap.fragments.FavoriteFragment;
import cse.ust.twittermap.fragments.FollowerFragment;
import cse.ust.twittermap.fragments.HomeFragment;

@SuppressLint("NewApi")
public class TabListener implements ActionBar.TabListener {

	private static TabListener tabManager = new TabListener();
	private FragmentManager fragmentManager = null;
	
	public static TabListener getInstance() {
		return tabManager;
	}
	
	public void setFragmentManager(Activity activity) {
		fragmentManager = activity.getFragmentManager();
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		Fragment newFragment = null;
		if (tab.getTag() == "HOMETAB") {
			newFragment = new HomeFragment(TwitterFactory.getMyScreenName(), false);
		}
		else if (tab.getTag() == "FOLLWERTAB") {
			newFragment = new FollowerFragment(TwitterFactory.getMyScreenName());
		}
		else {
			newFragment = new FavoriteFragment(TwitterFactory.getMyScreenName());
		}
			
		if (newFragment != null) {
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.replace(R.id.content_fragment, newFragment, "CURRENTFRAGMENT");
			transaction.commit();
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		onTabSelected(tab, ft);
	}
}
