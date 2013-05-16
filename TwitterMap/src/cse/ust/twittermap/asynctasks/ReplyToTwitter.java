package cse.ust.twittermap.asynctasks;

import twitter4j.StatusUpdate;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import cse.ust.twittermap.R;
import cse.ust.twittermap.TwitterFactory;
import cse.ust.twittermap.fragments.HomeFragment;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ReplyToTwitter extends AsyncTask<String, Void, String> {
		
	private ProgressDialog postDialog = null;
	private Context context = null;
	private Fragment fragment = null;
	private twitter4j.Status status = null;
		
	public ReplyToTwitter(Context context, Fragment fragment, twitter4j.Status status){
		this.context = context;
		this.fragment = fragment;
		this.status = status;
	}

	@Override
	protected void onPreExecute() {
		postDialog = ProgressDialog.show(context, "Status", "Replying...", true, false); 
	}
	
	@Override
	protected String doInBackground(String... statuses) {
		try {			
			TwitterFactory.get4jInstance().updateStatus(new StatusUpdate(" " + statuses[0])
															.inReplyToStatusId(status.getId()));
			Thread.sleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
		} 

		return context.getString(R.string.success_reply);
	}
	
	@Override
	protected void onPostExecute(String result) {
		postDialog.dismiss();
		if (fragment instanceof HomeFragment) {
			((HomeFragment) fragment).refreshCompletelyUsingNetwork();
		}
	}
	
}
