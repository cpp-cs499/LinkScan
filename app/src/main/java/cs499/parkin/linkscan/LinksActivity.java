package cs499.parkin.linkscan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import org.apache.commons.validator.UrlValidator;
import org.json.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cs499.parkin.linkscan.data.ImageData;
import cs499.parkin.linkscan.data.ImageWords;
import cs499.parkin.linkscan.data.Lines;
import cs499.parkin.linkscan.data.ParsedResults;
import cs499.parkin.linkscan.data.TextOverlay;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;


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

    private void runCallAPI(){
        Log.i("LOG", "Running CallAPI to communicate with OCR REST interface");
        CallAPI backgroundTask = new CallAPI(this);
        backgroundTask.execute(ImageContainer.getInstance());
        /*
        OcrAPIService service;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.ocr.space")
                .addConverterFactory(JacksonConverterFactory.create(new ObjectMapper()
                        .setPropertyNamingStrategy(new OcrNamingStrategy())))
                .build();
        service = retrofit.create(OcrAPIService.class);

        //static variables
        String apikey = "helloworld";
        String lang = "eng";
        String overlay = "false";
        MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
        File imageFile = new File("file.jpg");
        //RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JPG, imageFile);
        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addFormDataPart("apikey", apikey)
                .addFormDataPart("language", lang)
                .addFormDataPart("file", "file.jpg",
                        RequestBody.create(MEDIA_TYPE_JPG, imageFile))
                .build();

        //make the api call
        Call<ImageData> call = service.postImageData(requestBody);
        call.enqueue(new Callback<ImageData>(){
            @Override
            public void onResponse(Call<ImageData> call, Response<ImageData> response) {
                ImageData data = response.body();

                List<ParsedResults> results = data.getParsedResults();

                for(ParsedResults result : results){
                    TextOverlay overlay = result.getTextOverlay();

                    for(Lines line : overlay.getLines()){
                        for(ImageWords word : line.getWords()){
                            Log.i("CONTENTS", word.getWordText());
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<ImageData> call, Throwable t) {
                Log.e("API", "Something went wrong!");
            }
        });
        */
    }

    private String[] parseJsonData(){
        Log.i("LOG", ImageContainer.getJsonResponse());
        /*ObjectMapper objectMapper = new ObjectMapper();
        //first letter is capitalized on json variables which is not standard
        objectMapper.setPropertyNamingStrategy(new OcrNamingStrategy());
        try {
            ImageData imageData = objectMapper.readValue(ImageContainer.getJsonResponse(), ImageData.class);

            //print all words on image
            for(ParsedResults result : imageData.getParsedResults()){
                int exitCode = result.getFileParseExitCode();
                Log.i("ParsedResult.Code", exitCode + "");
                Log.i("ParsedResult.Text", result.getParsedText());
                Log.i("ParsedResult.Details", result.getErrorDetails());
                Log.i("ParsedResult.Message", result.getErrorMessage());

                if(exitCode == 1) {
                    TextOverlay overlay = result.getTextOverlay();

                    /*List<Lines> linesList = result.getTextOverlay().getLines();

                    for (Lines line : linesList) {
                        for (ImageWords words : line.getWords()) {
                            Log.i("Words", words.getWordText());
                        }
                    }*/
                /*}
            }

        } catch (IOException e) {
            e.printStackTrace();
        }*/

        return null;
    }

    private String[] parseJsonDataLegacy(){
        Log.i("LOG", "Parsing JSON data");
        if(ImageContainer.getJsonResponse() != null) {
            Log.i("LOG", ImageContainer.getJsonResponse());

            try {
                JSONObject jsonObj = new JSONObject(ImageContainer.getJsonResponse());
                JSONArray jsonArr = jsonObj.getJSONArray(TAG_RESULTS);
                String errorMessage = "";
                String exitCode = "";
                String parsedText = "";

                //getting the text
                if (jsonArr.length() > 0) {
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
            } catch (JSONException e) {
                Log.e("JSON Error", e.getMessage());
            }
        }
        else{
            Log.i("LOG", "JsonResponse return is null");
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
        /*String[] possibleURLs = parseJsonData();

        String[] validURLs = getValidURLs(possibleURLs);

        System.out.println("Valid URLs: ");
        if(validURLs != null) {
            for (int i = 0; i < validURLs.length; i++) {
                System.out.println(validURLs[i]);
            }
        }*/

    }
    @Override
    public void onEventFailed(){
        Log.w("CallAPI", "Callback is null");
    }
}
