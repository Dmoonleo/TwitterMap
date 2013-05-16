package cse.ust.twittermap.listeners;

import twitter4j.Status;
import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import cse.ust.twittermap.fragments.ReplyTweetDialogFragment;

@SuppressLint("NewApi")
public class ReplyButtonListener implements OnClickListener {

	private Status status = null;
	private FragmentManager fragmentManager = null;
	
	public ReplyButtonListener(Status status, FragmentManager fragmentManager) {
		this.status = status;
		this.fragmentManager = fragmentManager;
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		new ReplyTweetDialogFragment(fragmentManager.findFragmentByTag("CURRENTFRAGMENT"), status)
								.show(fragmentManager, "dialog");
	}
}
