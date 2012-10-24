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
	
	public static int getBiggestContourIndex(List<MatOfPoint> contours){
		// Find max contour area
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
        Log.i(TAG, "k="+k+" area="+maxArea);
        return k;
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
        Core.putText(src, center.x+"|"+center.y, new Point(10, 200), 4/*font*/, 1, new Scalar(255, 0, 0, 255), 3);

        
        
        Core.putText(src, "Y", boundRect.tl(), 0/*font*/, 1, new Scalar(255, 0, 0, 255), 3);
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
//		Imgproc.minEnclosingCircle( x /*contours.get(i)*/, center, radius );
//		Core.circle(orig, center, 1, new Scalar(255, 255, 255), -1, 8, 0 );
//
//		System.out.println((i+1) + ": (" + center.x + ", " + center.y + ")");

		return src;
	}
	
	public static void getCenterPoint(Point tl, Point br, Point dst){
		dst.x = (tl.x + br.x)/2;
		dst.y = (tl.y + br.y)/2;
	}
	
	
	
	public static void getVioletMat(Mat src, Mat dst){
		Imgproc.cvtColor(src,dst,Imgproc.COLOR_YUV420sp2RGB);
    	Imgproc.cvtColor(dst,dst, Imgproc.COLOR_RGB2HSV);
    	Core.inRange(dst, new Scalar(130, 40, 40), new Scalar(150, 255, 255), dst);	//131-170-108---142-255-255
	}
	
	public static void getCyanMat(Mat src, Mat dst){
		Imgproc.cvtColor(src,dst,Imgproc.COLOR_YUV420sp2RGB);
    	Imgproc.cvtColor(dst,dst, Imgproc.COLOR_RGB2HSV);
    	Core.inRange(dst, new Scalar(85, 131, 125), new Scalar(98, 255, 255), dst);
	}
	
	public static void getGreenMat(Mat src, Mat dst){
		Imgproc.cvtColor(src,dst,Imgproc.COLOR_YUV420sp2RGB);
    	Imgproc.cvtColor(dst,dst, Imgproc.COLOR_RGB2HSV);
    	Core.inRange(dst, new Scalar(50, 145, 90), new Scalar(75, 255, 255), dst);	//49-109-61---70-255-255
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
	
	public static void getRedMat_YCRCB(Mat src, Mat dst){
		Mat bgr = new Mat();
		Mat ycrcb = new Mat();
		
		Imgproc.cvtColor(src, bgr, Imgproc.COLOR_YUV420sp2BGR);//CV_BGR2YCrCb);
		Imgproc.cvtColor(bgr, ycrcb, Imgproc.COLOR_BGR2YCrCb);
		Core.inRange(ycrcb, new Scalar(0, 230, 0), new Scalar(255, 255, 120), dst);
	}
	
	public static void getGreenMat_YCRCB(Mat src, Mat dst){
		Mat bgr = new Mat();
		Mat ycrcb = new Mat();
		
		Imgproc.cvtColor(src, bgr, Imgproc.COLOR_YUV420sp2BGR);//CV_BGR2YCrCb);
		Imgproc.cvtColor(bgr, ycrcb, Imgproc.COLOR_BGR2YCrCb);
		Core.inRange(ycrcb, new Scalar(0, 0, 0), new Scalar(255, 120, 120), dst);
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
	

