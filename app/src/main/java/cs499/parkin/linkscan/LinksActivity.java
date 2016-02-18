package cs499.parkin.linkscan;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.commons.validator.UrlValidator;
import org.json.*;

import java.util.ArrayList;
import java.util.List;

import cs499.parkin.linkscan.data.ImageDataHolder;


public class LinksActivity extends AppCompatActivity implements AsyncTaskInterface{

    private static final String TAG_TEXT = "ParsedText";
    private static final String TAG_ERROR_MESSAGE = "ErrorMessage";
    private static final String TAG_EXIT_CODE = "FileParseExitCode";
    private static final String TAG_RESULTS = "ParsedResults";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_links);

        //check if internet is up
        if(Device.haveNetworkConnection(this)) {
            Log.i("LOG", "Network connection is up.");

            runCallAPI();
        }else{
            //internet is not up!!
            Log.i("LOG", "Network connection is down.");

        }
    }

    private void runCallAPI() {
        Log.i("LOG", "Running CallAPI to communicate with OCR REST interface");
        CallAPI backgroundTask = new CallAPI(this);
        backgroundTask.execute(ImageContainer.getInstance());
    }

    private void buildLayout(ImageDataHolder holder){
        Log.i("LOG", "Updating links layout");
        List<String> textList = holder.getTextList();
        int totalNum = textList.size();

        LinearLayout container = (LinearLayout) findViewById(R.id.linkContainer);
        int previousId = 0;

        for(int i = 0; i < totalNum; i++){
            RelativeLayout layout = new RelativeLayout(this);
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
                                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT);
            final TextView textBox = new TextView(this);

            //set the text
            textBox.setText(textList.get(i));
            textBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupActionBox(textBox.getText().toString());
                }
            });

            //set below previous text
            if(i != 0) {
                p.addRule(RelativeLayout.BELOW, previousId);
            }

            layout.setLayoutParams(p);

            //add to the containers
            previousId = View.generateViewId();
            layout.addView(textBox);
            layout.setPadding(5,5,5,5);
            layout.setId(previousId);

            container.addView(layout);
        }


    }

    private void popupActionBox(String text){

        //final String inputString = text;
        final String inputString = "https://google.com";

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(inputString));
                            startActivity(browserIntent);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("Image Text", inputString);
                            clipboard.setPrimaryClip(clip);
                            break;
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Action?").setPositiveButton("Open in browser", dialogClickListener)
                .setNegativeButton("Copy to Clipboard", dialogClickListener).show();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onEventCompleted(){

        //get JSON response
        Gson gson = new Gson();
        ImageDataHolder holder = gson.fromJson(ImageContainer.getJsonResponse(),
                                               ImageDataHolder.class);

        if(holder.getExitCode() != -1) {
            buildLayout(holder);
        }else{
            Toast.makeText(this, "Unable to get text from the image!", Toast.LENGTH_LONG);
        }


    }
    @Override
    public void onEventFailed(){
        Log.w("CallAPI", "Callback is null");
    }
}
