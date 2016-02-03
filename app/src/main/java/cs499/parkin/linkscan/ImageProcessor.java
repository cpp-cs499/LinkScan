package cs499.parkin.linkscan;

import android.os.AsyncTask;

import com.googlecode.leptonica.android.Pix;
import com.googlecode.leptonica.android.ReadFile;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;

/**
 * Created by parkin on 1/27/2016.
 */
public class ImageProcessor extends AsyncTask<ImageContainer, String, String> {

    AsyncTaskInterface callback;

    public ImageProcessor(AsyncTaskInterface cb){
        callback = cb;
    }

    @Override
    protected String doInBackground(ImageContainer... params) {

        processImage();
        return null;
    }

    private void processImage(){
        File image = ImageContainer.getFile();
        String directoryPath = image.getParent();
        File directory = new File(directoryPath + "/tessdata");
        Pix picture = ReadFile.readFile(image);
        TessBaseAPI tbapi = new TessBaseAPI();

        if(!directory.exists()){
            directory.mkdir();
        }

        tbapi.init(directoryPath,"eng");
        tbapi.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "1234567890/:");
        tbapi.setImage(picture);

        String recognizedText = tbapi.getUTF8Text();
        System.out.println("Recognized Text: " + recognizedText);
    }

    protected void onPostExecute(String result) {

    }
}
