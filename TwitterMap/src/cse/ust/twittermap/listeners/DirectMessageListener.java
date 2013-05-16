package cse.ust.twittermap.listeners;

import twitter4j.User;
import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import cse.ust.twittermap.fragments.DirectMessageFragment;

public class DirectMessageListener implements OnClickListener {

	FragmentManager fragmentManager;
	User user;
	
	@SuppressLint("NewApi")
	public void setParameters(FragmentManager fragmentManager, User user) {
		this.fragmentManager = fragmentManager;
		this.user = user;
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		new DirectMessageFragment(user).show(fragmentManager, "dialog");
	}
}