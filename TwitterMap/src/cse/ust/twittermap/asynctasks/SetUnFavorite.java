package cse.ust.twittermap.asynctasks;

import twitter4j.Status;
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


public class SetUnFavorite extends AsyncTask<Status, Void, Boolean> {
	
	private ProgressDialog postDialog = null;
	private Context context = null;
	private Fragment fragment = null;
	private TweetData localdata;
	private int index;
		
	public SetUnFavorite(Context context, Fragment fragment, TweetData localData, int index){
		this.context = context;
		this.fragment = fragment;
		this.localdata = localData;
		this.index = index;
	}
		
	@Override
	protected void onPreExecute() {
		postDialog = ProgressDialog.show(context, "Status", "Unfavoring...", true, false);
	}

	@SuppressLint("NewApi") @Override
	protected void onPostExecute(Boolean result) {
		postDialog.dismiss();
		if (result == true)
			localdata.setFavorite(index, false);
		else
			Toast.makeText(fragment.getActivity(), R.string.rate_limit_reached, Toast.LENGTH_SHORT).show();
		/*if (fragment instanceof HomeFragment) {
			((HomeFragment) fragment).refreshCompletelyUsingNetwork();
		}
		else if (fragment instanceof FavoriteFragment) {
			((FavoriteFragment) fragment).refreshCompletelyUsingNetwork();
		}*/
		if (fragment instanceof HomeFragment) {
			((HomeFragment) fragment).refreshWithCurrentData();
		}
		else if (fragment instanceof SearchListFragment) {
			((SearchListFragment) fragment).refreshWithCurrentData();
		}
		else if (fragment instanceof FavoriteFragment) {
			((FavoriteFragment) fragment).refreshWithCurrentData();
		}
	}

	@Override
	protected Boolean doInBackground(twitter4j.Status... statuses) {
		try {
			TwitterFactory.get4jInstance().destroyFavorite(statuses[0].getId());
			Thread.sleep(3000);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
