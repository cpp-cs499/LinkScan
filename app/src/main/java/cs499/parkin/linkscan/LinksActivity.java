package cs499.parkin.linkscan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.apache.commons.validator.UrlValidator;
import org.json.*;

import java.util.ArrayList;


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

            ImageContainer.setUsingRestAPI(true);
            runCallAPI();
        }else{
            //internet is not up!!
            Log.i("LOG", "Network connection is down.");

            ImageContainer.setUsingRestAPI(false);
            runImageProcessor();
        }
    }
    private void runImageProcessor(){
        Log.i("LOG", "Running ImageProcessor");

        ImageProcessor backgroundTask = new ImageProcessor(this);
        backgroundTask.execute(ImageContainer.getInstance());
    }

    private void runCallAPI(){
        Log.i("LOG", "Running CallAPI to communicate with OCR REST interface");
        CallAPI backgroundTask = new CallAPI(this);
        backgroundTask.execute(ImageContainer.getInstance());
    }

    private String[] parseJsonData(){
        Log.i("LOG", "Parsing JSON data");
        if(ImageContainer.getJsonResponse() != null){
            Log.i("LOG", ImageContainer.getJsonResponse());
        }else{
            Log.i("LOG", "JsonResponse return is null");
        }
        try {
            JSONObject jsonObj = new JSONObject(ImageContainer.getJsonResponse());
            JSONArray jsonArr = jsonObj.getJSONArray(TAG_RESULTS);
            String errorMessage = "";
            String exitCode = "";
            String parsedText = "";

            //getting the text
            if(jsonArr.length() > 0) {
                JSONObject obj = jsonArr.getJSONObject(0);
                parsedText = getInnerText(obj.toString(), TAG_TEXT);
                errorMessage = obj.getString(TAG_ERROR_MESSAGE);
                exitCode = obj.getString(TAG_EXIT_CODE);

                String[] possibleURLs = parsedText.split("\\\\r?\\\\n");

                //logs
                Log.i("LOG", "Exit Code: " + exitCode);
                Log.i("LOG", "Error from JSON data: \n" + errorMessage);
                Log.i("LOG", "Text from JSON data: \n" + parsedText);

                return possibleURLs;
            }
        }catch(JSONException e){
            Log.e("JSON Error", e.getMessage());
        }

        return null;
    }

    public String getInnerText(String jsonMessage, String tag){
        String[] elements = jsonMessage.split(":");

        for(int i =0; i < elements.length; i++){
            System.out.println(i + ": " + elements[i]);
            if(elements[i].contains("\"" + tag + "\"")){
                if(i < elements.length-1)
                    return elements[i+1];
            }
        }
        return "";
    }

    public String[] getValidURLs(String[] input){
        if(input == null){
            return null;
        }

        ArrayList<String> arrList = new ArrayList<String>();
        String[] schemes = {"http", "https", "www"};
        UrlValidator urlValidator = new UrlValidator(schemes);

        for(int i = 0; i < input.length; i++) {
            if (urlValidator.isValid(input[i])) {
                arrList.add(input[i]);
            }
        }

        return arrList.toArray(new String[arrList.size()]);
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
        TextView txtProcessing = (TextView) findViewById(R.id.txtProcessing);
        txtProcessing.setText(ImageContainer.getJsonResponse());

        //get text from image
        String[] possibleURLs = parseJsonData();

        String[] validURLs = getValidURLs(possibleURLs);

        System.out.println("Valid URLs: ");
        for(int i = 0; i < validURLs.length; i++){
            System.out.println(validURLs[i]);
        }

    }
    @Override
    public void onEventFailed(){
        Log.w("CallAPI", "Callback is null");
    }
}
