package com.duckie;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import android.widget.LinearLayout.LayoutParams;

public class EActivity extends Activity implements ViewSwitcher.ViewFactory {
    private static final String TAG = "Sample::Activity";

    private MenuItem            mItemPreviewRGBA;
    private MenuItem            mItemPreviewGray;
    private MenuItem            mItemPreviewCanny;
    private EView         		mView;

    private TextSwitcher 		mSwitcher1, mSwitcher2;
    private String				prevText1 = null, prevText2 = null;
    private RefreshHandler 		mRedrawHandler = new RefreshHandler();
    
    class RefreshHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
          EActivity.this.updateUI();
        }

        public void sleep(long delayMillis) {
          this.removeMessages(0);
          sendMessageDelayed(obtainMessage(0), delayMillis);
        }
      };
      
      public void updateUI() {
      	String currText1, currText2;
  		// TODO Auto-generated method stub
          //int currentInt = Integer.parseInt() + 10;
      	mRedrawHandler.sleep(100);
      	currText1 = EView.strClass;
      	currText2 = EView.strLetter;
      	//currText2 = Sample1View.strResult;
          if(prevText1 != currText1){          
            mSwitcher1.setText(currText1);        	
            prevText1 = currText1;
          }
          //mSwitcher2.setText("TEST");   
          if(prevText2 != currText2){          
              mSwitcher2.setText(currText2);        	
              prevText2 = currText2;
          }
      	//mSwitcher.setText(Integer.toString(Motion.getDirectionCounter()));
  	}
  
    private BaseLoaderCallback  mOpenCVCallBack = new BaseLoaderCallback(this) {
    	@Override
    	public void onManagerConnected(int status) {
    		switch (status) {
				case LoaderCallbackInterface.SUCCESS:
				{
					Log.i(TAG, "OpenCV loaded successfully");
					// Create and set View
					mView = new EView(mAppContext);
					
					mSwitcher1 = new TextSwitcher(mAppContext);
				    mSwitcher1.setFactory(EActivity.this);
				    
					mSwitcher2 = new TextSwitcher(mAppContext);
				    mSwitcher2.setFactory(EActivity.this);
				    //setListener(Sample1Java.this);
				    
				    Animation in = AnimationUtils.loadAnimation(EActivity.this,
				               R.anim.push_up_in);
				    Animation out = AnimationUtils.loadAnimation(EActivity.this,
				    		R.anim.push_up_out);
				    //animations
				    mSwitcher1.setInAnimation(in);
				    in.setDuration(3000);
				    mSwitcher1.setOutAnimation(out);
				    out.setDuration(1000);
				    
				    mSwitcher2.setInAnimation(in);
				    in.setDuration(1000);
				    mSwitcher2.setOutAnimation(out);
				    out.setDuration(3000);
				    
			        LinearLayout layout1 = new LinearLayout(EActivity.this);  
			        layout1.setOrientation(LinearLayout.VERTICAL);  
			        layout1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));  
			        layout1.setGravity(Gravity.BOTTOM);  
			        	        
			        LinearLayout.LayoutParams LayoutParam1 = //new LayoutParams(1000, 50);
			        		new LinearLayout.LayoutParams(
			        				LinearLayout.LayoutParams.WRAP_CONTENT,
			        				LinearLayout.LayoutParams.WRAP_CONTENT);
			        LayoutParam1.weight = 1;       
  
			        //setContentView(mView);
			        
			        
			        mSwitcher1.setLayoutParams(LayoutParam1);
			        mSwitcher2.setLayoutParams(LayoutParam1);
			        
			        layout1.addView(mSwitcher1);
			        layout1.addView(mSwitcher2);
					
					// Loads neural net
					mView.loadData();
					setContentView(mView);
					// Check native OpenCV camera
					addContentView(layout1, LayoutParam1);	
					updateUI(); 
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
    
    public EActivity() {
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
        mItemPreviewRGBA = menu.add("Load");
        mItemPreviewGray = menu.add("DTCAA Final");
        mItemPreviewCanny = menu.add("Canny");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "Menu Item selected " + item);
        if (item == mItemPreviewRGBA) {
        	mView.setViewMode(EView.VIEW_MODE_RGBA);
        } else if (item == mItemPreviewGray) {
        	mView.setViewMode(EView.VIEW_MODE_GRAY);
        } else if (item == mItemPreviewCanny) {
        	mView.setViewMode(EView.VIEW_MODE_CANNY);
        }
        return true;
    }

	public View makeView() {
		// TODO Auto-generated method stub
        TextView t = new TextView(this);        
        t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        t.setTextSize(50);
        t.setTypeface( Typeface.createFromAsset(getAssets(), "fonts/dkcrayoncrumble.ttf"));
        //t.setTypeface( Typeface.createFromFile("res/raw/dkcrayoncrumble.tff"));
        return t;
	}
}
