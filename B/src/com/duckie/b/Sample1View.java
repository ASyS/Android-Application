package com.duckie.b;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

class Sample1View extends SampleViewBase {

    public static final int     VIEW_MODE_RGBA  = 0;
    public static final int     VIEW_MODE_GREEN	= 1;
    public static final int     VIEW_MODE_BLUE	= 2;
    public static final int     VIEW_MODE_BLACK	= 3;
    public static final int     VIEW_MODE_MAIN	= 4;

    private Mat mYuv;
    private Mat mRgba;
    private Mat mGraySubmat;
    private Mat mIntermediateMat;
	private Bitmap mBitmap;
	private int mViewMode;
	
	private Mat mColor;
	private Mat mHsv;
	private Mat mResult;

    public Sample1View(Context context) {
        super(context);
        mViewMode = VIEW_MODE_RGBA;
    }

	@Override
	protected void onPreviewStarted(int previewWidth, int previewHeight) {
	    synchronized (this) {
        	// initialize Mats before usage
        	mYuv = new Mat(getFrameHeight() + getFrameHeight() / 2, getFrameWidth(), CvType.CV_8UC1);
        	mGraySubmat = mYuv.submat(0, getFrameHeight(), 0, getFrameWidth());

        	mRgba = new Mat();
        	mIntermediateMat = new Mat();
        	
        	mColor = new Mat();
        	mHsv = new Mat();
        	mResult  = new Mat();
        	
        	mBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.ARGB_8888); 
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

            mYuv = null;
            mRgba = null;
            mGraySubmat = null;
            mIntermediateMat = null;
            
            if (mColor != null)
            	mColor.release();
            if (mHsv != null)
                mHsv.release();
            if (mResult != null)
                mResult.release();
            
            mColor = null;
            mHsv = null;
            mResult = null;
        }
    }

    @Override
    protected Bitmap processFrame(byte[] data) {
        mYuv.put(0, 0, data);

        final int viewMode = mViewMode;

        switch (viewMode) {
        case VIEW_MODE_GREEN:		// unstable
        	IP.cvt_YUVtoHSV(mYuv,mHsv);
        	IP.getGreenMat(mHsv, mRgba);
        	
        	break;
        
        case VIEW_MODE_BLUE:		// looks ok
        	IP.cvt_YUVtoHSV(mYuv,mHsv);
        	IP.getBlueMat(mHsv, mRgba);
        	
        	break;
        	
        case VIEW_MODE_BLACK:		// not yet tested
        	IP.cvt_YUVtoHSV(mYuv,mHsv);
        	IP.getBlackMat(mHsv, mRgba);
        	
        	break;
        case VIEW_MODE_MAIN:
        	/*
        	 * insert codes here
        	 * 
        	 * 
        	 */
        	
        	break;

        case VIEW_MODE_RGBA:
            Imgproc.cvtColor(mYuv, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);
            //Core.putText(mRgba, "OpenCV + Android", new Point(10, 100), 3/* CV_FONT_HERSHEY_COMPLEX */, 2, new Scalar(255, 0, 0, 255), 3);
            break;
            
            
         
        /*case VIEW_MODE_GRAY:
            Imgproc.cvtColor(mGraySubmat, mRgba, Imgproc.COLOR_GRAY2RGBA, 4);
            break;    
        case VIEW_MODE_CANNY:
            Imgproc.Canny(mGraySubmat, mIntermediateMat, 80, 100);
            Imgproc.cvtColor(mIntermediateMat, mRgba, Imgproc.COLOR_GRAY2BGRA, 4);
            break;*/
        }

        Bitmap bmp = mBitmap;

        try {
            Utils.matToBitmap(mRgba, bmp);
        } catch(Exception e) {
            Log.e("org.opencv.samples.tutorial1", "Utils.matToBitmap() throws an exception: " + e.getMessage());
            bmp.recycle();
            bmp = null;
        }
        return bmp;
    }

    public void setViewMode(int viewMode) {
    	mViewMode = viewMode;
    }

}
