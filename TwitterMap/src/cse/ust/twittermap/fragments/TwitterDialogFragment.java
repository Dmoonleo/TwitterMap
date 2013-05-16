package cse.ust.twittermap.fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import cse.ust.twittermap.dialog.PostTweetDialog;

@SuppressLint({ "NewApi", "ValidFragment" }) 
public class TwitterDialogFragment extends DialogFragment  {
	
	private String directTweetUserScreenName = null;
	private PostTweetDialog dialog = null;
	private Fragment fragment = null;
	
	public TwitterDialogFragment(String name, Fragment fragment) {
		super();
		directTweetUserScreenName = name;
		this.fragment = fragment;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		System.out.println(getActivity().getClass().toString());
		dialog = new PostTweetDialog(getActivity(), directTweetUserScreenName, fragment);
		return dialog;
	}
}
