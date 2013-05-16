package cse.ust.twittermap;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import twitter4j.User;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;

public class BitmapCache {

	private static LruCache<String, Bitmap> mMemoryCache = null;
	
	static private void checkCache() {
		if (mMemoryCache == null) {
			final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
			final int cacheSize = maxMemory / 8;
			mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
				@SuppressLint("NewApi")
				@Override
				protected int sizeOf(String key, Bitmap bitmap) {
					return bitmap.getByteCount() / 1024;
				}
			};
		}
	}
	
	static public Bitmap getUserImageFromMemCache(User user) {
		checkCache();
		
		String key = user.toString();
		Bitmap result = mMemoryCache.get(key);
		if (result == null) {	
			try {
				URL profileImageUrl = new URL(user.getProfileImageURL());
				Bitmap d = getBitmapFromUrl(profileImageUrl);
				result = Bitmap.createScaledBitmap(d, 36, 36, false);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
		
		return result;
	}
	
	static public Bitmap getBitmapFromUrl(URL url) throws IOException {
		Bitmap result = mMemoryCache.get(url.toString());
		if (result == null) {
			InputStream content= (InputStream) url.getContent();
			Drawable profile = Drawable.createFromStream(content, null);
			result = ((BitmapDrawable) profile).getBitmap();
			mMemoryCache.put(url.toString(), result);
		}
		return result;
	}
}
