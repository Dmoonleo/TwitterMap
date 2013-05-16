package cse.ust.twittermap.asynctasks;

import twitter4j.Twitter;
import twitter4j.User;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Adapter;
import android.widget.Toast;
import cse.ust.twittermap.adapters.DirectMessageAdapter;

public class SendDirectMessage extends AsyncTask<String, Void, Object> {
	
	Context context;
	Twitter sender;
	User user;
	String message;
	Adapter adapter;
	
	public SendDirectMessage(Context context, Twitter sender, User user, String message, DirectMessageAdapter adapter){
		this.context = context;
		this.sender = sender;
		this.user = user;
		this.message = message;
		this.adapter = adapter;
	}
	
	@Override
	protected Object doInBackground(String... params) {
		
		try{
			sender.sendDirectMessage(user.getId(), message);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
			return e;
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Object result) {		
		if(result != null && result instanceof Exception) {
			((Exception)result).printStackTrace();
			Toast.makeText(context, ((Exception)result).getMessage(), Toast.LENGTH_SHORT).show();
	    }
		else{
			Toast.makeText(context, "Direct Message sent successfully!", Toast.LENGTH_SHORT).show();
			
		}
	}

}