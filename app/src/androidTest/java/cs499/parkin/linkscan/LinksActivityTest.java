package cs499.parkin.linkscan;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.view.ViewGroup;

/**
 * Created by parkin on 2/25/2016.
 */
public class LinksActivityTest extends ApplicationTestCase<Application> {
    LinksActivity activity;
    public LinksActivityTest() {
        super(Application.class);

        setupBeforeTest();
    }

    private void setupBeforeTest(){
        activity = new LinksActivity();
        ImageContainer.setJsonResponse("{\"textList\":[\"http://google.com\"," +
                "\"http://google.com\",\"http://facebook.com\"," +
                "\"http://amazon.com\",\"http://blah.com\"],\"exitCode\":1}");
    }

    public void testOnEventComplete(){
        activity.onEventCompleted();
        //root view
        ViewGroup viewGroup = (ViewGroup)activity.findViewById(R.id.linksActivityWrapperView);
    }
}
