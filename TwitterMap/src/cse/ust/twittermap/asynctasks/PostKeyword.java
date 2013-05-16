package cse.ust.twittermap.asynctasks;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import twitter4j.Paging;
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
import cse.ust.twittermap.WordBlock;
import cse.ust.twittermap.fragments.HomeFragment;

@SuppressLint("NewApi")
public class PostKeyword extends AsyncTask<String, Void, String> {
		
	private ProgressDialog postDialog = null;
	private Context context = null;
	private Fragment fragment = null;
		
	public PostKeyword(Context context, Fragment fragment){
		this.context = context;
		this.fragment = fragment;
	}
		
	@Override
	protected void onPreExecute() {
		postDialog = ProgressDialog.show(context, 
				"Status", "Updating ...", true,	false);
	}
		
	@SuppressWarnings({ "unchecked", "rawtypes"})
	@Override
	protected String doInBackground(String... message) {
		try {
			Paging paging = new Paging(1,10000);
			try {
				List<twitter4j.Status> homeTimeLine = TwitterFactory.get4jInstance().getHomeTimeline(paging);
				List<String> words = new ArrayList<String>();
				for(twitter4j.Status s : homeTimeLine){
					words.addAll(stringParse(s.getText()));
				}
				Map<String, Integer> countMap = new HashMap<String, Integer>();
				for(String temp : words){
					if(!countMap.containsKey(temp)){
						countMap.put(temp, 1);
					}
					else{
						Integer value = countMap.get(temp)+1;
						countMap.put(temp, value);
					}
				}

				List sortList = new ArrayList(countMap.entrySet());
				
				Collections.sort(sortList,new Comparator(){  
		            @Override  
		            public int compare(Object o1, Object o2) {  
		                 Map.Entry obj1 = (Map.Entry) o1;  
		                 Map.Entry obj2 = (Map.Entry) o2;  
		                 if((Integer)obj1.getValue()==(Integer)obj2.getValue()){  
		                     return 0;  
		                 }else{  
		                     return -((Integer)obj1.getValue()-(Integer)obj2.getValue());  
		                 }  
		                   
		            }  
		        }); 
				
				Bitmap bmp = Bitmap.createBitmap(370, 370, Bitmap.Config.ARGB_8888);
				Canvas c = new Canvas(bmp);
				Paint myPaint = new Paint();
				
				// draw background
				myPaint.setColor(Color.rgb(235, 235, 255));
				c.drawRect(0, 0, bmp.getWidth(), bmp.getHeight(), myPaint);
				
				// draw text
				Typeface tf = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);
				myPaint.setTypeface(tf);
				
				
				ArrayList<WordBlock> wordBlockList = new ArrayList<WordBlock>();
				int fontSize = 35;
				
				int[] size   = {45,30,25,20,10};
				int[] number = {2,2,7,8,21};

				
				int index = 0;
				
				for(int i = 0;i<40;i++){
					
					String word = ((Map.Entry) sortList.get(i)).getKey().toString();
					Random r = new Random();
					
					int x = -1;
					int y = -1;
					
					WordBlock wordBlock = null;
					
													
					while(index<size.length){
						
						if(number[index] <= 0){
							index++;
							continue;
						}
						fontSize = size[index];
						int count = 0;
						x = r.nextInt(360-(word.length()*fontSize*3)/5);
						y = r.nextInt(360-fontSize)+fontSize;
						wordBlock = new WordBlock(word,fontSize,x,y);
						Boolean success = true;
						while(wordBlock.overlap(wordBlockList)){
							count++;
							x = r.nextInt(360-(word.length()*fontSize*3)/5);
							y = r.nextInt(360-fontSize)+fontSize;
							wordBlock = new WordBlock(word,fontSize,x,y);
							
							System.out.println("count: "+count);
							if(count == 30){
								x = -1;
								y = -1;
								success = false;
								break;
							}
						}
						number[index]--;
						if(success == true){
							break;
						}
					}
					
					if(x!=-1 || y!=-1){
						wordBlockList.add(wordBlock);
						
						myPaint.setTextSize(fontSize);
						
						int red = r.nextInt(100)+55;
						int green = r.nextInt(100)+55;
						int blue = r.nextInt(100)+55;
						myPaint.setColor(Color.rgb(red, green, blue));
						
						c.drawText(word, 5+x, 5+y, myPaint);
					}		
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
				        StatusUpdate statusUpdate = new StatusUpdate("My keywords: ");
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
				
			} catch (TwitterException e) {
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
	
	@SuppressLint("DefaultLocale")
	private static List<String> stringParse(String original){
		List<String> result = new ArrayList<String>();
		String item = "";
		for(int i=0;i<original.length();i++){
			if(charValid(original.charAt(i))){
				item += original.substring(i, i+1);		
			}
			else if(item.compareTo("")!=0){
				item = item.toUpperCase();
				if(item.length()>=5)
					result.add(item);
				item = "";
			}
		}
		if(item.compareTo("")!=0){
			item = item.toUpperCase();
			if(item.length()>=5)
				result.add(item);
			item = "";
			}
		return result;
	}
}