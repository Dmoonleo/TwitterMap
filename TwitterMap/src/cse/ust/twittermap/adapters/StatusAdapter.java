package cse.ust.twittermap.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import twitter4j.GeoLocation;
import twitter4j.HashtagEntity;
import twitter4j.Query;
import twitter4j.Status;
import twitter4j.URLEntity;
import twitter4j.User;
import twitter4j.UserMentionEntity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Browser;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cse.ust.twittermap.BitmapCache;
import cse.ust.twittermap.R;
import cse.ust.twittermap.TweetData;
import cse.ust.twittermap.TwitterFactory;
import cse.ust.twittermap.Utility;
import cse.ust.twittermap.activities.ProfilePageActivity;
import cse.ust.twittermap.fragments.SearchListFragment;
import cse.ust.twittermap.listeners.DeleteButtonListener;
import cse.ust.twittermap.listeners.ExpandButtonListener;
import cse.ust.twittermap.listeners.FavoriteButtonListener;
import cse.ust.twittermap.listeners.LocationButtonListener;
import cse.ust.twittermap.listeners.ReplyButtonListener;
import cse.ust.twittermap.listeners.RetweetButtonListener;

@SuppressLint("DefaultLocale")
public class StatusAdapter extends BaseAdapter {
	
	private LayoutInflater mInflater = null;
	private TweetData tweetData = null;
	private Activity parentActivity = null;
	private List<Vector<Boolean>> expanded = new ArrayList<Vector<Boolean>>();
	
	public StatusAdapter(Activity parentActivity, List<Status> statusList, List<Boolean> ifFavorite, 
							List<Boolean> ifRetweet, Vector<Vector<String>> imageUrlsList) {		
		this.parentActivity = parentActivity;
		mInflater = LayoutInflater.from(parentActivity.getApplicationContext());
		tweetData = new TweetData(statusList, ifFavorite, ifRetweet, imageUrlsList);
		for (int i = 0; i < 1000; ++i) {
			Vector<Boolean> curVec = new Vector<Boolean>();
			curVec.add(Boolean.valueOf(false));
			expanded.add(curVec);
		}
	}
	
	public TweetData getTweetData() {
		return tweetData;
	}
	
