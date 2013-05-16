package cse.ust.twittermap.listeners;

import android.app.Activity;
import android.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import cse.ust.twittermap.TweetData;
import cse.ust.twittermap.asynctasks.SetFavorite;
import cse.ust.twittermap.asynctasks.SetUnFavorite;

public class FavoriteButtonListener implements OnClickListener {

	private TweetData data = null;
	private int position = 0;
	private Activity activity = null;
	private Fragment fragment = null;
	
	public void setParameter(int position, TweetData data,
								Activity activity, Fragment fragment) {
		this.position = position;
		this.data = data;
		this.activity = activity;
		this.fragment = fragment;
	}

	@Override
	public void onClick(View v) {
		
		if (data.getIfFavorite(position)) {
			new SetUnFavorite(activity, fragment, data, position).execute(data.getStatus(position));
		}
		else {
			new SetFavorite(activity, fragment, data, position).execute(data.getStatus(position));
		}
	}
}


