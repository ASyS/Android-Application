package com.duckie.a;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;


public class ColorDetection {
	
	public static void getVioletMat(Mat src, Mat dst){
		Imgproc.cvtColor(src,dst,Imgproc.COLOR_YUV420sp2RGB);
    	Imgproc.cvtColor(dst,dst, Imgproc.COLOR_RGB2HSV);
    	Core.inRange(dst, new Scalar(130, 50, 50), new Scalar(150, 255, 255), dst);	//131-170-108---142-255-255
	}
	
	public static void getCyanMat(Mat src, Mat dst){
		Imgproc.cvtColor(src,dst,Imgproc.COLOR_YUV420sp2RGB);
    	Imgproc.cvtColor(dst,dst, Imgproc.COLOR_RGB2HSV);
    	Core.inRange(dst, new Scalar(85, 131, 125), new Scalar(98, 255, 255), dst);
	}
	
	public static void getGreenMat(Mat src, Mat dst){
		Imgproc.cvtColor(src,dst,Imgproc.COLOR_YUV420sp2RGB);
    	Imgproc.cvtColor(dst,dst, Imgproc.COLOR_RGB2HSV);
    	Core.inRange(dst, new Scalar(40, 80, 100), new Scalar(80, 255, 255), dst);	//49-109-61---70-255-255
	}
	
	public static void getBlueMat(Mat src, Mat dst){
		Imgproc.cvtColor(src,dst,Imgproc.COLOR_YUV420sp2RGB);
    	Imgproc.cvtColor(dst,dst, Imgproc.COLOR_RGB2HSV);
    	Core.inRange(dst, new Scalar(100, 100, 100), new Scalar(120, 255, 255), dst);
	}
	
	
	public static void getYellowMat(Mat src, Mat dst){
//		Mat mMattemp = new Mat();
		Imgproc.cvtColor(src,dst,Imgproc.COLOR_YUV420sp2RGB);
    	Imgproc.cvtColor(dst,dst, Imgproc.COLOR_RGB2HSV);
    	Core.inRange(dst, new Scalar(20, 100, 100), new Scalar(30, 255, 255), dst);
//		Mat mMattemp_rgb = new Mat();
//		Mat mMattemp_hsv = new Mat();
//		Mat mMattemp_ir = new Mat();
//		Imgproc.cvtColor(m,mMattemp_rgb,Imgproc.COLOR_YUV420sp2RGB);
//    	Imgproc.cvtColor(mMattemp_rgb,mMattemp_hsv, Imgproc.COLOR_RGB2HSV);
//    	Core.inRange(mMattemp_hsv, new Scalar(20, 100, 100), new Scalar(30, 255, 255), mMattemp_ir);
//		return mMattemp_ir;
	}
	
	
	public static void getRedMat(Mat src, Mat dst){
		Mat c1 = new Mat();
		Mat c2 = new Mat();
		Imgproc.cvtColor(src,dst,Imgproc.COLOR_YUV420sp2RGB);
    	Imgproc.cvtColor(dst,dst, Imgproc.COLOR_RGB2HSV);
    	Core.inRange(dst, new Scalar(0, 100, 100), new Scalar(10, 255, 255), c1); //0-130-179---6-255-255
    	Core.inRange(dst, new Scalar(170, 100, 100), new Scalar(180, 255, 255), c2); //177-200-120---183-255-255
    	Core.bitwise_or(c1,c2,dst);
	}

//	public static Mat getRedMat(Mat m){
//		Mat mRgb = new Mat();
//		Mat hsv = new Mat();
//		Mat c1 = new Mat();
//		Mat c2 = new Mat();
//		Mat colorFiltered = new Mat();
//		
//		Imgproc.cvtColor(m,mRgb,Imgproc.COLOR_YUV420sp2RGB);
//		Imgproc.cvtColor(mRgb, hsv, Imgproc.COLOR_RGB2HSV);
//		
//		Core.inRange(hsv, new Scalar(0, 175, 179), new Scalar(4, 255, 255), c1);
//		Core.inRange(hsv, new Scalar(177, 216, 165), new Scalar(181, 255, 255), c2);
//		Core.add(c1,c2,colorFiltered);
//		return colorFiltered;
//	}
}
