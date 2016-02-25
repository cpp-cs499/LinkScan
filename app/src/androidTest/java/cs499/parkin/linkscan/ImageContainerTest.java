package cs499.parkin.linkscan;

import android.app.Application;
import android.test.ApplicationTestCase;

import junit.framework.Assert;

import java.io.File;

/**
 * Created by parkin on 2/25/2016.
 */
public class ImageContainerTest extends ApplicationTestCase<Application> {

    public ImageContainerTest() {
        super(Application.class);
    }

    public void testSingleton(){
        ImageContainer container = ImageContainer.getInstance();

        //test json
        String testJsonMessage = "Test";
        container.setJsonResponse(testJsonMessage);
        ImageContainer.setJsonResponse(testJsonMessage);

        Assert.assertEquals(ImageContainer.getJsonResponse(), container.getJsonResponse());

        //test url and file
        String testUrl = "http://google.com";
        File testFile = null;
        container.setImageContainer(testUrl, testFile);
        ImageContainer.setImageContainer(testUrl, testFile);

        Assert.assertEquals(ImageContainer.getUrl(), container.getUrl());
        Assert.assertEquals(ImageContainer.getFile(), container.getFile());

        //test instance
        Assert.assertEquals(container, ImageContainer.getInstance());
    }
}
