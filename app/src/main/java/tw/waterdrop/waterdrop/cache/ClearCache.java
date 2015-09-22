package tw.waterdrop.waterdrop.cache;
import android.content.Context;
import android.util.Log;

import java.io.File;

public class ClearCache {
	public static void trimCache(Context context) {
	    try {
	       File dir = context.getCacheDir();
	       if (dir != null && dir.isDirectory()) {
	    	   Log.i("MainActivity",dir.toString());
	          deleteDir(dir);
	       }
	    } catch (Exception e) {
	       // TODO: handle exception
	    }
	 }

	 public static boolean deleteDir(File dir) {
	    if (dir != null && dir.isDirectory()) {
	       String[] children = dir.list();
	       for (int i = 0; i < children.length; i++) {
	          boolean success = deleteDir(new File(dir, children[i]));
	          if (!success) {
	             return false;
	          }
	       }
	    }

	    // The directory is now empty so delete it
	    return dir.delete();
	 }
}
