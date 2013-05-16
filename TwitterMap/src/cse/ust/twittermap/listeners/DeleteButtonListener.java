package cse.ust.twittermap.listeners;

import twitter4j.Status;
import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import cse.ust.twittermap.TweetData;
import cse.ust.twittermap.fragments.DeleteEnsuranceDialogFragment;

@SuppressLint("NewApi")
public class DeleteButtonListener implements OnClickListener {

	private Status status = null;
	private FragmentManager fragmentManager = null;
	private TweetData localData;
	private int index;
	
	public DeleteButtonListener(Status status, FragmentManager fragmentManager, TweetData localData, int index) {
		this.status = status;
		this.fragmentManager = fragmentManager;
		this.localData = localData;
		this.index = index;
	}
	
	@Override
	public void onClick(View v) {
		new DeleteEnsuranceDialogFragment(status, fragmentManager.findFragmentByTag("CURRENTFRAGMENT"), localData, index)
									.show(fragmentManager, "dialog");
	}
}
