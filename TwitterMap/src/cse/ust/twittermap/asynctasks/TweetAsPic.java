package cse.ust.twittermap.asynctasks;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import cse.ust.twittermap.R;
import cse.ust.twittermap.TwitterFactory;
import cse.ust.twittermap.fragments.HomeFragment;

@SuppressLint("NewApi")
public class TweetAsPic extends AsyncTask<String, Void, String> {
		
	private ProgressDialog postDialog = null;
	private Context context = null;
	private Fragment fragment = null;
		
	public TweetAsPic(Context context, Fragment fragment){
		this.context = context;
		this.fragment = fragment;
	}
		
	@Override
	protected void onPreExecute() {
		postDialog = ProgressDialog.show(context, 
				"Status", "Updating ...", true,	false);
	}
		
	@Override
	protected String doInBackground(String... statuses) {
		try {
			String message = statuses[0];
			ArrayList<String> words = new ArrayList<String>();
			ArrayList<String> lines = new ArrayList<String>();
			lines.add("");
			words.addAll(stringParse(message));
			//words.addAll(stringParse(message));
			int index = 0;
			while(!words.isEmpty()){
				String word = words.remove(0);
				if(word.equals("\n"))
				{
					lines.add("");
					index++;
					continue;
				}		
				//System.out.println(word);
				if(word.length()<50){
					String line = lines.get(index);
					if(getLength(line)+getLength(word)<=50){
						System.out.println("ifif");
						lines.set(index, line+word);
					}
						
					else{
						System.out.println("ifelse");
						lines.add("");
						words.add(0,word);
						index++;
					}
				}
				else{
					System.out.println("else");
					if(!lines.get(index).equals(""))
					{
						lines.add("");
						index++;
					}
					lines.set(index, word.substring(0,50));
					word = word.substring(50);
					words.add(0,word);
					lines.add("");
					index++;
				}
			}
			
			for(String line : lines){
				System.out.println(line);
			}
			
			int line_num = lines.size();
			
			Bitmap bmp = Bitmap.createBitmap(470, line_num*15+20, Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(bmp);
			Paint myPaint = new Paint();
			
			// draw background
			myPaint.setColor(Color.rgb(235, 235, 255));
			c.drawRect(0, 0, bmp.getWidth(), bmp.getHeight(), myPaint);
			
			// draw text
			Typeface tf = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);
			myPaint.setTypeface(tf);
			
			myPaint.setTextSize(15);
			myPaint.setColor(Color.BLACK);
			
			for(int i=0;i<lines.size();i++){
				c.drawText(lines.get(i), 10, 10+(i+1)*15, myPaint);
			}	
			
			try {
				File f = new File(context.getCacheDir(), "img.png");
				f.createNewFile();

				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bmp.compress(Bitmap.CompressFormat.PNG, 0, bos);
				byte[] bitmapdata = bos.toByteArray();

				FileOutputStream fos = new FileOutputStream(f);
				fos.write(bitmapdata);
				fos.close();
			
			    try{
			        StatusUpdate statusUpdate = new StatusUpdate("I've said that: ");
			        statusUpdate.setMedia(f);
			        TwitterFactory.get4jInstance().updateStatus(statusUpdate);
			        }
			    catch(TwitterException e){
			        Log.d("TAG", "Pic Upload error: " + e.getErrorMessage());
			    }					
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Thread.sleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
			return context.getString(R.string.fail_tweet);
		} 
		return context.getString(R.string.success_tweet);
	}	

	@Override
	protected void onPostExecute(String result) {		
		Toast.makeText(context, result, 
				Toast.LENGTH_SHORT).show();
		postDialog.dismiss();
		if (fragment instanceof HomeFragment) {
			((HomeFragment) fragment).refreshCompletelyUsingNetwork();
		}
	}
	
	public void uploadPic(File file, String message,Twitter twitter) throws Exception  {
	    try{
	        StatusUpdate status = new StatusUpdate(message);
	        status.setMedia(file);
	        twitter.updateStatus(status);}
	    catch(TwitterException e){
	        Log.d("TAG", "Pic Upload error" + e.getErrorMessage());
	        throw e;
	    }
	}
	
	private static Boolean charValid(char a){
		if(((int) a >= (int) '0' && (int) a <= (int) '9') 
				|| ((int) a >= (int) 'a' && (int) a <= (int) 'z')
				|| ((int) a >= (int) 'A' && (int) a <= (int) 'Z')){
			return true;
		}
		else{
			return false;
		}
	}
	
	@SuppressLint("UseValueOf")
	private static int getLength(String s){
		double result = 0;
		for(int i=0;i<s.length();i++){
			if(new Integer(s.charAt(i)) > 127)
				result += 1.666666;
			else
				result += 1;
		}
		return (int) Math.ceil(result);
	}
	
	@SuppressLint("DefaultLocale")
	private static List<String> stringParse(String original){
		List<String> result = new ArrayList<String>();
		int index = 0;
		String newString;
		for(int i=0;i<original.length();)
		{
			if(original.charAt(i) == '\n')
			{
				newString = "\n";
				result.add(newString);
				index = i+1;
				i++;
			}
			//System.out.println(i + " " + new Integer(original.charAt(i)));
			else if(i >= original.length() - 1 || original.charAt(i+1) > 127 || original.charAt(i+1) == '\n' || charValid(original.charAt(i+1)) && original.charAt(i) <= 127 && !charValid(original.charAt(i)))
			{
				if(i == original.length())
					newString = original.substring(index, i);
				else
					newString = original.substring(index, i+1);
				index = i+1;
				//newString = newString.trim();
				if(!newString.equals(""))
					result.add(newString);
				i++;
			}
			else if(Integer.valueOf(original.charAt(i)) > 127)
			{
				//System.out.println("t");
				newString = original.substring(index, i);
				//newString = newString.trim();
				if(!newString.equals(""))
					result.add(newString);
				newString = original.substring(i, i+1);
				//newString = newString.trim();
				if(!newString.equals(""))
					result.add(newString);
				index = i+1;
				i+=1;
			}
			else
			{
				i++;
			}
			
		}
		return result;
	}
}