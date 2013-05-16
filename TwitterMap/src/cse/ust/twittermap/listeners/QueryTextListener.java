package cse.ust.twittermap.listeners;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView.OnQueryTextListener;
import cse.ust.twittermap.R;
import cse.ust.twittermap.activities.BaseActivity;
import cse.ust.twittermap.activities.SearchActivity;

@SuppressLint("NewApi")
public class QueryTextListener implements OnQueryTextListener {

	private BaseActivity activity = null;
	private MenuItem searchItem = null;
	
	public void setActivity(BaseActivity activity) {
		this.activity = activity;
	}
	
	public void setSearchItem(Menu menu) {
		searchItem = menu.findItem(R.id.menu_search);
	}
	
	@Override
	public boolean onQueryTextChange(String newText) {
		
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		Intent intent = new Intent(activity.getApplicationContext(), SearchActivity.class);
		intent.putExtra(SearchManager.QUERY, query);
		searchItem.collapseActionView();
		activity.startActivity(intent);
		return true;
	}
}
