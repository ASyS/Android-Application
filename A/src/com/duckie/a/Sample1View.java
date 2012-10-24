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
    public static final int		VIEW_MODE_RED		= 1;
    public static final int		VIEW_MODE_YELLOW	= 2;
    public static final int		VIEW_MODE_BLUE		= 3;
    public static final int		VIEW_MODE_GREEN		= 4;
    public static final int		VIEW_MODE_CYAN		= 5;
    public static final int		VIEW_MODE_VIOLET	= 6;

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
        mYuv.put(0, 0, data);
        
        final int viewMode = mViewMode;

        switch (viewMode) {
//        case VIEW_MODE_GRAY:
//        	ColorDetection.getVioletMat(mYuv,mRgba);
//            break;
        case VIEW_MODE_RGBA:
            Imgproc.cvtColor(mYuv, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);
//            Core.putText(mRgba, "OpenCV + Android", new Point(10, 100), 3/* CV_FONT_HERSHEY_COMPLEX */, 2, new Scalar(255, 0, 0, 255), 3);
            break;
//        case VIEW_MODE_CANNY:
//            Imgproc.Canny(mGraySubmat, mIntermediateMat, 80, 100);
//            Imgproc.cvtColor(mIntermediateMat, mRgba, Imgproc.COLOR_GRAY2BGRA, 4);
//            break;  
        case VIEW_MODE_RED:
        	ColorDetection.getRedMat(mYuv,mRgba);
            break;
        case VIEW_MODE_YELLOW:
        	ColorDetection.getYellowMat(mYuv,mRgba);
            break;
        case VIEW_MODE_BLUE:
        	ColorDetection.getBlueMat(mYuv,mRgba);
            break;
        case VIEW_MODE_GREEN:
//        	ColorDetection.getRedMat1(mYuv,mRgba);
        	ColorDetection.getGreenMat(mYuv,mRgba);
//        	ColorDetection.getGreenMat_YCRCB(mYuv,mRgba);
            break;
        case VIEW_MODE_CYAN:
        	ColorDetection.getCyanMat(mYuv,mRgba);
            break;
        case VIEW_MODE_VIOLET:
        	ColorDetection.getVioletMat(mYuv,mRgba);
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
