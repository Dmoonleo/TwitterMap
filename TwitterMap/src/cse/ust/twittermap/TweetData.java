package cse.ust.twittermap;

import java.util.List;
import java.util.Vector;

import twitter4j.Status;

public class TweetData {
	
	private List<Status> statusList = null;
	private List<Boolean> ifFavorite = null;
	private List<Boolean> ifRetweet = null;
	private Vector<Vector<String>> imageUrlsList = null;
	
	public TweetData(List<Status> statusList, List<Boolean> ifFavorite, List<Boolean> ifRetweet, 
			Vector<Vector<String>> imageUrlsList) {
		this.statusList = statusList;
		this.ifFavorite = ifFavorite;
		this.ifRetweet = ifRetweet;
		this.imageUrlsList = imageUrlsList;
	}
	
	public Vector<String> getImageUrls(int position) {
		
		assert(position >= 0 && position < size()) : "index over bound!";
		System.out.println("test imageurl size&posi "+imageUrlsList.size()+" "+position);
		return imageUrlsList.get(position);
	}
	
	public void remove(int position) {
		if (position >= 0 && position < statusList.size()) {
			statusList.remove(position);
			ifFavorite.remove(position);
			ifRetweet.remove(position);
			imageUrlsList.remove(position);
		}
	}
	
	public boolean isFavorite(int position) {
		if (position >= 0 && position < ifFavorite.size()) {
			return ifFavorite.get(position).booleanValue();
		}
		else return false;
	}
	
	public void setFavorite(int position, boolean value) {
		if (position >= 0 && position < ifFavorite.size()) {
			ifFavorite.set(position, Boolean.valueOf(value)); 
		}
	}
	
	public boolean getIfFavorite(int position) {
		return ifFavorite.get(position);
	}
	
	public boolean isRetweetByMe(int position) {
		if (position >= 0 && position < ifRetweet.size()) {
			return ifRetweet.get(position);
		}
		else return false;
	}
	
	public void setRetweet(int position, boolean value) {
		if (position >= 0 && position < ifFavorite.size()) {
			ifRetweet.set(position, Boolean.valueOf(value)); 
		}
	}
	
	public boolean getRetweet(int position) {
		return ifRetweet.get(position);
	}
	
	public Status getStatus(int position)  {
		return statusList.get(position);
	}
	
	public int indexOf(Status s) {
		return statusList.indexOf(s);
	}
	
	public int size() {
		return statusList.size();
	}
	
	public void addFrontIfRetweet(boolean value) {
		ifRetweet.add(0, Boolean.valueOf(value));
	}
	
	public void addFrontIfFavorite(boolean value) {
		ifFavorite.add(0, Boolean.valueOf(value));
	}
	
	public void addFrontStatus(Status s) {
		statusList.add(0, s);
	}

	public void setPhotoUrls(int position, List<String> photoUrls) {
		Vector<String> r = new Vector<String>();
		for (int i = 0; i < photoUrls.size(); i++) {
			r.add(photoUrls.get(i));
		}
		imageUrlsList.set(position, r);
	}
}