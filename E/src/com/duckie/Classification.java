package com.duckie;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import android.util.Log;

public class Classification {

	static int pX;
	static int pY;
	static Mat binary = new Mat();

	public static int classify(Mat src, Mat object) {
		// Find height of frame and hand
		int imgHeight = src.rows();
		int handHeight = object.rows();

		if (object.size().area() == 0)
			return 0;
		else if (isMotionClass(object, src)) {
			return 1;
		} else if (isHeightClass(imgHeight, handHeight)) {
			return 2;
		}
		// else if (isMidHeightClass(imgHeight, handHeight)){
		// return 3;
		// }
		else if (isBGClass(object)) {
			return 3;
		} else {
			if (isCentroidClass(object)) {
				return 4;
			} else
				return 5;
		}
	}

	public static boolean isMotionClass(Mat object, Mat src) {
    	Mat mHand = new Mat(), mHSV = new Mat();
    	String currDir;
		//Imgproc.cvtColor(mYuv, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);
    	Imgproc.cvtColor(src, mHSV, Imgproc.COLOR_RGB2HSV);
    	//IP.getBlueMat(mHsv, mHand);   
    	Core.inRange(mHSV, new Scalar(90, 50, 50), new Scalar(160, 255, 255), mHand);
    	Imgproc.threshold(mHand, mHand, 0, 255, Imgproc.THRESH_BINARY_INV);
    	MotionClass.detectMotion(mHand, src);
    	currDir = MotionClass.getCurrentDirection();
    	System.out.println("Motion: "+currDir.contains("Static"));
    	if(currDir.contains("Static")){
    		return false;
    	}
    	else
    		return true;
	}

	public static boolean isHeightClass(int imgHeight, int handHeight) {
		float thresh = 0.85f;
		if ((float) handHeight / imgHeight > thresh)
			return true;
		else
			return false;
	}

	public static boolean isBGClass(Mat img) {
		Mat image = Filter.furtherClean(img);

		// Convert to B&W image
		Imgproc.threshold(image, binary, 0, 255, Imgproc.THRESH_BINARY);

		// Convert to binary image, 1 channel
		Imgproc.cvtColor(binary, binary, Imgproc.COLOR_RGB2GRAY);

		int x = image.cols() * 2 / 3;
		int y = image.rows() / 3;
		pX = x;
		pY = y;
		// Core.circle(rgb, new Point(UL.x+x, UL.y+y), 5, new Scalar(0, 255, 0),
		// -1);

		double pixel[] = binary.get(y, x);
		if (pixel[0] == 0.0) {
			for (int i = y; i >= 0; i--) {
				pixel = binary.get(i, x);
				if (pixel[0] != 0.0) {
					// int upperPt = i;
					// for(int j = y; j < binary.rows(); j++){
					// pixel = binary.get(j, x);
					// if(pixel[0] != 0.0){
					// int lowerPt = j;
					// pX = x;
					// pY = (upperPt+lowerPt)/2;
					// Core.circle(rgb, new Point(UL.x+pX, UL.y+pY), 5, new
					// Scalar(255, 0, 0), -1);
					// return "background";
					// }
					// }
					return true;
				}

			}
		}
		return false;
	}

	public static boolean isMidHeightClass(int imgHeight, int handHeight) {
		float thresh = 0.65f;
		if ((float) handHeight / imgHeight > thresh)
			return true;
		else
			return false;
	}

	public static boolean isCentroidClass(Mat image) {
		Mat binary = new Mat();
		// Find contours from binary image
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		int handWidth = image.cols();

		// Convert to B&W image
		Imgproc.threshold(image, binary, 0, 255, Imgproc.THRESH_BINARY);

		// Convert to binary image, 1 channel
		Imgproc.cvtColor(binary, binary, Imgproc.COLOR_RGB2GRAY);

		// Find contour
		Imgproc.findContours(binary, contours, hierarchy, Imgproc.RETR_TREE,
				Imgproc.CHAIN_APPROX_SIMPLE);

		// Get the moment
		Moments mu = new Moments();
		Iterator<MatOfPoint> each = contours.iterator();
		int ctr = 0;
		while (each.hasNext()) {
			MatOfPoint wrapper = each.next();
			if (ctr == 0)
				mu = Imgproc.moments(wrapper, false);
			ctr++;
		}

		// Get the centroid or mass center
		Point mc;
		mc = new Point(mu.get_m10() / mu.get_m00(), mu.get_m01() / mu.get_m00());

		// // Plot centroid
		// Core.circle(image, mc, 4, new Scalar(0, 255, 0), -1, 8, 0);

		if ((float) mc.x / handWidth < 0.55)
			return true;
		else
			return false;
	}

	public static String checkCurve() {
		for (int j = pX; j < binary.cols(); j++) {
			double pixel[] = binary.get(pY, j);
			Log.i("CHECKpixel", "" + pixel[0]);
			if (pixel[0] != 0.0)
				return "O";
		}
		return "C";
	}
}