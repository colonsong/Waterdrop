package tw.waterdrop.waterdrop.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import tw.waterdrop.waterdrop.fragment.AlbumFragment;

public class AlbumImageDownloader extends
        AsyncTask<HashMap<String, Object>, Void, HashMap<String, Object>> {
    private final String TAG = "AlbumImageDownload";

    @Override
    protected HashMap<String, Object> doInBackground(
            HashMap<String, Object>... hm) {
        // TODO Auto-generated method stub
        String imgUrl = (String) hm[0].get("image");
        Integer position = (Integer) hm[0].get("position");

        Log.i(TAG, imgUrl);

        Bitmap bitmap = null;
        try {
           // bitmap imageData = getImageFromUrl(imgUrl);
            //ByteArrayInputStream inputStream = new ByteArrayInputStream(
              //      imageData);

            // Temporary file to store the downloaded image

            //File tmpFile = new File(MainActivity.cacheDirectory.getPath()
            //		+ "/album_" + position + ".png");

            // The FileOutputStream to the temporary file
            //FileOutputStream fOutStream = new FileOutputStream(tmpFile);

            bitmap = getImageFromUrl(imgUrl);
            AlbumFragment.imageLoaded.put(position, "1");

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
            hmBitmap.put("bitmap2", bitmap);


            // Storing the position of the image in the listview
            hmBitmap.put("position", position);
            hmBitmap.put("image_url", imgUrl);

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


            //ImageView imageVIew = (ImageView) AlbumFragment.album_grid.findViewWithTag(result.get("image_url"));


            //if(imageVIew != null &&  result.get("bitmap2") != null) {
              //  imageVIew.setImageBitmap((Bitmap) result.get("bitmap2"));
            //}
           // BaseAdapter adapter = (BaseAdapter) AlbumFragment.album_grid.getAdapter();
            String path = (String) result.get("flag");
            int position = (Integer) result.get("position");

            // Getting the position of the downloaded image


            //Log.i(TAG, "image onpause" + position);

            // Getting adapter of the listview

            // Getting the hashmap object at the specified position of the
            // listview
            HashMap<String, Object> hm = (HashMap<String, Object>) AlbumFragment.baseAdapter
                    .getItem(position);



            // Overwriting the existing path in the adapter
            hm.put("pic_blank", path);
            hm.put("bitmap", result.get("bitmap2"));

            // Noticing listview about the dataset changes
            AlbumFragment.baseAdapter.notifyDataSetChanged();
        }


    }

    public Bitmap getImageFromUrl(String imageUrl) throws Exception {
        Log.i(TAG, "getImageFromUrl");
        Bitmap bitmap = null;
        //InputStream in = null;
        HttpURLConnection conn = null;
       // byte[] byteimage = null;
        try {
            URL url = new URL(imageUrl);
            conn = (HttpURLConnection) url.openConnection();
           // conn.setAllowUserInteraction(false);
            //conn.setInstanceFollowRedirects(true);
            //conn.setRequestMethod("GET");
            conn.setConnectTimeout(5*1000);
            conn.setReadTimeout(10*1000);
            //conn.connect();

            int response = conn.getResponseCode();
            if (response != HttpURLConnection.HTTP_OK) {
                throw new Exception("Http error");
            }
            bitmap = BitmapFactory.decodeStream(conn.getInputStream());

            /*
            in = conn.getInputStream();
            int nRead;
            byte[] data = new byte[1000];

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            while ((nRead = in.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
            byteimage = buffer.toByteArray();
            */

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(conn != null)
            {
                conn.disconnect();
            }
        }
        return bitmap;

    }

}
