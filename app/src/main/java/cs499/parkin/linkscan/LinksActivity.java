package cs499.parkin.linkscan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import java.util.List;

import cs499.parkin.linkscan.data.ImageDataHolder;


public class LinksActivity extends AppCompatActivity implements AsyncTaskInterface{

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_links);

        //check if internet is up
        if(Device.haveNetworkConnection(this)) {
            Log.i("LOG", "Network connection is up.");

            progress = new ProgressDialog(this);
            progress.setTitle("Processing");
            progress.setMessage("Please wait while processing...");
            progress.show();

            runCallAPI();
        }else{
            //internet is not up!!
            Log.i("LOG", "Network connection is down.");
            final Activity activity = this;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("No internet connection!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            activity.finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
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
            textBox.setTextSize(getResources().getDimension(R.dimen.activity_horizontal_margin));
            textBox.setPadding(10, 10, 10, 10);
            textBox.setBackgroundResource(R.drawable.textview_style);
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
            layout.setPadding(10, 10, 10, 10);
            layout.setId(previousId);

            container.addView(layout);
        }


    }

    private void popupActionBox(String text){

        final String inputString = text;

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
        progress.dismiss();

        //get JSON response
        Gson gson = new Gson();
        try {
            ImageDataHolder holder = gson.fromJson(ImageContainer.getJsonResponse(),
                    ImageDataHolder.class);

            if (holder.getExitCode() != -1) {
                buildLayout(holder);
            } else {
                displayNoLinksAlert(this);
            }
        }catch(Exception e){
            displayNoLinksAlert(this);
        }


    }
    @Override
    public void onEventFailed(){
        Log.w("CallAPI", "Callback is null");
    }

    public void displayNoLinksAlert(Activity activity){
        final Activity localActivity = activity;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Unable to get links from the image!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        localActivity.finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
