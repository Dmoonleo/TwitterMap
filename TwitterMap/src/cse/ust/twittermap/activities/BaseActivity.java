package cse.ust.twittermap.activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import cse.ust.twittermap.R;
import cse.ust.twittermap.fragments.TwitterDialogFragment;
import cse.ust.twittermap.listeners.QueryTextListener;
import cse.ust.twittermap.listeners.SearchItemListener;

@SuppressLint("NewApi")
public class BaseActivity extends Activity {
	protected FragmentManager fragmentManager = getFragmentManager();
	//They are shared by all subclasses of activities so are declared as static protected.
	static public double longitude = -1.0;
	static public double latitude = -1.0;
	
	private Menu menu = null;
	private static final long MINTIME_UPDATELOCATION = 1000;
	private static final float MINDISTANCE_UPDATELOCATION = 10;
	//We use locationmanager and locationlistener to asyncly get lontitude and latitude.	
	private final LocationListener locationListener = new LocationListener() {
	    public void onLocationChanged(Location location) {
	        longitude = location.getLongitude();
	        latitude = location.getLatitude();
	    }

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		setActionBar();
		//set the request interval and minimum distance for location updates.
		try {
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 
					BaseActivity.MINTIME_UPDATELOCATION, BaseActivity.MINDISTANCE_UPDATELOCATION, locationListener);
		} catch (IllegalArgumentException e) {//GPS might not be enabled on android VMs. 
			e.printStackTrace();
		}
	}
	
	public Menu getMenu() {
		return menu;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		this.menu = menu;
		
		SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
		QueryTextListener queryTextListener = new QueryTextListener();
		queryTextListener.setActivity(this);
		queryTextListener.setSearchItem(menu);
		searchView.setOnQueryTextListener(queryTextListener);
		
	    SearchItemListener searchItemListener = new SearchItemListener();
	    searchItemListener.setPostItem(menu);
	    menu.findItem(R.id.menu_search).setOnActionExpandListener(searchItemListener);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_post) {			
			new TwitterDialogFragment(null, fragmentManager.findFragmentByTag("CURRENTFRAGMENT"))
								.show(fragmentManager, "dialog");
			return true;
		}
		else return super.onOptionsItemSelected(item);
	}
	
	private void setActionBar() {
		final ActionBar actionbar = getActionBar();
		actionbar.setDisplayShowTitleEnabled(false);
	}
}
