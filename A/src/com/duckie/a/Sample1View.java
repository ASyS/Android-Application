package com.duckie.a;

import org.opencv.android.Utils;
//import org.opencv.core.Core;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
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
    public static final int		VIEW_MODE_FINGERS	= 2;
    public static final int		VIEW_MODE_BLUE		= 3; 
    public static final int		VIEW_MODE_YELLOW	= 4;
    public static final int		VIEW_MODE_RED		= 5;
    public static final int		VIEW_MODE_GREEN		= 6;
    public static final int		VIEW_MODE_CYAN		= 7;
    public static final int		VIEW_MODE_VIOLET	= 8;

    private Mat mYuv;
    private Mat mRgba;
    private Mat mGraySubmat;
    private Mat mIntermediateMat;
	private Bitmap mBitmap;
	private int mViewMode;
	
	private Mat mColor,mYellow,mBlue,mRed,mGreen,mCyan,mViolet,mPink;
	private Mat mResult;
	
	private Mat mRgb;
	private Mat mHsv;
	

	private Point p_yellow, p_blue, p_red, p_green, p_cyan;
	
	char letter;
	

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
        	mPink = new Mat();
        	mResult = new Mat();
        	
        	
        	p_yellow = new Point();
        	p_blue = new Point();
        	p_red = new Point();
        	p_green = new Point();
        	p_cyan = new Point();
        	
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
            if (mPink != null)
            	mPink.release();
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
            printTextBL(mRgba, "Y");
            printTextUL(mRgba, "Z");
            printTextCL(mRgba, "X");
            
//            Core.putText(mRgba, "OpenCV + Android", new Point(10, 100), 3/* CV_FONT_HERSHEY_COMPLEX */, 2, new Scalar(255, 0, 0, 255), 3);
            break;
            
        case VIEW_MODE_FINGERS:
        	
        	/**  // simplified detect all colors
        	ColorDetection.cvt_YUVtoRGBtoHSV(mYuv,mHsv);
        	ColorDetection.detectAllBlobs(mYuv, mHsv, mResult);
        	stopTime = System.currentTimeMillis();
        	Imgproc.cvtColor(mResult, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);
        	 */
        	long startTime = System.currentTimeMillis();
        	ColorDetection.cvt_YUVtoRGBtoHSV(mYuv,mHsv);



        	ColorDetection.getGreenMat(mHsv,mColor);
        	ColorDetection.detectSinglePoint(mColor,p_green);

        	ColorDetection.getRedMat(mHsv,mColor);
        	ColorDetection.detectSinglePoint(mColor,p_red);

        	ColorDetection.getYellowMat(mHsv,mColor);
        	ColorDetection.detectSinglePoint(mColor,p_yellow);

        	ColorDetection.getBlueMat(mHsv,mColor);
        	ColorDetection.detectSinglePoint(mColor,p_blue);

        	ColorDetection.getCyanMat(mHsv,mColor);
        	ColorDetection.detectSinglePoint(mColor,p_cyan);

        	Core.putText(mYuv, "G", p_green, 4/*font*/, 1, new Scalar(255, 0, 0, 255), 3);
        	Core.putText(mYuv, "R", p_red, 4/*font*/, 1, new Scalar(255, 0, 0, 255), 3);
        	Core.putText(mYuv, "B", p_blue, 4/*font*/, 1, new Scalar(255, 0, 0, 255), 3);
        	Core.putText(mYuv, "Y", p_yellow, 4/*font*/, 1, new Scalar(255, 0, 0, 255), 3);
        	Core.putText(mYuv, "C", p_cyan, 4/*font*/, 1, new Scalar(255, 0, 0, 255), 3);

        	
////        	
//        	String t = "" + Math.hypot( (p_cyan.x - p_yellow.x) , (p_cyan.y - p_yellow.y));
//        	String t = "x=" + (p_yellow.x - p_cyan.x) + " y=" + (p_yellow.y - p_cyan.y);
//        	Core.putText(mYuv, t, new Point(10, 250), 4, 1, new Scalar(255, 0, 0, 255), 3);
//        	
        	
        	
        	ColorDetection.testIdentifyHandGesture(p_cyan, p_red, p_blue, p_green, p_yellow, mYuv);
        	

        	
