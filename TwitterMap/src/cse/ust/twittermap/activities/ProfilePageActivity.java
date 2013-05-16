package cse.ust.twittermap.activities;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import cse.ust.twittermap.R;
import cse.ust.twittermap.fragments.ProfilePageFragment;

@SuppressLint("NewApi")
public class ProfilePageActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ProfilePageFragment newFragment = new ProfilePageFragment(getIntent().getStringExtra("screenName"), this);
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.content_fragment, newFragment);
		transaction.commit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		return true;
	}
}
