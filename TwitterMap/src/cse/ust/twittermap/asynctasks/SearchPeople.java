package cse.ust.twittermap.asynctasks;

import java.util.ArrayList;
import java.util.List;

import twitter4j.User;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import cse.ust.twittermap.BitmapCache;
import cse.ust.twittermap.TwitterFactory;
import cse.ust.twittermap.adapters.FollowingAdapter;

public class SearchPeople extends AsyncTask<String, Void, Void> {
	
	private ProgressBar progressbar = null;
	private ListView listview = null;
	private FollowingAdapter adapter = null;
	private List<User> userList = null;
	private ArrayList<Boolean> isFollowing = null;
	
	public SearchPeople(ProgressBar progressbar, ListView listview, FollowingAdapter adapter,
						List<User> userList, ArrayList<Boolean> isFollowing) {
		this.progressbar = progressbar;
		this.listview = listview;
		this.adapter = adapter;
		this.userList = userList;
		this.isFollowing = isFollowing;
	}
	
	@Override
	protected Void doInBackground(String... queries) {
		try {			
			List<User> resList = TwitterFactory.get4jInstance().searchUsers(queries[0], 1);
			User curUser = null;
			Boolean curVal = null;
			for(int i = 0; i < resList.size(); ++i) {
				curUser = resList.get(i);
				BitmapCache.getUserImageFromMemCache(curUser);
			    userList.add(curUser);
			    curVal = TwitterFactory.get4jInstance().showFriendship(TwitterFactory.get4jInstance().getId(), curUser.getId()).isSourceFollowingTarget();
			    isFollowing.add(curVal);
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
