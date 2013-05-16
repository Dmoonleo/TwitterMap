package cse.ust.twittermap.activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cse.ust.twittermap.R;
import cse.ust.twittermap.listeners.TabListener;

@SuppressLint({ "NewApi", "ShowToast" })
public class MainActivity extends BaseActivity {
	private TabListener tabListener = TabListener.getInstance();
	private Fragment fragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBar();
	}

	public FragmentManager getFragManager() {
		return fragmentManager;
	}
	
	public View getFragmentView() {
		return fragment.getView();
	}
	
	public void setFragment(Fragment fragment) {
		this.fragment = fragment;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		return true;
	}
	
	private void setActionBar() {
		final ActionBar actionbar = getActionBar();
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionbar.setDisplayShowTitleEnabled(false);
		
        tabListener.setFragmentManager(this);
		Tab homeTab = actionbar.newTab();
		Tab followerTab = actionbar.newTab();
		Tab favoriteTab = actionbar.newTab();
		homeTab.setTag("HOMETAB");
		followerTab.setTag("FOLLWERTAB");
		favoriteTab.setTag("FAVORITETAB");
		homeTab.setTabListener(tabListener);
		followerTab.setTabListener(tabListener);
		favoriteTab.setTabListener(tabListener);
		
		homeTab.setCustomView(R.layout.actionbar_tab);
		followerTab.setCustomView(R.layout.actionbar_tab);
		favoriteTab.setCustomView(R.layout.actionbar_tab);
		

        actionbar.addTab(homeTab, 0, true);
        actionbar.addTab(followerTab, 1, false);
        actionbar.addTab(favoriteTab, 2, false);

        ((ImageView) actionbar.getTabAt(0).getCustomView().findViewById(R.id.tab_icon)).setImageResource(R.drawable.actionbar_home);
        ((ImageView) actionbar.getTabAt(1).getCustomView().findViewById(R.id.tab_icon)).setImageResource(R.drawable.actionbar_follower);
        ((ImageView) actionbar.getTabAt(2).getCustomView().findViewById(R.id.tab_icon)).setImageResource(R.drawable.actionbar_favorite);
        ((TextView) actionbar.getTabAt(0).getCustomView().findViewById(R.id.tab_title)).setText("HOME");
        ((TextView) actionbar.getTabAt(1).getCustomView().findViewById(R.id.tab_title)).setText("FOLLOWER");
        ((TextView) actionbar.getTabAt(2).getCustomView().findViewById(R.id.tab_title)).setText("FAVORITE");
	}
}
