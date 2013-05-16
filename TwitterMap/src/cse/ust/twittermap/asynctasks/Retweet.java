package cse.ust.twittermap.asynctasks;

import twitter4j.Status;
import twitter4j.Twitter;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import cse.ust.twittermap.TweetData;
import cse.ust.twittermap.TwitterFactory;
import cse.ust.twittermap.fragments.FavoriteFragment;
import cse.ust.twittermap.fragments.HomeFragment;
import cse.ust.twittermap.fragments.SearchListFragment;

public class Retweet extends AsyncTask<Status, Void, Boolean> {
	
	private ProgressDialog progressDialog;
	private Context context = null;
	private Fragment fragment = null;
	private TweetData localData;
	private int index;
	
	public Retweet(Context context, Fragment fragment, TweetData data, int index){
		this.context = context;
		this.fragment = fragment;
		this.localData = data;
		this.index = index;
	}
	
	@Override
	protected void onPreExecute() {
		progressDialog = ProgressDialog.show(context, "Retweet", "Please Wait...", 
												true, false);
	}

	protected Boolean doInBackground(twitter4j.Status... params) {
		twitter4j.Status us = params[0];
		Twitter twitter = TwitterFactory.get4jInstance();

		try {
			twitter4j.Status retweetStatus = us.getRetweetedStatus();
			if (retweetStatus != null) {
				twitter.retweetStatus(retweetStatus.getId());
			} else {
				twitter.retweetStatus(us.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	@Override
	protected void onPostExecute(Boolean successful) {
		if (successful) {
			Toast.makeText(context, "Retweet suceeds", Toast.LENGTH_SHORT).show();
			localData.setRetweet(index, true);
			if (fragment instanceof HomeFragment)
				((HomeFragment)fragment).refreshWithCurrentData();//!!BUG using refreshwithnetwork
			else if (fragment instanceof SearchListFragment)
				((SearchListFragment) fragment).refreshWithCurrentData();
			else if (fragment instanceof FavoriteFragment)
				((FavoriteFragment) fragment).refreshWithCurrentData();//!!BUG using refreshwithnetwork
		}
		else {
			Toast.makeText(context, "Retweet fails", Toast.LENGTH_SHORT).show();	
		}
		progressDialog.dismiss();
	}
}

