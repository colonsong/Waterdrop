package tw.waterdrop.waterdrop.xmlparser;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;

import tw.waterdrop.waterdrop.R;
import tw.waterdrop.waterdrop.fragment.BlogFragment;


public class HandlingXMLStuff extends DefaultHandler {
    private String TAG = "HandlingXMLStuff";
    private String currentElement = null;
    private HashMap<String, Object> mp;
    public tw.waterdrop.waterdrop.xmlparser.XMLDataCollected info;
    private int c = 0;
    private String myLog = "HandlingXMLStuff";
    private final String dateTitle = "滴一滴水美食部落格";

    private boolean is_main_cover = false;

    @Override
    public void startDocument() throws SAXException {
        // TODO Auto-generated method stub
        super.startDocument();
        info = new tw.waterdrop.waterdrop.xmlparser.XMLDataCollected();
        Log.v(myLog, "Start");
    }

    @Override
    public void endDocument() throws SAXException {
        // TODO Auto-generated method stub
        super.endDocument();
        // System.out.print(mp);
        Log.v(myLog, "END");

    }

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        Log.v("Colon startElement", localName);
        this.currentElement = localName;
        // TODO Auto-generated method stub
        if (this.currentElement.equals("item")) {
            // 代表可以開始抓了 資料都放在item裡
            info.isItem = true;
            this.mp = new HashMap<String, Object>();
            this.is_main_cover = true;
            BlogFragment.rssDialog.setProgress(c++);

        }

    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        // TODO Auto-generated method stub

        String value = new String(ch, start, length);

        value = value.trim();
        Log.v("Colon characters", value);
        if (value.length() == 0)

        {

            return; // ignore white space

        }

        // 沒進item代表還沒要抓
        if (info.isItem == false) {
            return;
        }
        if (this.currentElement.equals("title")) {
            Log.w("title", value);
            info.title += value;
        } else if (this.currentElement.equals("pubDate")) {

            info.publishDate += value;
        } else if (this.currentElement.equals("description")) {

            Log.d("description", value);
            mp.put("image_blank", R.drawable.blank);
            // Log.w("description",""+ info.firstImageUrl.length());
            // Log.w("description",""+
            // value.indexOf("http://waterdrop.tw/useralbum/waterdrop/"));
            if (info.firstImageUrl.length() == 0
                    && value.indexOf("staticflickr.com") != -1) {
                String base_img = value.toString().substring(29,
                        value.toString().length() - 15);

                if (this.is_main_cover) {

                    mp.put("image", base_img + "q.jpg");
                    this.is_main_cover = false;
                }

				/*
                 * try { //MainActivity.myDialog.setProgress(c++); value =
				 * value.toString().substring(0, value.toString().length() - 5)
				 * + "n.jpg";
				 * 
				 * URL url = new URL(value); URLConnection conn =
				 * url.openConnection();
				 * 
				 * HttpURLConnection httpConn = (HttpURLConnection)conn;
				 * httpConn.setRequestMethod("GET"); httpConn.connect();
				 * 
				 * if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK)
				 * { InputStream inputStream = httpConn.getInputStream();
				 * 
				 * Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				 * inputStream.close(); info.firstImageUrl = value;
				 * mp.put("image", bitmap); } } catch (MalformedURLException e1)
				 * { // TODO Auto-generated catch block e1.printStackTrace(); }
				 * catch (IOException e) { // TODO Auto-generated catch block
				 * e.printStackTrace();
				 * 
				 * }
				 */
            }

            info.description += value;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        Log.v("Colon endElement", localName);

        if (mp != null) {
            if (localName.equals("title")) {
                mp.put("title", info.title);
                info.title = "";
            }
            if (localName.equals("description")) {
                mp.put("description", info.description);
                mp.put("descriptonNoHtml",
                        info.description.replaceAll("(<.*?>)", ""));
                info.description = "";
            }
            if (localName.equals("pubDate")) {
				/*
				 * SimpleDateFormat formatter = new
				 * SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz"); try { Date
				 * date = (Date)
				 * formatter.parse("Sat, 24 Apr 2010 14:01:00 GMT"); } catch
				 * (ParseException e) { // TODO Auto-generated catch block
				 * e.printStackTrace(); }
				 */
                mp.put("publishDate", dateTitle + "．" + info.publishDate);
                info.publishDate = "";
            }
            if (localName.equals("item")) {
                info.list.add(mp);
                info.firstImageUrl = "";
                this.is_main_cover = true;
                mp = null;
            }

        }

        // TODO Auto-generated method stub

    }

}
