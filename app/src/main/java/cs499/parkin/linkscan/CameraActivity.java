package cs499.parkin.linkscan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class CameraActivity extends Activity implements PictureCallback, SurfaceHolder.Callback {

    public static final String EXTRA_CAMERA_DATA = "camera_data";

    private static final String KEY_IS_CAPTURING = "is_capturing";

    //private String urlStr = "https://api.ocr.space/Parse/Image";
    /*private String OCR_URL = "http://ec2-52-36-80-70.us-west-2.compute.amazonaws.com:8080" +
                            "/OCRServlet-1/ocrtest";*/
    //final static private String OCR_URL = "http://ocr-eb.us-west-2.elasticbeanstalk.com/ocrtest";
    private static String OCR_URL = "";

    private Camera mCamera;
    private ImageView mCameraImage;
    private SurfaceView mCameraPreview;
    private Button mCaptureImageButton;
    private Button resendButton;
    private byte[] mCameraData;
    private boolean mIsCapturing;

    private OnClickListener mCaptureImageButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            captureImage();
        }
    };

    private OnClickListener mRecaptureImageButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            setupImageCapture();
        }
    };

    private OnClickListener resendButtonClickListner = new OnClickListener() {
        @Override
        public void onClick(View v) {
            resendImage();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera);

        //process passed values
        OCR_URL = getIntent().getExtras().getString("OCR_URL");
        boolean imagePassed = getIntent().getExtras().getBoolean("INTENT_INPUT");

        if(OCR_URL == ""){
            //log error
            Log.e("ERROR", "OCR_URL is null!");
        }

        mCameraImage = (ImageView) findViewById(R.id.camera_image_view);
        mCameraImage.setVisibility(View.INVISIBLE);

        mCameraPreview = (SurfaceView) findViewById(R.id.preview_view);
        final SurfaceHolder surfaceHolder = mCameraPreview.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mCameraPreview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Camera.Parameters params = mCamera.getParameters();

                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                mCamera.setParameters(params);
            }
        });

        mCaptureImageButton = (Button) findViewById(R.id.capture_image_button);
        mCaptureImageButton.setOnClickListener(mCaptureImageButtonClickListener);

        resendButton = (Button) findViewById(R.id.resend_image_button);
        resendButton.setOnClickListener(resendButtonClickListner);

        mIsCapturing = true;

        //if image was passed
        if(imagePassed){
            Uri passedUri = Uri.parse(getIntent().getExtras().getString("IMAGE_URI"));
            mCameraImage.setImageURI(passedUri);

            startLinksActivity();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putBoolean(KEY_IS_CAPTURING, mIsCapturing);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mIsCapturing = savedInstanceState.getBoolean(KEY_IS_CAPTURING, mCameraData == null);
        if (mCameraData != null) {
            setupImageDisplay();
        } else {
            setupImageCapture();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mCamera == null) {
            try {
                mCamera = Camera.open();
                mCamera.setPreviewDisplay(mCameraPreview.getHolder());
                if (mIsCapturing) {
                    mCamera.startPreview();
                }
            } catch (Exception e) {
                Toast.makeText(CameraActivity.this, "Unable to open camera.", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        mCameraData = data;
        setupImageDisplay();
        File image = storeImageFromCamera();

        //send image to url
        ImageContainer.setImageContainer(OCR_URL, image);

        //start new activity
        startLinksActivity();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mCamera != null) {
            try {
                //set orientation to portrait and change the display orientation
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.set("orientation", "portrait");
                parameters.setRotation(90);
                mCamera.setParameters(parameters);
                mCamera.setDisplayOrientation(90);

                mCamera.setPreviewDisplay(holder);
                if (mIsCapturing) {
                    mCamera.startPreview();
                }
            } catch (IOException e) {
                Toast.makeText(CameraActivity.this, "Unable to start camera preview.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    private void captureImage() {
        mCamera.takePicture(null, null, this);
    }

    private File storeImageFromCamera(){
        String directoryPath = getApplicationContext().getFilesDir().getAbsolutePath();

        String strFilePath = directoryPath + "/image";
        Log.i("LOG", "Writing image to " + strFilePath);
        try {
            FileOutputStream fos = new FileOutputStream(strFilePath);

            fos.write(mCameraData);
            fos.close();
        }
        catch(FileNotFoundException ex)   {
            System.out.println("FileNotFoundException : " + ex);
        }
        catch(IOException ioe)  {
            System.out.println("IOException : " + ioe);
        }

        return new File(strFilePath);
    }

    private void setupImageCapture() {
        mCameraImage.setVisibility(View.INVISIBLE);
        mCameraPreview.setVisibility(View.VISIBLE);
        mCamera.startPreview();
        mCaptureImageButton.setText("Capture Image");
        resendButton.setVisibility(View.INVISIBLE);
        resendButton.setLayoutParams(
                new LinearLayout.LayoutParams(0,0));
        mCaptureImageButton.setOnClickListener(mCaptureImageButtonClickListener);
    }

    private void setupImageDisplay() {
        Bitmap bitmap = BitmapFactory.decodeByteArray(mCameraData, 0, mCameraData.length);
        mCameraImage.setImageBitmap(bitmap);
        mCamera.stopPreview();
        mCameraPreview.setVisibility(View.INVISIBLE);
        mCameraImage.setVisibility(View.VISIBLE);
        mCaptureImageButton.setText("Recapture Image");
        resendButton.setVisibility(View.VISIBLE);
        resendButton.setLayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
        mCaptureImageButton.setOnClickListener(mRecaptureImageButtonClickListener);
    }

    private void resendImage(){
        startLinksActivity();
    }

    private void startLinksActivity(){
        startActivity(new Intent(CameraActivity.this, LinksActivity.class));
    }
}
