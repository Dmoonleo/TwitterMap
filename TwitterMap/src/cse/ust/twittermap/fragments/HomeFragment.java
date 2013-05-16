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
import cse.ust.twittermap.asynctasks.GetHome;
import cse.ust.twittermap.listeners.MoreButtonListener;

@SuppressLint({ "NewApi", "ValidFragment" })
public class HomeFragment extends Fragment {

	// UI variables
	private ProgressBar progressBar = null;
	private ListView listView = null;
	private Button moreButton = null;
	
	// adapter variables
	/*by yzhangad: the declaration of these local datas are now static, so that
	the destruction of fragments won't destroy them and when rate limit is reached,
	older data can be used to display the home timeline.
	*/
	static private List<twitter4j.Status> statusList = new ArrayList<twitter4j.Status>();
	static private List<Boolean> ifFavorite = new ArrayList<Boolean>();
	static private List<Boolean> ifRetweet = new ArrayList<Boolean>();
	static private Vector<Vector<String>> imageUrlsList = new Vector<Vector<String>>();
	private StatusAdapter adapter = null;
	
	// constructor variables
	public String userScreenName = null;
	private boolean inProfilePage = false;
	
	// task
	private GetHome task = null;
	
	// total page number
	private int totalPage = 1;
	private int statusCount = 0;
	
	public HomeFragment() { 
		super();
	}
	
	public HomeFragment(String userScreenName) {
		super();
		this.userScreenName = userScreenName;
		this.inProfilePage = false;
	}
	
	public HomeFragment(String userScreenName, boolean inProfilePage) {
		super();
		this.userScreenName = userScreenName;
		this.inProfilePage = inProfilePage;
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
		
		task = new GetHome(statusList, ifFavorite, ifRetweet, imageUrlsList, this, inProfilePage);
		task.execute(userScreenName, "homeline");
	}
	
	public void loading() {
		listView.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
	}
	
	public void refreshWithCurrentData() {
		listView.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
	    adapter.notifyDataSetChanged();
		progressBar.setVisibility(View.GONE);
	    listView.setVisibility(View.VISIBLE);
	    listView.invalidateViews();
	    if(totalPage != 1)
	    	listView.setSelection(statusCount-1);
	}
	
	public void refreshWithMoreTweets() {
		totalPage += 1;
		statusCount = listView.getAdapter().getCount();
		
		//statusList = new ArrayList<twitter4j.Status>();
		//ifFavorite = new ArrayList<Boolean>();
		//ifRetweet = new ArrayList<Boolean>();
		//imageUrlsList = new Vector<Vector<String>>();
		
		adapter = new StatusAdapter(getActivity(), statusList, ifFavorite, ifRetweet, imageUrlsList);						
		listView.setAdapter(adapter);
		
		task = new GetHome(statusList, ifFavorite, ifRetweet, imageUrlsList, this, inProfilePage, totalPage);
		task.execute(userScreenName, "homeline");
	}
	
	@Override
	public void onStart() {
		super.onStart();
		totalPage = 1;
		if (progressBar == null) {
			progressBar = (ProgressBar) getView().findViewById(R.id.loading);
			listView = (ListView) getView().findViewById(R.id.list);
			
			moreButton = (Button) getView().findViewById(R.id.more_button);
			MoreButtonListener listener = new MoreButtonListener(this);
			moreButton.setOnClickListener(listener);
			
			refreshCompletelyUsingNetwork();
		}
	}
	
	@Override
	public void onStop() {
		if(task != null) {
			boolean cancelled = task.cancel(true);
			assert(cancelled == true):"home fragement cancel async task fail!";
		}
		super.onStop();
	}
}
