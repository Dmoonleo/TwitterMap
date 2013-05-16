package cse.ust.twittermap.activities;

import twitter4j.GeoLocation;
import twitter4j.Query;
import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.os.Bundle;
import android.view.Menu;
import cse.ust.twittermap.R;
import cse.ust.twittermap.fragments.SearchListFragment;

@SuppressLint({ "ShowToast", "NewApi" })
public class LocationSearchActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		double[] params = getIntent().getDoubleArrayExtra(SearchManager.QUERY);
		Query query = new Query().geoCode(new GeoLocation(params[0], params[1]), params[2], Query.KILOMETERS);
		
		SearchListFragment newFragment = new SearchListFragment(query);
		
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
