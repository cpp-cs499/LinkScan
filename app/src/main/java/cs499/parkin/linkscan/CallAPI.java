package cs499.parkin.linkscan;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.android.internal.http.multipart.MultipartEntity;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by parkin on 1/11/2016.
 */
public class CallAPI extends AsyncTask<ImageContainer, String, String> {
    private String jsonReturnStr = "";
    @Override
    protected String doInBackground(ImageContainer... container) {
        String json = postImage(container[0].getUrl(), container[0].getFile());
        jsonReturnStr = json;
        return json;
    }

    protected void onPostExecute(String result) {
        DataHolder holder = new DataHolder();

        holder.setData(jsonReturnStr);
        holder.setProcessing(false);
    }

    protected static String postImage(String urlToPost, File image) {
        try{
            SSLContextBuilder sslbuilder = new SSLContextBuilder();
            sslbuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslbuilder.build());
            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

            HttpPost uploadFile = new HttpPost(urlToPost);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addTextBody("apikey", "helloworld");
            builder.addTextBody("language", "eng");
            builder.addBinaryBody("file", image, ContentType.APPLICATION_OCTET_STREAM, "file.jpg");
            HttpEntity multipart = builder.build();

            uploadFile.setEntity(multipart);

            CloseableHttpResponse response = httpClient.execute(uploadFile);
            HttpEntity responseEntity = response.getEntity();

            if(response.getEntity().getContentLength() == 0){
                return null;
            }

            StringBuilder sb = new StringBuilder();

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(response.getEntity().getContent()), 65728);
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            return sb.toString();
        }
        catch (IOException e) { e.printStackTrace(); }
        catch (Exception e) { e.printStackTrace(); }

        return null;
    }
}