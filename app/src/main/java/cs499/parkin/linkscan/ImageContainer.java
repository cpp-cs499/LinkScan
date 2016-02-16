package cs499.parkin.linkscan;

import java.io.File;
import java.net.URL;

/**
 * Created by parkin on 1/16/2016.
 */

//Singleton of Image
public class ImageContainer {
    private static ImageContainer container = new ImageContainer();
    private static String postUrl;
    private static File image;
    private static String jsonResponse;

    private ImageContainer(){}

    public static ImageContainer getInstance(){
        return container;
    }
    public static void setImageContainer(String url, File inImage){
        postUrl = url;
        image = inImage;
    }
    public static void setJsonResponse(String json){
        jsonResponse = json;
    }

    public static String getUrl(){
        return postUrl;
    }
    public static File getFile(){
        return image;
    }
    public static String getJsonResponse(){
        return jsonResponse;
    }

}
