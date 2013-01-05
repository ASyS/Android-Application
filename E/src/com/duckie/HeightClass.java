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

	// public static String findTips(Mat image) {
	// Mat binary = new Mat();
	// int left = 0, right = 0;
	//
	// // Find contours from binary image
	// List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	// List<MatOfPoint> hullPoints = new ArrayList<MatOfPoint>();
	// List<Point> hullPointList = new ArrayList<Point>();
	// List<Point> filteredPointList = new ArrayList<Point>();
	// MatOfPoint hullPointMat = new MatOfPoint();
	// MatOfInt hull = new MatOfInt();
	// Mat hierarchy = new Mat();
	//
	// // Convert to B&W image
	// Imgproc.threshold(image, binary, 0, 255, Imgproc.THRESH_BINARY);
	//
	// // Convert to binary image, 1 channel
	// Imgproc.cvtColor(binary, binary, Imgproc.COLOR_RGB2GRAY);

	// Mat binary2 = binary.clone();
	//
	// // Find contour
	// Imgproc.findContours(binary, contours, hierarchy, Imgproc.RETR_TREE,
	// Imgproc.CHAIN_APPROX_SIMPLE);
	//
	// // Find convex hull
	// for (int k = 0; k < contours.size(); k++) {
	// Imgproc.convexHull(contours.get(k), hull);
	//
	// for (int j = 0; j < hull.toList().size(); j++) {
	// hullPointList.add(contours.get(k).toList()
	// .get(hull.toList().get(j)));
	// }
	//
	// hullPointMat.fromList(hullPointList);
	// hullPoints.add(hullPointMat);
	// }
	//
	// // Filter hull points. Only hull points relevant to hull classification
	// // will remain.
	// // Also, count the number of filtered hull points in left and right.
	// for (int l = 0; l < hullPointList.size(); l++) {
	//
	// if (hullPointList.get(l).y < image.rows() * 0.25) {
	//
	// if (l != hullPointList.size() - 1) {
	//
	// if (Math.hypot(
	// hullPointList.get(l).x - hullPointList.get(l + 1).x,
	// hullPointList.get(l).y - hullPointList.get(l + 1).y)
	// / image.cols() > 0.08) {
	//
	// filteredPointList.add(hullPointList.get(l));
	//
	// if (hullPointList.get(l).x > image.cols() / 2)
	// right += 1;
	// else
	// left += 1;
	// }
	// }
	// }
	// }
	//
	// // Identify hand signals according to characteristics.
	// if (left + right == 1) {
	// Mat img1 = image.clone();
	// Mat filtered = Filter.furtherClean(image);
	// // Core.circle(rgb, new Point(UL.x+image.cols()*2/3,
	// // UL.y+image.rows()*2/3), 5, new Scalar(255, 0, 0));
	// if (Core.norm(img1, filtered) == 0) {
	// return "L"; // L
	// } else
	// return "D"; // D
	// }
	//
	// if (left == 0 && right > 1) {
	// // Log.i("LEFT", "left: " + left);
	// // Log.i("RIGHT", "right: " + right);
	//
	// // Sort points by x axis, increasing
	// if (filteredPointList.size() > 1) {
	// if (filteredPointList.get(0).x > filteredPointList.get(1).x) {
	// Point temp = filteredPointList.get(0);
	// filteredPointList.set(0, filteredPointList.get(1));
	// filteredPointList.set(1, temp);
	// }
	//
	// if (filteredPointList.get(0).y > filteredPointList.get(1).y)
	// return "R"; // R
	// else
	// return "U"; // U
	// } else
	// return " ";
	// } else if (left == 1) {
	// if (right == 0) {
	// Log.i("checking", "in");
	// return "D"; // D
	// } else if (right > 1) {
	// return "W"; // W
	// } else {
	//
	// // Sort points by x axis, increasing
	// if (filteredPointList.size() > 1) {
	// if (filteredPointList.get(0).x > filteredPointList.get(1).x) {
	// Point temp = filteredPointList.get(0);
	// filteredPointList.set(0, filteredPointList.get(1));
	// filteredPointList.set(1, temp);
	// }
	// }
	//
	// int startX = (int) filteredPointList.get(0).x;
	// int startY = (int) filteredPointList.get(0).y;
	// int lastDir = 0; // 0 up; 1 down
	// int currDir = 0;
	// int ctr = 0;
	// Point KV_PtsArray[] = new Point[3];
	//
	// while (startX != filteredPointList.get(1).x) {
	//
	// for (int k = 0; k < binary2.rows(); ++k) {
	//
	// double pixel[] = binary2.get(k, startX + 1);
	//
	// if (pixel[0] != 0.0) {
	// // Plots the edges from hullPoint[0] to hullPoint[1]
	// // Core.circle(rgb, new Point(UL.x + startX + 1,
	// // UL.y
	// // + k), 5, new Scalar(0, 255, 0));
	// if (k > startY)
	// currDir = 1;
	// else if (k < startY)
	// currDir = 0;
	// if (currDir != lastDir && ctr < 3) {
	// // Plots 3 significant pts in K and V
	// // Core.circle(rgb, new Point(UL.x+startX+1,
	// // UL.y+k), 5, new Scalar(0, 0, 255));
	// KV_PtsArray[ctr] = new Point(startX + 1, k);
	// ctr++;
	// }
	// lastDir = currDir;
	// startX = startX + 1;
	// startY = k;
	// break;
	// }
	//
	// }
	//
	// }
	//
	// if (ctr == 3) {
	// double d1 = Math.hypot(KV_PtsArray[0].x - KV_PtsArray[1].x,
	// KV_PtsArray[0].y - KV_PtsArray[1].y);
	// double d2 = Math.hypot(KV_PtsArray[2].x - KV_PtsArray[1].x,
	// KV_PtsArray[2].y - KV_PtsArray[1].y);
	// if (d2 < d1 / 2)
	// return "K"; // K
	// else {
	// if (d2 < d1)
	// return "V"; // V
	// else
	// return "R";
	// }
	// } else
	// return " ";
	// }
	// } else if (left > 1 && right < 2) {
	// return "F"; // F
	// } else if (left > 1 && right > 1) {
	// return "B"; // B
	// }
	//
	// // For testing
	// // Log.i("# of fingers", "" + left+right);
	//
	// return "";
	// }

	/**
	 * new algo
	 */

	public static String findTips(Mat image) {
		Mat binary = new Mat();

		// Convert to B&W image
		Imgproc.threshold(image, binary, 0, 255, Imgproc.THRESH_BINARY);

		// Convert to binary image, 1 channel
		Imgproc.cvtColor(binary, binary, Imgproc.COLOR_RGB2GRAY);

		boolean flag_bg = false;
		boolean handInQuarterOfImg = false;
		int count_finger = 0;

		int start = -1;
		int end = -1;

		int tester = 0; // test variable CAN BE REMOVED

		double startPercent = 0, endPercent = 0;

		int leftHeight = -1;

		int firstHandPixel = -1;
		int lastHandPixel = -1;

		int KV_midFinger = -1;
		int KV_indxFinger = -1;

		double pixel[];
		for (int i = 0; i < binary.cols(); i++) {
			pixel = binary.get((int) (binary.rows() * 0.23), i);
			if (pixel[0] != 0.0) {
				if (firstHandPixel == -1)
					firstHandPixel = i;
				if (i < image.cols() / 4)
					handInQuarterOfImg = true;
				lastHandPixel = i;
				leftHeight = i;
			}

			if (pixel[0] != 0.0) {
				if (flag_bg == false) { // flag_bg is false
					count_finger++;
					flag_bg = true;

					start = i;

				}
				end = i;
			} else {
				if (count_finger == 1)
					KV_midFinger = end;
				else if (count_finger == 2)
					KV_indxFinger = start;
				flag_bg = false;
				if (tester != count_finger) { // remove this line -- just for
												// testing
					startPercent = ((double) start / (double) binary.cols()) * 100;
					endPercent = ((double) end / (double) binary.cols()) * 100;

				}
				tester = count_finger; // remove this line -- just for testing

			}

		}

		if (count_finger == 3) {
			if (endPercent > 80.0) {
				return "W";
			} else {
				return "F";
			}
		} else if (count_finger == 4) {
			return "B";
		} else if (count_finger == 1) {
			if (endPercent > 70.0)
				if (handInQuarterOfImg)
					return "B";
				else {
					int result = identify_R_U(binary);
					switch (result) {
					case 1:
						return "R";
					case 2:
						return "U";
					default:
						return " ";
						// return "RU";
					}
				}
			else {
				if ((float) (lastHandPixel - firstHandPixel) / image.cols() > 0.35)
					return "F";
				else {
					image = Filter.furtherClean(image);
					Mat binary2 = new Mat();
					// Convert to B&W image
					Imgproc.threshold(image, binary2, 0, 255,
							Imgproc.THRESH_BINARY);

					// Convert to binary image, 1 channel
					Imgproc.cvtColor(binary2, binary2, Imgproc.COLOR_RGB2GRAY);

					flag_bg = false;
					int countHandArea = 0;
					for (int i = 0; i < binary2.rows(); i++) {

						pixel = binary2.get(i, (int) (binary2.cols() * 0.70));

						if (pixel[0] != 0.0) {
							if (!flag_bg) {
								flag_bg = true;
								countHandArea++;
							}
						} else
							flag_bg = false;

					}
					if (countHandArea == 1) {
						if (checkAngleL(binary))
							return "L";
						else {
							int result = identify_R_U(binary);
							switch (result) {
							case 1:
								return "R";
							case 2:
								return "U";
							default:
								return " ";
							}
						}
					} else if (countHandArea == 2)
						return "D";
					else
						return " ";
				}

			}
		} else if (count_finger == 2) {
			// int result = identify_K_V(rgb, binary, new Point(KV_midFinger,
			// (int) (binary.rows() * 0.23)), new Point(KV_indxFinger,
			// (int) (binary.rows() * 0.23)));
			int startX = (int) KV_midFinger;
			int startY = (int) (binary.rows() * 0.23);
			int lastDir = 1; // 0 up; 1 down
			int currDir = 1;
			int ctr = 1;
			Point KV_PtsArray[] = new Point[3];
			KV_PtsArray[0] = new Point(startX, startY);
			// Core.circle(rgb,
			// new Point(UL.x + startX, UL.y + startY), 5,
			// new Scalar(0, 0, 255));
			// double pixel[];
			Log.i("START", "" + startX);
			Log.i("END", "" + startY);

			for (int i = startX; i < KV_indxFinger; i++) {

				for (int k = (int) (binary.rows() * 0.23); k < binary.rows(); k++) {

					pixel = binary.get(k, i + 1);
					Log.i("CHECK1", "" + binary.rows());
					Log.i("CHECK2", "" + binary.rows() * 0.23);
					Log.i("CHECK3", "" + (int) (binary.rows() * 0.23));
					Log.i("CHECK4", "" + pixel[0]);

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
							// Core.circle(rgb,
							// new Point(UL.x + i + 1, UL.y + k), 5,
							// new Scalar(0, 0, 255));
							KV_PtsArray[ctr] = new Point(i + 1, k);
							ctr++;
						}
						lastDir = currDir;
						// startX = startX + 1;
						startY = k;
						break;
					}

				}

				if (ctr < 3 && i + 1 == (int) KV_indxFinger) {
					// Core.circle(rgb, new Point(UL.x + startX, UL.y + startY),
					// 5,
					// new Scalar(0, 0, 255));
					KV_PtsArray[ctr] = new Point(i, startY);
					ctr++;
				}

				if (ctr == 3)
					break;

			}

			if (ctr == 3) {
				if (KV_PtsArray[0].y == KV_PtsArray[2].y)
					return "V"; // V
				else
					return "K"; // K
			} else
				return " ";

		}
		return Integer.toString(count_finger);
	}

	public static int identify_R_U(Mat binary) {
		// 0: hull points identified < 2
		// 1: R
		// 2: U
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		List<MatOfPoint> hullPoints = new ArrayList<MatOfPoint>();
		List<Point> hullPointList = new ArrayList<Point>();
		List<Point> filteredPointList = new ArrayList<Point>();
		MatOfPoint hullPointMat = new MatOfPoint();
		MatOfInt hull = new MatOfInt();
		Mat hierarchy = new Mat();

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

			if (hullPointList.get(l).y < binary.rows() * 0.25) {

				if (l != hullPointList.size() - 1) {

					if (Math.hypot(
							hullPointList.get(l).x - hullPointList.get(l + 1).x,
							hullPointList.get(l).y - hullPointList.get(l + 1).y)
							/ binary.cols() > 0.08) {

						filteredPointList.add(hullPointList.get(l));
						// Core.circle(rgb,
						// new Point(UL.x + hullPointList.get(l).x, UL.y
						// + hullPointList.get(l).y), 5,
						// new Scalar(0, 255, 0));
					}
				}
			}
		}

		// Sort points by x axis, increasing
		if (filteredPointList.size() > 1) {
			if (filteredPointList.get(0).x > filteredPointList.get(1).x) {
				Point temp = filteredPointList.get(0);
				filteredPointList.set(0, filteredPointList.get(1));
				filteredPointList.set(1, temp);
			}

			if (filteredPointList.get(0).y > filteredPointList.get(1).y)
				return 1; // R
			else
				return 2; // U
		}

		return 0;

	}

	// public static int identify_K_V(Mat rgb, Mat binary, Point start, Point
	// end) {
	// // true: V
	// // false: K
	//
	// }

	public static boolean checkAngleL(Mat binary) {
		int leftPointX = -1;

		double pixel[];

		for (int i = 0; i < binary.cols(); i++) {
			pixel = binary.get(0, i);
			if (pixel[0] != 0.0) {
				leftPointX = i;
			}

		}

		int rightPointY = -1;
		for (int i = 0; i < binary.rows(); i++) {
			pixel = binary.get(i, binary.cols() - 1);
			if (pixel[0] != 0.0) {
				rightPointY = i;
				break;
			}
		}

		double opposite = rightPointY;
		double adjacent = binary.cols() - 1 - leftPointX;

		double angle = Math.atan2(opposite, adjacent) * (180 / Math.PI);

		if (angle >= 50.0 & angle < 70.0) {
			return true; // angle indicates L
		} else {
			return false; // angle indicates not L
		}

	}

}
