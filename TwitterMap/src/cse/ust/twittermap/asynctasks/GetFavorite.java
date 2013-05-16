package cse.ust.twittermap.asynctasks;

import java.util.List;
import java.util.Vector;

import twitter4j.MediaEntity;
import twitter4j.TwitterException;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.widget.Toast;
import cse.ust.twittermap.BitmapCache;
import cse.ust.twittermap.R;
import cse.ust.twittermap.TwitterFactory;
import cse.ust.twittermap.fragments.FavoriteFragment;

public class GetFavorite extends AsyncTask<String, Void, Boolean> {
	
	private List<twitter4j.Status> statusList = null;
	private List<Boolean> ifFavorite = null;
	private List<Boolean> ifRetweet = null;
	private Vector<Vector<String>> imageUrlsList = null;
	private FavoriteFragment fragment = null;
	
	public GetFavorite(List<twitter4j.Status> statusList, List<Boolean> ifFavorite, 
				List<Boolean> ifRetweet, Vector<Vector<String>> imageUrlsList, FavoriteFragment fragment) {
		
		this.statusList = statusList;
		this.ifFavorite = ifFavorite;
		this.ifRetweet = ifRetweet;
		this.imageUrlsList = imageUrlsList;
		this.fragment = fragment;
	}
	
	@Override
	protected Boolean doInBackground(String... userScreenNames) {
		try {			
			List<twitter4j.Status> resList = null;
			
			resList = TwitterFactory.get4jInstance().getFavorites();
			
			twitter4j.Status curStatus = null;
			
			statusList.clear();
			ifFavorite.clear();
			ifRetweet.clear();
			imageUrlsList.clear();
			
			//jtwitter, get ifRetweet			
			winterwell.jtwitter.Twitter jtwitter=TwitterFactory.getInstance();
			List<winterwell.jtwitter.Status> jresList = jtwitter.getFavorites();
			for(int i=0;i<jresList.size();++i){
				Boolean b=jtwitter.getRetweeters(jresList.get(i)).contains(jtwitter.getSelf());
				//System.out.println("test"+b);
				ifRetweet.add(b);
			}
			for(int i = 0; i < resList.size(); ++i) {
				curStatus = resList.get(i);
				BitmapCache.getUserImageFromMemCache(curStatus.getUser());
				statusList.add(curStatus);
				ifFavorite.add(Boolean.valueOf(true));			
				//ifRetweet.add(Boolean.valueOf(TwitterFactory.get4jInstance().showStatus(curStatus.getId()).isRetweetedByMe()));				
				MediaEntity[] medias = curStatus.getMediaEntities();
				Vector<String> curURLs = new Vector<String>();
				for (int j = 0; j < medias.length; j++) {
					if (medias[j].getType().equals("photo")) 
						curURLs.add(medias[j].getMediaURL());
				}
				imageUrlsList.add(curURLs);
			}
		} catch (TwitterException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	@SuppressLint("NewApi") @Override
	protected void onPostExecute(Boolean param) {
		fragment.refreshWithCurrentData();
		if (param == false)
			Toast.makeText(fragment.getActivity(), R.string.rate_limit_reached, Toast.LENGTH_SHORT).show();
	}
}
