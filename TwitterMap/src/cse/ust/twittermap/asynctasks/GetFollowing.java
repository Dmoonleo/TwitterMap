package cse.ust.twittermap.asynctasks;

import java.util.List;

import twitter4j.User;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import cse.ust.twittermap.BitmapCache;
import cse.ust.twittermap.TwitterFactory;
import cse.ust.twittermap.adapters.FollowingAdapter;

public class GetFollowing extends AsyncTask<String, Void, Void> {
	
	private ProgressBar progressbar = null;
	private ListView listview = null;
	private List<User> userList = null;
	private FollowingAdapter adapter = null;
	
	public GetFollowing(ProgressBar progressbar, ListView listview, FollowingAdapter adapter, List<User> userList) {
		this.progressbar = progressbar;
		this.listview = listview;
		this.userList = userList;
		this.adapter = adapter;
	}
	
	@Override
	protected Void doInBackground(String... userScreenNames) {
		try {			
			List<User> resList = TwitterFactory.get4jInstance().getFriendsList(userScreenNames[0], -1);
			for(int i = 0; i < resList.size(); ++i) {
				User curUser = resList.get(i);
				BitmapCache.getUserImageFromMemCache(curUser);
				userList.add(curUser);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(Void param) {	
		adapter.notifyDataSetChanged();
		progressbar.setVisibility(View.GONE);
	    listview.setVisibility(View.VISIBLE);
	}
}
