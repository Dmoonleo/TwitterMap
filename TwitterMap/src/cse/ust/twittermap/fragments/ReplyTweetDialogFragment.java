package cse.ust.twittermap.fragments;

import twitter4j.Status;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import cse.ust.twittermap.dialog.ReplyTweetDialog;

@SuppressLint({ "NewApi", "ValidFragment" }) 
public class ReplyTweetDialogFragment extends DialogFragment  {
	
	private Fragment fragment = null;
	private Status status = null;
	private ReplyTweetDialog dialog = null;
	
	public ReplyTweetDialogFragment(Fragment fragment, Status status) {
		this.fragment = fragment;
		this.status = status;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		dialog = new ReplyTweetDialog(getActivity(), fragment, status);
		return dialog;
	}
}