//        	if ( ColorDetection.isNorthEastOf(p_yellow, p_green) &
//        			ColorDetection.isEastOf(p_green, p_blue) &
//        			ColorDetection.isEastOf(p_blue, p_red) &
//        			ColorDetection.isEastOf(p_red, p_cyan) 
//        			
//        			
//        			){
//        		Core.putText(mYuv, "A", new Point(10, 250), 4, 1, new Scalar(255, 0, 0, 255), 3);
//        	}
//        	
//        	else if (ColorDetection.isEastOf(p_green, p_blue) &
//        			ColorDetection.isEastOf(p_blue, p_red) &
//        			ColorDetection.isNorthEastOf(p_red, p_cyan) &
//        			
//        			(ColorDetection.isSouthOf(p_yellow,p_cyan) &
//        					ColorDetection.isSouthWestOf(p_yellow,p_green) &
//        					ColorDetection.isSouthOf(p_yellow,p_red) &
//        					ColorDetection.isSouthOf(p_yellow,p_blue) )
//        			
//        			){
//        		Core.putText(mYuv, "B", new Point(10, 250), 4, 1, new Scalar(255, 0, 0, 255), 3);
//        	}
//        	
//        	else if (ColorDetection.isEastOf(p_blue,p_red) &
//        			ColorDetection.isEastOf(p_red,p_cyan)&
//        			ColorDetection.isNorthEastOf(p_green,p_blue)&
//        			ColorDetection.isNorthWestOf(p_green,p_yellow)&
//        			!ColorDetection.isNearOf(p_green, p_blue)
//        			
//        			
//        			){
//        		
//        		Core.putText(mYuv, "L", new Point(10, 250), 4, 1, new Scalar(255, 0, 0, 255), 3);
//        	}
//        	
//        	else if ( ColorDetection.isSouthEastOf(p_yellow, p_green) &
//        			ColorDetection.isEastOf(p_green, p_blue) &
//        			ColorDetection.isEastOf(p_blue, p_red) &
//        			ColorDetection.isEastOf(p_red, p_cyan)
//        			){
//        		Core.putText(mYuv, "E", new Point(10, 250), 4, 1, new Scalar(255, 0, 0, 255), 3);
//        	}
//        	
//        	else if ( (ColorDetection.isNotVisible(p_red)&(ColorDetection.isNotVisible(p_cyan))) &
//        			
//        			ColorDetection.isNorthEastOf(p_blue, p_green) &
//        			ColorDetection.isEastOf(p_yellow, p_green)
//        			){
//        		Core.putText(mYuv, "Q", new Point(10, 250), 4, 1, new Scalar(255, 0, 0, 255), 3);
//        	}
//
//        	else if (ColorDetection.isNorthEastOf(p_red, p_cyan) &
//        			ColorDetection.isEastOf(p_blue,p_red) &
//        			ColorDetection.isNorthWestOf(p_blue, p_green) &
//        			ColorDetection.isSouthOf(p_yellow, p_green) 
//        			){
//        		Core.putText(mYuv, "F", new Point(10, 250), 4, 1, new Scalar(255, 0, 0, 255), 3);
//        	}        
        	
        	
        	
        	Imgproc.cvtColor(mYuv, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);
        	
        	long stopTime = System.currentTimeMillis();
        	long elapsedTime = stopTime - startTime;
            Log.i(TAG, "time="+elapsedTime);
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
        	
        	
        case VIEW_MODE_ALL:
        	/* 10/24/X2edit:
        	 * mBlue = new Mat();
        	mYellow = new Mat();
        	mRed = new Mat();
        	mGreen = new Mat();
        	mCyan = new Mat();
        	mViolet = new Mat();
        	mResult = new Mat();*/
        
        	
        	
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
        	
        	ColorDetection.getPinkMat(mHsv,mPink);
        	ColorDetection.detectSingleBlob(mResult, mPink, "P", mResult);
        
        	
        	Imgproc.cvtColor(mResult, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);

            
        	
            break;
          
        case VIEW_MODE_YELLOW:
        	
        	
        	/* 10/24/X2edit:
        	 * mYellow = new Mat();
        	mResult = new Mat();*/
//        	ColorDetection.XgetYellowMat(mYuv,mYellow);  

        	ColorDetection.cvt_YUVtoRGBtoHSV(mYuv,mHsv);
        	ColorDetection.getYellowMat(mHsv,mColor);

        	ColorDetection.detectSingleBlob(mYuv, mColor, "Y", mResult);
        	Imgproc.cvtColor(mResult, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);

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
    
    public void printTextBL(Mat img, String text){
    	final float bottom = 305.0f;		//320
    	final float left = 5.0f;
    	// Get the screen's density scale
    	final float scale = getFrameHeight()/320.0f;
    	// Convert the dps to pixels, based on density scale
    	int iBottom = (int) (bottom * scale + 0.5f);
    	int iLeft =  (int) (left * scale + 0.5f);
    	Core.putText(img, text, new Point(iLeft, iBottom), 4, 1.0f*scale, new Scalar(255, 0, 0, 255), 3);
    	Log.i(TAG, "scale: "+scale);
    	Log.i(TAG, "height: "+getHeight());
    	Log.i(TAG, "y: "+iBottom);
    	//mini 2: 285
    	//one x: 690
    }
    
    public void printTextUL(Mat img, String text){
    	final float top = 35.0f;
    	final float left = 5.0f;
    	// Get the screen's density scale
    	final float scale = getFrameHeight()/320.0f;
    	// Convert the dps to pixels, based on density scale
    	int iTop = (int) (top * scale + 0.5f);
    	int iLeft =  (int) (left * scale + 0.5f);
    	Core.putText(img, text, new Point(iLeft, iTop), 4, 1.0f*scale, new Scalar(255, 0, 0, 255), 3);
    }
    
    public void printTextCL(Mat img, String text){
    	final float center = 170f;		//180
    	final float left = 5.0f;
    	// Get the screen's density scale
    	final float scale = getFrameHeight()/320.0f;
    	// Convert the dps to pixels, based on density scale
    	int iCenter = (int) (center * scale + 0.5f);
    	int iLeft =  (int) (left * scale + 0.5f);
    	Core.putText(img, text, new Point(iLeft, iCenter), 4, 1.0f*scale, new Scalar(255, 0, 0, 255), 3);
    }
}
