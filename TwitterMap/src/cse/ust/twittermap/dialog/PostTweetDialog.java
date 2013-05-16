package cse.ust.twittermap.dialog;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import cse.ust.twittermap.asynctasks.PostKeyword;
import cse.ust.twittermap.asynctasks.PostToTwitter;
import cse.ust.twittermap.asynctasks.TweetAsPic;

@SuppressLint("DefaultLocale")
public class PostTweetDialog extends AbstractTwitterDialog implements OnClickListener {
	
	private Fragment fragment = null;
	private PostToTwitter postTweetTask = null;
	private PostKeyword postKeywordTask = null;
	private TweetAsPic tweetAsPicTask = null;
	
	public PostTweetDialog(Context context, String directTweetUserScreenName, Fragment fragment) {
		super(context, directTweetUserScreenName);
		this.fragment = fragment;
	}

	@Override
	public void onClick(View v) {
		final Context context = this.getContext();
		if (v == this.positiveBtn) {
			String status = this.tweetEdit.getText().toString();
			postTweetTask = new PostToTwitter(context, fragment, this.locationBtn.isChecked());
			postTweetTask.execute(status);
			this.dismiss();	
		}
		
		else if(v == this.keywordBtn) {
			postKeywordTask = new PostKeyword(context, fragment);
			postKeywordTask.execute();
			this.dismiss();	
		}
		
		else if(v == this.picBtn) {
			String status = this.tweetEdit.getText().toString();
			tweetAsPicTask = new TweetAsPic(context, fragment);
			tweetAsPicTask.execute(status);
			this.dismiss();	
		}

		else if (v == this.negativeBtn) {
			this.dismiss();
		}
	}
}
