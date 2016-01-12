package cs499.parkin.linkscan;

import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by parkin on 1/11/2016.
 */
public class CallAPI extends AsyncTask<URL, String, String> {

    @Override
    protected String doInBackground(URL... url) {

        try {
            // curl_init and url
            HttpURLConnection con = (HttpURLConnection) url[0].openConnection();

            String key = "apikey";
            String value = "";
            setupPostConnection(con);
            setupValues(con, key, value);


            DataOutputStream output = new DataOutputStream(con.getOutputStream());
            output.writeBytes(postData);
            output.close();

            // "Post data send ... waiting for reply");
            int code = con.getResponseCode(); // 200 = HTTP_OK
            System.out.println("Response    (Code):" + code);
            System.out.println("Response (Message):" + con.getResponseMessage());

            // read the response
            DataInputStream input = new DataInputStream(con.getInputStream());
            int c;
            StringBuilder resultBuf = new StringBuilder();
            while ((c = input.read()) != -1) {
                resultBuf.append((char) c);
            }
            input.close();

            return resultBuf.toString();
        }catch(IOException e){
            e.printStackTrace();
        }

        return null;
    }

    protected void setupPostConnection(HttpURLConnection connection){
        try{
            connection.setRequestMethod("POST");
            connection.setInstanceFollowRedirects(true);
            connection.setDoOutput(true);
            connection.setDoInput(true);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    protected void setupValues(HttpURLConnection connection, String key, String value){

        connection.setRequestProperty(key, value);
    }

    protected void onPostExecute(String result) {

    }

}