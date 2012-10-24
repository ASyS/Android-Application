package com.duckie.a;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.core.Rect;
import org.opencv.core.*;



public class ColorDetection {
//	Mat detectSingleBlob(Mat orig, Mat image)
//	{
//		
//		//Mat image, List<MatOfPoint> contours, Mat hierarchy, int mode, int method, Point offset
//		List<MatOfPoint> contours = new ArrayList<MatOfPoint>(); //vector<vector<Point> > contours;
//		Mat hierarchy = new Mat();
//		Mat temp = new Mat();
//		image.copyTo(temp);
//
//		Imgproc.findContours(temp, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0) );
//
//		Rect boundRect = new Rect();//( contours.size() );
////		vector<Point2f>center( contours.size() );
////		vector<float>radius( contours.size() );
//
//		Core.Rec
//		
//		
//		int i = getBiggestContour(contours);
//		boundRect[i] = boundingRect( Mat(contours[i]) );
//		rectangle(orig, boundRect[i].tl(), boundRect[i].br(), Scalar(255, 255, 255), 2, 8, 0 );
//		minEnclosingCircle( contours[i], center[i], radius[i] );
//		circle(orig, center[i], 1, Scalar(255, 255, 255), -1, 8, 0 );
//
//		System.out.println((i+1) + ": (" + center[i].x + ", " + center[i].y + ")");
//
//		namedWindow( "Border", CV_WINDOW_AUTOSIZE );
//		imshow( "Border", orig);
//
//		return orig;
//	}
	
	
	
	
//	MatOfPoint2f getCentroidPoint(Mat contours) {
//		Moments mu;
//		mu = Imgproc.moments(contours,false);
//		
//		MatOfPoint2f mc;
//		mc = MatOfPoint2f(mu.get_m10() / mu.get_m00(), mu.get_m01() / mu.get_m00());
//		return mc;
//	}
	
	int getBiggestContour(List<List<Point> > contours) {
		int indexOfBiggestContour = -1;
		int sizeOfBiggestContour = 0;
		for (int i = 0; i < contours.size(); i++) {
			if (contours.get(i).size() > sizeOfBiggestContour) {
				sizeOfBiggestContour = contours.get(i).size();
				indexOfBiggestContour = i;
			}
		}
		return indexOfBiggestContour;
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
