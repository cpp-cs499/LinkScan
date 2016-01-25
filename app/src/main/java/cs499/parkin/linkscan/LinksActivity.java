package cs499.parkin.linkscan;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class LinksActivity extends AppCompatActivity implements AsyncTaskInterface{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_links);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        runCallAPI();
    }
    private void runCallAPI(){
        CallAPI backgroundTask = new CallAPI(this);
        backgroundTask.execute(ImageContainer.getInstance());
    }

    private void getJsonData(){

        System.out.println("HOLDER JSON: " + ImageContainer.getJsonResponse());
    }

    @Override
    public void onEventCompleted(){
        getJsonData();
    }
    @Override
    public void onEventFailed(){
        Log.w("CallAPI", "Callback is null");
    }
}
