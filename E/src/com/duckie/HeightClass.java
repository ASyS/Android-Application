package com.duckie;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import android.util.Log;

public class HeightClass {
	public static String findTips(Mat image){
		Mat binary = new Mat();
		int left = 0, right = 0;
		// Find contours from binary image
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		List<MatOfPoint> hullPoints = new ArrayList<MatOfPoint>();
		List<Point> hullPointList  = new ArrayList<Point>();
		List<Point> filteredPointList = new ArrayList<Point>();
		Point hullPointArray[] = new Point[2];
	    MatOfPoint hullPointMat = new MatOfPoint();
		MatOfInt hull = new MatOfInt();
		Mat hierarchy = new Mat();

		// Convert to B&W image
		Imgproc.threshold(image, binary, 0, 255, Imgproc.THRESH_BINARY);

		// Convert to binary image, 1 channel
		Imgproc.cvtColor(binary, binary, Imgproc.COLOR_RGB2GRAY);

		// Find contour
		Imgproc.findContours(binary, contours, hierarchy, Imgproc.RETR_TREE,
				Imgproc.CHAIN_APPROX_SIMPLE);
		
		// Find convex hull
		for (int k=0; k < contours.size(); k++){
			Imgproc.convexHull(contours.get(k), hull);			
////			Imgproc.convexityDefects(mContours.get(k), hullInt, defectsInt);
			
			for(int j=0; j < hull.toList().size(); j++){				
		        hullPointList.add(contours.get(k).toList().get(hull.toList().get(j)));
		    }

//			for(int j=0; j < defectsInt.toList().size(); j++){					     
//				defectsPointList.add(mContours.get(k).toList().get(defectsInt.toList().get(j)));
//		    }
			
			hullPointMat.fromList(hullPointList);
		    hullPoints.add(hullPointMat);
		    	    
//			defectsPointMat.fromList(defectsPointList);
//		    defectsPoints.add(defectsPointMat);   
		}
		
		for(int l = 0; l < hullPointList.size(); l++){
////			cout<<image.cols;
			if(hullPointList.get(l).y < image.rows()*0.25){
////				if(j != hull[0].size()-1){
				if(l != hullPointList.size()-1){
					if (Math.hypot(hullPointList.get(l).x - hullPointList.get(l+1).x, hullPointList.get(l).y - hullPointList.get(l+1).y)/image.cols() > 0.08){
	////					Imgproc.circle(image, hullPointList.get(l), 5, new Scalar(0, 0, 255), -1);
	////					cout<<hull[0][j]<<endl;
						filteredPointList.add(new Point(hullPointList.get(l).x, hullPointList.get(l).y));
						if(hullPointList.get(l).x > image.cols()/2)
							right += 1;
						else
							left += 1;
					}
				}
			}
		}
		Log.i("List SIZE", ""+filteredPointList.size());
		if(left == 0){
			if(right == 1){
				return "L"; //L
			}
			else{
//				p1 = hullPointList.get(0);
//				p2 = hullPointList.get(1);
				Log.i("COMPARE!", "["+hullPointList.get(0).x+" "+hullPointList.get(0).y+"], ["+hullPointList.get(1).x+" "+hullPointList.get(1).y+"]");
				Log.i("LEFT", "left: "+left);
				Log.i("RIGHT", "right: "+right);
				
//				Core.circle(rgb, new Point(TL.x+filteredPointList.get(0).x, TL.y+filteredPointList.get(0).y), 10, new Scalar(255, 0, 0));
//				Core.circle(rgb, new Point(TL.x+filteredPointList.get(1).x, TL.y+filteredPointList.get(1).y), 10, new Scalar(0, 255, 0));
				if(filteredPointList.get(0).y > filteredPointList.get(1).y) return "R"; //R
				else return "U"; //U
			}
		}
		else if(left == 1){
			if(right == 0){
				return "D"; //D
			}
			else if (right > 1){
				return "W"; //W
			}
			else{
//				Point center((hullArray[0].x + hullArray[1].x)/2, (hullArray[0].y + hullArray[1].y)/2);
//				circle(image, Point(center.x, center.y), 5, Scalar(255, 0, 0), -1);
//				for (int k = center.y; k < binary.rows; ++k){
//					uchar * pixel = binary.ptr<uchar>(k);
//					if(pixel[center.x] != 0){
//						circle(image, Point(center.x, k), 5, Scalar(0, 255, 0), -1);
//						break;
//					}
//				}
				return "KV"; //KV
			}
		}
		else if(left > 1 && right < 2){
			return "F"; //F
		}
		else if(left > 1 && right > 1){
			return "B"; //B
		}
				
		return "";
}
	}