	@SuppressLint("NewApi")
	@Override
	public View getView(final int position, View reUse, ViewGroup parent) {
		
		final Status status = tweetData.getStatus(position);
		final User user = status.getUser();
		String timeStamp = Utility.getTimeStamp(status);
		reUse = mInflater.inflate(R.layout.list_item_home_normal_view, null);
		boolean isSelf = false;
		try {
			isSelf = user.getScreenName().toLowerCase().equals(TwitterFactory.getMyScreenName());
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		final FragmentManager fragmentManager = parentActivity.getFragmentManager();
		Fragment currentFragment = parentActivity.getFragmentManager().findFragmentByTag("CURRENTFRAGMENT");
		
		TextView time = (TextView)reUse.findViewById(R.id.time);
		time.setText(timeStamp);
		
		TextView title = (TextView)reUse.findViewById(R.id.title);
		title.setText(user.getScreenName());
		
		TextView statusText = (TextView)reUse.findViewById(R.id.status);
		statusText.setText(status.getText());

		//For recognize url
		SpannableString ss = new SpannableString(status.getText());
		URLEntity[] urls = status.getURLEntities();
		if(urls!=null) { 
			for (int i = 0; i < urls.length; i++){
				URLEntity url=urls[i];
				ss.setSpan(new URLSpan(url.getURL()){
					@Override
					public void onClick(View widget){
						Uri uri=Uri.parse(getURL());
						Context context=widget.getContext();
						Intent intent=new Intent(Intent.ACTION_VIEW,uri);
						intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent);
					}
				}, url.getStart(), url.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		
		//For recogonize mentions
		UserMentionEntity[] mentions =status.getUserMentionEntities();
		if(mentions.length > 0) { 
			for (int i = 0; i < mentions.length; i++){
				final UserMentionEntity mention=mentions[i];
				ss.setSpan(new URLSpan(mention.getScreenName()){
					@Override
					public void onClick(View widget){
						Intent intent=new Intent(parentActivity, ProfilePageActivity.class);
						intent.putExtra("screenName", mention.getScreenName());
						parentActivity.startActivityForResult(intent, ProfilePageActivity.class.hashCode());
					}
				}, mention.getStart(), mention.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		
		//for recognize hashes
		HashtagEntity[] hashs =status.getHashtagEntities();
		if(hashs.length > 0) { 
			for (int i = 0; i < hashs.length; i++){
				HashtagEntity hash=hashs[i];
				ss.setSpan(new URLSpan("#"+hash.getText()){
					@Override
					public void onClick(View widget){
						SearchListFragment newFragment = new SearchListFragment(new Query(getURL()));
						
						FragmentTransaction transaction = fragmentManager.beginTransaction();
						transaction.replace(R.id.content_fragment, newFragment);
						transaction.commit();
					}
				}, hash.getStart(), hash.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		statusText.setText(ss);
		statusText.setMovementMethod(LinkMovementMethod.getInstance());

		// status user image setting
		ImageView image = (ImageView)reUse.findViewById(R.id.list_image);
		image.setImageBitmap(BitmapCache.getUserImageFromMemCache(user));
		image.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(parentActivity, ProfilePageActivity.class);
				intent.putExtra("screenName", user.getScreenName());
				parentActivity.startActivityForResult(intent, ProfilePageActivity.class.hashCode());
			}
		});

		//location button setting

		ImageButton locationBtn=(ImageButton)reUse.findViewById(R.id.locationButton);
		TextView locationTv=(TextView)reUse.findViewById(R.id.locationText);
		GeoLocation geo=status.getGeoLocation();
		//geo=new GeoLocation(40,30);//for test
		if(geo==null ){
			locationBtn.setVisibility(View.GONE);
			locationTv.setVisibility(View.GONE);
		}else{
			locationBtn.setVisibility(View.VISIBLE);
			locationTv.setVisibility(View.VISIBLE);
			locationTv.setText("From "+geo.getLatitude()+","+geo.getLongitude()); 
			LocationButtonListener locationBtnListener = new LocationButtonListener(geo,parentActivity.getFragmentManager());
			locationBtn.setOnClickListener(locationBtnListener);
		}
		if(status.getPlace()!=null){
			if(status.getPlace().getName()!=null){
				locationTv.setVisibility(View.VISIBLE);
				locationTv.setText("From "+status.getPlace().getName());
			}
		} 

		// reply button setting
		ImageButton replyBtn = (ImageButton)reUse.findViewById(R.id.replyButton);
		ReplyButtonListener replyBtnListener = new ReplyButtonListener(status, parentActivity.getFragmentManager());
		replyBtn.setOnClickListener(replyBtnListener);

		// retweet button setting
		ImageButton retweetBtn = (ImageButton)reUse.findViewById(R.id.retweetButton);
		if (isSelf) {
			retweetBtn.setEnabled(false);
		}
		else {
			//check if it is RetweetedByMe			
			if(tweetData.isRetweetByMe(position))
				retweetBtn.setImageResource(R.drawable.retweet_on);
			else
				retweetBtn.setImageResource(R.drawable.retweet_default);

			RetweetButtonListener retweetBtnListener = new RetweetButtonListener(parentActivity, 
															currentFragment, status, tweetData, position);
			retweetBtn.setOnClickListener(retweetBtnListener);
		}
		
		// favorite button setting
		ImageButton favoriteBtn = (ImageButton)reUse.findViewById(R.id.favoriteButton);
		if (tweetData.getIfFavorite(position)) 
			favoriteBtn.setImageResource(R.drawable.favorite_on);
		else {
			favoriteBtn.setImageResource(R.drawable.favorite_default);
		}
		FavoriteButtonListener favoriteListener = new FavoriteButtonListener();
		favoriteListener.setParameter(position, tweetData, parentActivity, currentFragment);
		favoriteBtn.setOnClickListener(favoriteListener);

		// delete button setting
		ImageButton deleteBtn = null;
		if (isSelf) {
			deleteBtn = new ImageButton(reUse.getContext());
			deleteBtn.setImageResource(R.drawable.delete);
			deleteBtn.setTag("delete_button");
			
			LinearLayout buttons = (LinearLayout)reUse.findViewById(R.id.button_group);
			buttons.addView(deleteBtn);
			
			DeleteButtonListener listener = new DeleteButtonListener(status, parentActivity.getFragmentManager(), tweetData, position);
			deleteBtn.setOnClickListener(listener);
		}
		
		// expand button setting
		final ImageButton expandBtn = (ImageButton) reUse.findViewById(R.id.expandButton);
		final LinearLayout photoLayout = (LinearLayout) reUse.findViewById(R.id.photos);
	
		if(tweetData.getImageUrls(position).size() > 0)
			expandBtn.setOnClickListener(new ExpandButtonListener(expandBtn, photoLayout, 
					tweetData.getImageUrls(position), parentActivity, expanded.get(position)));
		else
			expandBtn.setVisibility(View.GONE);
		
		return reUse;
	}

	@Override
	public int getCount() {
		return tweetData.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public Status getItem(int position) {
		return tweetData.getStatus(position);
	}
}
