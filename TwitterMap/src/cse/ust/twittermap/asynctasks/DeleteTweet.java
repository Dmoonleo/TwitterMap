package cse.ust.twittermap.asynctasks;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import cse.ust.twittermap.R;
import cse.ust.twittermap.TweetData;
import cse.ust.twittermap.TwitterFactory;
import cse.ust.twittermap.fragments.FavoriteFragment;
import cse.ust.twittermap.fragments.HomeFragment;
import cse.ust.twittermap.fragments.SearchListFragment;

public class DeleteTweet extends AsyncTask<Long, Void, Boolean> {
	
	private Context context = null;
	private ProgressDialog postDialog = null;
	private Fragment fragment = null;
	private TweetData localData = null;
	private int index;
	private boolean unretweet;
	
	public DeleteTweet(Context context, Fragment fragment, TweetData localData, int index, boolean unretweet) {
		this.context = context;
		this.fragment = fragment;
		this.localData = localData;
		this.index = index;
		this.unretweet = unretweet;
	}
		
	@Override
	protected void onPreExecute() {
		postDialog = ProgressDialog.show(context, "Status", "Deleting...", true, false);
	}

	@SuppressLint("NewApi") 
	@Override
	protected void onPostExecute(Boolean result) {
		postDialog.dismiss();
		
		if (result == false)
			Toast.makeText(fragment.getActivity(), R.string.rate_limit_reached, Toast.LENGTH_SHORT).show();
		else {
			if (unretweet) {
				localData.setRetweet(index, false);
			}
			else {
				localData.remove(index);
			}
			if (fragment instanceof HomeFragment)
				((HomeFragment) fragment).refreshWithCurrentData();
			else if (fragment instanceof SearchListFragment)
				((SearchListFragment) fragment).refreshWithCurrentData();
			else if (fragment instanceof FavoriteFragment)
				((FavoriteFragment) fragment).refreshWithCurrentData();
		}
	}

	@Override
	protected Boolean doInBackground(Long... statusIDs) {
		try {
			TwitterFactory.get4jInstance().destroyStatus(statusIDs[0]);
			Thread.sleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
