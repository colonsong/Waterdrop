package tw.waterdrop.waterdrop.cache;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class BitmapHelper
{
	public static boolean saveBitmap(File file, Bitmap bitmap){
	
	    if(file == null || bitmap == null)
	
	        return false;
	
	    try {
	
	        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
	
	        return bitmap.compress(CompressFormat.JPEG, 100, out);
	
	    } catch (FileNotFoundException e) {
	
	        e.printStackTrace();
	
	        return false;
	
	    }
	
	}
}
