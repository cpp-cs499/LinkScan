package cs499.parkin.linkscan;

import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by parkin on 1/11/2016.
 */
public class CallAPI extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... params) {
        String urlString=params[0];
        String resultToDisplay;
        emailVerificationResult result = null;
        InputStream in = null;

        // HTTP Get
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream());
        } catch (Exception e ) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }

        // Parse XML
        XmlPullParserFactory pullParserFactory;

        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            result = parseXML(parser);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Simple logic to determine if the email is dangerous, invalid, or valid
        if (result != null ) {
            if( result.hygieneResult.equals("Spam Trap")) {
                resultToDisplay = "Dangerous email, please correct";
            }
            else if( Integer.parseInt(result.statusNbr) >= 300) {
                resultToDisplay = "Invalid email, please re-enter";
            }
            else {
                resultToDisplay = "Thank you for your submission";
            }
        }
        else {
            resultToDisplay = "Exception Occured";
        }

        return resultToDisplay;
    }

    protected void onPostExecute(String result) {

    }

}