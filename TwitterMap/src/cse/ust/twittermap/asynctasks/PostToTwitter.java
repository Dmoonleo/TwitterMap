package cse.ust.twittermap.asynctasks;

import twitter4j.GeoLocation;
import twitter4j.StatusUpdate;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import cse.ust.twittermap.R;
import cse.ust.twittermap.TwitterFactory;
import cse.ust.twittermap.activities.BaseActivity;
import cse.ust.twittermap.fragments.HomeFragment;

@SuppressLint("NewApi")
public class PostToTwitter extends AsyncTask<String, Void, String> {
		
	private ProgressDialog postDialog = null;
	private Context context = null;
	private Fragment fragment = null;
	private boolean withLocation;
		
	public PostToTwitter(Context context, Fragment fragment, boolean withLocation){
		this.context = context;
		this.fragment = fragment;
		this.withLocation = withLocation;
	}
		
	@Override
	protected void onPreExecute() {
		postDialog = ProgressDialog.show(context, 
				"Status", "Updating ...", true,	false);
	}
		
	@Override
	protected String doInBackground(String... statuses) {
		try {
			
			StatusUpdate update = new StatusUpdate(statuses[0]);
			if (withLocation == true) {
				double longitude = BaseActivity.longitude;
				double latitude = BaseActivity.latitude;
				if (longitude < 0 || latitude < 0) return context.getString(R.string.gps_no_fix);
				update.setLocation(new GeoLocation(latitude, longitude));
			}
			TwitterFactory.get4jInstance().updateStatus(update);
			Thread.sleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
			return context.getString(R.string.fail_tweet);
		} 
		return context.getString(R.string.success_tweet);
	}	
	
	@Override
	protected void onPostExecute(String result) {		
		Toast.makeText(context, result, 
				Toast.LENGTH_SHORT).show();
		postDialog.dismiss();
		if (fragment instanceof HomeFragment) {
			((HomeFragment) fragment).refreshCompletelyUsingNetwork();
		}
	}
}