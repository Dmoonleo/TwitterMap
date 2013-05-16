package cse.ust.twittermap.listeners;

import twitter4j.Status;
import android.app.Activity;
import android.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import cse.ust.twittermap.TweetData;
import cse.ust.twittermap.asynctasks.DeleteTweet;
import cse.ust.twittermap.asynctasks.Retweet;

public class RetweetButtonListener implements OnClickListener{

	private Activity activity = null;
	private Fragment fragment = null;
	private Status status = null;
	private TweetData tweetData=null;
	private boolean isRetweetedByMe = false;
	private int position=-1;
	
	public RetweetButtonListener(Activity activity, Fragment fragment,
									Status status, TweetData tweetData,int position) {
		this.activity = activity;
		this.fragment = fragment;
		this.status = status;
		this.tweetData = tweetData;
		this.position = position;
		//set isRetweetedByMe by twitter4j library
		this.isRetweetedByMe = tweetData.isRetweetByMe(position);
	}
	
	@Override
	public void onClick(View v) {
		
		if(isRetweetedByMe){
			new DeleteTweet(activity, fragment, tweetData, position, true).execute(status.getCurrentUserRetweetId());
			isRetweetedByMe = false;
		} 
		else {
			new Retweet(activity, fragment, tweetData, position).execute(status);
			isRetweetedByMe = true;
		}
	}
}
