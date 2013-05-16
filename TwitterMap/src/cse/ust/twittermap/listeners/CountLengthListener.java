/**
 * 
 */
package cse.ust.twittermap.listeners;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;
import cse.ust.twittermap.R;

/**
 * @author yzhangad
 * The listener used to count the remaining characters and display them.
 * Plus, it is also responsible for disabling and enabling the button
 * if the character count exceeds maximum or is within maximum.
 */
public class CountLengthListener implements TextWatcher {

	private TextView remainCountLabel;
	private TextView editText;
	private Button postBtn;
	public static final int MAXCHARNUM = 140;
	
	public CountLengthListener(TextView editText, TextView remainCountLabel, Button postBtn) {
		this.editText = editText;
		this.remainCountLabel = remainCountLabel;
		this.postBtn = postBtn;
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		int remainCharNum = MAXCHARNUM - editText.getText().toString().length();
		String prefix = remainCountLabel.getContext().getString(R.string.remaining_count);
		String remainCharMsg = prefix + remainCharNum;
		remainCountLabel.setText(remainCharMsg);
		if (remainCharNum <= 0) {
			remainCountLabel.setTextColor(Color.RED);
			postBtn.setEnabled(false);
		}
		else {
			remainCountLabel.setTextColor(Color.BLUE);
			postBtn.setEnabled(true);
		}
	}
}
