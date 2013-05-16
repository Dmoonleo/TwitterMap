package cse.ust.twittermap.adapters;

import java.util.List;

import twitter4j.User;
import android.annotation.SuppressLint;
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
import cse.ust.twittermap.TwitterFactory;
import cse.ust.twittermap.activities.ProfilePageActivity;
import cse.ust.twittermap.listeners.DirectMessageListener;

public class FollowerAdapter extends BaseAdapter  {

	private Activity parentActivity = null;
	private LayoutInflater mInflater = null;
	private List<User> userList = null;
	private String userScreenName = null;
	
	private class ViewHolder {
        TextView name;
        TextView id;
        ImageView userImage;
        ImageView followButton;
    }
	
	public FollowerAdapter(Activity parentActivity, List<User> userList, String userScreenName) {
		this.parentActivity = parentActivity;
		mInflater = LayoutInflater.from(parentActivity.getApplicationContext());
		this.userList = userList;
		this.userScreenName = userScreenName;
	}
	
	@Override
	public int getCount() {
		if(userList!=null)
			return userList.size();
		return 0;
	}

	@SuppressLint("NewApi")
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
		
		Bitmap bitmapOrig = BitmapCache.getUserImageFromMemCache(user);
		holder.userImage.setImageBitmap(bitmapOrig);
		
		try {
			if (userScreenName == TwitterFactory.getMyScreenName()) {
				holder.followButton.setImageResource(R.drawable.message_button);
				DirectMessageListener directMessageListener = new DirectMessageListener();
				directMessageListener.setParameters(parentActivity.getFragmentManager(), user);
				holder.followButton.setOnClickListener(directMessageListener);
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		
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
