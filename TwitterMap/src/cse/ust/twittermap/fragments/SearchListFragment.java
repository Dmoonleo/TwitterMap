
package cse.ust.twittermap.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import twitter4j.Query;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import cse.ust.twittermap.R;
import cse.ust.twittermap.adapters.StatusAdapter;
import cse.ust.twittermap.asynctasks.SearchList;

@SuppressLint({ "NewApi", "ValidFragment" })
public class SearchListFragment extends Fragment {
	
	private ProgressBar progressBar = null;
	private ListView listView = null;
	private List<twitter4j.Status> statusList = null;
	private List<Boolean> ifFavorite = null;
	private List<Boolean> ifRetweet = null;
	private Vector<Vector<String>> imageUrlsList = null;
	private StatusAdapter adapter = null;
	private Query query = null;
	private SearchList task;
	
	public SearchListFragment() { 
		super();
	}
	
	public SearchListFragment(Query query) {
		super();
		this.query = query;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_listview, container, false);
    }
	
	public void refreshCompletelyUsingNetwork() {
		statusList = new ArrayList<twitter4j.Status>();
		ifFavorite = new ArrayList<Boolean>();
		ifRetweet = new ArrayList<Boolean>();
		imageUrlsList = new Vector<Vector<String>>();
		for (int i = 0; i < statusList.size(); i++) 
			imageUrlsList.add(new Vector<String>());
		adapter = new StatusAdapter(getActivity(), statusList, ifFavorite, ifRetweet, imageUrlsList);
		listView.setAdapter(adapter);
	    
		task = new SearchList(progressBar, listView, adapter, statusList, ifFavorite, ifRetweet, imageUrlsList);
		task.execute(query);
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
			refreshCompletelyUsingNetwork();
		}
	}
	
	public void onStop(){
		if(task != null) {
			boolean cancelled = task.cancel(true);
			assert(cancelled == true):"search list fragement cancel async task fail!";
		}
		super.onStop();
	}
}
