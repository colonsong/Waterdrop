package tw.waterdrop.waterdrop.xmlparser;

import android.os.Handler;
import android.os.Message;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ThreadXMLParser {

    public Handler HandlerManager;

    private URL website;
    final private String myURL = "http://waterdrop.tw/blog/rss";

    final Runnable runnable = new Runnable() {

        @Override
        public void run() {
            try {

                website = new URL(myURL);
                // getting xmlreader to parse data
                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser sp = spf.newSAXParser();
                XMLReader xr = sp.getXMLReader();

                HandlingXMLStuff doingWork = new HandlingXMLStuff();
                xr.setContentHandler(doingWork);
                InputSource input_source = new InputSource(website.openStream());
                input_source.setEncoding("UTF-8");
                xr.parse(input_source);
                Message msg = new Message();
                msg.obj = doingWork.info.list;
                HandlerManager.sendMessage(msg);

            } catch (Exception e) {
                System.out.print(e);

            }

        }
    };

    public void start_parser() {
        new Thread(runnable).start();
    }

}
