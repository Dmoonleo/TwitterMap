package cse.ust.twittermap.activities;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.os.Bundle;
import android.view.Menu;
import cse.ust.twittermap.R;
import cse.ust.twittermap.fragments.SearchFragment;

@SuppressLint({ "ShowToast", "NewApi" })
public class SearchActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    
	    SearchFragment newFragment = new SearchFragment(getIntent().getStringExtra(SearchManager.QUERY));
		
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.content_fragment, newFragment);
		transaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		return true;
	}
}
