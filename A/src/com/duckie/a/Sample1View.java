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

class Sample1View extends SampleViewBase {

    public static final int     VIEW_MODE_RGBA  	= 0;
//    public static final int     VIEW_MODE_GRAY  = 1;
//    public static final int     VIEW_MODE_CANNY = 2;
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
	
//	private Mat mMattemp, hsv, bw;

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
        	
        	
        	
//        	mMattemp = new Mat();
//        	hsv = new Mat();
//        	bw = new Mat();
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
            
//            mMattemp = null;
//        	hsv = null;
//        	bw = null;
            
            
        }
    }

    @Override
    protected Bitmap processFrame(byte[] data) {
    	Mat mYellow,mBlue,mRed,mGreen,mCyan,mViolet;
    	Mat mResult;
        mYuv.put(0, 0, data);
        
        final int viewMode = mViewMode;

        switch (viewMode) {
        case VIEW_MODE_RGBA:
            Imgproc.cvtColor(mYuv, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);
//            Core.putText(mRgba, "OpenCV + Android", new Point(10, 100), 3/* CV_FONT_HERSHEY_COMPLEX */, 2, new Scalar(255, 0, 0, 255), 3);
            break;
        case VIEW_MODE_RED:
//        	ColorDetection.getRedMat(mYuv,mRgba);
        	
        	mRed = new Mat();
        	mResult = new Mat();
        	ColorDetection.getRedMat(mYuv,mRed);
        	ColorDetection.detectSingleBlob(mYuv, mRed, "R", mResult);
        	Imgproc.cvtColor(mResult, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);
            break;
        case VIEW_MODE_YELLOW:
        	mYellow = new Mat();
        	mResult = new Mat();
        	ColorDetection.getYellowMat(mYuv,mYellow);
        	ColorDetection.detectSingleBlob(mYuv, mYellow, "Y", mResult);
        	Imgproc.cvtColor(mResult, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);
            break;
        case VIEW_MODE_BLUE:
//        	ColorDetection.getBlueMat(mYuv,mRgba);
        	
        	mBlue = new Mat();
        	mResult = new Mat();
        	ColorDetection.getBlueMat(mYuv,mBlue);
        	ColorDetection.detectSingleBlob(mYuv, mBlue, "B", mResult);
        	Imgproc.cvtColor(mResult, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);
            break;
        case VIEW_MODE_GREEN:
//        	ColorDetection.getGreenMat(mYuv,mRgba);
        	
        	mGreen = new Mat();
        	mResult = new Mat();
        	ColorDetection.getGreenMat(mYuv,mGreen);
        	ColorDetection.detectSingleBlob(mYuv, mGreen, "G", mResult);
        	Imgproc.cvtColor(mResult, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);

        	
            break;
        case VIEW_MODE_CYAN:
//        	ColorDetection.getCyanMat(mYuv,mRgba); 
        	
        	mCyan = new Mat();
        	mResult = new Mat();
        	ColorDetection.getCyanMat(mYuv,mCyan);
        	ColorDetection.detectSingleBlob(mYuv, mCyan, "C", mResult);
        	Imgproc.cvtColor(mResult, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);
        	
        	
        	break;
        case VIEW_MODE_VIOLET:
//        	ColorDetection.getVioletMat(mYuv,mRgba);
        	
        	mViolet = new Mat();
        	mResult = new Mat();
        	ColorDetection.getVioletMat(mYuv,mViolet);
        	ColorDetection.detectSingleBlob(mYuv, mViolet, "V", mResult);
        	Imgproc.cvtColor(mResult, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);
            break;
            
            
        case VIEW_MODE_ALL:
        	mBlue = new Mat();
        	mYellow = new Mat();
        	mRed = new Mat();
        	mGreen = new Mat();
        	mCyan = new Mat();
        	mViolet = new Mat();
        	mResult = new Mat();
        	
        	ColorDetection.getBlueMat(mYuv,mBlue);
        	ColorDetection.detectSingleBlob(mYuv, mBlue, "B", mResult);
        	
        	ColorDetection.getYellowMat(mYuv,mYellow);
        	ColorDetection.detectSingleBlob(mResult, mYellow, "Y", mResult);
        	
        	ColorDetection.getRedMat(mYuv,mRed);
        	ColorDetection.detectSingleBlob(mResult, mRed, "R", mResult);
        	
        	ColorDetection.getGreenMat(mYuv,mGreen);
        	ColorDetection.detectSingleBlob(mResult, mGreen, "G", mResult);
        	
        	ColorDetection.getCyanMat(mYuv,mCyan);
        	ColorDetection.detectSingleBlob(mResult, mCyan, "C", mResult);
        	
        	ColorDetection.getVioletMat(mYuv,mViolet);
        	ColorDetection.detectSingleBlob(mResult, mViolet, "V", mResult);
        	
        	Imgproc.cvtColor(mResult, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);
        	
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
