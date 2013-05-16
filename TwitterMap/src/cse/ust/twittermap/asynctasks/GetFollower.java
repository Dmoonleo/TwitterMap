package cse.ust.twittermap.asynctasks;

import java.util.List;

import twitter4j.TwitterException;
import twitter4j.User;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import cse.ust.twittermap.BitmapCache;
import cse.ust.twittermap.TwitterFactory;
import cse.ust.twittermap.adapters.FollowerAdapter;

public class GetFollower extends AsyncTask<String, Void, Boolean> {
	
	private ProgressBar progressbar = null;
	private ListView listview = null;
	private FollowerAdapter adapter = null;
	private List<User> userList = null;
	
	public GetFollower(ProgressBar progressbar, ListView listview, FollowerAdapter adapter, List<User> userList) {
		this.progressbar = progressbar;
		this.listview = listview;
		this.adapter = adapter;
		this.userList = userList;
	}
	
	@Override
	protected Boolean doInBackground(String... userScreenNames) {
		try {			
			List<User> resList = TwitterFactory.get4jInstance().getFollowersList(userScreenNames[0], -1);
			for(int i = 0; i < resList.size(); ++i) {
				User curUser = resList.get(i);
				BitmapCache.getUserImageFromMemCache(curUser);
				userList.add(curUser);
			}
		} catch (TwitterException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	@Override
	protected void onPostExecute(Boolean param) {
		if (param == true)
			adapter.notifyDataSetChanged();
		progressbar.setVisibility(View.GONE);
	    listview.setVisibility(View.VISIBLE);
	}
}
