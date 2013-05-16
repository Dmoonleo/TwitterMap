package cse.ust.twittermap.asynctasks;
//
import java.util.List;
import java.util.Vector;

import twitter4j.MediaEntity;
import twitter4j.Paging;
import twitter4j.TwitterException;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.widget.Toast;
import cse.ust.twittermap.BitmapCache;
import cse.ust.twittermap.R;
import cse.ust.twittermap.TwitterFactory;
import cse.ust.twittermap.fragments.HomeFragment;

public class GetHome extends AsyncTask<String, Void, Boolean> {

	private List<twitter4j.Status> statusList = null;
	private List<Boolean> ifFavorite = null;
	private List<Boolean> ifRetweet = null;
	private Vector<Vector<String>> imageUrlsList = null;
	private HomeFragment fragment = null;
	private boolean inProfilePage;
	private int pageNum = 1;

	public GetHome(List<twitter4j.Status> statusList, List<Boolean> ifFavorite, List<Boolean> ifRetweet, 
			Vector<Vector<String>> imageUrlsList, HomeFragment fragment, boolean inProfilePage, int pageNum) {
		this.statusList = statusList;
		this.ifFavorite = ifFavorite;
		this.ifRetweet = ifRetweet;
		this.imageUrlsList = imageUrlsList;
		this.statusList = statusList;
		this.fragment = fragment;
		this.inProfilePage = inProfilePage;
		this.pageNum = pageNum;
	}
	
	public GetHome(List<twitter4j.Status> statusList, List<Boolean> ifFavorite, List<Boolean> ifRetweet, 
						Vector<Vector<String>> imageUrlsList, HomeFragment fragment, boolean inProfilePage) {
		this.statusList = statusList;
		this.ifFavorite = ifFavorite;
		this.ifRetweet = ifRetweet;
		this.imageUrlsList = imageUrlsList;
		this.statusList = statusList;
		this.fragment = fragment;
		this.inProfilePage = inProfilePage;
	}

	@Override
	protected Boolean doInBackground(String... userScreenNames) {
		try {			
			Paging paging = new Paging(1, pageNum*20);
			List<twitter4j.Status> resList = null;
			//download status list from twitter.
			if (userScreenNames[0].equals(TwitterFactory.getMyScreenName()) && userScreenNames.length > 1
					&& userScreenNames[1] == "homeline"){
				resList = TwitterFactory.get4jInstance().getHomeTimeline(paging);
			}
			else
				resList = TwitterFactory.get4jInstance().getUserTimeline(userScreenNames[0],paging);

			if (resList.size() > 4 && inProfilePage) {
				resList = resList.subList(0, 4);
			}
			
			twitter4j.Status curStatus = null;
			List<twitter4j.Status> favoriteList = TwitterFactory.get4jInstance().getFavorites();
			
			statusList.clear();
			ifFavorite.clear();
			ifRetweet.clear();
			imageUrlsList.clear();
			
			for(int i = 0; i < resList.size(); ++i) {
				curStatus = resList.get(i);
				BitmapCache.getUserImageFromMemCache(curStatus.getUser());
				statusList.add(curStatus);
				ifFavorite.add(Boolean.valueOf(favoriteList.contains(curStatus)));
				ifRetweet.add(Boolean.valueOf(curStatus.isRetweetedByMe()));
				
				MediaEntity[] medias = curStatus.getMediaEntities();
				Vector<String> curURLs = new Vector<String>();
				for (int j = 0; j < medias.length; j++) {
					if (medias[j].getType().equals("photo")) 
						curURLs.add(medias[j].getMediaURL());
				}
				if(imageUrlsList != null){
					imageUrlsList.add(curURLs);
				}
			}
		} catch (TwitterException e) {	
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	@Override 
	protected void onPreExecute() {
		fragment.loading();
	}
	
	@SuppressLint("NewApi") @Override
	protected void onPostExecute(Boolean param) {	
		fragment.refreshWithCurrentData();
		
		if (param == false)
			Toast.makeText(
					this.fragment.getActivity(), 
					R.string.rate_limit_reached, 
					Toast.LENGTH_SHORT).show();			
	}
}
