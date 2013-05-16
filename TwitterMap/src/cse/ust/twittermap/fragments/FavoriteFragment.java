package cse.ust.twittermap.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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
import cse.ust.twittermap.adapters.StatusAdapter;
import cse.ust.twittermap.asynctasks.GetFavorite;

@SuppressLint({ "NewApi", "ValidFragment" })
public class FavoriteFragment extends Fragment {
	private ProgressBar progressBar = null;
	private ListView listView = null;
	private Button moreButton = null;
	
	/*by yzhangad: the declaration of these local datas are now static, so that
	the destruction of fragments won't destroy them and when rate limit is reached,
	older data can be used to display the home timeline.
	*/
	static private List<twitter4j.Status> statusList = new ArrayList<twitter4j.Status>();
	static private List<Boolean> ifFavorite = new ArrayList<Boolean>();
	static private List<Boolean> ifRetweet = new ArrayList<Boolean>();
	static private Vector<Vector<String>> imageUrlsList = new Vector<Vector<String>>();
	
	private StatusAdapter adapter = null;
	private String userScreenName = null;
	private GetFavorite task;

	public FavoriteFragment() { 
		super();
	}
	
	public FavoriteFragment(String userScreenName) {
		super();
		this.userScreenName = userScreenName;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_listview, container, false);
    }

	public void refreshCompletelyUsingNetwork() {
		//statusList = new ArrayList<twitter4j.Status>();
		//ifFavorite = new ArrayList<Boolean>();
		//ifRetweet = new ArrayList<Boolean>();
		//imageUrlsList = new Vector<Vector<String>>();
		
		adapter = new StatusAdapter(getActivity(), statusList, ifFavorite, ifRetweet, imageUrlsList);
		listView.setAdapter(adapter);
		
		task = new GetFavorite(statusList, ifFavorite, ifRetweet, imageUrlsList, this);
		task.execute(userScreenName);
	}
	
	public void refreshWithCurrentData() {
		listView.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
	    adapter.notifyDataSetChanged();
		progressBar.setVisibility(View.GONE);
	    listView.setVisibility(View.VISIBLE);
	    listView.invalidateViews();
	}
	
	public void onStart() {
		super.onStart();
		if (progressBar == null) {
			progressBar = (ProgressBar) getView().findViewById(R.id.loading);
			listView = (ListView) getView().findViewById(R.id.list);
			moreButton = (Button) getView().findViewById(R.id.more_button);
			moreButton.setVisibility(View.GONE);
			refreshCompletelyUsingNetwork();
		}
	}
	
	@Override
	public void onStop(){
		if(task != null) {
			boolean cancelled = task.cancel(true);
			assert(cancelled == true):"favorite fragment cancel async task fail!";
		}
		
		super.onStop();
	}
}
