package com.duckie;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;


public class WidthClass {

	private static Mat mThresh, mHierarchy;
	
	public static MatOfPoint detectHull(Mat src_gray, Mat dst){
		mThresh = new Mat();
		mHierarchy = new Mat();
		Point hullPeak = new Point(0,0);
		
		List<MatOfPoint> mContours = new ArrayList<MatOfPoint>();
		List<MatOfPoint> hullPoints = new ArrayList<MatOfPoint>();
		List<Point> hullPointList  = new ArrayList<Point>(mContours.size());  
	    
	    MatOfPoint hullPointMat = new MatOfPoint();
		MatOfInt hullInt = new MatOfInt();
   
		Imgproc.threshold(src_gray, mThresh, 30, 255, Imgproc.THRESH_BINARY_INV);    
		Imgproc.findContours(mThresh, mContours, mHierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);
		 
		for (int k=0; k < mContours.size(); k++){
			Imgproc.convexHull(mContours.get(k), hullInt);					
			for(int j=0; j < hullInt.toList().size(); j++){				
		        hullPointList.add(mContours.get(k).toList().get(hullInt.toList().get(j)));
		    }	
			hullPointMat.fromList(hullPointList);
		    hullPoints.add(hullPointMat);
		}

		for (int i = 0; i<hullPointList.size(); i++){
			if(hullPeak.y != 0){
				if(hullPointList.get(i).y<hullPeak.y){
					hullPeak = hullPointList.get(i);
				}
			}
			else{
				hullPeak = hullPointList.get(i);
			}
		 }
		//Imgproc.drawContours( dst, hullPoints, -1,  new Scalar(255,0,0, 255), 1);
		return hullPointMat;
	}

	public static void getBlueMat(Mat src, Mat dst){
		Core.inRange(src, new Scalar(90, 50, 50), new Scalar(160, 255, 255), dst);
	}
}
