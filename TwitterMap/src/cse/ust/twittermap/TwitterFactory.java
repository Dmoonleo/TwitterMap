package cse.ust.twittermap;

import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import winterwell.jtwitter.OAuthSignpostClient;
import winterwell.jtwitter.Twitter;
import android.annotation.SuppressLint;
import android.os.StrictMode;

public class TwitterFactory {
	private static Twitter instance = null;
	private static twitter4j.Twitter instance4J = null;
	private static String screenName = null;
	
	@SuppressLint("NewApi")
	public TwitterFactory(){
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
		OAuthSignpostClient oauthClient = new OAuthSignpostClient(Key.CONSUMER_KEY, Key.CONSUMER_SECRET, Key.ACCESS_TOKEN, Key.ACCESS_TOKEN_SECRET);
		instance = new Twitter(null, oauthClient);
	}
	
	@SuppressLint("NewApi")
	public static Twitter getInstance(){
		if(instance==null){
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
			OAuthSignpostClient oauthClient = new OAuthSignpostClient(Key.CONSUMER_KEY, Key.CONSUMER_SECRET, Key.ACCESS_TOKEN, Key.ACCESS_TOKEN_SECRET);
			instance = new Twitter(null, oauthClient);
		}
		return instance;
	}
	
	public static twitter4j.Twitter get4jInstance() {
		if (instance4J == null) {
			ConfigurationBuilder builder = new ConfigurationBuilder(); 
	        builder.setOAuthConsumerKey(Key.CONSUMER_KEY); 
	        builder.setOAuthConsumerSecret(Key.CONSUMER_SECRET); 
	        AccessToken accessToken = new AccessToken(Key.ACCESS_TOKEN, Key.ACCESS_TOKEN_SECRET); 
	        instance4J = new twitter4j.TwitterFactory(builder.build()).getInstance(accessToken);
		}
		return instance4J;
	}
	
	public static String getMyScreenName() {
		if (screenName == null) {
			try {
				screenName = getInstance().getScreenName();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}
		return screenName;
	}
}
