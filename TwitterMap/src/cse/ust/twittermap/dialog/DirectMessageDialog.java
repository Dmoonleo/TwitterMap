package cse.ust.twittermap.dialog;

import java.util.ArrayList;
import java.util.List;

import twitter4j.DirectMessage;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cse.ust.twittermap.R;
import cse.ust.twittermap.TwitterFactory;
import cse.ust.twittermap.adapters.DirectMessageAdapter;

public class DirectMessageDialog extends Dialog implements 
		OnClickListener{

	User user;
	Button sendBtn;
	Twitter twitter;
	ListView messagesListView;
	DirectMessageAdapter dMAdapter;
	List<DirectMessage> messages;
	Activity parentActivity;

	protected TextView dm_title;
	protected TextView dm_message;
	

	private Handler handler = new Handler();
	private Runnable runnable = new Runnable() {
        public void run() {
            this.update();
            handler.postDelayed(this, 1000 * 60);
        }
        void update() {
            getMessagesView();
        }
    }; 
	
	public DirectMessageDialog(Activity parentActivity, Context context, User user) {
		super(context);
		this.parentActivity = parentActivity;
		this.user = user;		
	}
	@Override
	public void onDetachedFromWindow() {
		 handler.removeCallbacks(runnable);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.direct_message_dialog);
		dm_title = (TextView) findViewById(R.id.dm_title);
		dm_message = (TextView) findViewById(R.id.dm_message);
		sendBtn =  (Button) findViewById(R.id.send_button);
		twitter = TwitterFactory.get4jInstance();
		retrieveMessages();
		if(messages.size() == 0){
			dm_title.setText("No Recent Messages");
		}
		else{
			dm_title.setText("Chat with "+user.getName());
		}
		getMessagesView();
		sendBtn.setOnClickListener(this);
				
		super.onCreate(savedInstanceState);	
		handler.postDelayed(runnable, 0);
	}
	
	public void retrieveMessages(){
		List<DirectMessage> messages_received;
		List<DirectMessage> messages_sent;
		try {
			messages_received = twitter.getDirectMessages();
			messages_sent = twitter.getSentDirectMessages();
		} catch (TwitterException e) {
			e.printStackTrace();
			return;
		}
		messages = new ArrayList<DirectMessage>();
		
		int i = messages_received.size()-1;
		int j = messages_sent.size()-1;
		while(i >= 0 || j >= 0){
			DirectMessage temp_message;
			if(i>=0 && j >=0){
				if(messages_received.get(i).getCreatedAt().before(messages_sent.get(j).getCreatedAt())){
					temp_message = messages_received.get(i);
					i--;
					if(temp_message.getSender().getScreenName().toString().compareTo(user.getScreenName().toString())==0){
						messages.add(temp_message);
					}		
				}
				else{
					temp_message = messages_sent.get(j);
					j--;
					if(temp_message.getRecipient().getScreenName().toString().compareTo(user.getScreenName().toString())==0){
						messages.add(temp_message);
					}	
				}
			}
			else if(i >= 0){
				temp_message = messages_received.get(i);
				i--;
				if(temp_message.getSender().getScreenName().toString().compareTo(user.getScreenName().toString())==0){
					messages.add(temp_message);
				}		
			}
			else if(j >= 0){
				temp_message = messages_sent.get(j);
				j--;
				if(temp_message.getRecipient().getScreenName().toString().compareTo(user.getScreenName().toString())==0){
					messages.add(temp_message);
				}	
			}		
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v == sendBtn){
			final Context context = this.getContext();
			String message = dm_message.getText().toString();
			try{
				//twitter.sendMessage(user.getScreenName(), message);
				twitter.sendDirectMessage(user.getScreenName(), message);
				Toast.makeText(context, "Direct Message sent successfully!", 
						Toast.LENGTH_LONG).show();
				//new SendDirectMessage(context, twitter, user, message, dMAdapter).execute();
			}
			catch(Exception e)
			{
				e.printStackTrace();
				Toast.makeText(context, e.getMessage(), 
						Toast.LENGTH_LONG).show();
			}
			
			dm_message.setText("");
			getMessagesView();
			
		}		
	}
	
	public void getMessagesView(){
		retrieveMessages();
		if(messages.size() != 0){
			dm_title.setText("Chat with "+user.getName());
		}
		dMAdapter = new DirectMessageAdapter(parentActivity, parentActivity.getApplicationContext(), messages, null);
		dMAdapter.notifyDataSetChanged();
		
		messagesListView = (ListView) findViewById(R.id.dm_listview);
		messagesListView.setAdapter(dMAdapter);
		messagesListView.invalidateViews();
		messagesListView.setSelection(messagesListView.getAdapter().getCount()-1); 
	}
	
}