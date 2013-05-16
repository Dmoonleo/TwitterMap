package cse.ust.twittermap.fragments;

import java.util.concurrent.ExecutionException;

import twitter4j.TwitterException;
import twitter4j.User;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cse.ust.twittermap.BitmapCache;
import cse.ust.twittermap.R;
import cse.ust.twittermap.TwitterFactory;
import cse.ust.twittermap.activities.OthersTimeLineActivity;
import cse.ust.twittermap.listeners.DirectMessageListener;
import cse.ust.twittermap.listeners.ProfileFollowButtonListener;

@SuppressLint({ "NewApi", "ValidFragment" })
public class ProfilePageFragment extends Fragment {

	private String userScreenName = null;
	private Activity parentActivity = null;
	
	public ProfilePageFragment() {
		super();
	}
	
	public ProfilePageFragment(String userScreenName, Activity parentActivity) {
		super();
		this.userScreenName = userScreenName;
		this.parentActivity = parentActivity;
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_page, container, false);
        User user = null;
        try {
        	user = new AsyncTask<String, Void, User>() {
        		
				@Override
				protected User doInBackground(String... arg0) {
					try {
						return TwitterFactory.get4jInstance().showUser(arg0[0]);
					} catch (TwitterException e) {
						e.printStackTrace();
						return null;
					}					
				}
        	}.execute(userScreenName).get();
        } catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
        
        assert(user != null);
        ImageView profileImage = (ImageView) v.findViewById(R.id.profile_image);
        profileImage.setImageBitmap(BitmapCache.getUserImageFromMemCache(user));
        
        TextView nameLabel = (TextView) v.findViewById(R.id.profile_name);
        nameLabel.setText(user.getName());
        
        TextView screenNameLabel = (TextView)v.findViewById(R.id.profile_screenname);
        screenNameLabel.setText(user.getScreenName());
        
        TextView discriptionLabel = (TextView) v.findViewById(R.id.profile_discription);
        discriptionLabel.setText(user.getDescription());
        
        TextView tweetCount = (TextView) v.findViewById(R.id.profile_tweet_count);
        tweetCount.setText(String.format("%d", user.getStatusesCount()));
        
        TextView followingCount = (TextView)v.findViewById(R.id.profile_following_count);
        followingCount.setText(String.format("%d", user.getFriendsCount()));
        
        TextView followerCount = (TextView)v.findViewById(R.id.profile_follower_count);
        followerCount.setText(String.format("%d", user.getFollowersCount()));
        
        // if not myself, set follow button
        boolean isSelf = user.getScreenName().equals(TwitterFactory.getMyScreenName());
        try {
	        if (!isSelf) {
	        	ImageView followButton = (ImageView) v.findViewById(R.id.Follow_Button);
	            boolean isFollowing = 
	            		TwitterFactory.get4jInstance().showFriendship(TwitterFactory.
	            				get4jInstance().getId(), user.getId()).isSourceFollowingTarget();
	            if (isFollowing)
		        	followButton.setBackgroundResource(R.drawable.follow_true);
		        else
		        	followButton.setBackgroundResource(R.drawable.follow_false);
		        ProfileFollowButtonListener listener = new ProfileFollowButtonListener();
		        listener.setParameters(user, isFollowing);
		        followButton.setOnClickListener(listener);
	        }
        } catch (TwitterException e) {
        	e.printStackTrace();
        	((ImageView) v.findViewById(R.id.Follow_Button)).setEnabled(false);
        }
        
        // if not myself and is a follower, set direct message button.
        if (!isSelf) {
        	try {
				boolean isFollower = TwitterFactory.get4jInstance().showFriendship(user.getId(), 
										TwitterFactory.get4jInstance().getId()).isSourceFollowingTarget();
				Button directMsgBtn = (Button) v.findViewById(R.id.directmsg_btn);
	            if (isFollower) {
					DirectMessageListener directMessageListener = new DirectMessageListener();
		    		directMessageListener.setParameters(parentActivity.getFragmentManager(), user);
		    		directMsgBtn.setOnClickListener(directMessageListener);
				}
	            else {
	            	directMsgBtn.setEnabled(false);
	            }
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (TwitterException e) {
				e.printStackTrace();
			}
        }
        
		// set status fragment
        HomeFragment newFragment = new HomeFragment(userScreenName, true);
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.profile_tweet_list, newFragment, "CURRENTFRAGMENT");
		transaction.commit();
		
		// set more tweets
		LinearLayout moreTweets = (LinearLayout) v.findViewById(R.id.More_Tweets_View);
		moreTweets.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), OthersTimeLineActivity.class);
				intent.putExtra("screenName", userScreenName);
				getActivity().startActivity(intent);
			}
		});
        
        return v;
    }
}
