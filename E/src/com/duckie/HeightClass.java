package com.duckie;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import android.util.Log;

public class HeightClass {

	public static String findTips(Mat image) {
		Mat binary = new Mat();
		int left = 0, right = 0;

		// Find contours from binary image
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		List<MatOfPoint> hullPoints = new ArrayList<MatOfPoint>();
		List<Point> hullPointList = new ArrayList<Point>();
		List<Point> filteredPointList = new ArrayList<Point>();
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
		for (int k = 0; k < contours.size(); k++) {
			Imgproc.convexHull(contours.get(k), hull);

			for (int j = 0; j < hull.toList().size(); j++) {
				hullPointList.add(contours.get(k).toList()
						.get(hull.toList().get(j)));
			}

			hullPointMat.fromList(hullPointList);
			hullPoints.add(hullPointMat);
		}

		// Filter hull points. Only hull points relevant to hull classification
		// will remain.
		// Also, count the number of filtered hull points in left and right.
		for (int l = 0; l < hullPointList.size(); l++) {

			if (hullPointList.get(l).y < image.rows() * 0.25) {

				if (l != hullPointList.size() - 1) {

					if (Math.hypot(
							hullPointList.get(l).x - hullPointList.get(l + 1).x,
							hullPointList.get(l).y - hullPointList.get(l + 1).y)
							/ image.cols() > 0.08) {

						filteredPointList.add(hullPointList.get(l));

						if (hullPointList.get(l).x > image.cols() / 2)
							right += 1;
						else
							left += 1;
					}
				}
			}
		}

		// Identify hand signals according to characteristics.
		if (left + right == 1) {
			Mat filtered = Filter.furtherClean(image);
			if (Core.norm(image, filtered) == 0) {
				return "L"; // L
			} else
				return "D"; // D
		}

		if (left == 0 && right > 1) {
			// Log.i("LEFT", "left: " + left);
			// Log.i("RIGHT", "right: " + right);

			// Sort points by x axis, increasing
			if (filteredPointList.size() > 1) {
				if (filteredPointList.get(0).x > filteredPointList.get(1).x) {
					Point temp = filteredPointList.get(0);
					filteredPointList.set(0, filteredPointList.get(1));
					filteredPointList.set(1, temp);
				}

				if (filteredPointList.get(0).y > filteredPointList.get(1).y)
					return "R"; // R
				else
					return "U"; // U
			} else
				return " ";
		} else if (left == 1) {
			if (right == 0) {
				return "D"; // D
			} else if (right > 1) {
				return "W"; // W
			} else {

				// Sort points by x axis, increasing
				if (filteredPointList.size() > 1) {
					if (filteredPointList.get(0).x > filteredPointList.get(1).x) {
						Point temp = filteredPointList.get(0);
						filteredPointList.set(0, filteredPointList.get(1));
						filteredPointList.set(1, temp);
					}
				}

				int startX = (int) filteredPointList.get(0).x;
				int startY = (int) filteredPointList.get(0).y;
				int lastDir = 0; // 0 up; 1 down
				int currDir = 0;
				int ctr = 0;
				Point KV_PtsArray[] = new Point[3];

				while (startX != filteredPointList.get(1).x) {

					for (int k = 0; k < binary.rows(); ++k) {

						double pixel[] = binary.get(k, startX + 1);

						if (pixel[0] != 0.0) {
							// Plots the edges from hullPoint[0] to hullPoint[1]
							// Core.circle(rgb, new Point(UL.x + startX + 1,
							// UL.y
							// + k), 5, new Scalar(0, 255, 0));
							if (k > startY)
								currDir = 1;
							else if (k < startY)
								currDir = 0;
							if (currDir != lastDir && ctr < 3) {
								// Plots 3 significant pts in K and V
								// Core.circle(rgb, new Point(UL.x+startX+1,
								// UL.y+k), 5, new Scalar(0, 0, 255));
								KV_PtsArray[ctr] = new Point(startX + 1, k);
								ctr++;
							}
							lastDir = currDir;
							startX = startX + 1;
							startY = k;
							break;
						}

					}

				}

				if (ctr == 3) {
					double d1 = Math.hypot(KV_PtsArray[0].x - KV_PtsArray[1].x,
							KV_PtsArray[0].y - KV_PtsArray[1].y);
					double d2 = Math.hypot(KV_PtsArray[2].x - KV_PtsArray[1].x,
							KV_PtsArray[2].y - KV_PtsArray[1].y);
					if (d2 < d1 / 2)
						return "K"; // K
					else {
						if (d2 < d1)
							return "V"; // V
						else
							return "R";
					}
				} else
					return " ";
			}
		} else if (left > 1 && right < 2) {
			return "F"; // F
		} else if (left > 1 && right > 1) {
			return "B"; // B
		}
		
		// For testing
		// Log.i("# of fingers", "" + left+right);

		return "";
	}
}
