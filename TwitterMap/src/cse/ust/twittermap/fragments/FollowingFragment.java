package cse.ust.twittermap.fragments;

import java.util.ArrayList;
import java.util.List;

import twitter4j.User;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import cse.ust.twittermap.R;
import cse.ust.twittermap.adapters.FollowingAdapter;
import cse.ust.twittermap.asynctasks.GetFollowing;

@SuppressLint({ "NewApi", "ValidFragment" })
public class FollowingFragment extends Fragment {
	
	private ProgressBar progressbar = null;
	private ListView listview = null;
	private List<User> followings = null;
	private String userScreenName = null;
	private FollowingAdapter adapter = null;
	private GetFollowing task = null;

	public FollowingFragment() { 
		super();
	}
	
	public FollowingFragment(String userScreenName) {
		super();
		this.userScreenName = userScreenName;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_listview, container, false);
    }

	public void refreshCompletelyUsingNetwork() {
		listview.setVisibility(View.GONE);
		followings = new ArrayList<User>();
		task = new GetFollowing(progressbar, listview, adapter, followings);
		task.execute(userScreenName);
	}
	
	public void refreshWithCurrentData() {
		listview.setVisibility(View.GONE);
		progressbar.setVisibility(View.VISIBLE);
	    adapter.notifyDataSetChanged();
		progressbar.setVisibility(View.GONE);
	    listview.setVisibility(View.VISIBLE);
	    listview.invalidateViews();
	}
	
	public void onStart() {
		super.onStart();
		if (progressbar == null) {
			progressbar = (ProgressBar) getView().findViewById(R.id.loading);
			listview = (ListView) getView().findViewById(R.id.list);
			refreshCompletelyUsingNetwork();
		}
	}
	
	@Override
	public void onStop(){
		if(task != null) {
			boolean cancelled = task.cancel(true);
			assert(cancelled == true):"get following fragement cancel async task fail!";
		}
		super.onStop();
	}
	
	public void setUserScreenName(String userScreenName) {
		this.userScreenName = userScreenName;
	}
}
