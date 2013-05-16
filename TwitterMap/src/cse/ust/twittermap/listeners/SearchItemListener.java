package cse.ust.twittermap.listeners;

import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import cse.ust.twittermap.R;

public class SearchItemListener implements OnActionExpandListener {

	private MenuItem postItem = null;
	
	public void setPostItem(Menu menu) {
		postItem = menu.findItem(R.id.menu_post);
	}
	
	@Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
		postItem.setVisible(true);
		return true;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
    	postItem.setVisible(false);
    	return true;
    }

}
