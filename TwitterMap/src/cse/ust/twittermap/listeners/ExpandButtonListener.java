package cse.ust.twittermap.listeners;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cse.ust.twittermap.BitmapCache;
import cse.ust.twittermap.R;

public class ExpandButtonListener implements OnClickListener {

	private ImageButton expandButton = null;
	private LinearLayout photoLayout = null;
	private Vector<String> mediaURLs = null;
	private Activity parentActivity = null;
	private Vector<Boolean> expanded = null;
	
	public ExpandButtonListener(ImageButton expandButton, LinearLayout photoLayout, Vector<String> mediaURLs,
									Activity parentActivity, Vector<Boolean> expanded) {
		this.expandButton = expandButton;
		this.photoLayout = photoLayout;
		this.mediaURLs = mediaURLs;
		this.parentActivity = parentActivity;
		this.expanded = expanded;
		if (expanded.get(0)) {
			expandCellWithPhoto();
			expandButton.setImageResource(R.drawable.arrow_down);
		}
		else {
			photoLayout.removeAllViews();
			expandButton.setImageResource(R.drawable.arrow);
		}
	}

	@Override
	public void onClick(View v) {
		if (expanded.get(0)) {
			expanded.set(0, Boolean.valueOf(false));
			photoLayout.removeAllViews();
			expandButton.setImageResource(R.drawable.arrow);
		}
		else {
			expanded.set(0, Boolean.valueOf(true));
			expandCellWithPhoto();
			expandButton.setImageResource(R.drawable.arrow_down);
		}
	}
	
	private void expandCellWithPhoto() {
		try {
			for (int i = 0; i < mediaURLs.size(); i++) {
				Bitmap b = BitmapCache.getBitmapFromUrl(new URL(mediaURLs.get(i)));
				ImageView img = new ImageView(parentActivity);
				img.setImageBitmap(b);
				photoLayout.addView(img);
			}					
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
