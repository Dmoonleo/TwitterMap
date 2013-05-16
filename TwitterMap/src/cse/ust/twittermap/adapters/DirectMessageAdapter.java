package cse.ust.twittermap.adapters;

import java.util.List;

import twitter4j.DirectMessage;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import cse.ust.twittermap.Utility;

public class DirectMessageAdapter extends BaseAdapter {
	
	protected LayoutInflater mInflater;
	Context parentContext;
	Activity parentActivity;
	List<DirectMessage> messages;
	
	public DirectMessageAdapter(Context context, List<DirectMessage> messages) {		
		parentContext = context;
        mInflater = LayoutInflater.from(context);
        this.messages = messages;
	}
	
	public DirectMessageAdapter(Activity activity, Context context, List<DirectMessage> messages, LruCache<String, Bitmap> cache) {		
		parentActivity = activity;
		parentContext = context;
        mInflater = LayoutInflater.from(context);
        this.messages = messages;
	}
	
	public void setMessages(List<DirectMessage> messages){
		this.messages = messages;
	}
	
	@Override
	public int getCount() {
		if(messages!=null)
			return messages.size();			
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(messages != null && (position >= 0 && position < messages.size()))
			return messages.get(position);
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	
	@Override
	public View getView(int position, View reUse, ViewGroup parent) {

		reUse = Utility.getMessageCellView(position, messages, mInflater);
		return reUse;
	}

}