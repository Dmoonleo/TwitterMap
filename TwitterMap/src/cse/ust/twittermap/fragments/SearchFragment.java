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
import cse.ust.twittermap.asynctasks.SearchPeople;

@SuppressLint({ "NewApi", "ValidFragment" })
public class SearchFragment extends Fragment {
	
	private ProgressBar progressbar = null;
	private ListView listview = null;
	private List<User> followings = null;
	private ArrayList<Boolean> isFollowing = null;
	private FollowingAdapter adapter = null;
	private String query = null;
	private SearchPeople task;
	
	public SearchFragment() { 
		super();
	}
	
	public SearchFragment(String query) {
		super();
		this.query = query;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_listview, container, false);
    }
	
	public void refreshCompletelyUsingNetwork() {
		followings = new ArrayList<User>();
	    isFollowing = new ArrayList<Boolean>();
	    adapter = new FollowingAdapter(getActivity(), followings, isFollowing);
	    listview.setAdapter(adapter);
	    
		task = new SearchPeople(progressbar, listview, adapter, followings, isFollowing);
		task.execute(query);
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
			assert(cancelled == true):"search fragment cancel async task fail!";
		}	
		super.onStop();
	}
}
