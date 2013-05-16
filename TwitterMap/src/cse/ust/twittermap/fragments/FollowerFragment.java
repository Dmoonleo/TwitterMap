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
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import cse.ust.twittermap.R;
import cse.ust.twittermap.adapters.FollowerAdapter;
import cse.ust.twittermap.asynctasks.GetFollower;

@SuppressLint({ "NewApi", "ValidFragment" })
public class FollowerFragment extends Fragment {
	
	private ProgressBar progressbar = null;
	private ListView listview = null;
	private Button moreButton = null;
	private List<User> followers = null;
	private FollowerAdapter adapter = null;
	private String userScreenName = null;
	private GetFollower task;
	
	public FollowerFragment() { 
		super();
	}
	
	public FollowerFragment(String userScreenName) {
		super();
		this.userScreenName = userScreenName;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_listview, container, false);
    }
	
	public void onStart() {
		super.onStart();
		if (progressbar == null) {
			progressbar = (ProgressBar) getView().findViewById(R.id.loading);
			listview = (ListView) getView().findViewById(R.id.list);
			moreButton = (Button) getView().findViewById(R.id.more_button);
			moreButton.setVisibility(View.GONE);
			refreshCompletelyUsingNetwork();
		}
	}
	
	public void refreshCompletelyUsingNetwork() {
		followers = new ArrayList<User>();
		adapter = new FollowerAdapter(getActivity(), followers, userScreenName);						
		listview.setAdapter(adapter);
		
		task = new GetFollower(progressbar, listview,  adapter, followers);
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
	
	@Override
	public void onStop(){
		if(task != null) {
			boolean cancelled = task.cancel(true);
			assert(cancelled == true):"get follower fragement cancel async task fail!";
		}
		super.onStop();
	}
}
