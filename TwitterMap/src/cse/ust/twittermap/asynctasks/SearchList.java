package cse.ust.twittermap.asynctasks;

import java.util.List;
import java.util.Vector;

import twitter4j.MediaEntity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import cse.ust.twittermap.TwitterFactory;
import cse.ust.twittermap.adapters.StatusAdapter;

public class SearchList extends AsyncTask<twitter4j.Query, Void, Void> {
	
	private ProgressBar progressbar = null;
	private ListView listview = null;
	private StatusAdapter adapter = null;
	private List<twitter4j.Status> statusList = null;
	private List<Boolean> ifFavorite = null;
	private List<Boolean> ifRetweet = null;
	private Vector<Vector<String>> imageUrlsList=null;
	
	public SearchList(ProgressBar progressbar, ListView listview, StatusAdapter adapter, List<twitter4j.Status> statusList,
						List<Boolean> ifFavorite, List<Boolean> ifRetweet, Vector<Vector<String>> imageUrlsList) {
		this.progressbar = progressbar;
		this.listview = listview;
		this.adapter = adapter;
		this.ifFavorite = ifFavorite;
		this.ifRetweet = ifRetweet;
		this.statusList = statusList;
		this.imageUrlsList=imageUrlsList;
	}

	@Override
	protected Void doInBackground(twitter4j.Query... queries) {
		try {			
			List<twitter4j.Status> resList = TwitterFactory.get4jInstance().search(queries[0]).getTweets();		

			//jtwitter, get ifRetweet			
			winterwell.jtwitter.Twitter jtwitter=TwitterFactory.getInstance();
			List<winterwell.jtwitter.Status> jresList = jtwitter.getFavorites();
			for(int i = 0; i < jresList.size(); ++i){
				Boolean b = jtwitter.getRetweeters(jresList.get(i)).contains(jtwitter.getSelf());
				//System.out.println("test"+b);
				ifRetweet.add(b);
			}
				
			twitter4j.Status curStatus = null;
			for(int i = 0; i < resList.size(); ++i) {
				curStatus = resList.get(i);
				statusList.add(curStatus);
				ifFavorite.add(curStatus.isFavorited());
				
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(Void param) {		
		adapter.notifyDataSetChanged();
		progressbar.setVisibility(View.GONE);
	    listview.setVisibility(View.VISIBLE);
	}
}
