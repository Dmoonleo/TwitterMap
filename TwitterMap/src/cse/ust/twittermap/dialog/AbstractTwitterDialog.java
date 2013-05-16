package cse.ust.twittermap.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import cse.ust.twittermap.R;
import cse.ust.twittermap.listeners.CountLengthListener;

public abstract class AbstractTwitterDialog extends Dialog implements 
		OnClickListener {
	
	protected TextView remainCountText;
	protected EditText tweetEdit;
	protected Button positiveBtn;
	protected Button negativeBtn;
	protected Button keywordBtn;
	protected Button picBtn;
	protected ToggleButton locationBtn;
	protected ImageView mediaImage;
	protected TextView title;
	
	private String directTweetUserScreenName;
	
	public AbstractTwitterDialog(Context context, String directTweetUserScreenName) {
		super(context);
		this.directTweetUserScreenName = directTweetUserScreenName;
	}
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.tweet_dialog);
		title = (TextView) findViewById(R.id.tweet_dialog_title);
		remainCountText = (TextView)findViewById(R.id.remainingCount);
		tweetEdit = (EditText)findViewById(R.id.tweetEdit);
		if (this.directTweetUserScreenName != null) {
			//in this case the user should be directly tweeting to a user.
			tweetEdit.setText("@" + this.directTweetUserScreenName);
		}
		positiveBtn = (Button)findViewById(R.id.dialog_positive_btn);
		negativeBtn = (Button)findViewById(R.id.dialog_negative_btn);
		locationBtn = (ToggleButton)findViewById(R.id.location_btn);
		keywordBtn = (Button) findViewById(R.id.post_keyword_button);		
		picBtn = (Button) findViewById(R.id.post_as_pic_button);		
		locationBtn.setChecked(false);
		TextWatcher watcher = new CountLengthListener(tweetEdit, remainCountText, positiveBtn);
		tweetEdit.addTextChangedListener(watcher);
		negativeBtn.setOnClickListener(this);
		positiveBtn.setOnClickListener(this);
		keywordBtn.setOnClickListener(this);
		picBtn.setOnClickListener(this);
		super.onCreate(savedInstanceState);
	}
}
