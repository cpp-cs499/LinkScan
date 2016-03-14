package cs499.parkin.linkscan;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by parkin on 3/13/2016.
 */
public class MainActivity extends Activity {
    final static int OLD_CAMERA_API = 21;
    final static private String OCR_URL = "http://ocr-eb.us-west-2.elasticbeanstalk.com/ocrtest";

    boolean intentInput = false;
    Uri intentImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Check to see if image was sent from another app
        Intent intent = getIntent();
        if(intent != null){
            String action = intent.getAction();
            String type = intent.getType();

            if(Intent.ACTION_SEND.equals(action) && type != null) {
                if(type.startsWith("image/")) {
                    handleSendImage(intent);
                    intentInput = true;
                }
            }
        }


        //Checks to see what camera to use
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        Intent newIntent;

        if(currentapiVersion >= OLD_CAMERA_API) {
            newIntent = new Intent(MainActivity.this, CameraActivity.class);
        }else{
            newIntent = new Intent(MainActivity.this, CameraActivity2.class);
        }

        //building extra messages
        newIntent.putExtra("OCR_URL", OCR_URL);
        if(intentInput) {
            newIntent.putExtra("INTENT_INPUT", intentInput);
            newIntent.putExtra("IMAGE_URI", intentImageUri.toString());
        }
        startActivity(newIntent);
    }

    private void handleSendImage(Intent intent){
        Bundle extras = intent.getExtras();
        if(extras.containsKey(Intent.EXTRA_STREAM)) {
            Uri imageUri = (Uri)extras.getParcelable(Intent.EXTRA_STREAM);
            String scheme = imageUri.getScheme();

            if(scheme.equals("content")) {
                String mimeType = intent.getType();
                ContentResolver contentResolver = getContentResolver();
                Cursor cursor = contentResolver.query(imageUri, null, null, null, null);
                cursor.moveToFirst();

                //send image to url
                String filePath = cursor.getString(
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                ImageContainer.setImageContainer(OCR_URL, new File(filePath));

                intentImageUri = imageUri;

            }
        }
    }
}
