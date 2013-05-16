package cse.ust.twittermap.fragments;

import java.util.List;

import twitter4j.DirectMessage;
import twitter4j.User;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.ListView;
import cse.ust.twittermap.adapters.DirectMessageAdapter;
import cse.ust.twittermap.dialog.DirectMessageDialog;

@SuppressLint({ "NewApi", "ValidFragment" }) 
public class DirectMessageFragment extends DialogFragment {
	
	User user;
	List<DirectMessage> messages;
	ListView messagesListView;
	DirectMessageAdapter dMAdapter;
	private DirectMessageDialog dialog;
	
	public DirectMessageFragment(User user){
		this.user = user;
	}
	
	@Override
	public void onStart(){
		super.onStart();
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		dialog =  new DirectMessageDialog(getActivity(), getActivity(), user);
		return dialog;
	}
	
	@Override
	public void onStop(){
		if(dialog != null) {
			dialog.cancel();
		}
		super.onStop();
	}
}