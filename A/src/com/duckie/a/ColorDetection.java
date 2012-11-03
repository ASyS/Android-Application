package com.duckie.a;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.core.*;
import android.util.Log;


public class ColorDetection {
	private static final String TAG = "CD";
//	private static Mat mYellow;
	private static Mat mSrc;
	
	private static final double nearThresh = 22.0;
	
	
	public static boolean isNearOf(Point is, Point of){
		if ( (Math.abs(is.x - of.x) < nearThresh) & (Math.abs(is.y - of.y) < nearThresh) )
			return true;
		return false;
	}
	
	
	public static void detectSinglePoint(Mat src, Point center){
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>(); //vector<vector<Point> > contours;
		Mat hierarchy = new Mat();
		Imgproc.findContours(src, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        int k = getBiggestContourIndex(contours);
        Rect boundRect = setContourRect(contours, k);
        getCenterPoint(boundRect.tl(), boundRect.br(), center);
	}
	
	
	
	/* 10/27/X2edit: added */
	public static void cvt_YUVtoRGBtoHSV(Mat src, Mat dst){
		mSrc = new Mat();
		src.copyTo(mSrc); 
		Imgproc.cvtColor(mSrc,dst,Imgproc.COLOR_YUV420sp2RGB);
    	Imgproc.cvtColor(dst,dst, Imgproc.COLOR_RGB2HSV);
	}
	
	public static void getRedMat(Mat src, Mat dst){
//		mSrc = new Mat(); // added: oct25 11am
//		src.copyTo(mSrc); // added: oct25 11am
		
		Mat c1 = new Mat();
		Mat c2 = new Mat();
    	Core.inRange(src, new Scalar(0, 100, 100), new Scalar(10, 255, 255), c1); //0-130-179---6-255-255
    	Core.inRange(src, new Scalar(170, 100, 100), new Scalar(180, 255, 255), c2); //177-200-120---183-255-255
    	Core.bitwise_or(c1,c2,dst);
	}
	
	public static void getVioletMat(Mat src, Mat dst){
    	Core.inRange(src, new Scalar(130, 40, 40), new Scalar(150, 255, 255), dst);	//131-170-108---142-255-255
	}
	
	public static void getCyanMat(Mat src, Mat dst){
		Mat c1 = new Mat();
		Mat c2 = new Mat();
    	Core.inRange(src, new Scalar(88, 144, 161), new Scalar(94, 255, 179), c1); //177-200-120---183-255-255
    	Core.inRange(src, new Scalar(85, 144, 180), new Scalar(96, 255, 255), c2);
    	Core.bitwise_or(c1,c2,dst);
		//Core.inRange(src, new Scalar(84, 144, 180), new Scalar(96, 255, 255), dst); //[85-98],[131-255],[125-255]
    	//Core.inRange(src, new Scalar(85, 131, 125), new Scalar(105, 255, 255), dst); //[85-98],[131-255],[125-255]
	}
	
	public static void getGreenMat(Mat src, Mat dst){
		//Core.inRange(src, new Scalar(50, 145, 90), new Scalar(75, 255, 255), dst);	//49-109-61---70-255-255
    	Core.inRange(src, new Scalar(44, 135, 160), new Scalar(69, 255, 255), dst);	//(50, 145, 90)(75, 255, 255) //49-109-61---70-255-255
	}
	
	public static void getBlueMat(Mat src, Mat dst){
    	Core.inRange(src, new Scalar(100, 100, 100), new Scalar(120, 255, 255), dst); //[106-125],[100-255],[100-255]
	}
	
	
	public static void getYellowMat(Mat src, Mat dst){
    	Core.inRange(src, new Scalar(28, 126, 180), new Scalar(34, 255, 255), dst); //(20, 100, 100), new Scalar(30, 255, 255)
	}
	
	public static void getPinkMat(Mat src, Mat dst){
		Core.inRange(src, new Scalar(148, 141, 231), new Scalar(163, 255, 255), dst); //177-200-120---183-255-255
	}
	
	public static void detectAllBlobs(Mat mYuv, Mat mHsv, Mat dst) {
		Mat mColor = new Mat();
		Mat mResult = new Mat();
		
		getBlueMat(mHsv,mColor);
		detectSingleBlob(mYuv, mColor, "B", mResult);
		
		getYellowMat(mHsv,mColor);
		detectSingleBlob(mResult, mColor, "Y", mResult);
		
		getRedMat(mHsv,mColor);
		detectSingleBlob(mResult, mColor, "R", mResult);
		
		getGreenMat(mHsv,mColor);
		detectSingleBlob(mResult, mColor, "G", mResult);
		
		getCyanMat(mHsv,mColor);
		detectSingleBlob(mResult, mColor, "C", mResult);
		
		getVioletMat(mHsv,mColor);
		detectSingleBlob(mResult, mColor, "V", dst);		
	}
	
	/**
	 *  if wala ba ang point
	 * 
	 */

	public static boolean isNotVisible(Point p){
		if (p.x == 0 & p.y ==0){
			return true;
		}
		return false;
	}
	
	public static boolean isVisible(Point p){
		return !(isNotVisible(p));
	}
	
	
	/**
	 * "of" is reference point
	 * is "is" East of "of"?
	 */	
	public static boolean isEastOf(Point is, Point of){
		if (isNotVisible(is))	// temporary dirty code
			return false;		// temporary dirty code
		if (isNotVisible(of))	// temporary dirty code
			return false;		// temporary dirty code
		
		
		if (is.x > of.x){
			return true;
		}
		return false;
	}
	
	/**
	 * "of" is reference point
	 * is "is" West of "of"?
	 */	
	public static boolean isWestOf(Point is, Point of){
		if (isNotVisible(is))	// temporary dirty code
			return false;		// temporary dirty code
		if (isNotVisible(of))	// temporary dirty code
			return false;		// temporary dirty code
		
		
		if (is.x < of.x){
			return true;
		}
		return false;
	}
	
	/**
	 * "of" is reference point
	 * is "is" North of "of"?
	 */	
	public static boolean isNorthOf(Point is, Point of){
		if (isNotVisible(is))	// temporary dirty code
			return false;		// temporary dirty code
		if (isNotVisible(of))	// temporary dirty code
			return false;		// temporary dirty code
		
		
		if (is.y < of.y){
			return true;
		}
		return false;
	}
	
	/**
	 * "of" is reference point
	 * is "is" South of "of"?
	 */	
	public static boolean isSouthOf(Point is, Point of){
		if (isNotVisible(is))	// temporary dirty code
			return false;		// temporary dirty code
		if (isNotVisible(of))	// temporary dirty code
			return false;		// temporary dirty code
		
		
		if (is.y > of.y){
			return true;
		}
		return false;
	}
	
	public static boolean isNorthEastOf(Point is, Point of){return isNorthOf(is,of) & isEastOf(is,of);}
	public static boolean isNorthWestOf(Point is, Point of){return isNorthOf(is,of) & isWestOf(is,of);}
	public static boolean isSouthEastOf(Point is, Point of){return isSouthOf(is,of) & isEastOf(is,of);}
	public static boolean isSouthWestOf(Point is, Point of){return isSouthOf(is,of) & isWestOf(is,of);}
	
	
	public static void detectSingleBlob(Mat src, Mat image, String text, Mat dst){
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>(); //vector<vector<Point> > contours;
		Mat hierarchy = new Mat();
//		Mat tempMat = new Mat();
		src.copyTo(dst);
		
		Imgproc.findContours(image, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        int k = getBiggestContourIndex(contours);
        Rect boundRect = setContourRect(contours, k);

        Point center = new Point();
        getCenterPoint(boundRect.tl(), boundRect.br(), center);
        Core.rectangle(dst, boundRect.tl(), boundRect.br(), new Scalar(255, 255, 0), 2, 8, 0 );
//        Core.putText(dst, center.x+"|"+center.y, new Point(10, 200), 4/*font*/, 1, new Scalar(255, 0, 0, 255), 3);
        
        Core.putText(dst, text, boundRect.tl(), 0/*font*/, 1, new Scalar(255, 0, 0, 255), 3);
//        Log.i(TAG, "x="+boundRect.tl().x+" y="+boundRect.tl().y);

	}
	
	public static void getCenterPoint(Point tl, Point br, Point dst){
		dst.x = (tl.x + br.x)/2;
		dst.y = (tl.y + br.y)/2;
	}
	
	public static int getBiggestContourIndex(List<MatOfPoint> contours){
        double maxArea = 0;
        Iterator<MatOfPoint> each = contours.iterator();
        int j = 0;
        int k = -1;
        while (each.hasNext())
        {
        	MatOfPoint wrapper = each.next();
        	double area = Imgproc.contourArea(wrapper);
        	if (area > maxArea){
        		maxArea = area;
        		k = j;
        	}
        	j++;
        }
//        Log.i(TAG, "k="+k+" area="+maxArea);
        return k;
	}
	
	public static Rect setContourRect(List<MatOfPoint> contours,int k){
		Rect boundRect = new Rect();
		Iterator<MatOfPoint> each = contours.iterator();
        int j = 0;
        while (each.hasNext()){
        	MatOfPoint wrapper = each.next();
        	if (j==k){
        		return Imgproc.boundingRect( wrapper );
        	}
        	j++;
        }
        return boundRect;
	}
	
}
//public static void XgetVioletMat(Mat src, Mat dst){
//	mSrc = new Mat(); // added: oct25 11am
//	src.copyTo(mSrc); // added: oct25 11am
//	
//	Imgproc.cvtColor(mSrc,dst,Imgproc.COLOR_YUV420sp2RGB);
//	Imgproc.cvtColor(dst,dst, Imgproc.COLOR_RGB2HSV);
//	Core.inRange(dst, new Scalar(130, 40, 40), new Scalar(150, 255, 255), dst);	//131-170-108---142-255-255
//}
//
//public static void XgetCyanMat(Mat src, Mat dst){
//	mSrc = new Mat(); // added: oct25 11am
//	src.copyTo(mSrc); // added: oct25 11am
//	
//	Imgproc.cvtColor(mSrc,dst,Imgproc.COLOR_YUV420sp2RGB);
//	Imgproc.cvtColor(dst,dst, Imgproc.COLOR_RGB2HSV);
//	Core.inRange(dst, new Scalar(85, 131, 125), new Scalar(98, 255, 255), dst);
//}
//
//public static void XgetGreenMat(Mat src, Mat dst){
//	mSrc = new Mat(); // added: oct25 11am
//	src.copyTo(mSrc); // added: oct25 11am
//	
//	Imgproc.cvtColor(mSrc,dst,Imgproc.COLOR_YUV420sp2RGB);
//	Imgproc.cvtColor(dst,dst, Imgproc.COLOR_RGB2HSV);
//	Core.inRange(dst, new Scalar(50, 145, 90), new Scalar(75, 255, 255), dst);	//49-109-61---70-255-255
//}
//
//public static void XgetBlueMat(Mat src, Mat dst){
//	mSrc = new Mat(); // added: oct25 11am
//	src.copyTo(mSrc); // added: oct25 11am
//	
//	Imgproc.cvtColor(mSrc,dst,Imgproc.COLOR_YUV420sp2RGB);
//	Imgproc.cvtColor(dst,dst, Imgproc.COLOR_RGB2HSV);
//	Core.inRange(dst, new Scalar(100, 100, 100), new Scalar(120, 255, 255), dst);
//}
//
//
//public static void XgetYellowMat(Mat src, Mat dst){
//	mSrc = new Mat();
//	src.copyTo(mSrc);
//	
//	Imgproc.cvtColor(mSrc,dst,Imgproc.COLOR_YUV420sp2RGB);
//	Imgproc.cvtColor(dst,dst, Imgproc.COLOR_RGB2HSV);
//	Core.inRange(dst, new Scalar(20, 100, 100), new Scalar(30, 255, 255), dst);
//}
//
//public static void getRedMat_YCRCB(Mat src, Mat dst){
//	Mat bgr = new Mat();
//	Mat ycrcb = new Mat();
//	
//	Imgproc.cvtColor(src, bgr, Imgproc.COLOR_YUV420sp2BGR);//CV_BGR2YCrCb);
//	Imgproc.cvtColor(bgr, ycrcb, Imgproc.COLOR_BGR2YCrCb);
//	Core.inRange(ycrcb, new Scalar(0, 230, 0), new Scalar(255, 255, 120), dst);
//}
//
//public static void getGreenMat_YCRCB(Mat src, Mat dst){
//	Mat bgr = new Mat();
//	Mat ycrcb = new Mat();
//	
//	Imgproc.cvtColor(src, bgr, Imgproc.COLOR_YUV420sp2BGR);//CV_BGR2YCrCb);
//	Imgproc.cvtColor(bgr, ycrcb, Imgproc.COLOR_BGR2YCrCb);
//	Core.inRange(ycrcb, new Scalar(0, 0, 0), new Scalar(255, 120, 120), dst);
//}
//
//
//public static void XgetRedMat(Mat src, Mat dst){
//	mSrc = new Mat(); // added: oct25 11am
//	src.copyTo(mSrc); // added: oct25 11am
//	
//	Mat c1 = new Mat();
//	Mat c2 = new Mat();
//	Imgproc.cvtColor(mSrc,dst,Imgproc.COLOR_YUV420sp2RGB);
//	Imgproc.cvtColor(dst,dst, Imgproc.COLOR_RGB2HSV);
//	Core.inRange(dst, new Scalar(0, 100, 100), new Scalar(10, 255, 255), c1); //0-130-179---6-255-255
//	Core.inRange(dst, new Scalar(170, 100, 100), new Scalar(180, 255, 255), c2); //177-200-120---183-255-255
//	Core.bitwise_or(c1,c2,dst);
//}
//
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
	
//	MatOfPoint2f getCentroidPoint(Mat contours) {
//	Moments mu;
//	mu = Imgproc.moments(contours,false);
//	
//	MatOfPoint2f mc;
//	mc = MatOfPoint2f(mu.get_m10() / mu.get_m00(), mu.get_m01() / mu.get_m00());
//	return mc;
//}

//int getBiggestContour(List<List<Point> > contours) {
//	int indexOfBiggestContour = -1;
//	int sizeOfBiggestContour = 0;
//	for (int i = 0; i < contours.size(); i++) {
//		if (contours.get(i).size() > sizeOfBiggestContour) {
//			sizeOfBiggestContour = contours.get(i).size();
//			indexOfBiggestContour = i;
//		}
//	}
//	return indexOfBiggestContour;
//}
	

/*

	public static Mat detectSingleBlob(Mat src, Mat image) //, Mat dst)
	{
		

//		//Mat image, List<MatOfPoint> contours, Mat hierarchy, int mode, int method, Point offset
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>(); //vector<vector<Point> > contours;
		Mat hierarchy = new Mat();
		Mat tempMat = new Mat();
		image.copyTo(tempMat);
		
		Imgproc.findContours(tempMat, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        int k = getBiggestContourIndex(contours);
        
//        Rect boundRect = new Rect();
        Rect boundRect = setContourRect(contours, k);
//        setContourRect(boundRect,contours,k);
        
        
//        Iterator<MatOfPoint> each = contours.iterator();
//        int j = 0;
//        while (each.hasNext())
//        {
//        	MatOfPoint wrapper = each.next();
//
//        	if (j==k){
//        		 
//        		 boundRect = Imgproc.boundingRect( wrapper );
////        		 MatOfPoint2f  y = new MatOfPoint2f( wrapper.toArray() );
////        		 Imgproc.minEnclosingCircle(y, center, radius);
//        	}
//        	j++;
//        }
       
        Point center = new Point();
        getCenterPoint(boundRect.tl(), boundRect.br(), center);
        
//        Core.circle(src, center, 1, new Scalar(255, 255, 255), -1, 8, 0 );
        Core.rectangle(src, boundRect.tl(), boundRect.br(), new Scalar(255, 255, 0), 2, 8, 0 );
        Core.putText(src, center.x+"|"+center.y, new Point(10, 200), 4, 1, new Scalar(255, 0, 0, 255), 3);

        
        
        Core.putText(src, "Y", boundRect.tl(), 0, 1, new Scalar(255, 0, 0, 255), 3);
        Log.i(TAG, "x="+boundRect.tl().x+" y="+boundRect.tl().y);
//		Mat temp = new Mat();
//		image.copyTo(temp);
//		Imgproc.findContours(temp, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0) );
//
//		Rect boundRect = new Rect();//( contours.size() );
//		Point center = new Point();
//		float[] radius = new float[1];
//
//		int i = 0;//getBiggestContour(contours);
//		boundRect = Imgproc.boundingRect( contours.get(i) );
//		Core.rectangle(orig, boundRect.tl(), boundRect.br(), new Scalar(255, 255, 255), 2, 8, 0 );
//		
//		MatOfPoint2f x = new MatOfPoint2f();
//		contours.get(i).convertTo(x,CvType.CV_32FC2);
//		Imgproc.minEnclosingCircle( x , center, radius );
//		Core.circle(orig, center, 1, new Scalar(255, 255, 255), -1, 8, 0 );
//
//		System.out.println((i+1) + ": (" + center.x + ", " + center.y + ")");

		return src;
	}
	
	
	
		public static Rect setContourRect(List<MatOfPoint> contours,int k){
		Rect boundRect = new Rect();
		Iterator<MatOfPoint> each = contours.iterator();
        int j = 0;
        while (each.hasNext()){
        	MatOfPoint wrapper = each.next();
        	
        	if (j==k){
        		 
//        		 boundRect = Imgproc.boundingRect( wrapper );
        		return Imgproc.boundingRect( wrapper );
//        		 MatOfPoint2f  y = new MatOfPoint2f( wrapper.toArray() );
//        		 Imgproc.minEnclosingCircle(y, center, radius);
        	}
        	j++;
        }
        return boundRect;
	}
	
		public static void getYellowMat(Mat src, Mat dst){
		mSrc = new Mat();
		src.copyTo(mSrc);
		
//		Mat mMattemp = new Mat();
		Imgproc.cvtColor(mSrc,dst,Imgproc.COLOR_YUV420sp2RGB);
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


*/