package tw.waterdrop.waterdrop.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tw.waterdrop.waterdrop.R;
import tw.waterdrop.waterdrop.fragment.AlbumFragment;



public class AlbumPic extends Activity {
    private ImageView albumPicView;
    protected static HashMap<Integer, String> albumPicHash = new HashMap<Integer, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.album_pic);
        albumPicView = (ImageView) findViewById(R.id.album_pic_view);


        Bundle bundle = this.getIntent().getExtras();
        String pic = bundle.getString("pic");
        Integer position = Integer.valueOf((String) bundle.get("position"));
        if (albumPicHash.get(position) != null) {
            Bitmap myBitmap = BitmapFactory.decodeFile(albumPicHash.get(position));


            albumPicView.setImageBitmap(myBitmap);
            return;
        }
        Map<String, Object> hm = AlbumFragment.albumList.get(position);


        albumPicView.setImageBitmap((Bitmap) hm.get("bitmap"));

        String patternStr = "(http://.*?staticflickr.*?)_.\\.jpg";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(pic);

        while (matcher.find()) {

            pic = matcher.group(1) + "_b.jpg";

            break;
        }


        PicImageDownloader imageLoader = new PicImageDownloader();
        HashMap<String, Object> hmDownload = new HashMap<String, Object>();
        hmDownload.put("image", pic);
        hmDownload.put("position", position);
        imageLoader.execute(hmDownload);

    }

    public class PicImageDownloader extends
            AsyncTask<HashMap<String, Object>, Void, HashMap<String, Object>> {
        private final String TAG = "AlbumPicImageDownload";

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
                Log.i(TAG, MainActivity.cacheDirectory.getPath());
                File tmpFile = new File(MainActivity.cacheDirectory.getPath()
                        + "/albumPic_" + position + ".png");

                // The FileOutputStream to the temporary file
                FileOutputStream fOutStream = new FileOutputStream(tmpFile);

                bitmap = BitmapFactory.decodeStream(inputStream);

                // Writing the bitmap to the temporary file as png file
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOutStream);

                // Flush the FileOutputStream
                fOutStream.flush();

                // Close the FileOutputStream
                fOutStream.close();

                // Create a hashmap object to store image path and its position
                // in
                // the listview
                HashMap<String, Object> hmBitmap = new HashMap<String, Object>();

                // Storing the path to the temporary image file
                hmBitmap.put("flag", tmpFile.getPath());

                // Storing the position of the image in the listview
                hmBitmap.put("position", position);

                return hmBitmap;

            } catch (Exception e) {
                Log.d(TAG, "Exception:" + imgUrl);
                e.printStackTrace();
            }
            return null;
            // Returning the HashMap object containing the image path and
            // position

        }

        @Override
        protected void onPostExecute(HashMap<String, Object> result) {
            // TODO Auto-generated method stub
            // Getting the path to the downloaded image
            // SimpleAdapter adapter = (SimpleAdapter)
            // MainActivity.mListView.getAdapter();

            if (result != null) {

                String path = (String) result.get("flag");


                albumPicHash.put((Integer) result.get("position"), path);
                Bitmap myBitmap = BitmapFactory.decodeFile(path);


                albumPicView.setImageBitmap(myBitmap);
                // Noticing listview about the dataset changes

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

}
