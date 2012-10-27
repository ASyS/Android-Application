package com.duckie.a;

import org.opencv.android.Utils;
//import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
//import org.opencv.core.Point;
//import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.TimingLogger;

class Sample1View extends SampleViewBase {

	private static final String TAG = "View";
	
    public static final int     VIEW_MODE_RGBA  	= 0;
    public static final int		VIEW_MODE_ALL		= 1;
    public static final int		VIEW_MODE_BLUE		= 2; 
    public static final int		VIEW_MODE_YELLOW	= 3;
    public static final int		VIEW_MODE_RED		= 4;
    public static final int		VIEW_MODE_GREEN		= 5;
    public static final int		VIEW_MODE_CYAN		= 6;
    public static final int		VIEW_MODE_VIOLET	= 7;

    private Mat mYuv;
    private Mat mRgba;
    private Mat mGraySubmat;
    private Mat mIntermediateMat;
	private Bitmap mBitmap;
	private int mViewMode;
	
	private Mat mColor,mYellow,mBlue,mRed,mGreen,mCyan,mViolet;
	private Mat mResult;
	
	private Mat mRgb;
	private Mat mHsv;
	
	TimingLogger timings;

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

        	mBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.ARGB_8888); 
        	
        	
        	/* 10/27/X2edit:*/
        	mRgb = new Mat();
        	mHsv = new Mat();
        	
        	mColor = new Mat();
        	mBlue = new Mat();
        	mYellow = new Mat();
        	mRed = new Mat();
        	mGreen = new Mat();
        	mCyan = new Mat();
        	mViolet = new Mat();
        	mResult = new Mat();
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
            
            /* 10/27/X2edit:*/
            if (mBlue != null)
            	mBlue.release();
            if (mYellow != null)
            	mYellow.release();
            if (mRed != null)
            	mRed.release();
            if (mGreen != null)
            	mGreen.release();
            if (mCyan != null)
            	mCyan.release();
            if (mViolet != null)
            	mViolet.release();
            if (mResult != null)
            	mResult.release();
            if (mRgb != null)
            	mRgb.release();
            if (mHsv != null)
            	mHsv.release();
            if (mColor != null)
            	mColor.release();
            
            mColor = null;
            mBlue = null;
        	mYellow = null;
        	mRed = null;
        	mGreen =null;
        	mCyan = null;
        	mViolet = null;
        	mResult = null;
        	mRgb = null;
        	mHsv = null;
        }
    }

    @Override
    protected Bitmap processFrame(byte[] data) {
    	/* 10/24/X2edit:
    	 * Mat mYellow,mBlue,mRed,mGreen,mCyan,mViolet;
		Mat mResult;*/
        mYuv.put(0, 0, data);
        
//        ColorDetection.cvt_YUVtoRGBtoHSV(mYuv,mHsv);
        
        final int viewMode = mViewMode;

        switch (viewMode) {
        case VIEW_MODE_RGBA:
            Imgproc.cvtColor(mYuv, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);
//            Core.putText(mRgba, "OpenCV + Android", new Point(10, 100), 3/* CV_FONT_HERSHEY_COMPLEX */, 2, new Scalar(255, 0, 0, 255), 3);
            break;
        case VIEW_MODE_RED:
        	/*	10/22/X2edit: 	ColorDetection.XgetRedMat(mYuv,mRgba);*/
        	
        	/* 10/24/X2edit:
        	 * mRed = new Mat();
        	mResult = new Mat();*/
        	
        	/* 10/27/X2edit:
        	 */
        	ColorDetection.cvt_YUVtoRGBtoHSV(mYuv,mHsv);
        	
        	ColorDetection.getRedMat(mHsv,mColor);
        	ColorDetection.detectSingleBlob(mYuv, mColor, "R", mResult);
        	Imgproc.cvtColor(mResult, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);
            break;
        case VIEW_MODE_YELLOW:
        	
        	
        	/* 10/24/X2edit:
        	 * mYellow = new Mat();
        	mResult = new Mat();*/
//        	ColorDetection.XgetYellowMat(mYuv,mYellow);  
        	timings = new TimingLogger("TopicLogTag","preparePicturesFromList");
        	ColorDetection.cvt_YUVtoRGBtoHSV(mYuv,mHsv);
        	ColorDetection.getYellowMat(mHsv,mColor);

        	ColorDetection.detectSingleBlob(mYuv, mColor, "Y", mResult);
        	Imgproc.cvtColor(mResult, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);
        	timings.dumpToLog();
            break;
        case VIEW_MODE_BLUE:
        	/* 10/22/X2edit:
        	ColorDetection.getBlueMat(mYuv,mRgba); */
        	
        	/* 10/24/X2edit:
        	 * mBlue = new Mat();
        	mResult = new Mat();*/
//        	ColorDetection.XgetBlueMat(mYuv,mBlue);
        	
        	ColorDetection.cvt_YUVtoRGBtoHSV(mYuv,mHsv);
        	ColorDetection.getBlueMat(mHsv,mColor);   
        	
        	ColorDetection.detectSingleBlob(mYuv, mColor, "B", mResult);
        	Imgproc.cvtColor(mResult, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);
            break;
        case VIEW_MODE_GREEN:
        	/*	10/22/X2edit: 	ColorDetection.getGreenMat(mYuv,mRgba); */
        	
        	/* 10/24/X2edit:
        	 * mGreen = new Mat();
        	mResult = new Mat(); */
//        	ColorDetection.XgetGreenMat(mYuv,mGreen);
        	
        	ColorDetection.cvt_YUVtoRGBtoHSV(mYuv,mHsv);
        	ColorDetection.getGreenMat(mHsv,mColor);
        	ColorDetection.detectSingleBlob(mYuv, mColor, "G", mResult);
        	Imgproc.cvtColor(mResult, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);

        	
            break;
        case VIEW_MODE_CYAN:
        	/*	10/22/X2edit: 	ColorDetection.getCyanMat(mYuv,mRgba); */
        	
        	/* 10/24/X2edit:
        	 * mCyan = new Mat();
        	mResult = new Mat();*/
//        	ColorDetection.XgetCyanMat(mYuv,mCyan);
        	
        	ColorDetection.cvt_YUVtoRGBtoHSV(mYuv,mHsv);
        	ColorDetection.getCyanMat(mHsv,mColor);
        	ColorDetection.detectSingleBlob(mYuv, mColor, "C", mResult);
        	Imgproc.cvtColor(mResult, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);
        	
        	
        	break;
        case VIEW_MODE_VIOLET:
        	/*	10/22/X2edit: 	ColorDetection.getVioletMat(mYuv,mRgba); */
        	
        	/* 10/24/X2edit:
        	 * mViolet = new Mat();
        	mResult = new Mat();*/
//        	ColorDetection.XgetVioletMat(mYuv,mViolet);
        	
        	ColorDetection.cvt_YUVtoRGBtoHSV(mYuv,mHsv);
        	ColorDetection.getVioletMat(mHsv,mColor);
        	ColorDetection.detectSingleBlob(mYuv, mColor, "V", mResult);
        	Imgproc.cvtColor(mResult, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);
            break;
            
            
        case VIEW_MODE_ALL:
        	/* 10/24/X2edit:
        	 * mBlue = new Mat();
        	mYellow = new Mat();
        	mRed = new Mat();
        	mGreen = new Mat();
        	mCyan = new Mat();
        	mViolet = new Mat();
        	mResult = new Mat();*/
        
        	
        	long startTime = System.currentTimeMillis();
        	ColorDetection.cvt_YUVtoRGBtoHSV(mYuv,mHsv);
        	
        	ColorDetection.getBlueMat(mHsv,mBlue);
        	ColorDetection.detectSingleBlob(mYuv, mBlue, "B", mResult);
        	
        	ColorDetection.getYellowMat(mHsv,mYellow);
        	ColorDetection.detectSingleBlob(mResult, mYellow, "Y", mResult);
        	
        	ColorDetection.getRedMat(mHsv,mRed);
        	ColorDetection.detectSingleBlob(mResult, mRed, "R", mResult);
        	
        	ColorDetection.getGreenMat(mHsv,mGreen);
        	ColorDetection.detectSingleBlob(mResult, mGreen, "G", mResult);
        	
        	ColorDetection.getCyanMat(mHsv,mCyan);
        	ColorDetection.detectSingleBlob(mResult, mCyan, "C", mResult);
        	
        	ColorDetection.getVioletMat(mHsv,mViolet);
        	ColorDetection.detectSingleBlob(mResult, mViolet, "V", mResult);
        	
        	Imgproc.cvtColor(mResult, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);
        	long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            Log.i(TAG, "time="+elapsedTime);
            
        	
            break;
            
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
