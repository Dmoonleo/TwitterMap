package cse.ust.twittermap.dialog;

import twitter4j.Status;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import cse.ust.twittermap.asynctasks.ReplyToTwitter;

public class ReplyTweetDialog extends AbstractTwitterDialog {
	
	private Fragment fragment = null;
	private Status status = null;
	
	public ReplyTweetDialog(Context context, Fragment fragment, Status status) {
		super(context, null);
		this.fragment = fragment;
		this.status = status;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.title.setText("reply to @"+ status.getUser().getName());
		this.tweetEdit.setText("@" + status.getUser().getName());
		this.tweetEdit.setSelection(tweetEdit.getText().length());
	}
	
	@Override
	public void onClick(View v) {
		System.out.println("debug");
		
		if (v == this.positiveBtn) {
			String replyText = this.tweetEdit.getText().toString();
			final Context context = this.getContext();
			new ReplyToTwitter(context, fragment, status).execute(replyText);
			this.dismiss();
		}
		else if (v == this.negativeBtn) {
			this.dismiss();
		}
	}
}
