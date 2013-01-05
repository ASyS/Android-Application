package com.duckie;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.util.Log;

public class CentroidClass {

	public static boolean isQ(Mat image) { // identifying Q

		Mat binary = new Mat();

		// Convert to B&W image
		Imgproc.threshold(image, binary, 0, 255, Imgproc.THRESH_BINARY);

		// Convert to binary image, 1 channel
		Imgproc.cvtColor(binary, binary, Imgproc.COLOR_RGB2GRAY);

		int height_75percent = (int) (binary.rows() * 0.75);

		int smallest = -1;
		int biggest = 0;

		double pixel[];
		for (int i = 0; i < binary.cols(); i++) {
			pixel = binary.get(height_75percent, i);

			if (pixel[0] != 0.0) {
				if (smallest == -1) {
					smallest = i;
				}

				if (i > biggest) {
					biggest = i;
				}

			}

		}

		int average = (smallest + biggest) / 2;

		pixel = binary.get(height_75percent, average);
		if (pixel[0] == 0) {
			return true;
		} else {
			return false;
		}

	}

	public static int identify_I_Y(Mat image) {
		Mat binary = new Mat();

		// Convert to B&W image
		Imgproc.threshold(image, binary, 0, 255, Imgproc.THRESH_BINARY);

		// Convert to binary image, 1 channel
		Imgproc.cvtColor(binary, binary, Imgproc.COLOR_RGB2GRAY);

		int leftHeight = -1;
		double pixel[];
		for (int i = 0; i < binary.cols(); i++) {
			pixel = binary.get(0, i);
			if (pixel[0] != 0.0 && i < binary.cols() * 0.30) {
				leftHeight = 0; // 0
			}

		}

		int rightHeight = -1;
		for (int i = 0; i < binary.rows(); i++) {
			pixel = binary.get(i, binary.cols() - 1);
			if (pixel[0] != 0.0) {
				rightHeight = i;
				break;
			}
		}

		if (leftHeight == -1 || rightHeight == -1) {
			return 0;
		} else {

			double opposite = rightHeight - leftHeight;
			double adjacent = binary.cols() - 1;

			double angle = Math.atan2(opposite, adjacent) * (180 / Math.PI);

			if (angle >= -5.0 & angle < 25.0) {
				return 1; // Y
			} else if (angle >= 25.0 && angle < 50.0) {
				return 2; // I
			} else {
				return 0;
			}
		}

	}

	public static boolean isEX(Mat rgb, Mat image) { // identifying Q
		float thresh = 0.68f;
		Log.i("THRESHOLDING EX", "" + (float) image.rows() / rgb.rows());
		if ((float) image.rows() / rgb.rows() > thresh)
			return true;
		else
			return false;
	}

	public static int identify_E_X(Mat image) {
		Mat binary = new Mat();

		// Convert to B&W image
		Imgproc.threshold(image, binary, 0, 255, Imgproc.THRESH_BINARY);

		// Convert to binary image, 1 channel
		Imgproc.cvtColor(binary, binary, Imgproc.COLOR_RGB2GRAY);

		int leftHeight = -1;
		int leftWidth = -1;

		double pixel[];
		for (int i = 0; i < binary.cols(); i++) {
			pixel = binary.get(0, i);
			if (pixel[0] != 0.0) {
				leftHeight = 0;
				leftWidth = i;
				break;
			}

		}

		int rightHeight = -1;
		for (int i = 0; i < image.rows(); i++) {
			pixel = binary.get(i, image.cols() - 1);
			if (pixel[0] != 0.0) {
				rightHeight = i;
				break;
			}
		}

		if (leftHeight == -1 || rightHeight == -1) {
			return 0;

		} else {

			double opposite = rightHeight - leftHeight;
			double adjacent = image.cols() - leftWidth;

			double angle = Math.atan2(opposite, adjacent) * (180 / Math.PI);

			if (angle >= 40.0 & angle < 65.0) {
				return 1; // E
			} else if (angle >= 65.0 && angle < 85.0) {
				return 2; // X
			} else {
				return 0;
			}
		}

	}

	public static boolean isA(Mat image) {
		Mat binary = new Mat();

		// Convert to B&W image
		Imgproc.threshold(image, binary, 0, 255, Imgproc.THRESH_BINARY);

		// Convert to binary image, 1 channel
		Imgproc.cvtColor(binary, binary, Imgproc.COLOR_RGB2GRAY);

		int topWidth = -1;

		double pixel[];
		
		for (int i = 0; i < binary.cols(); i++) {
			pixel = binary.get(0, i);
			Log.i("CHECK4", ""+pixel[0]);

			if (pixel[0] != 0.0) {
				topWidth = i;
				break;
			}

		}

		if (topWidth == -1) {
			return false;
		} else if (topWidth >= (0.80 * binary.cols())) {
			return true;
		} else {
			return false;
		}

	}
	
	public static boolean isT(Mat image){
		Mat binary = new Mat();

		// Convert to B&W image
		Imgproc.threshold(image, binary, 0, 255, Imgproc.THRESH_BINARY);

		// Convert to binary image, 1 channel
		Imgproc.cvtColor(binary, binary, Imgproc.COLOR_RGB2GRAY);

		int leftPointX = -1;
		int g = (int)(0.15 * binary.rows());
		
		double pixel[];
		
		for (int i=0; i<binary.cols() ; i++){
			pixel = binary.get(g, i);
			if(pixel[0] != 0.0){
				leftPointX = i;
				break;
			}

		}

		double sh = ((double) leftPointX / (double) binary.cols()) * 100;

		if (leftPointX == -1) {
			return false;
		} else if (sh > 39.0 && sh < 70.0){
			return true;
		}
		else{
			return false;
		}
	}

	
	public static Mat crop4NN(Mat image)
	{
		int width = (int)image.size().width ;
		int height = (int)image.size().height ;

		Rect quart1 = new Rect(0, 0, (int)(width), (int)(height/2));//Rect quart1(0, 0, width, height/2);
		Mat quarter_1 = new Mat(image, quart1);
		Imgproc.resize(quarter_1, quarter_1, new Size(50, 40));

		return quarter_1;
	}


}
