package cse.ust.twittermap.listeners;

import twitter4j.GeoLocation;
import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import cse.ust.twittermap.dialog.LocationDialogFragment;

@SuppressLint("NewApi")
public class LocationButtonListener implements OnClickListener {

	private GeoLocation geo = null;
	private FragmentManager fragmentManager = null;
	
	public LocationButtonListener(GeoLocation geo, FragmentManager fragmentManager) {
		this.geo = geo;
		this.fragmentManager = fragmentManager;
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		if(geo != null ){
			new LocationDialogFragment(geo).show(fragmentManager, "dialog");
		}
	}
}
