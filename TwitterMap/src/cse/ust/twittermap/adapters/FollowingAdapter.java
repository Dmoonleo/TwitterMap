package cse.ust.twittermap.adapters;

import java.util.ArrayList;
import java.util.List;

import twitter4j.User;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cse.ust.twittermap.BitmapCache;
import cse.ust.twittermap.R;
import cse.ust.twittermap.activities.ProfilePageActivity;
import cse.ust.twittermap.listeners.FollowButtonListener;

public class FollowingAdapter extends BaseAdapter  {

	private Activity parentActivity = null;
	private LayoutInflater mInflater = null;
	private List<User> userList = null;
	private ArrayList<Boolean> isFollowing = null;
	
	private class ViewHolder {
        TextView name;
        TextView id;
        ImageView userImage;
        ImageView followButton;
    }
	
	public FollowingAdapter(Activity parentActivity, List<User> userList, ArrayList<Boolean> isFollowing) {
		this.parentActivity = parentActivity;
		mInflater = LayoutInflater.from(parentActivity.getApplicationContext());
		this.userList = userList;
		this.isFollowing = isFollowing;
	}
	
	@Override
	public int getCount() {
		if(userList!=null)
			return userList.size();
		return 0;
	}

	@Override
	public View getView(int position, View reUse, ViewGroup parent) {
		ViewHolder holder;
		if(reUse == null) {
			reUse = mInflater.inflate(R.layout.list_item_follower_and_following, null);
			holder = new ViewHolder();
			holder.name = (TextView) reUse.findViewById(R.id.name);
			holder.id = (TextView) reUse.findViewById(R.id.id);
			holder.userImage = (ImageView) reUse.findViewById(R.id.image);
			holder.followButton = (ImageView) reUse.findViewById(R.id.button);
			reUse.setTag(holder);
		} else {
			holder = (ViewHolder) reUse.getTag();
		}
		
		final User user = this.getItem(position);

		holder.name.setText(user.getName());
		holder.id.setText("@" + user.getScreenName());
		
		if (isFollowing != null) {
			if (isFollowing.get(position))
				holder.followButton.setImageResource(R.drawable.follow_true);
			else
				holder.followButton.setImageResource(R.drawable.follow_false);
			FollowButtonListener btnListener = new FollowButtonListener();
			btnListener.setParameters(user, isFollowing, position, this);
			holder.followButton.setOnClickListener(btnListener);
		}
			
		Bitmap bitmapOrig = BitmapCache.getUserImageFromMemCache(user);
		
		if (bitmapOrig!=null)
			holder.userImage.setImageBitmap(bitmapOrig);
		
		reUse.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(parentActivity, ProfilePageActivity.class);
				intent.putExtra("screenName", user.getScreenName());
				parentActivity.startActivityForResult(intent, ProfilePageActivity.class.hashCode());
			}
		});
		
		return reUse;
	}

	@Override
	public User getItem(int position) {
		if(userList != null && (position >= 0 && position < userList.size()))
			return userList.get(position);
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
