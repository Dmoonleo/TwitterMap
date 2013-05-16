package cse.ust.twittermap.listeners;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.View.OnClickListener;
import cse.ust.twittermap.fragments.HomeFragment;

@SuppressLint("NewApi")
public class MoreButtonListener implements OnClickListener {

	private HomeFragment homeFragment;
	
	public MoreButtonListener(HomeFragment homeFragment) {
		this.homeFragment = homeFragment;
	}
	
	@Override
	public void onClick(View v) {
		
		homeFragment.refreshWithMoreTweets();
	}
}