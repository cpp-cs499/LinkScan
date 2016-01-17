package cs499.parkin.linkscan;

import java.io.File;
import java.net.URL;

/**
 * Created by parkin on 1/16/2016.
 */
public class ImageContainer {
    String postUrl;
    File image;

    public ImageContainer(String url, File image){
        this.postUrl = url;
        this.image = image;
    }
    public String getUrl(){
        return postUrl;
    }
    public File getFile(){
        return image;
    }
}
