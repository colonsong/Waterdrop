package tw.waterdrop.waterdrop.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import tw.waterdrop.waterdrop.fragment.BlogFragment;

public class ImageDownloader extends
        AsyncTask<HashMap<String, Object>, Void, HashMap<String, Object>> {
    private final String TAG = "ImageDownload";

    @Override
    protected HashMap<String, Object> doInBackground(
            HashMap<String, Object>... hm) {
        // TODO Auto-generated method stub
        String imgUrl = (String) hm[0].get("image");
        int position = (Integer) hm[0].get("position");

        Log.i(TAG, imgUrl);

        Bitmap bitmap = null;
        try {
            byte[] imageData = getImageFromUrl(imgUrl);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(
                    imageData);

            // Temporary file to store the downloaded image
            //Log.i(TAG, MainActivity.cacheDirectory.getPath());
            //File tmpFile = new File(MainActivity.cacheDirectory.getPath()
            //		+ "/wpta_" + position + ".png");

            // The FileOutputStream to the temporary file
            //FileOutputStream fOutStream = new FileOutputStream(tmpFile);

            bitmap = BitmapFactory.decodeStream(inputStream);

            BlogFragment.imageLoaded.put(position, "1");

            // Writing the bitmap to the temporary file as png file
            //bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOutStream);

            // Flush the FileOutputStream
            //fOutStream.flush();

            // Close the FileOutputStream
            //fOutStream.close();

            // Create a hashmap object to store image path and its position in
            // the listview
            HashMap<String, Object> hmBitmap = new HashMap<String, Object>();

            // Storing the path to the temporary image file
            //hmBitmap.put("flag", tmpFile.getPath());
            hmBitmap.put("bitmap", bitmap);

            // Storing the position of the image in the listview
            hmBitmap.put("position", position);

            return hmBitmap;

        } catch (Exception e) {
            Log.d(TAG, "Exception:" + imgUrl);
            e.printStackTrace();
        }
        return null;
        // Returning the HashMap object containing the image path and position

    }

    @Override
    protected void onPostExecute(HashMap<String, Object> result) {
        // TODO Auto-generated method stub
        // Getting the path to the downloaded image
        //SimpleAdapter adapter = (SimpleAdapter) MainActivity.mListView.getAdapter();


        if (result != null) {
            BaseAdapter adapter = (BaseAdapter) BlogFragment.mListView.getAdapter();

            //String path = (String) result.get("flag");
            int position = (Integer) result.get("position");


            // Getting the position of the downloaded image

            Log.i(TAG, "image onpause" + position);


            HashMap<String, Object> hm = (HashMap<String, Object>) adapter.getItem(position);


            //hm.put("image_blank", path);
            hm.put("bitmap", (Bitmap) result.get("bitmap"));

            adapter.notifyDataSetChanged();
        }


    }

    public byte[] getImageFromUrl(String url) throws Exception {
        Log.i(TAG, "getImageFromUrl");
        InputStream in = null;
        byte[] byteimage = null;
        try {
            URL imageUrl = new URL(url);
            URLConnection conn = imageUrl.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            int response = httpConn.getResponseCode();
            if (response != HttpURLConnection.HTTP_OK) {
                throw new Exception("Http error");
            }
            in = httpConn.getInputStream();
            int nRead;
            byte[] data = new byte[10000];

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            while ((nRead = in.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
            byteimage = buffer.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteimage;
    }

}
