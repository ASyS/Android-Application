package com.duckie.b;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class IP {
	
	private static final String TAG = "CD";
	private static Mat mSrc;
	
	
	public static void cvt_YUVtoHSV(Mat src, Mat dst){
		/** convert YUV image to RGB then HSV colorspace */
		mSrc = new Mat();
		src.copyTo(mSrc); 
		Imgproc.cvtColor(mSrc,dst,Imgproc.COLOR_YUV420sp2RGB);
		Imgproc.cvtColor(dst,dst, Imgproc.COLOR_RGB2HSV);
	}
	

	public static void getGreenMat(Mat src, Mat dst){
		Core.inRange(src, new Scalar(50, 50, 50), new Scalar(85, 255, 255), dst);
	}
	
	public static void getBlueMat(Mat src, Mat dst){
		/** light green |<--------------------->| purple*/
		Core.inRange(src, new Scalar(90, 50, 50), new Scalar(160, 255, 255), dst);
	}
	
	public static void getBlackMat(Mat src, Mat dst){
		Core.inRange(src, new Scalar(0, 0, 0), new Scalar(255, 100, 70), dst);
	}
}
