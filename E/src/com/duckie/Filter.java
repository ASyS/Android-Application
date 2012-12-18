package com.duckie;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import android.util.Log;

public class Filter {
	
	public static Mat subtractBG(Mat src, Mat rgb) {
//		Mat temp = src.clone();
//		Mat rgb = new Mat();
		Imgproc.cvtColor(src, rgb, Imgproc.COLOR_YUV420sp2RGB);
		Mat temp = rgb.clone();
		Imgproc.cvtColor(temp, temp, Imgproc.COLOR_RGB2HSV);
		Core.inRange(temp, new Scalar(100, 80, 60), new Scalar(125, 255, 255), temp); //(90, 50, 50), Scalar(160, 255, 255)
		Imgproc.threshold(temp, temp, 0, 255, Imgproc.THRESH_BINARY_INV);

		// Find contours from binary image
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(temp, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
		
		// Find contour with biggest area
		Iterator<MatOfPoint> each = contours.iterator();
		int ctr = 0;
		int indexOfBiggestContour = -1;
		double sizeOfBiggestContour = 0;
		while (each.hasNext()){
			MatOfPoint wrapper = each.next();
			double area = Imgproc.contourArea(wrapper);
			if (area > sizeOfBiggestContour){
				sizeOfBiggestContour = area;
				indexOfBiggestContour = ctr;
			}
			ctr++;
		}

		// Get bounding box for contour
		Rect boundRect = new Rect();
		each = contours.iterator();
		ctr = 0;
		while (each.hasNext()){
			MatOfPoint wrapper = each.next();
			if (ctr==indexOfBiggestContour){
				boundRect = Imgproc.boundingRect( wrapper );
			}
			ctr++;
		}
		
//		// Draw bounding box around contour
//		Core.rectangle(rgb, boundRect.tl(), boundRect.br(), new Scalar(255, 255, 0), 2, 8, 0 );
		
		// Create a mask for the contour to mask out that region from image.
		Mat mask = Mat.zeros(src.size(), CvType.CV_8UC1);
		Imgproc.drawContours(mask, contours, indexOfBiggestContour, new Scalar(255), -1);
		
		// At this point, mask has value of 255 for pixels within the contour and value of 0 for those not in contour.
		// Extract region using mask for region
//		Mat imageROI, contourRegion;
		rgb.copyTo(mask, mask);

//		Mat contourRegion2 = mask(roi);
		Mat contourRegion = new Mat(mask, boundRect);

		return contourRegion;
	}
}
