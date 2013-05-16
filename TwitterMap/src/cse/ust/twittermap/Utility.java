package cse.ust.twittermap;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.User;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public final class Utility {

	static public View getMessageCellView(int pos, List<DirectMessage> messages, LayoutInflater mInflater){
		View reUse;
		DirectMessage message = messages.get(pos);
		User sender = message.getSender();
		reUse = mInflater.inflate(R.layout.list_item_direct_message, null);
		TextView dm_send_time = (TextView) reUse.findViewById(R.id.dm_send_time);
		TextView dm_text = (TextView) reUse.findViewById(R.id.dm_text);
		ImageView sender_avatar = (ImageView) reUse.findViewById(R.id.sender_avatar);
		dm_send_time.setText(getTimeStamp(message));
		dm_text.setText(message.getText());
		sender_avatar.setImageBitmap(BitmapCache.getUserImageFromMemCache(sender));
		//setUserImageWithCache(sender, sender_avatar, null);
		
		return reUse;
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            //listItem.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

	static public String getTimeStamp(Status us) {
		String t;

		Date GMUTime = us.getCreatedAt();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
		sdf.setTimeZone(Calendar.getInstance().getTimeZone());
		String localTime = sdf.format(GMUTime);

		int index1 = localTime.indexOf('-');
		int index2 = localTime.indexOf('-', index1 + 1);
		int index3 = localTime.indexOf(' ', index2 + 1);
		int index4 = localTime.indexOf(':', index3 + 1);
		String month = localTime.substring(index1 + 1, index2);
		String day = localTime.substring(index2 + 1, index3);
		String hour = localTime.substring(index3 + 1, index4);
		String minute = localTime.substring(index4 + 1);		

		if (Calendar.getInstance().get(Calendar.MONTH) + 1 == Integer.parseInt(month) 
				&& Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == Integer.parseInt(day)) {
			t = hour + ": " + minute;
		}
		else {
			if (month.equals("01")) month = "Jan";
			else if (month.equals("02")) month = "Feb";
			else if (month.equals("03")) month = "Mar";
			else if (month.equals("04")) month = "Apr";
			else if (month.equals("05")) month = "May";
			else if (month.equals("06")) month = "Jun";
			else if (month.equals("07")) month = "Jul";
			else if (month.equals("08")) month = "Aug";
			else if (month.equals("09")) month = "Sep";
			else if (month.equals("10")) month = "Oct";
			else if (month.equals("11")) month = "Nov";
			else if (month.equals("12")) month = "Dec";
			t = month + " " + day;
		}
		
		return t;
	}
	
	static public String getTimeStamp(DirectMessage message) {
		String t;

		Date GMUTime = message.getCreatedAt();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
		sdf.setTimeZone(Calendar.getInstance().getTimeZone());
		String localTime = sdf.format(GMUTime);

		int index1 = localTime.indexOf('-');
		int index2 = localTime.indexOf('-', index1 + 1);
		int index3 = localTime.indexOf(' ', index2 + 1);
		int index4 = localTime.indexOf(':', index3 + 1);
		String month = localTime.substring(index1 + 1, index2);
		String day = localTime.substring(index2 + 1, index3);
		String hour = localTime.substring(index3 + 1, index4);
		String minute = localTime.substring(index4 + 1);		

		if (Calendar.getInstance().get(Calendar.MONTH) + 1 == Integer.parseInt(month) 
				&& Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == Integer.parseInt(day)) {
			t = hour + ": " + minute;
		}
		else {
			if (month.equals("01")) month = "Jan";
			else if (month.equals("02")) month = "Feb";
			else if (month.equals("03")) month = "Mar";
			else if (month.equals("04")) month = "Apr";
			else if (month.equals("05")) month = "May";
			else if (month.equals("06")) month = "Jun";
			else if (month.equals("07")) month = "Jul";
			else if (month.equals("08")) month = "Aug";
			else if (month.equals("09")) month = "Sep";
			else if (month.equals("10")) month = "Oct";
			else if (month.equals("11")) month = "Nov";
			else if (month.equals("12")) month = "Dec";
			t = month + " " + day;
		}
		
		return t;
	}
}
