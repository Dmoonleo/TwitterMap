package cse.ust.twittermap.fragments;

import twitter4j.Status;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import cse.ust.twittermap.R;
import cse.ust.twittermap.TweetData;
import cse.ust.twittermap.asynctasks.DeleteTweet;

@SuppressLint({ "NewApi", "ValidFragment" }) 
public class DeleteEnsuranceDialogFragment extends DialogFragment  {
	
	private Status status = null;
	private Fragment fragment = null;
	private DeleteTweet task = null;
	private TweetData localData = null;
	private int index;
	
	public DeleteEnsuranceDialogFragment(Status status, Fragment fragment, TweetData localData, int index) {
		this.status = status;
		this.fragment = fragment;
		this.localData = localData;
		this.index = index;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.delete_dialog_title).setMessage(R.string.delete_dialog_message)
        	.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
        		public void onClick(DialogInterface dialog, int id) {
        			
        		}
        	})
        	.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
        		public void onClick(DialogInterface dialog, int id) {
        			task = new DeleteTweet(getActivity(), fragment, localData, index, false);
        			task.execute(status.getId());
        		}
        	});
		return builder.create();
	}	
}