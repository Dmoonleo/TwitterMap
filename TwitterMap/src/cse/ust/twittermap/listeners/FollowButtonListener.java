package cse.ust.twittermap.listeners;

import java.util.ArrayList;

import twitter4j.Twitter;
import twitter4j.User;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import cse.ust.twittermap.R;
import cse.ust.twittermap.TwitterFactory;
import cse.ust.twittermap.adapters.FollowingAdapter;

public class FollowButtonListener implements OnClickListener {

	User user = null;
	Twitter twitter = null;
	ArrayList<Boolean> isFollowing = null;
	FollowingAdapter adpater = null;
	int position;
	
	public void setParameters(User user, ArrayList<Boolean> isFollowing, int position, FollowingAdapter adapter) {
		this.user = user;
		this.isFollowing = isFollowing;
		this.position = position;
		this.adpater = adapter;
	}
	
	@Override
	public void onClick(View v) {
		if(twitter == null) twitter = TwitterFactory.get4jInstance();
		
		if(isFollowing.get(position)) {
			try {
				twitter.destroyFriendship(user.getId());
				((ImageView) v).setImageResource(R.drawable.follow_false);
				isFollowing.set(position, false);
			} catch (Exception e) {
				e.printStackTrace();
				((ImageView) v).setImageResource(R.drawable.follow_true);
				isFollowing.set(position, true);
			}
		}
		else {
			try {
				twitter.createFriendship(user.getId());	
				((ImageView) v).setImageResource(R.drawable.follow_true);
				isFollowing.set(position, true);
			} catch (Exception e) {
				e.printStackTrace();
				((ImageView) v).setImageResource(R.drawable.follow_false);
				isFollowing.set(position, false);
			}
		}
		
		adpater.notifyDataSetChanged();
	}
}
