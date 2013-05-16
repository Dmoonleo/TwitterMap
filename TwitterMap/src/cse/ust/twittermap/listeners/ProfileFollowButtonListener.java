package cse.ust.twittermap.listeners;

import twitter4j.TwitterException;
import twitter4j.User;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import cse.ust.twittermap.R;
import cse.ust.twittermap.TwitterFactory;

public class ProfileFollowButtonListener implements OnClickListener {

	private User user = null;
	private boolean isFollowing;
	
	public void setParameters(User user, boolean isFollowing) {
		this.user = user;
		this.isFollowing = isFollowing;
	}
	
	@Override
	public void onClick(View v) {
		try {
			if(isFollowing) {
				((ImageView) v).setImageResource(R.drawable.follow_false);
				//TwitterFactory.getInstance().users().stopFollowing(user);
				TwitterFactory.get4jInstance().destroyFriendship(user.getId());
				isFollowing = false;
			}
			else {
				((ImageView) v).setImageResource(R.drawable.follow_true);
				//TwitterFactory.getInstance().users().follow(user);
				TwitterFactory.get4jInstance().createFriendship(user.getId());
				isFollowing = true;
			}
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}
}
