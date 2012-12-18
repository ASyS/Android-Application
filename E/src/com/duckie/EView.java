package com.duckie;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.duckie.Filter;
import com.duckie.Classification;

class EView extends EViewBase {

    public static final int     VIEW_MODE_RGBA  = 0;
    public static final int     VIEW_MODE_GRAY  = 1;
    public static final int     VIEW_MODE_CANNY = 2;

    private Mat mYuv;
    private Mat mRgba;
    private Mat mGraySubmat;
    private Mat mIntermediateMat;
    private Mat mHand;
	private Bitmap mBitmap;
	private int mViewMode;
 
    public EView(Context context) {
        super(context);
        mViewMode = VIEW_MODE_GRAY;
    }

	@Override
	protected void onPreviewStarted(int previewWidth, int previewHeight) {
	    synchronized (this) {
        	// initialize Mats before usage
        	mYuv = new Mat(getFrameHeight() + getFrameHeight() / 2, getFrameWidth(), CvType.CV_8UC1);	//what is the purpose of creating Mat object of height more than needed?
        	mGraySubmat = mYuv.submat(0, getFrameHeight(), 0, getFrameWidth());

        	mRgba = new Mat();
        	mIntermediateMat = new Mat();
        	mHand = new Mat();

        	mBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.ARGB_8888); 		//each pixel is stored on 4 bytes(R, G, B, alpha). hence, 4 channels
    	    }
	}

	@Override
	protected void onPreviewStopped() {
		if(mBitmap != null) {
			mBitmap.recycle();
		}

		synchronized (this) {
            // Explicitly deallocate Mats
            if (mYuv != null)
                mYuv.release();
            if (mRgba != null)
                mRgba.release();
            if (mGraySubmat != null)
                mGraySubmat.release();
            if (mIntermediateMat != null)
                mIntermediateMat.release();
            if (mHand != null)
            	mHand.release();
            mYuv = null;
            mRgba = null;
            mGraySubmat = null;
            mIntermediateMat = null;
            mHand = null;
        }
    }

    @Override
    protected Bitmap processFrame(byte[] data) {
        mYuv.put(0, 0, data);

        final int viewMode = mViewMode;

        switch (viewMode) {
        case VIEW_MODE_GRAY:
			// Subtract external background from hand
        	mHand = Filter.subtractBG(mYuv, mRgba);
        	// Classify hand signal
        	int group = Classification.classify(mRgba, mHand);
        	if(group == 0)
        		printTextUL(mRgba, "Input Hand");
        	else if(group == 1){
        		printTextUL(mRgba, "Motion");
        		Motion.getASLMotionLetter();
        	}
        	else if(group == 2){
        		printTextUL(mRgba, "Height");
//        		Log.i("size", ""+mHand.rows());
        		printTextBL(mRgba, HeightClass.findTips(mHand));
        	}
        	else if(group == 3)
        		printTextUL(mRgba, "Mid");
        	else if(group == 4)
        		printTextUL(mRgba, "Centroid");
        	else if(group == 5)
        		printTextUL(mRgba, "Width");
//       	try {
//			Thread.sleep(300);//in ms
//		} catch (InterruptedException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//        	Core.putText(mRgba, Classification.classify(mRgba, mHand), new Point(10, 100), 3/* CV_FONT_HERSHEY_COMPLEX */, 2, new Scalar(255, 0, 0, 255), 3);
            break;
        case VIEW_MODE_RGBA:
            Imgproc.cvtColor(mYuv, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);
            Core.putText(mRgba, "OpenCV + Android", new Point(10, 100), 3/* CV_FONT_HERSHEY_COMPLEX */, 2, new Scalar(255, 0, 0, 255), 3);
            break;
            
        case VIEW_MODE_CANNY:
//            Imgproc.Canny(mGraySubmat, mIntermediateMat, 80, 100);
//            Imgproc.cvtColor(mIntermediateMat, mRgba, Imgproc.COLOR_GRAY2BGRA, 4);
        	
        	
            break;
        }

        Bitmap bmp = mBitmap;

        try {
            Utils.matToBitmap(mRgba, bmp);
        } catch(Exception e) {
            Log.e("com.duckie", "Utils.matToBitmap() throws an exception: " + e.getMessage());
            bmp.recycle();
            bmp = null;
        }
        return bmp;
    }

    public void setViewMode(int viewMode) {
    	mViewMode = viewMode;
    }
    
    public void printTextUL(Mat img, String text){
    	final float top = 35.0f;
    	final float left = 5.0f;
    	// Get the screen's density scale
    	final float scale = getFrameHeight()/320.0f;
    	// Convert the dps to pixels, based on density scale
    	int iTop = (int) (top * scale + 0.5f);
    	int iLeft =  (int) (left * scale + 0.5f);
    	Core.putText(img, text, new Point(iLeft, iTop), 4, 1.0f*scale, new Scalar(255, 0, 0, 255), 1);
    }

    public void printTextBL(Mat img, String text){
    	final float bottom = 305.0f;		//320
    	final float left = 5.0f;
    	// Get the screen's density scale
    	final float scale = getFrameHeight()/320.0f;
    	// Convert the dps to pixels, based on density scale
    	int iBottom = (int) (bottom * scale + 0.5f);
    	int iLeft =  (int) (left * scale + 0.5f);
    	Core.putText(img, text, new Point(iLeft, iBottom), 4, 1.0f*scale, new Scalar(255, 0, 0, 255), 1);
    }

}
