package com.duckie.a;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class Sample1Java extends Activity {
    private static final String TAG = "Sample::Activity";

    private MenuItem            mItemPreviewRGBA;
    private MenuItem			mItemPreviewAll;
    private MenuItem			mItemPreviewFingers;
    private MenuItem			mItemPreviewRed;
    private MenuItem			mItemPreviewYellow;
    private MenuItem			mItemPreviewBlue;
    private MenuItem			mItemPreviewGreen;
    private MenuItem			mItemPreviewCyan;
    private MenuItem			mItemPreviewViolet;

    
    private Sample1View         mView;

    private BaseLoaderCallback  mOpenCVCallBack = new BaseLoaderCallback(this) {
    	@Override
    	public void onManagerConnected(int status) {
    		switch (status) {
				case LoaderCallbackInterface.SUCCESS:
				{
					Log.i(TAG, "OpenCV loaded successfully");
					// Create and set View
					mView = new Sample1View(mAppContext);
					setContentView(mView);
					// Check native OpenCV camera
					if( !mView.openCamera() ) {
						AlertDialog ad = new AlertDialog.Builder(mAppContext).create();
						ad.setCancelable(false); // This blocks the 'BACK' button
						ad.setMessage("Fatal error: can't open camera!");
						ad.setButton("OK", new DialogInterface.OnClickListener() {
						    public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							finish();
						    }
						});
						ad.show();
					}
				} break;
				default:
				{
					super.onManagerConnected(status);
				} break;
			}
    	}
	};
    
    public Sample1Java() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    @Override
	protected void onPause() {
        Log.i(TAG, "onPause");
		super.onPause();
		if (null != mView)
			mView.releaseCamera();
	}

	@Override
	protected void onResume() {
        Log.i(TAG, "onResume");
		super.onResume();
		if( (null != mView) && !mView.openCamera() ) {
			AlertDialog ad = new AlertDialog.Builder(this).create();  
			ad.setCancelable(false); // This blocks the 'BACK' button  
			ad.setMessage("Fatal error: can't open camera!");  
			ad.setButton("OK", new DialogInterface.OnClickListener() {  
			    public void onClick(DialogInterface dialog, int which) {  
				dialog.dismiss();
				finish();
			    }  
			});  
			ad.show();
		}
	}

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Log.i(TAG, "Trying to load OpenCV library");
        if (!OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_2, this, mOpenCVCallBack))
        {
        	Log.e(TAG, "Cannot connect to OpenCV Manager");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu");
        mItemPreviewRGBA = menu.add("RGBA");
        mItemPreviewAll = menu.add("A");
        mItemPreviewFingers = menu.add("F");
        mItemPreviewBlue = menu.add("B");        
        mItemPreviewYellow = menu.add("Y");    
        mItemPreviewRed = menu.add("R");
        mItemPreviewGreen = menu.add("G");
        mItemPreviewCyan = menu.add("O");
        mItemPreviewViolet = menu.add("W");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "Menu Item selected " + item);
        if (item == mItemPreviewRGBA) {
        	mView.setViewMode(Sample1View.VIEW_MODE_RGBA);
        } else if (item == mItemPreviewAll){
        	mView.setViewMode(Sample1View.VIEW_MODE_ALL);
        } else if (item == mItemPreviewFingers) {
        	mView.setViewMode(Sample1View.VIEW_MODE_FINGERS);
        } else if (item == mItemPreviewBlue) {
        	mView.setViewMode(Sample1View.VIEW_MODE_BLUE);
        } else if (item == mItemPreviewYellow) {
        	mView.setViewMode(Sample1View.VIEW_MODE_YELLOW);
        } else if (item == mItemPreviewRed) {
        	mView.setViewMode(Sample1View.VIEW_MODE_RED);
        } else if (item == mItemPreviewGreen) {
        	mView.setViewMode(Sample1View.VIEW_MODE_GREEN);
        } else if (item == mItemPreviewCyan) {
        	mView.setViewMode(Sample1View.VIEW_MODE_CYAN);
        } else if (item == mItemPreviewViolet) {
        	mView.setViewMode(Sample1View.VIEW_MODE_VIOLET);
        }
        return true;
    }
}
